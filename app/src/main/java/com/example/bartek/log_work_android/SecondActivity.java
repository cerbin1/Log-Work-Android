package com.example.bartek.log_work_android;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.bartek.log_work_android.ui.Creator;

import static android.app.AlertDialog.Builder;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static java.lang.Double.parseDouble;
import static utils.Formatter.formatAsDouble;

public class SecondActivity extends AppCompatActivity {
    private static final double SALARY_PER_HOUR = 9.0;

    private TableLayout tableLayout;
    private TextView workHistoryTextView;
    private TextView workedHoursTextView;
    private TextView salaryTextView;

    private double sumOfWorkedHours;

    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        database = new DatabaseHelper(this);

        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        workHistoryTextView = (TextView) findViewById(R.id.workHistory);
        workedHoursTextView = (TextView) findViewById(R.id.sumOfWorkedHours);
        salaryTextView = (TextView) findViewById(R.id.sumOfSalary);

        sumOfWorkedHours = parseDouble(getSumOfWorkedHoursFromDatabase());

        updateSumOfWorkedHours();
        updateSumOfSalary();

        displayWorkHistory();
    }

    private String getSumOfWorkedHoursFromDatabase() {
        Cursor data = database.getSumOfWorkedHours();
        if (data.moveToFirst()) {
            return Double.toString(data.getDouble(0));
        }
        return "0";
    }

    private void updateSumOfWorkedHours() {
        workedHoursTextView.setText("Worked hours: " + formatAsDouble(sumOfWorkedHours));
    }

    private void updateSumOfSalary() {
        salaryTextView.setText("Salary: " + getFormattedSalary());
    }

    private String getFormattedSalary() {
        return formatAsDouble(Double.toString((sumOfWorkedHours * SALARY_PER_HOUR)));
    }

    private void displayWorkHistory() {
        Cursor data = database.getWorkHistory();
        resetTableLayout();
        if (isEmpty(data)) {
            workHistoryTextView.setText("Work history is empty");
            return;
        }
        Button deleteAllButton = new Button(this);
        deleteAllButton.setText("Delete all");
        deleteAllButton.setOnClickListener(createDeleteAllListener());
        tableLayout.addView(deleteAllButton);
        while (data.moveToNext()) {
            String builder = data.getString(1) + " "
                    + "[ " + data.getString(2) + " ]";
            final TableRow row = new TableRow(this);
            row.setBackgroundResource(R.drawable.border);
            TextView textView = new TextView(this);
            textView.setTextSize(20);
            textView.setText(builder);
            row.addView(textView);
            ImageButton button = Creator.createDeleteRecordButton(this);
            button.setOnClickListener(createOnClickListener(row, data));
            row.addView(button);
            tableLayout.addView(row);
        }
    }

    @NonNull
    private View.OnClickListener createOnClickListener(final TableRow row, Cursor data) {
        final int buttonId = Integer.parseInt(data.getString(0));
        final double workedHoursOfDeletedRecord = Double.parseDouble(data.getString(2));

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sumOfWorkedHours -= workedHoursOfDeletedRecord;
                int deletedRows = database.delete(Integer.toString(buttonId));
                tableLayout.removeView(row);
                makeText(SecondActivity.this, deletedRows > 0 ? "Data deleted" : "Data not deleted", LENGTH_SHORT).show();
                updateSumOfWorkedHours();
                updateSumOfSalary();
                if (isEmptySumOfWorkedHours()) {
                    salaryTextView.setText("Salary: 0");
                    resetTableLayout();
                    workHistoryTextView.setText("Work history is empty");
                }
            }

            private boolean isEmptySumOfWorkedHours() {
                return sumOfWorkedHours == 0;
            }
        };
    }


    private void resetTableLayout() {
        tableLayout.removeAllViews();
    }

    private boolean isEmpty(Cursor data) {
        return data.getCount() == 0;
    }

    @NonNull
    private View.OnClickListener createDeleteAllListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Builder builder = new Builder(SecondActivity.this);
                builder.setMessage("Are you sure you want to delete sum of worked hours?")
                        .setCancelable(true)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                removeWholeWorkHistory();
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
        };
    }

    private void removeWholeWorkHistory() {
        workedHoursTextView.setText("Worked hours: 0");
        salaryTextView.setText("Salary: 0");
        workHistoryTextView.setText("Work history is empty");
        sumOfWorkedHours = 0;
        database.clearHistory();
        resetTableLayout();
    }
}
