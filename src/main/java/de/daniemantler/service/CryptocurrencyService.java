package de.daniemantler.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CryptocurrencyService {
	private static final String CURRENCY = "usd";
	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
	public static final String[] CRYPTO_IDS = { "iota", "bitcoin", "neo", "ethereum", "ripple" };

	public static Double getCurrentPrice(String cryptoID) {

		HttpRequest request = HttpRequest.newBuilder().GET()
				.uri(URI.create(
						"https://api.coingecko.com/api/v3/simple/price?ids=" + cryptoID + "&vs_currencies=" + CURRENCY))
				.setHeader("accept", "application/json").build();

		try {
			HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.body());
			if (response.statusCode() == 200) {
				Pattern digits = Pattern.compile("\\d+.\\d+");
				Matcher matcher = digits.matcher(response.body());
				int count = 0;
				while (matcher.find()) {
					count++;
					if (count > 1) {
						throw new Exception("Found more then one number in response Body!");
					}
					return Double.valueOf(matcher.group());
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

}