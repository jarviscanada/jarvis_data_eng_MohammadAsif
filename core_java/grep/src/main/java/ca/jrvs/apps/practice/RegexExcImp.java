package ca.jrvs.apps.practice;

import java.util.regex.Pattern;

public class RegexExcImp implements RegexExc {

    public boolean matchJpeg(String filename) {
        String regex = ".*\\.(jpg|jpeg)$";
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(filename).matches();
    }

    @Override
    public boolean matchIp(String ip) {
        String regex = "^([0-9]{1,3}\\.){3}[0-9]{1,3}$";
        return Pattern.compile(regex).matcher(ip).matches();
    }

    @Override
    public boolean isEmptyLine(String line) {
        String regex = "^\\s*$";
        return line.matches(regex);
    }
}
