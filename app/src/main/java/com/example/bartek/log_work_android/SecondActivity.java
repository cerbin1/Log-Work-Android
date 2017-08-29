package com.example.bartek.log_work_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static utils.Formatter.formatDouble;

public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private final double SALARY_PER_HOUR = 9.0;
    private TextView sumOfWorkedHoursTextView;
    private double sumOfWorkedHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        String sumOfWorkedHoursString = getIntent().getExtras().getString("string");
        sumOfWorkedHoursTextView = (TextView) findViewById(R.id.sumOfWorkedHours);
        sumOfWorkedHoursTextView.setText(sumOfWorkedHoursString);
        sumOfWorkedHours = Double.parseDouble(sumOfWorkedHoursString);

        try {
            FileInputStream fileInputStream = SecondActivity.this.openFileInput("work_history.txt");
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder text = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                text.append("\n");
                text.append(line);
            }
            ((TextView) findViewById(R.id.workHistory)).setText(text);
            fileInputStream.close();
            reader.close();
            bufferedReader.close();
        } catch (IOException e) {
            Log.e(TAG, "IOException " + e.getMessage());
        }
    }

    public void deleteSumOfWorkedHours(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete sum of worked hours?")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        makeText(SecondActivity.this, "Deleted", LENGTH_SHORT).show();
                        try {
                            clearFiles();
                        } catch (FileNotFoundException e) {
                            Log.e(TAG, "FileNotFoundException " + e.getMessage());
                        } catch (IOException e) {
                            Log.e(TAG, "IOException " + e.getMessage());
                        }
                        sumOfWorkedHoursTextView.setText("0");
                        sumOfWorkedHours = 0;
                        ((TextView) findViewById(R.id.workHistory)).setText("");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void clearFiles() throws IOException {
        clearSumOfWorkedHoursFile();
        clearWorkHistoryFile();
    }

    private void clearWorkHistoryFile() throws IOException {
        FileOutputStream outputStream = openFileOutput("work_history.txt", Context.MODE_PRIVATE);
        outputStream.write("".getBytes());
        outputStream.close();
    }

    private void clearSumOfWorkedHoursFile() throws IOException {
        FileOutputStream outputStream = openFileOutput("sum_of_worked_hours.txt", Context.MODE_PRIVATE);
        outputStream.write("0".getBytes());
        outputStream.close();
    }

    public void displaySalary(View view) {
        makeText(SecondActivity.this, formatDouble(getSalaryAsString()), LENGTH_LONG).show();
    }

    private String getSalaryAsString() {
        return Double.toString((sumOfWorkedHours * SALARY_PER_HOUR));
    }
}
