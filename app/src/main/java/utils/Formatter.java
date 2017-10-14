package utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import static android.graphics.Color.RED;
import static android.graphics.Color.TRANSPARENT;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class Formatter {
    public static String formatDoubleAsString(String string) {
        return new DecimalFormat("#.#").format(Double.parseDouble(string));
    }

    public static String formatDoubleAsString(double number) {
        return formatDoubleAsString(Double.toString(number));
    }

    public static Toast getToastFormattedAsError(Context context, String string) {
        @SuppressLint("ShowToast") Toast toast = makeText(context, string, LENGTH_SHORT);
        toast.getView().setBackgroundColor(TRANSPARENT);
        TextView textView = toast.getView().findViewById(android.R.id.message);
        textView.setTextColor(RED);
        textView.setTextSize(40);
        return toast;
    }
}
