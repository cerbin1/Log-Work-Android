package com.example.bartek.log_work_android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity {
    private Context context = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setButtonsListeners();
    }

    private void setButtonsListeners() {
        Button addWorkingHoursButton = (Button) findViewById(R.id.addHoursButton);
        addWorkingHoursButton.setOnClickListener(addWorkingHours());

        Button displayWorkingHoursButton = (Button) findViewById(R.id.displayHoursButtonId);
        displayWorkingHoursButton.setOnClickListener(displayWorkingHours());
    }

    @NonNull
    private View.OnClickListener addWorkingHours() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int workingHours = Integer.parseInt(getWorkingHoursCount());
                EditText workingHoursEditText = (EditText) findViewById(R.id.workingHoursEditText);
                workingHours += Integer.parseInt(workingHoursEditText.getText().toString());
                saveWorkingHours();
            }
        };
    }

    private void saveWorkingHours() {
        
    }

    @NonNull
    private View.OnClickListener displayWorkingHours() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Hours worked: " + getWorkingHoursCount();
                makeText(context, message, LENGTH_LONG).show();
            }
        };
    }

    private String getWorkingHoursCount() {
        try {
            InputStream inputStream = context.getAssets().open("working_hours.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String message = "Hours worked: " + reader.readLine();
            makeText(context, message, LENGTH_LONG).show();

            reader.close();
            inputStream.close();
            context.getAssets().close();
            return message;
        } catch (IOException e) {
            Log.d("IOException", e.getMessage());
        }
        return "";
    }
}
