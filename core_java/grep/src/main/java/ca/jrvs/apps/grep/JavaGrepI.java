package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JavaGrepI implements JavaGrep{

    @Override
    public void process() throws IOException {

    }

    @Override
    public List<File> listFiles(String rootDir) {
        return Collections.emptyList();
    }

    @Override
    public List<String> readLines(File inputFile) {
        return Collections.emptyList();
    }

    @Override
    public boolean containsPattern(String line) {
        return false;
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {

    }

    @Override
    public String getRootPath() {
        return "";
    }

    @Override
    public void setRootPath(String rootPath) {

    }

    @Override
    public String getRegex() {
        return "";
    }

    @Override
    public void setRegex(String regex) {

    }

    @Override
    public String getOutFile() {
        return "";
    }

    @Override
    public void setOutFile(String outFile) {

    }
}
