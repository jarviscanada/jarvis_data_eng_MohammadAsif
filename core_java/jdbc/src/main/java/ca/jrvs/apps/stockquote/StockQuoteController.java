package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.service.PositionService;
import ca.jrvs.apps.stockquote.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class StockQuoteController {

    Scanner scanner = new Scanner(System.in);
    private static final Logger logger = LoggerFactory.getLogger("logger");
    private QuoteService quoteService;
    private PositionService positionService;

    public StockQuoteController(QuoteService quoteService, PositionService positionService) {
        this.quoteService = quoteService;
        this.positionService = positionService;
    }

    /**
     * User interface for our application
     */
    public void initClient() {
        int selection;
        System.out.println("You are now within the Stock Quote Application Main Menu");
        System.out.println("Please select one of the following options:");
        do {
            selection = scanner.nextInt();
            scanner.nextLine();
            switch (selection) {
                case 1:
                    logger.info("Starting Stock Quote Application Purchase Menu");
                    break;
                case 2:
                    logger.info("Starting Stock Quote Application Sell Menu");
                    break;
                case 3:
                    logger.info("Viewing User's Portfolio");
                case 4:
                    logger.info("Exiting Stock Quote Application");
                    System.out.println("Exiting Stock Quote Application");
                    break;
                default:
                    System.out.println("Invalid selection");
            }
        } while (selection != 4);

        scanner.close();
    }

    public void mainMenu() {
        System.out.print("\n\n\n");
        System.out.println("Please select an option in the Stock Quote Application [1], [2], [3], [4]:");
        System.out.println("1 - Find via search and purchase a new stock.");
        System.out.println("2 - View and sell stocks in your portfolio.");
        System.out.println("3 - View your portfolio to see all of your owned stocks.");
        System.out.println("4 - Close the application.");
    }

}
