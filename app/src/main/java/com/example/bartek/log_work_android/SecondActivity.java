package com.example.bartek.log_work_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class SecondActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        textView = (TextView) findViewById(R.id.sumOfWorkedHours);
        textView.setText(getIntent().getExtras().getString("string"));
    }

    public void deleteSumOfWorkedHours(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete sum of worked hours?")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        makeText(SecondActivity.this, "Deleted", LENGTH_SHORT).show();
                        try {
                            FileOutputStream outputStream = openFileOutput("sum_of_worked_hours.txt", Context.MODE_PRIVATE);
                            outputStream.write("0".getBytes());
                            outputStream.close();
                            textView.setText("0");
                        } catch (FileNotFoundException e) {
                            Log.e("", "FileNotFoundException " + e.getMessage());
                        } catch (IOException e) {
                            Log.e("", "IOException " + e.getMessage());
                        }
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
}
