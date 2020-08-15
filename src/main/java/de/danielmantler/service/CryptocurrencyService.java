package de.danielmantler.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import de.danielmantler.model.CryptoPrices;

public class CryptocurrencyService {
	private static final String CURRENCY = "usd";

	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
	public static final String[] CRYPTO_IDS = { "iota", "bitcoin", "neo", "ethereum", "ripple" };
	private static Jsonb jsonb = JsonbBuilder.create();
	
	public static CryptoPrices getCurrentPrice(String cryptoID, int days) {

		HttpRequest request = HttpRequest
				.newBuilder().GET().uri(URI.create("https://api.coingecko.com/api/v3/coins/" + cryptoID
						+ "/market_chart?vs_currency=" + CURRENCY + "&days=" + days))
				.setHeader("accept", "application/json").build();

		try {
			HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.body());
			if (response.statusCode() == 200) {
				CryptoPrices crypto = jsonb.fromJson(response.body(),CryptoPrices.class);
				return crypto;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

}