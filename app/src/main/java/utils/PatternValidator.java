package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternValidator {
    public static boolean isValid(String sumOfWorkedHours) {
        Pattern pattern = Pattern.compile("^[0-9]{1,3}([.][5])?$");
        Matcher matcher = pattern.matcher(sumOfWorkedHours);
        return matcher.matches();
    }
}
