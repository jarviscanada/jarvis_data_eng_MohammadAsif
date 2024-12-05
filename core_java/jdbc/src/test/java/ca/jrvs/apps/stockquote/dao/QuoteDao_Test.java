package ca.jrvs.apps.stockquote.dao;

import ca.jrvs.apps.stockquote.service.DatabaseConnectionManager;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QuoteDao_Test {

    private DatabaseConnectionManager dcm;
    private Connection connection;
    private QuoteDao quoteDao;
    private Quote testQuote1, testQuote2, testQuote3, testQuote4;
    private final Logger logger = LoggerFactory.getLogger(QuoteDao_Test.class);

    @BeforeAll
    void init() {
        dcm = new DatabaseConnectionManager("localhost", "stock_quote", "postgres", "password");
    }

    @BeforeEach
    void setUp() {
        try {
            connection = dcm.getConnection();
            connection.setAutoCommit(false); // Enable transaction handling
            quoteDao = new QuoteDao(connection);
            initializeTestData();
            saveTestQuotes();
            logger.info("Test setup complete: DAOs initialized and data saved.");
        } catch (SQLException e) {
            logger.error("Test setup failed", e);
            Assertions.fail("Test setup failed due to SQLException: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        try {
            connection.rollback(); // Rollback transaction to ensure a clean state
            connection.close();
            logger.info("Test teardown complete: Transaction rolled back and connection closed.");
        } catch (SQLException e) {
            logger.error("Test teardown failed", e);
        }
    }

    private void initializeTestData() {
        testQuote1 = createTestQuote("FAKE1");
        testQuote2 = createTestQuote("FAKE2");
        testQuote3 = createTestQuote("FAKE3");
        testQuote4 = createTestQuote("FAKE4");
    }

    private Quote createTestQuote(String ticker) {
        Quote quote = new Quote();
        quote.setTicker(ticker);
        quote.setOpen(12.0);
        quote.setHigh(15.0);
        quote.setLow(11.0);
        quote.setPrice(14.4);
        quote.setVolume(12121);
        quote.setLatestTradingDay(Date.valueOf("2024-10-13"));
        quote.setPreviousClose(15);
        quote.setChange(1.1);
        quote.setChangePercent("1.33");
        quote.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));
        return quote;
    }

    private void saveTestQuotes() throws SQLException {
        quoteDao.save(testQuote1);
        quoteDao.save(testQuote2);
        quoteDao.save(testQuote3);
    }

    @Test
    @Order(1)
    public void testSaveInsert() {
        quoteDao.save(testQuote4);
        Optional<Quote> quote = quoteDao.findById(testQuote4.getTicker());
        Assertions.assertTrue(quote.isPresent(), "Quote should be saved successfully.");
    }

    @Test
    @Order(2)
    public void testSaveUpdate() {
        quoteDao.save(testQuote4);
        Optional<Quote> beforeUpdate = quoteDao.findById(testQuote4.getTicker());
        Assertions.assertTrue(beforeUpdate.isPresent());

        testQuote4.setHigh(17.7);
        testQuote4.setLow(10.3);
        testQuote4.setPrice(15.8);
        quoteDao.save(testQuote4);

        Optional<Quote> afterUpdate = quoteDao.findById(testQuote4.getTicker());
        Assertions.assertTrue(afterUpdate.isPresent());
        Assertions.assertNotEquals(beforeUpdate.get().getHigh(), afterUpdate.get().getHigh());
        Assertions.assertNotEquals(beforeUpdate.get().getLow(), afterUpdate.get().getLow());
        Assertions.assertNotEquals(beforeUpdate.get().getPrice(), afterUpdate.get().getPrice());
    }

    @Test
    @Order(3)
    public void testFindById() {
        Optional<Quote> quote = quoteDao.findById(testQuote2.getTicker());
        Assertions.assertTrue(quote.isPresent(), "Quote should be found by ID.");
        Assertions.assertEquals(testQuote2.getTicker(), quote.get().getTicker());
        Assertions.assertEquals(testQuote2.getHigh(), quote.get().getHigh());
        Assertions.assertEquals(testQuote2.getLow(), quote.get().getLow());
        Assertions.assertEquals(testQuote2.getLatestTradingDay(), quote.get().getLatestTradingDay());
        Assertions.assertEquals(testQuote2.getPrice(), quote.get().getPrice());
    }

    @Test
    @Order(4)
    public void testDeleteById() {
        quoteDao.save(testQuote4);
        Assertions.assertTrue(quoteDao.findById(testQuote4.getTicker()).isPresent());
        quoteDao.deleteById(testQuote4.getTicker());
        Assertions.assertFalse(quoteDao.findById(testQuote4.getTicker()).isPresent(), "Quote should be deleted.");
    }

    @Test
    @Order(5)
    public void testFindAll() {
        quoteDao.save(testQuote4);
        List<Quote> quotes = (List<Quote>) quoteDao.findAll();
        Assertions.assertFalse(quotes.isEmpty(), "FindAll should return a non-empty list.");
        System.out.println(quotes);
    }
}
