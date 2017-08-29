package com.example.bartek.log_work_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.Formatter;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {
    private Context context = MainActivity.this;

    private String sumOfWorkedHours;

    private static final String TAG = "MainActivity";

    long dateInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setButtonsListeners();
    }

    public void startSecondActivity(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("string", getSumOfWorkedHours());
        startActivity(intent);
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
        double workedHours = getWorkedHoursOrZero();
        sumOfWorkedHours = ((EditText) findViewById(R.id.workedHoursEditText)).getText().toString();
        Pattern pattern = Pattern.compile("^[0-9]{1,3}([.][5])?$");
        Matcher matcher = pattern.matcher(sumOfWorkedHours);
        if (matcher.matches()) {
            workedHours += Double.parseDouble(sumOfWorkedHours);
            String workedHoursToSave = Double.toString(workedHours);
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

            CheckBox setDate = (CheckBox) findViewById(R.id.setDate);
            if (setDate.isChecked()) {
                Intent intent = new Intent(this, DatePickerActivity.class);
                startActivityForResult(intent, 10);
            } else {
                dateInMillis = getCurrentDate();
                StringBuilder builder = new StringBuilder();
                readWorkHistoryFromFile(builder);
                try {
                    FileOutputStream outputStream = openFileOutput("work_history.txt", Context.MODE_PRIVATE);
                    outputStream.write((DateFormat.format("E, d MMMM, yyyy", dateInMillis) + " [" + sumOfWorkedHours + "]" + "\n" + builder).getBytes());
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "FileNotFoundException " + e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, "IOException " + e.getMessage());
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK) {
                dateInMillis = data.getLongExtra("Date", 10);

                StringBuilder builder = new StringBuilder();
                readWorkHistoryFromFile(builder);
                try {
                    FileOutputStream outputStream = openFileOutput("work_history.txt", Context.MODE_PRIVATE);
                    outputStream.write((DateFormat.format("E, d MMMM, yyyy", dateInMillis) + " [" + sumOfWorkedHours + "]" + "\n" + builder).getBytes());
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "FileNotFoundException " + e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, "IOException " + e.getMessage());
                }
            } else {
                Toast toast = Formatter.getToastFormattedAsError(context, "Wrong input!", LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public long getCurrentDate() {
        return Calendar.getInstance().getTime().getTime();
    }

    private void readWorkHistoryFromFile(StringBuilder text) {
        try {
            FileInputStream fileInputStream = MainActivity.this.openFileInput("work_history.txt");
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

    private double getWorkedHoursOrZero() {
        return getSumOfWorkedHours().equals("") ? 0 : Double.parseDouble(getSumOfWorkedHours());
    }

    @NonNull
    private View.OnClickListener displayHours() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Hours worked: " + getSumOfWorkedHours();
                makeText(context, message, LENGTH_SHORT).show();
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
            return Formatter.formatDouble(sumOfWorkedHours);
        } catch (IOException e) {
            Log.e(TAG, "IOException " + e.getMessage());
            return "";
        }
    }
}
