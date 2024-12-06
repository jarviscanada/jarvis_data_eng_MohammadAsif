package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.service.PositionService;
import ca.jrvs.apps.stockquote.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class StockQuoteController {

    private static final Logger logger = LoggerFactory.getLogger(StockQuoteController.class);
    private final Scanner scanner = new Scanner(System.in);
    private final QuoteService quoteService;
    private final PositionService positionService;

    public StockQuoteController(QuoteService quoteService, PositionService positionService) {
        this.quoteService = quoteService;
        this.positionService = positionService;
    }

    /**
     * Starts the Stock Quote Application.
     */
    public void initClient() {
        int selection = -1;
        while (selection != 4) {
            mainMenu();
            try {
                selection = Integer.parseInt(scanner.nextLine().trim());
                switch (selection) {
                    case 1:
                        logger.info("Starting Stock Quote Application Purchase Menu");
                        buyMenu();
                        break;
                    case 2:
                        logger.info("Starting Stock Quote Application Sell Menu");
                        sellMenu();
                        break;
                    case 3:
                        logger.info("Viewing User's Portfolio");
                        viewPortfolio();
                        break;
                    case 4:
                        logger.info("Exiting Stock Quote Application");
                        System.out.println("Thank you for using the Stock Quote Application. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid selection. Please choose a valid option.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number corresponding to the menu options.");
            }
        }
    }

    private void mainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1 - Search and purchase a stock.");
        System.out.println("2 - View and sell stocks in your portfolio.");
        System.out.println("3 - View your portfolio.");
        System.out.println("4 - Exit the application.");
        System.out.print("Enter your choice: ");
    }

    private void buyMenu() {
        try {
            System.out.println("\n--- Buy Stock ---");
            System.out.print("Enter the stock ticker: ");
            String ticker = scanner.nextLine().trim();

            Optional<Quote> quoteOptional = quoteService.fetchQuoteDataFromAPI(ticker);
            if (quoteOptional.isEmpty()) {
                System.out.println("Stock ticker not found. Returning to main menu.");
                return;
            }

            Quote quote = quoteOptional.get();
            System.out.printf("Current price for %s: $%.2f\n", quote.getTicker(), quote.getPrice());
            System.out.print("Enter the number of shares to buy: ");
            int shares = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter the price per share: ");
            double price = Double.parseDouble(scanner.nextLine().trim());

            positionService.buy(ticker, shares, price);
            System.out.println("Stock purchased successfully! Returning to main menu.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter valid numbers for shares and price.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred during the buy operation.", e);
            System.out.println("An error occurred. Please try again.");
        }
    }

    private void sellMenu() {
        try {
            System.out.println("\n--- Sell Stock ---");
            System.out.print("Enter the stock ticker to sell: ");
            String ticker = scanner.nextLine().trim();

            Optional<Position> positionOptional = positionService.fetchPosition(ticker);
            if (positionOptional.isEmpty()) {
                System.out.println("You do not own this stock. Returning to main menu.");
                return;
            }

            Position position = positionOptional.get();
            Optional<Quote> quoteOptional = quoteService.fetchQuoteDataFromAPI(ticker);
            if (quoteOptional.isEmpty()) {
                System.out.println("Could not fetch the latest stock data. Returning to main menu.");
                return;
            }

            Quote quote = quoteOptional.get();
            double currentValue = quote.getPrice() * position.getNumOfShares();
            double netGainLoss = currentValue - position.getValuePaid();

            System.out.printf("Selling %d shares of %s at $%.2f per share.\n",
                    position.getNumOfShares(), ticker, quote.getPrice());
            System.out.printf("Net gain/loss: $%.2f\n", netGainLoss);

            positionService.sell(ticker);
            System.out.println("Stock sold successfully! Returning to main menu.");
        } catch (Exception e) {
            logger.error("An unexpected error occurred during the sell operation.", e);
            System.out.println("An error occurred. Please try again.");
        }
    }

    private void viewPortfolio() {
        System.out.println("\n--- Portfolio ---");
        List<Position> positions = positionService.fetchAllPositions();
        if (positions.isEmpty()) {
            System.out.println("You have no stocks in your portfolio.");
        } else {
            System.out.println("Here is your current portfolio:");
            for (Position position : positions) {
                System.out.println(position);
            }
        }
        System.out.println("Returning to main menu.");
    }
}
