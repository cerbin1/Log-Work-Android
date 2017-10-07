package utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import static android.graphics.Color.RED;
import static android.graphics.Color.TRANSPARENT;
import static android.widget.Toast.makeText;

public class Formatter {
    public static String formatAsDouble(String string) {
        return new DecimalFormat("#.#").format(Double.parseDouble(string));
    }

    public static Toast getToastFormattedAsError(Context context, String string, int length) {
        @SuppressLint("ShowToast") Toast toast = makeText(context, string, length);
        toast.getView().setBackgroundColor(TRANSPARENT);
        TextView textView = toast.getView().findViewById(android.R.id.message);
        textView.setTextColor(RED);
        textView.setTextSize(40);
        return toast;
    }
}
