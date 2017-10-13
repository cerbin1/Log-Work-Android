package com.example.bartek.log_work_android.ui;


import android.content.Context;
import android.widget.ImageButton;
import android.widget.TableRow;

import com.example.bartek.log_work_android.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class Creator {

    public static ImageButton createDeleteRecordButton(Context context) {
        ImageButton button = new ImageButton(context);
        button.setImageResource(R.mipmap.image_clear);
        button.setPadding(0, 0, 0, 0);
        button.setBackground(null);
        button.setLayoutParams(new TableRow.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
        return button;
    }
}
