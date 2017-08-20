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

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setButtonsListeners();
    }

    private void setButtonsListeners() {
        Button addWorkedHoursButton = (Button) findViewById(R.id.addHoursButton);
        addWorkedHoursButton.setOnClickListener(addHours());

        Button displayWorkedHoursButton = (Button) findViewById(R.id.displayHoursButton);
        displayWorkedHoursButton.setOnClickListener(displayHours());
    }

    @NonNull
    private View.OnClickListener addHours() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWorkedHours();
            }
        };
    }

    private void addWorkedHours() {
        int workedHours = Integer.parseInt(getSumOfWorkedHours());
        EditText sumOfWorkedHours = (EditText) findViewById(R.id.workedHoursEditText);
        workedHours += Integer.parseInt(sumOfWorkedHours.getText().toString());
        String workedHoursToSave = Integer.toString(workedHours);
        String filename = "sum_of_worked_hours.txt";

        try {
            FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(workedHoursToSave.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException " + e.getMessage());
        }
    }

    @NonNull
    private View.OnClickListener displayHours() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Hours worked: " + getSumOfWorkedHours();
                makeText(context, message, LENGTH_LONG).show();
            }
        };
    }

    private String getSumOfWorkedHours() {
        try {
            FileInputStream fileInputStream = context.openFileInput("sum_of_worked_hours.txt");
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String sumOfWorkedHours = bufferedReader.readLine();

            fileInputStream.close();
            reader.close();
            bufferedReader.close();
            return sumOfWorkedHours;
        } catch (IOException e) {
            Log.e(TAG, "IOException " + e.getMessage());
        }
        return "";
    }
}
