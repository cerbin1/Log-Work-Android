package com.example.bartek.log_work_android;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.Formatter;

import static android.widget.Toast.LENGTH_SHORT;
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
        double workedHours = getWorkedHoursOrZero();
        String sumOfWorkedHours = ((EditText) findViewById(R.id.workedHoursEditText)).getText().toString();
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
        } else {
            Toast toast = Formatter.getToastFormattedAsError(context, "Wrong input!", LENGTH_SHORT);
            toast.show();
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
            return Formatter.formatAsWorkedHours(sumOfWorkedHours);
        } catch (IOException e) {
            Log.e(TAG, "IOException " + e.getMessage());
            return "";
        }
    }
}
