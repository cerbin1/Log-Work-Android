package com.example.bartek.log_work_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

import utils.PatternChecker;

import static android.widget.Toast.LENGTH_SHORT;
import static utils.Formatter.formatDouble;
import static utils.Formatter.getToastFormattedAsError;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final Context CONTEXT = MainActivity.this;

    private String input;
    long dateInMillis;

    private double sumOfWorkedHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sumOfWorkedHours = getSumOfWorkedHours();
    }

    public void startSecondActivity(View view) {
        Intent intent = new Intent(CONTEXT, SecondActivity.class);
        intent.putExtra("sumOfWorkedHours", formatDouble(Double.toString(sumOfWorkedHours)));
        startActivity(intent);
    }

    public void submitWorkedHours(View view) {
        input = ((EditText) findViewById(R.id.workedHoursEditText)).getText().toString();
        if (PatternChecker.matches(input)) {
            double workedHoursToSave = sumOfWorkedHours + Double.parseDouble(input);
            try {
                String filename = "sum_of_worked_hours.txt";
                FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(Double.toString(workedHoursToSave).getBytes());
                outputStream.close();
            } catch (FileNotFoundException e) {
                Log.e(TAG, "FileNotFoundException " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException " + e.getMessage());
            }

            CheckBox setDate = (CheckBox) findViewById(R.id.setDate);
            if (setDate.isChecked()) {
                Intent intent = new Intent(CONTEXT, DatePickerActivity.class);
                startActivityForResult(intent, 10);
            } else {
                dateInMillis = getCurrentDate();
                StringBuilder builder = new StringBuilder();
                readWorkHistoryFromFile(builder);
                try {
                    FileOutputStream outputStream = openFileOutput("work_history.txt", Context.MODE_PRIVATE);
                    outputStream.write((DateFormat.format("E, d MMMM, yyyy", dateInMillis) + " [" + input + "]" + "\n" + builder).getBytes());
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "FileNotFoundException " + e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, "IOException " + e.getMessage());
                }
            }
        } else {
            getToastFormattedAsError(CONTEXT, "Wrong input!", LENGTH_SHORT).show();
        }
    }

    private double getSumOfWorkedHours() {
        String string = getSumOfWorkedHoursFromFile();
        return string.equals("") ? 0 : Double.parseDouble(string);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                dateInMillis = data.getLongExtra("Date", 10);

                StringBuilder builder = new StringBuilder();
                readWorkHistoryFromFile(builder);
                try {
                    FileOutputStream outputStream = openFileOutput("work_history.txt", Context.MODE_PRIVATE);
                    outputStream.write((DateFormat.format("E, d MMMM, yyyy", dateInMillis) + " [" + input + "]" + "\n" + builder).getBytes());
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "FileNotFoundException " + e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, "IOException " + e.getMessage());
                }
            }
        }
    }

    public long getCurrentDate() {
        return Calendar.getInstance().getTime().getTime();
    }

    private void readWorkHistoryFromFile(StringBuilder text) {
        try {
            FileInputStream fileInputStream = CONTEXT.openFileInput("work_history.txt");
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                text.append(line);
                text.append("\n");
            }
            fileInputStream.close();
            reader.close();
            bufferedReader.close();
        } catch (IOException e) {
            Log.e(TAG, "IOException " + e.getMessage());
        }
    }

    private String getSumOfWorkedHoursFromFile() {
        try {
            FileInputStream fileInputStream = CONTEXT.openFileInput("sum_of_worked_hours.txt");
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String sumOfWorkedHours = bufferedReader.readLine();

            fileInputStream.close();
            reader.close();
            bufferedReader.close();
            return formatDouble(sumOfWorkedHours);
        } catch (IOException e) {
            Log.e(TAG, "IOException " + e.getMessage());
            return "";
        }
    }
}
