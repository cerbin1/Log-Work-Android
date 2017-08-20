package com.example.bartek.log_work_android;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

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
                saveWorkingHours();
            }
        };
    }

    private void saveWorkingHours() {
        int workingHours = Integer.parseInt(getWorkingHoursCount());
        EditText workingHoursEditText = (EditText) findViewById(R.id.workingHoursEditText);
        workingHours += Integer.parseInt(workingHoursEditText.getText().toString());
        String FILENAME = "test.txt";
        String string = Integer.toString(workingHours);

        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(string.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("FileNotFoundException", e.getMessage());
        } catch (IOException e) {
            Log.d("IOException", e.getMessage());
        }
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
            FileInputStream fis = context.openFileInput("test.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String workingHours = bufferedReader.readLine();

            fis.close();
            isr.close();
            bufferedReader.close();
            return workingHours;
        } catch (IOException e) {
            Log.d("IOException", e.getMessage());
        }
        return "";
    }
}
