package ca.jrvs.apps.grep;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepImp implements JavaGrep {

    final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

    private String regex;
    private String rootPath;
    private String outFile;

    @Override
    public void process() throws IOException {
        List<String> matchedLines = new ArrayList<>();
        for (File file : listFiles(this.rootPath)) {
            for (String line : readLines(file)) {
                if (containsPattern(line)) {
                    matchedLines.add(line);
                }
            }
        }
        writeToFile(matchedLines);
    }

    @Override
    public List<File> listFiles(String rootDir) {
        List<File> files = new ArrayList<>();
        try {
            Stream<Path> pathStream = Files.list(Paths.get(this.rootPath));
            files = pathStream.map(Path::toFile).filter(File::isFile).collect(Collectors.toList());
        } catch (IOException e) {
            this.logger.error("Error: Unable to list files", e);
        }
        return files;
    }

    @Override
    public List<String> readLines(File inputFile) throws IllegalArgumentException {
        if (!inputFile.isFile()) throw new IllegalArgumentException();
        List<String> lines = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(inputFile));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            this.logger.error("Error: File not found", e);
        } catch (IOException e) {
            this.logger.error("Error: IOException", e);
        }
        return lines;
    }

    @Override
    public boolean containsPattern(String line) {
        Pattern pattern = Pattern.compile(this.regex);
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {
        BufferedWriter writer;
        writer = new BufferedWriter(new FileWriter("out\\" + this.outFile));
        for (String line : lines) {
            writer.write(line);
            writer.newLine();
        }
        writer.close();
    }

    @Override
    public String getRootPath() {
        return this.rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getRegex() {
        return this.regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String getOutFile() {
        return this.outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        BasicConfigurator.configure();
        JavaGrepImp grep = new JavaGrepImp();

        grep.setRegex(args[0]);
        grep.setRootPath(args[1]);
        grep.setOutFile(args[2]);

        try{
            grep.process();
        } catch (Exception e) {
            grep.logger.error("Error: Unable to process", e);
        }
    }
}