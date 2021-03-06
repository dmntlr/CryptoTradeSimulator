
package de.danielmantler.security;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

import org.eclipse.microprofile.jwt.Claims;

import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;

/**
 * Utilities for generating a JWT for testing
 */
public class TokenUtils {

	private static String RELATIVE_PATH_PRIVATE_KEY = "/privateKey.pem";

	private TokenUtils() {
		// no-op: utility class
	}

	public static String generateTokenString(String jsonResName, Map<String, Long> timeClaims, String user, int roomID,
			double userBalance, double price, String crypto, double amount, int round, int maxRounds) throws Exception {
		PrivateKey privateKey = readPrivateKey(RELATIVE_PATH_PRIVATE_KEY);
		JwtClaimsBuilder claims = Jwt.claims(jsonResName);
		long currentTimeInSecs = currentTimeInSecs();
		long exp = timeClaims != null && timeClaims.containsKey(Claims.exp.name()) ? timeClaims.get(Claims.exp.name())
				: currentTimeInSecs + 300;
		
		claims.issuedAt(currentTimeInSecs);
		claims.claim(Claims.auth_time.name(), currentTimeInSecs);
		claims.expiresAt(exp);
		claims.claim("upn", user);
		claims.claim("room", roomID);
		claims.claim("balance", userBalance);
		claims.claim("price", price);
		claims.claim("crypto",crypto);
		claims.claim("amount", amount);
		claims.claim("round", round);
		claims.claim("maxRounds",maxRounds);
		return claims.jws().signatureKeyId(RELATIVE_PATH_PRIVATE_KEY).sign(privateKey);
	}

	/**
	 * Read a PEM encoded private key from the classpath
	 *
	 * @param pemResName - key file resource name
	 * @return PrivateKey
	 * @throws Exception on decode failure
	 */
	public static PrivateKey readPrivateKey(final String pemResName) throws Exception {
		try (InputStream contentIS = TokenUtils.class.getResourceAsStream(pemResName)) {
			byte[] tmp = new byte[4096];
			int length = contentIS.read(tmp);
			return decodePrivateKey(new String(tmp, 0, length, "UTF-8"));
		}
	}

	/**
	 * Decode a PEM encoded private key string to an RSA PrivateKey
	 *
	 * @param pemEncoded - PEM string for private key
	 * @return PrivateKey
	 * @throws Exception on decode failure
	 */
	public static PrivateKey decodePrivateKey(final String pemEncoded) throws Exception {
		byte[] encodedBytes = toEncodedBytes(pemEncoded);

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(keySpec);
	}

	private static byte[] toEncodedBytes(final String pemEncoded) {
		final String normalizedPem = removeBeginEnd(pemEncoded);
		return Base64.getDecoder().decode(normalizedPem);
	}

	private static String removeBeginEnd(String pem) {
		pem = pem.replaceAll("-----BEGIN (.*)-----", "");
		pem = pem.replaceAll("-----END (.*)----", "");
		pem = pem.replaceAll("\r\n", "");
		pem = pem.replaceAll("\n", "");
		return pem.trim();
	}

	/**
	 * @return the current time in seconds since epoch
	 */
	public static int currentTimeInSecs() {
		long currentTimeMS = System.currentTimeMillis();
		return (int) (currentTimeMS / 1000);
	}

}