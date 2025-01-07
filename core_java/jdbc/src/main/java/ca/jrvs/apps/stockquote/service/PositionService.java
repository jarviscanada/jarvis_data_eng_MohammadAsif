package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class PositionService {

    private final PositionDao dao;
    private final QuoteService quoteService;
    private final Logger logger = LoggerFactory.getLogger(PositionService.class);

    public PositionService(PositionDao dao, QuoteService quoteService) {
        this.dao = dao;
        this.quoteService = quoteService;
    }

    /**
     * Processes a buy order and updates the database accordingly.
     * @param ticker         symbol of the stock
     * @param numberOfShares number of shares to buy
     * @param price          price per share
     * @return The position in the database after processing the buy
     */
    public Position buy(String ticker, int numberOfShares, double price) {
        validateTransactionInputs(ticker, numberOfShares, price);

        Quote quote = quoteService.fetchQuoteDataFromAPI(ticker)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ticker symbol: " + ticker));

        if (numberOfShares > quote.getVolume()) {
            logger.error("Number of shares exceeds stock volume. Requested: {}, Available: {}", numberOfShares, quote.getVolume());
            throw new IllegalArgumentException("Number of shares must not exceed stock volume.");
        }

        Position position = new Position();
        position.setTicker(quote.getTicker());
        position.setNumOfShares(numberOfShares);
        position.setValuePaid(numberOfShares * price);
        dao.save(position);

        Position updatedPosition = dao.findById(ticker).orElseThrow(() ->
                new IllegalStateException("Failed to fetch updated position from database."));
        logger.info("Successfully purchased {} shares of {} at {} per share. Total cost: {}",
                numberOfShares, ticker, price, updatedPosition.getValuePaid());

        return updatedPosition;
    }

    /**
     * Sells all shares of the given ticker symbol.
     *
     * @param ticker symbol of the stock
     */
    public void sell(String ticker) {
        Position ownedPosition = dao.findById(ticker)
                .orElseThrow(() -> new IllegalArgumentException("No owned position found for ticker: " + ticker));

        Quote latestQuote = quoteService.fetchQuoteDataFromAPI(ticker)
                .orElseThrow(() -> new IllegalArgumentException("Failed to fetch latest stock quote for ticker: " + ticker));

        double stockPrice = latestQuote.getPrice();
        double totalSaleValue = stockPrice * ownedPosition.getNumOfShares();
        logger.info("Selling {} shares of {} at {} per share. Total sale value: {}",
                ownedPosition.getNumOfShares(), ticker, stockPrice, totalSaleValue);
        logger.info("Net gain/loss: {}", totalSaleValue - ownedPosition.getValuePaid());

        dao.deleteById(ticker);
        logger.info("Successfully sold all shares of {}", ticker);
    }


    public Optional<Position> fetchPosition(String ticker) {
        logger.info("Fetching position for ticker: {}", ticker);
        return dao.findById(ticker);
    }

    public List<Position> fetchAllPositions() {
        logger.info("Fetching all stock positions.");
        return dao.findAll();
    }

    private void validateTransactionInputs(String ticker, int numberOfShares, double price) {
        if (price <= 0) {
            logger.error("Invalid price: {}", price);
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        if (numberOfShares <= 0) {
            logger.error("Invalid number of shares: {}", numberOfShares);
            throw new IllegalArgumentException("Number of shares must be greater than zero.");
        }
        if (ticker == null || ticker.trim().isEmpty()) {
            logger.error("Invalid ticker symbol: {}", ticker);
            throw new IllegalArgumentException("Ticker symbol must not be null or empty.");
        }
    }
}
