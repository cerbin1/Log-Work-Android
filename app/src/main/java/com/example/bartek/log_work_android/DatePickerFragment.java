package com.example.bartek.log_work_android;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

import static android.app.DatePickerDialog.OnDateSetListener;

public class DatePickerFragment extends DialogFragment
        implements OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), (OnDateSetListener) getActivity(), year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
    }
}

