package ca.jrvs.apps.grep;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImp{

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
        javaGrepLambdaImp.setRegex(args[0]);
        javaGrepLambdaImp.setRootPath(args[1]);
        javaGrepLambdaImp.setOutFile(args[2]);

        try{
            javaGrepLambdaImp.process();
        } catch (Exception e) {
            javaGrepLambdaImp.logger.error("Error: Unable to process", e);
        }
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {
        Files.write(Paths.get("out", getOutFile()), lines);
    }

    @Override
    public List<String> readLines(File inputFile) {
        try (Stream<String> lines = Files.lines(inputFile.toPath())) {
            return lines.collect(Collectors.toList());
        } catch (IOException e) {
            this.logger.error("Error: Unable to read lines from file", e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<File> listFiles(String rootDir) {
        try {
            return Files.walk(Paths.get(rootDir)).filter(Files::isRegularFile).map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            this.logger.error("Error: Unable to list files", e);
            return Collections.emptyList();
        }
    }

    @Override
    public void process() throws IOException {
        List<String> matchedLines = listFiles(getRootPath()).stream().flatMap(file -> {
                    try {
                        return readLines(file).stream();
                    } catch (IllegalArgumentException e) {
                        this.logger.error("Error: IllegalArgumentException while reading lines", e);
                        return Stream.empty();
                    }
                }).filter(this::containsPattern).collect(Collectors.toList());
        writeToFile(matchedLines);
    }
}
