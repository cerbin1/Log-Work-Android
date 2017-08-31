package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternChecker {
    public static boolean matches(String sumOfWorkedHours) {
        Pattern pattern = Pattern.compile("^[0-9]{1,3}([.][5])?$");
        Matcher matcher = pattern.matcher(sumOfWorkedHours);
        return matcher.matches();
    }
}
