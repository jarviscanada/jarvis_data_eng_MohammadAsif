package ca.jrvs.apps.practice;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LambdaStreamExcImp implements LambdaStreamExc {

    @Override
    public Stream<String> createStrStream(String... strings) {
        return Arrays.stream(strings);
    }

    @Override
    public Stream<String> toUpperCase(String... strings) {
        return Arrays.stream(strings).map(String::toUpperCase);
    }

    @Override
    public Stream<String> filter(Stream<String> stringStream, String pattern) {
        Pattern pat = Pattern.compile(pattern);
        return stringStream.filter(string -> !pat.matcher(string).find());
    }

    @Override
    public IntStream createIntStream(int[] arr) {
        return Arrays.stream(arr);
    }

    @Override
    public <E> List<E> toList(Stream<E> stream) {
        return stream.collect(Collectors.toList());
    }

    @Override
    public List<Integer> toList(IntStream intStream) {
        return intStream.boxed().collect(Collectors.toList());
    }

    @Override
    public IntStream createIntStream(int start, int end) {
        return IntStream.range(start, end+1);
    }

    @Override
    public DoubleStream squareRootIntStream(IntStream intStream) {
        return intStream.mapToDouble(Math::sqrt);
    }

    @Override
    public IntStream getOdd(IntStream intStream) {
        return intStream.filter(n -> n % 2 == 1);
    }

    @Override
    public Consumer<String> getLambdaPrinter(String prefix, String suffix) {
        Consumer<String> res = string -> System.out.println(prefix + string + suffix);
        return res;
    }

    @Override
    public void printMessages(String[] messages, Consumer<String> printer) {
        for (String message : messages) {
            printer.accept(message);
        }
    }

    @Override
    public void printOdd(IntStream intStream, Consumer<String> printer) {
        IntStream oddStream = getOdd(intStream);
        oddStream.mapToObj(Integer::toString).forEach(printer::accept);
    }

    @Override
    public Stream<Integer> flatNestedInt(Stream<List<Integer>> ints) {
        return ints.flatMap(Collection::stream).map(n -> (int)Math.pow(n, 2));
    }
}