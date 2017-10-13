package com.example.bartek.log_work_android.ui;


import android.content.Context;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.bartek.log_work_android.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class Creator {
    private final Context context;

    private Creator() {
        context = null;
    }

    public Creator(Context context) {
        this.context = context;
    }

    public ImageButton createDeleteRecordButton() {
        ImageButton button = new ImageButton(context);
        button.setImageResource(R.mipmap.image_clear);
        button.setPadding(0, 0, 0, 0);
        button.setBackground(null);
        button.setLayoutParams(new TableRow.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
        return button;
    }

    public Button createDeleteAllButton() {
        Button deleteAllButton = new Button(context);
        deleteAllButton.setText("Delete all");
        return deleteAllButton;
    }

    public TextView createLogWorkTextView(String text) {
        TextView textView = new TextView(context);
        textView.setTextSize(20);
        textView.setText(text);
        return textView;
    }
}
