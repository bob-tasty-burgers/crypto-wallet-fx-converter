package com.bobburger.invest.fx;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FxServiceClientImpl implements FxServiceClient {
    private HttpClient httpClient;
    private String endpoint;

    public FxServiceClientImpl(String endpoint) {
        this.endpoint = endpoint;
        this.httpClient = HttpClient.newHttpClient();
    }


    @Override
    public Optional<BigDecimal> exchangeRate(String fromSymbol, String toSymbol) {
        try {
            URI uri = URI.create(String.format("%s?fsym=%s&tsyms=%s", endpoint, fromSymbol, toSymbol));
            HttpRequest request = HttpRequest
                    .newBuilder(uri)
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            switch(response.statusCode()) {
                case 200:
                    return parseExchangeRate(response.body(), toSymbol);
                default:
                    // We never enter here; the endpoint seems to always reply 200 OK
                    System.err.println("Could not obtain fx rate. Response: " + response.body());
                    return Optional.empty();
            }
        } catch (IOException | InterruptedException exception) {
            System.err.println(String.format(
                    "Could not obtain fx exchange rate from currency %s to currency %s. Exception: %s",
                    fromSymbol, toSymbol, exception.toString()));
            return Optional.empty();
        }
    }

    //@VisibleForTesting -> this method is package visible so that it can be accessed for tests
    static Optional<BigDecimal> parseExchangeRate(String textResponse, String toSymbol) {
        // Would normally use a library like Gson to parse the response
        if(textResponse == null) {
            System.err.println("Could not parse null fx rate.");
            return Optional.empty();
        }
        Matcher responseMatcher = Pattern
                .compile(String.format("\\{\"%s\":(\\d+(\\.\\d*)?)\\}", toSymbol))
                .matcher(textResponse);
        if(responseMatcher.matches()) {
            return Optional.of(new BigDecimal(responseMatcher.group(1)));
        } else {
            // When we query the API for a currency it doesn't recognize or without the fsym and/or tsyms parameters we get:
            //  {"Response":"Error","Message":"There is no data for the symbol BTCCC .","HasWarning":false,"Type":2,"RateLimit":{},"Data":{},"ParamWithError":"fsym"}
            //  {"Response":"Error","Message":"fsym param seems to be missing.","HasWarning":false,"Type":2,"RateLimit":{},"Data":{},"ParamWithError":"fsym"}
            System.err.println(String.format("Could not parse fx rate: %s", textResponse));
            return Optional.empty();
        }
    }
}
