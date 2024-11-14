package ca.jrvs.apps.practice;

//class HelloWorld {
//
//  // Your program begins with a call to main().
//  // Prints "Hello, World" to the terminal window.
//  public static void main(String[] args) {
//    System.out.println("Hello, World");
//  }
//}

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorld {

  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(HelloWorld.class);
    logger.info("Hello World");
  }
}