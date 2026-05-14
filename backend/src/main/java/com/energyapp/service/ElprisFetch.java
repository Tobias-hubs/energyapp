
package com.energyapp.service;

import com.google.gson.Gson;
import com.energyapp.model.Elpris;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class ElprisFetch {

private static final HttpClient HTTP = HttpClient.newBuilder().build();
private static final Gson GSON = new Gson();

    public List<Elpris> getPrice(String zon, LocalDate datum) {

        final String zone = zon == null ? "" : zon.trim().toUpperCase(Locale.ROOT);

        String url = String.format(
                "https://www.elprisetjustnu.se/api/v1/prices/%d/%02d-%02d_%s.json",
                datum.getYear(),
                datum.getMonthValue(),
                datum.getDayOfMonth(),
                zone
        );

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .header("User-Agent", "energyapp/1.0")
                    .build();

            HttpResponse<String> response =
                    HTTP.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return Collections.emptyList();
            }

            Elpris[] prices = GSON.fromJson(response.body(), Elpris[].class);

            return Arrays.asList(prices);

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
