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
public class PositionDao_Test {

    private DatabaseConnectionManager dcm;
    private Connection connection;
    private PositionDao positionDao;
    private QuoteDao quoteDao;
    private Position testPosition1, testPosition2, testPosition3;
    private Quote testQuote1, testQuote2, testQuote3;
    private final Logger logger = LoggerFactory.getLogger(PositionDao_Test.class);

    @BeforeAll
    void init() {
        dcm = new DatabaseConnectionManager("localhost", "stock_quote", "postgres", "password");
    }

    @BeforeEach
    void setUp() {
        try {
            connection = dcm.getConnection();
            connection.setAutoCommit(false); // Enable transaction handling
            positionDao = new PositionDao(connection);
            quoteDao = new QuoteDao(connection);
            initializeTestData();
            saveTestQuotes();
            saveTestPositions();
            logger.info("Test setup complete: DAOs initialized and data saved.");
        } catch (SQLException e) {
            logger.error("Test setup failed", e);
            Assertions.fail("Test setup failed due to SQLException: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        try {
            connection.rollback(); // Rollback changes to ensure isolation
            connection.close();
            logger.info("Test teardown complete: Transaction rolled back and connection closed.");
        } catch (SQLException e) {
            logger.error("Test teardown failed", e);
        }
    }

    private void initializeTestData() {
        testPosition1 = createTestPosition("FAKE1", 34, 400.50);
        testPosition2 = createTestPosition("FAKE2", 76, 1400.50);
        testPosition3 = createTestPosition("FAKE3", 36, 12300.50);

        testQuote1 = createTestQuote("FAKE1");
        testQuote2 = createTestQuote("FAKE2");
        testQuote3 = createTestQuote("FAKE3");
    }

    private Position createTestPosition(String ticker, int numOfShares, double valuePaid) {
        Position position = new Position();
        position.setTicker(ticker);
        position.setNumOfShares(numOfShares);
        position.setValuePaid(valuePaid);
        return position;
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

    private void saveTestPositions() throws SQLException {
        positionDao.save(testPosition1);
        positionDao.save(testPosition2);
    }

    @Test
    @Order(1)
    public void testSaveInsert() {
        positionDao.save(testPosition3);
        Optional<Position> position = positionDao.findById(testPosition3.getTicker());
        Assertions.assertTrue(position.isPresent(), "Position should be saved successfully.");
    }

    @Test
    @Order(2)
    public void testSaveUpdate() {
        // Save the position
        logger.info("Saving position: {}", testPosition3);
        positionDao.save(testPosition3);

        // Ensure that the position is saved before updating
        Optional<Position> beforeUpdate = positionDao.findById(testPosition3.getTicker());
        Assertions.assertTrue(beforeUpdate.isPresent(), "Position should exist before update.");
        logger.info("Position before update: {}", beforeUpdate.get());

        // Update the position
        testPosition3.setNumOfShares(100);
        logger.info("Updating position with new shares: {}", testPosition3);
        positionDao.save(testPosition3);

        // Sleep to allow time for the database update (useful for timing issues)
        try {
            Thread.sleep(500);  // Optional: wait a bit to ensure update is persisted
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Retrieve the position after update
        Optional<Position> afterUpdate = positionDao.findById(testPosition3.getTicker());
        Assertions.assertTrue(afterUpdate.isPresent(), "Position should exist after update.");
        logger.info("Position after update: {}", afterUpdate.get());

        // Assert that the number of shares was updated
        Assertions.assertNotEquals(beforeUpdate.get().getNumOfShares(), afterUpdate.get().getNumOfShares(),
                "Number of shares should be different after update.");
    }



    @Test
    @Order(3)
    public void testFindById() {
        Optional<Position> position = positionDao.findById(testPosition1.getTicker());
        Assertions.assertTrue(position.isPresent(), "Position should be found by ID.");
        Assertions.assertEquals(testPosition1.getTicker(), position.get().getTicker());
        Assertions.assertEquals(testPosition1.getNumOfShares(), position.get().getNumOfShares());
        Assertions.assertEquals(testPosition1.getValuePaid(), position.get().getValuePaid());
    }

    @Test
    @Order(4)
    public void testDeleteById() {
        positionDao.save(testPosition3);
        Assertions.assertTrue(positionDao.findById(testPosition3.getTicker()).isPresent());
        positionDao.deleteById(testPosition3.getTicker());
        Assertions.assertFalse(positionDao.findById(testPosition3.getTicker()).isPresent(), "Position should be deleted.");
    }

    @Test
    @Order(5)
    public void testFindAll() {
        positionDao.save(testPosition3);
        List<Position> positions = positionDao.findAll();
        Assertions.assertFalse(positions.isEmpty(), "FindAll should return a non-empty list.");
        System.out.println(positions);
    }
}
