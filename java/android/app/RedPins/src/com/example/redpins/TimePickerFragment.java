package com.example.redpins;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment
                            implements TimePickerDialog.OnTimeSetListener {
	
	public TimePick fragment;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        int hour = getArguments().getInt("hour");
        int minute = getArguments().getInt("minute");

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    	Log.i("TimePicker", "onTimeSet");
    	fragment.onTimeSet(hourOfDay, minute, getArguments().getInt("id"));
    }
}