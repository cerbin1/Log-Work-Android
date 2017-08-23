package com.example.bartek.log_work_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import utils.Formatter;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

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
    }

    public void deleteSumOfWorkedHours(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete sum of worked hours?")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        makeText(SecondActivity.this, "Deleted", LENGTH_SHORT).show();
                        try {
                            clearSumOfWorkedHours();
                        } catch (FileNotFoundException e) {
                            Log.e(TAG, "FileNotFoundException " + e.getMessage());
                        } catch (IOException e) {
                            Log.e(TAG, "IOException " + e.getMessage());
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        sumOfWorkedHoursTextView.setText("0");
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void clearSumOfWorkedHours() throws IOException {
        FileOutputStream outputStream = openFileOutput("sum_of_worked_hours.txt", Context.MODE_PRIVATE);
        outputStream.write("0".getBytes());
        outputStream.close();
    }

    public void displaySalary(View view) {
        makeText(SecondActivity.this, Formatter.formatDouble(Double.toString((sumOfWorkedHours * SALARY_PER_HOUR))), Toast.LENGTH_LONG).show();
    }
}
