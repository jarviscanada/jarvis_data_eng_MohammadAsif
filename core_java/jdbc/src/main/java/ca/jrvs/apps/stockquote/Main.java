package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.service.DatabaseConnectionManager;
import ca.jrvs.apps.stockquote.service.PositionService;
import ca.jrvs.apps.stockquote.service.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    // Declare controller as a class-level variable
    private static StockQuoteController controller;

    public static void main(String[] args) {
        try {
            // Load application properties
            Properties appProperties = loadProperties("/properties.txt");

            // Initialize database connection
            DatabaseConnectionManager dcm = new DatabaseConnectionManager(
                    appProperties.getProperty("server"),
                    appProperties.getProperty("database"),
                    appProperties.getProperty("username"),
                    appProperties.getProperty("password")
            );

            // Setup application resources
            initializeApp(dcm);

            // Start the controller
            controller.initClient();
            logger.info("Application started successfully.");
        } catch (IOException e) {
            logger.error("Failed to load application properties. Ensure the properties file exists and is valid.", e);
        } catch (SQLException e) {
            logger.error("Database connection error. Verify the database is running and connection properties are correct.", e);
        }
    }

    private static void initializeApp(DatabaseConnectionManager dcm) throws SQLException {
        try (Connection connection = dcm.getConnection()) {
            QuoteHttpHelper httpHelper = new QuoteHttpHelper();
            QuoteDao quoteDao = new QuoteDao(connection);
            PositionDao positionDao = new PositionDao(connection);
            QuoteService quoteService = new QuoteService(quoteDao, httpHelper);
            PositionService positionService = new PositionService(positionDao, quoteService);

            // Initialize controller
            controller = new StockQuoteController(quoteService, positionService);
            logger.info("App resources initialized successfully.");
        }
    }

    private static Properties loadProperties(String filePath) throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = Main.class.getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IOException("Properties file not found: " + filePath);
            }
            properties.load(inputStream);
            logger.info("Loaded properties from file: {}", filePath);
        }
        return properties;
    }
}
