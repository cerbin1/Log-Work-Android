package com.example.bartek.log_work_android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setButtonsListeners();
    }

    private void setButtonsListeners() {
        Button addWorkHoursButton = (Button) findViewById(R.id.displayHoursButton);
        addWorkHoursButton.setOnClickListener(displayWorkingHoursCount());
    }

    @NonNull
    private View.OnClickListener displayWorkingHoursCount() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                try {
                    displayWorkingHoursCount(context);
                } catch (IOException e) {
                    Log.d("IOException", e.getMessage());
                }
            }
        };
    }

    private void displayWorkingHoursCount(Context context) throws IOException {
        InputStream inputStream = context.getAssets().open("working_hours.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String message = "Hours worked: " + reader.readLine();
        makeText(context, message, LENGTH_LONG).show();

        reader.close();
        inputStream.close();
        context.getAssets().close();
    }
}
