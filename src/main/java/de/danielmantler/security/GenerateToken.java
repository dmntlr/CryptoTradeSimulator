package de.danielmantler.security;

import java.util.HashMap;

import org.eclipse.microprofile.jwt.Claims;

public class GenerateToken {

	private static String RELATIVE_PATH_STANDARD_CLAIMS = "/JwtClaims.json";

	public static String generateToken(String user, int roomID, double userBalance) {

		HashMap<String, Long> timeClaims = new HashMap<>();

		long duration = 3600;
		long exp = TokenUtils.currentTimeInSecs() + duration;
		timeClaims.put(Claims.exp.name(), exp);
		String token = "";
		try {
			token = TokenUtils.generateTokenString(RELATIVE_PATH_STANDARD_CLAIMS, timeClaims, user, roomID, userBalance);
		} catch (Exception e) {
			System.out.println("Error while generating Token!");
			e.printStackTrace();
		}
		
		System.out.println(token);

		return token;
	}
}
