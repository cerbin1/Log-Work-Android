package com.example.bartek.log_work_android;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import static android.app.AlertDialog.Builder;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static java.lang.Double.parseDouble;
import static utils.Formatter.formatDouble;

public class SecondActivity extends AppCompatActivity {
    private static final double SALARY_PER_HOUR = 9.0;

    private TextView workHistoryTextView;
    private TextView sumOfWorkedHoursTextView;
    private TextView sumOfSalary;
    private double sumOfWorkedHours;
    private TableLayout tableLayout;

    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        database = new DatabaseHelper(this);

        sumOfWorkedHoursTextView = (TextView) findViewById(R.id.sumOfWorkedHours);
        sumOfSalary = (TextView) findViewById(R.id.sumOfSalary);
        workHistoryTextView = (TextView) findViewById(R.id.workHistory);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        String sumOfWorkedHoursString = getSumOfWorkedHoursFromDatabase();
        sumOfWorkedHours = parseDouble(sumOfWorkedHoursString);

        sumOfWorkedHoursTextView.setText(formatDouble(sumOfWorkedHoursString));
        sumOfSalary.setText(formatDouble(getSalaryAsString()));
        displayWorkHistory();

    }

    private String getSumOfWorkedHoursFromDatabase() {
        Cursor cur = database.getSumOfWorkedHours();
        if (cur.moveToFirst()) {
            return Double.toString(cur.getDouble(0));
        }
        return "";
    }

    private void displayWorkHistory() {
        Cursor data = database.getWorkHistory();
        tableLayout.removeAllViews();
        if (data.getCount() == 0) {
            workHistoryTextView.setText("Work history is empty");
            return;
        }
        Button deleteAll = new Button(this);
        deleteAll.setText("Delete all");
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Builder builder = new Builder(SecondActivity.this);
                builder.setMessage("Are you sure you want to delete sum of worked hours?")
                        .setCancelable(false)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                resetTextViewsAndFields();
                                database.clearHistory();
                                tableLayout.removeAllViews();
                                makeText(SecondActivity.this, "Deleted", LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.create().show();
            }
        });
        tableLayout.addView(deleteAll);
        while (data.moveToNext()) {
            String builder = data.getString(1) + " "
                    + "[ " + data.getString(2) + " ]";
            final int buttonId = Integer.parseInt(data.getString(0));
            final TableRow row = new TableRow(this);
            row.setBackgroundResource(R.drawable.border);
            TextView textView = new TextView(this);
            textView.setTextSize(20);
            textView.setText(builder);
            row.addView(textView);
            ImageButton button = new ImageButton(this);
            button.setImageResource(R.mipmap.image_clear);
            final double workedHoursOfDeletedRecord = Double.parseDouble(data.getString(2));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sumOfWorkedHours -= workedHoursOfDeletedRecord;
                    int deletedRows = database.delete(Integer.toString(buttonId));
                    tableLayout.removeView(row);
                    Toast.makeText(SecondActivity.this, deletedRows > 0 ? "Data deleted" : "Data not deleted", Toast.LENGTH_SHORT).show();
                    sumOfWorkedHoursTextView.setText(formatDouble(getSumOfWorkedHoursFromDatabase()));
                    sumOfSalary.setText(formatDouble(getSalaryAsString()));
                    if (sumOfWorkedHoursTextView.getText().equals("0")) {
                        sumOfSalary.setText("0");
                        tableLayout.removeAllViews();
                        workHistoryTextView.setText("Work history is empty");
                    }
                }
            });
            button.setPadding(0, 0, 0, 0);
            button.setBackground(null);
            button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
            row.addView(button);
            tableLayout.addView(row);

        }
    }

    private void resetTextViewsAndFields() {
        sumOfWorkedHoursTextView.setText("0");
        sumOfSalary.setText("0");
        workHistoryTextView.setText("Work history is empty");
        sumOfWorkedHours = 0;
    }

    private String getSalaryAsString() {
        return Double.toString((sumOfWorkedHours * SALARY_PER_HOUR));
    }
}
