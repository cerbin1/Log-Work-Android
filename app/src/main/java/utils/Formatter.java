package utils;

import java.text.DecimalFormat;

public class Formatter {
    public static String formatAsWorkedHours(String sumOfWorkedHours) {
        return new DecimalFormat("#.#").format(Double.parseDouble(sumOfWorkedHours));
    }
}
