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
    private static final int DATE_PICKER_REQUEST_CODE = 10;
    private final Context CONTEXT = MainActivity.this;

    private String workedHoursAsFormattedString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startSecondActivity(View view) {
        Intent intent = new Intent(CONTEXT, SecondActivity.class);
        intent.putExtra("sumOfWorkedHours", formatDouble(getSumOfWorkedHoursAsString()));
        startActivity(intent);
    }

    public void submitWorkedHours(View view) {
        String input = ((EditText) findViewById(R.id.workedHoursEditText)).getText().toString();
        if (PatternChecker.matches(input)) {
            workedHoursAsFormattedString = formatDouble(input);
            saveSumOfWorkedHoursToFile();

            if (isCustomDateSetChecked()) {
                startDatePickerActivity();
            } else {
                saveWorkedHoursToFileWithCurrentDate();
            }
        } else {
            getToastFormattedAsError(CONTEXT, "Wrong input!", LENGTH_SHORT).show();
        }
    }

    private boolean isCustomDateSetChecked() {
        return ((CheckBox) findViewById(R.id.setDate)).isChecked();
    }

    private void saveWorkedHoursToFileWithCurrentDate() {
        long dateInMillis = getCurrentDate();
        String workHistory = getWorkHistory();
        saveWorkHistory(dateInMillis, workHistory);
    }

    private void saveWorkHistory(long dateInMillis, String workHistory) {
        try {
            saveToFile(dateInMillis, workHistory);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException " + e.getMessage());
        }
    }

    private void saveToFile(long dateInMillis, String workHistory) throws IOException {
        FileOutputStream outputStream = openFileOutput("work_history.txt", Context.MODE_PRIVATE);
        outputStream.write((DateFormat.format("E, d MMMM, yyyy", dateInMillis) + " [" + workedHoursAsFormattedString + "]" + "\n" + workHistory).getBytes());
        outputStream.close();
    }


    private String getWorkHistory() {
        try {
            return getWorkHistoryFromFile();
        } catch (IOException e) {
            Log.e(TAG, "IOException " + e.getMessage());
            return "";
        }
    }

    private String getWorkHistoryFromFile() throws IOException {
        FileInputStream fileInputStream = CONTEXT.openFileInput("work_history.txt");
        InputStreamReader reader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder workHistory = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            workHistory.append(line);
            workHistory.append("\n");
        }
        fileInputStream.close();
        reader.close();
        bufferedReader.close();
        return workHistory.toString();
    }

    private void startDatePickerActivity() {
        Intent intent = new Intent(CONTEXT, DatePickerActivity.class);
        startActivityForResult(intent, DATE_PICKER_REQUEST_CODE);
    }

    private void saveSumOfWorkedHoursToFile() {
        String sumOfWorkedHoursToSave = getFormattedSumOfWorkedHoursToSave();
        try {
            save(sumOfWorkedHoursToSave);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException " + e.getMessage());
        }
    }

    private void save(String sumOfWorkedHoursToSave) throws IOException {
        FileOutputStream outputStream = openFileOutput("sum_of_worked_hours.txt", Context.MODE_PRIVATE);
        outputStream.write(sumOfWorkedHoursToSave.getBytes());
        outputStream.close();
    }

    private String getFormattedSumOfWorkedHoursToSave() {
        return formatDouble(Double.toString(Double.parseDouble(getSumOfWorkedHoursAsString()) + Double.parseDouble(workedHoursAsFormattedString)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DATE_PICKER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                long dateInMillis = data.getLongExtra("Date", 0);
                String workHistory = getWorkHistory();
                saveWorkHistory(dateInMillis, workHistory);
            }
        }
    }

    public long getCurrentDate() {
        return Calendar.getInstance().getTimeInMillis();
    }

    private String getSumOfWorkedHoursAsString() {
        try {
            return getSumOfWorkedHoursFromFile();
        } catch (IOException e) {
            Log.e(TAG, "IOException " + e.getMessage());
            return "";
        }
    }

    private String getSumOfWorkedHoursFromFile() throws IOException {
        FileInputStream fileInputStream = CONTEXT.openFileInput("sum_of_worked_hours.txt");
        InputStreamReader reader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String sumOfWorkedHours = bufferedReader.readLine();

        fileInputStream.close();
        reader.close();
        bufferedReader.close();
        return sumOfWorkedHours == null ? "0" : formatDouble(sumOfWorkedHours);
    }
}
