package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class QuoteService {

    private final QuoteDao quoteDao;
    private final QuoteHttpHelper quoteHttpHelper;
    private final Logger logger = LoggerFactory.getLogger(QuoteService.class);

    public QuoteService(QuoteDao quoteDao, QuoteHttpHelper quoteHttpHelper) {
        this.quoteDao = quoteDao;
        this.quoteHttpHelper = quoteHttpHelper;
    }

    /**
     * Fetches the latest quote data from the API.
     *
     * @param ticker stock symbol
     * @return Optional containing the latest quote information or empty if the ticker is not found
     */
    public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
        logger.info("Fetching quote data for ticker: {} from API", ticker);
        Quote quote = quoteHttpHelper.fetchQuoteInfo(ticker);

        if (quote.getTicker() == null) {
            logger.warn("Ticker symbol '{}' not found. Returning empty Optional.", ticker);
            return Optional.empty();
        }

        logger.info("Ticker '{}' found. Saving to the database.", quote.getTicker());
        try {
            quoteDao.save(quote);
        } catch (IllegalArgumentException e) {
            logger.error("Error saving quote data for ticker '{}'", quote.getTicker(), e);
        }

        return Optional.of(quote);
    }

    public Optional<Quote> fetchQuoteDataFromDB(String ticker) {
        logger.info("Fetching quote data for ticker: {} from database", ticker);
        return quoteDao.findById(ticker);
    }
}
