package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.Quote;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class QuoteHttpHelper {
    private final String apiKey = System.getenv("ALPHA_V_KEY");;
    private final OkHttpClient httpClient = new OkHttpClient();
    final Logger logger = LoggerFactory.getLogger("logger");

    /**
     * Fetch latest quote data from Alpha Vantage endpoint
     * @param symbol symbol of stock to fetch
     * @return Quote with latest data
     * @throws IllegalArgumentException - if no data was found for the given symbol
     */
    public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException {
        if (symbol == null || symbol.isEmpty()) {
            throw new IllegalArgumentException("Symbol cannot be null or empty");
        }

        Quote quote = null;
        Request request = new Request.Builder()
                .url("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&datatype=json")
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected HTTP response: " + response);
            }

            ObjectMapper mapper = new ObjectMapper();
            String jsonResponse = response.body().string();
            JsonNode rootNode = mapper.readTree(jsonResponse);

            JsonNode quoteNode = rootNode.get("Global Quote");
            if (quoteNode == null || quoteNode.isEmpty()) {
                throw new IllegalArgumentException("No data found for the symbol: " + symbol);
            }

            // Convert the JSON node to a Quote object using Jackson
            quote = mapper.treeToValue(quoteNode, Quote.class);

            // Add a timestamp to the Quote object
            quote.setTimestamp(quoteTimestamp());

        } catch (IOException e) {
            logger.error("Error fetching quote info for symbol: " + symbol, e);
            throw new RuntimeException("Failed to fetch data from API", e);
        }

        return quote;
    }

    private Timestamp quoteTimestamp() {
        return new Timestamp(Instant.now().toEpochMilli());
    }

}