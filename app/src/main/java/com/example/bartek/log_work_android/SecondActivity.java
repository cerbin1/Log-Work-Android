package com.example.bartek.log_work_android;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import static android.app.AlertDialog.Builder;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static java.lang.Double.parseDouble;
import static utils.Formatter.formatDouble;

public class SecondActivity extends AppCompatActivity {
    private static final double SALARY_PER_HOUR = 9.0;

    private TextView sumOfWorkedHoursTextView;
    private TextView workHistoryTextView;
    private double sumOfWorkedHours;
    private TableLayout tableLayout;

    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        database = new DatabaseHelper(this);

        String sumOfWorkedHoursString = getSumOfWorkedHoursFromDatabase();
        sumOfWorkedHoursTextView = (TextView) findViewById(R.id.sumOfWorkedHours);
        sumOfWorkedHoursTextView.setText(formatDouble(sumOfWorkedHoursString));
        sumOfWorkedHours = parseDouble(sumOfWorkedHoursString);
        workHistoryTextView = (TextView) findViewById(R.id.workHistory);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
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
        if (data.getCount() == 0) {
            Toast.makeText(this, "No history work found", LENGTH_LONG).show();
            return;
        }
        tableLayout.removeAllViews();
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
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int deletedRows = database.delete(Integer.toString(buttonId));
                    tableLayout.removeView(row);
                    Toast.makeText(SecondActivity.this, deletedRows > 0 ? "Data deleted" : "Data not deleted", Toast.LENGTH_SHORT).show();
                }
            });
            button.setPadding(0, 0, 0, 0);
            button.setBackground(null);
            button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
            row.addView(button);
            tableLayout.addView(row);

        }
    }

    public void deleteSumOfWorkedHours(View view) {
        Builder builder = new Builder(this);
        builder.setMessage("Are you sure you want to delete sum of worked hours?")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        resetTextViewsAndFields();
                        database.clearHistory();
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

    private void resetTextViewsAndFields() {
        sumOfWorkedHoursTextView.setText("0");
        workHistoryTextView.setText("");
        sumOfWorkedHours = 0;
    }

    public void displaySalary(View view) {
        makeText(this, formatDouble(getSalaryAsString()), LENGTH_LONG).show();
    }

    private String getSalaryAsString() {
        return Double.toString((sumOfWorkedHours * SALARY_PER_HOUR));
    }
}
