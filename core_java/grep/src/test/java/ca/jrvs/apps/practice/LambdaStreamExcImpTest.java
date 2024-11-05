package ca.jrvs.apps.practice;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LambdaStreamExcImpTest extends TestCase {

    LambdaStreamExcImp lambdaStreamExcImp = new LambdaStreamExcImp();

    public void testCreateStrStream() {

        String[] input ={"a", "b", "c"};
        List<String> expected = Arrays.asList("a", "b", "c");

        Stream<String> resultStream = lambdaStreamExcImp.createStrStream(input);

        List<String> resultList = resultStream.collect(Collectors.toList());
        assertEquals(expected, resultList);
    }

    public void testToUpperCase() {
        String test = "Hello";
        String ans = "HELLO";
        assertEquals(test.toUpperCase(), ans);
    }

    public void testFilter() {

        Stream<String> input = Stream.of("air", "bike", "car");
        String pattern = "i";
        List<String> expected = Arrays.asList("car");

        Stream<String> resultStream = lambdaStreamExcImp.filter(input, pattern);

        List<String> resultList = resultStream.collect(Collectors.toList());
        assertEquals(expected, resultList);

    }

    public void testCreateIntStream() {

        int[] input ={1, 2, 3};
        List<Integer> expected = Arrays.asList(1, 2, 3);

        IntStream resultStream = lambdaStreamExcImp.createIntStream(input);

        List<Integer> resultList = resultStream.boxed().collect(Collectors.toList());
        assertEquals(expected, resultList);
    }

    public void testToList() {

        int[] test = {1, 2, 3};
        IntStream input = lambdaStreamExcImp.createIntStream(test);

        List<Integer> expected = Arrays.asList(1, 2, 3);

        List<Integer> resultList = lambdaStreamExcImp.toList(input);
        assertEquals(expected, resultList);
    }

    public void testToList2() {

        Stream<String> input = Stream.of("a", "b", "c");
        List<String> expected = Arrays.asList("a", "b", "c");

        List<String> resultList = lambdaStreamExcImp.toList(input);
        assertEquals(expected, resultList);
    }

    public void testTestCreateIntStream() {
        int start = 1;
        int end = 3;
        List<Integer> expected = Arrays.asList(1, 2, 3); // Expected output inclusive of start and end

        // Call the createIntStream method
        IntStream resultStream = lambdaStreamExcImp.createIntStream(start, end);

        // Collect the result into a List<Integer>
        List<Integer> resultList = resultStream.boxed().collect(Collectors.toList());

        // Assert: Check that the result matches the expected list
        assertEquals(expected, resultList);
    }

    public void testSquareRootIntStream() {
        int[] test ={1, 4, 9};
        List<Double> expected = Arrays.asList(1.0, 2.0, 3.0);

        IntStream input = lambdaStreamExcImp.createIntStream(test);

        DoubleStream resultStream = lambdaStreamExcImp.squareRootIntStream(input);
        List<Double> resultList = resultStream.boxed().collect(Collectors.toList());

        assertEquals(expected, resultList);
    }

    public void testGetOdd() {

        int[] test = {1, 2, 3};
        IntStream input = lambdaStreamExcImp.createIntStream(test);

        List<Integer> expected = Arrays.asList(1, 3);

        IntStream oddStream = lambdaStreamExcImp.getOdd(input);
        List<Integer> resultList = lambdaStreamExcImp.toList(oddStream);
        assertEquals(expected, resultList);

    }

    public void testGetLambdaPrinter() {
    }

    public void testPrintMessages() {
    }

    public void testPrintOdd() {
        int[] test = {1, 2, 3};
        IntStream input = lambdaStreamExcImp.createIntStream(test);

        // Create a list to capture the printed output
        List<String> capturedOutput = new ArrayList<>();
        Consumer<String> printer = capturedOutput::add;

        lambdaStreamExcImp.printOdd(input, printer);

        // Define the expected output
        List<String> expectedOutput = Arrays.asList("1", "3"); // Adjust based on your printing format

        assertEquals(expectedOutput, capturedOutput);
    }

    public void testFlatNestedInt() {
        List<List<Integer>> nestedList = Arrays.asList(
                Arrays.asList(1, 2, 3)
        );
        // Define the expected output
        List<Integer> expected = Arrays.asList(1, 4, 9); // Squares of 1, 2, 3, and 4

        // Create a Stream<List<Integer>> from the nested list
        Stream<List<Integer>> input = nestedList.stream();

        // Call the flatNestedInt method
        Stream<Integer> resultStream = lambdaStreamExcImp.flatNestedInt(input);
        List<Integer> resultList = resultStream.collect(Collectors.toList());

        // Assert: Check that the result matches the expected list
        assertEquals(expected, resultList);
    }
}