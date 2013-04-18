package com.example.redpins;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddEventFragment extends Fragment implements OnClickListener, JSONResponseHandler, MapPicker {
	private String locationName;
	private EditText titleField;
	private AutoCompleteTextView locationField;
	private String startTime, endTime;
	private double latitude = -360.0;
	private double longitude = -360.0;
	private Button startDateButton, endDateButton, startTimeButton, endTimeButton, mapButton, createButton;
	private AddEventFragmentButtonListener dateTimeButtonListener;
	private int startYear;
	private int startMonth;
	private int startDay;
	private int startHour;
	private int startMinute;
	private int endYear;
	private int endMonth;
	private int endDay;
	private int endHour;
	private int endMinute;
	
	private static final int startID = 0;
	private static final int endID = 1;
	private static final int startDateID = 0;
	private static final int startTimeID = 1;
	private static final int endDateID = 2;
	private static final int endTimeID = 3;
	private static final int NEWEVENT_TIME_OFFSET_DEFAULT = 75;
	private static final String[] WEEKDAY_ABBREVIATIONS = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
	
    @Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
        try {
            // dateTimeButtonListener = (AddEventFragmentButtonListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AddEventFragmentButtonListener in Activity");
        }
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_event_fragment, container, false);
		
		// Attaching onClickListeners to Buttons
		startDateButton = (Button) view.findViewById(R.id.newevent_startDatePicker);
		startDateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("clicked the start date button");
				showDateDialog();
				//showDialog(startDateID);
			}
		});
		startTimeButton = (Button) view.findViewById(R.id.newevent_startTimePicker);
		startTimeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("clicked the start time button");
			}
		});
		endDateButton = (Button) view.findViewById(R.id.newevent_endDatePicker);
		endDateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("clicked the end date button");
			}
		});
		endTimeButton = (Button) view.findViewById(R.id.newevent_endTimePicker);
		endTimeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("clicked the end time button");
			}
		});
		
		createButton = (Button) view.findViewById(R.id.newevent_create_btn);
		createButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("clicked the create button");
				String defaultTime = "2013-04-19T00:00:00Z";
				startTime = defaultTime;
				endTime = defaultTime;
				//Utility.addEvent(AddEventFragment.this, AddEventFragment.this.titleField.toString() , startTime, endTime, AddEventFragment.this.locationField.toString(), "", latitude, longitude);
				Utility.addEvent(AddEventFragment.this, AddEventFragment.this.titleField.toString(), defaultTime, defaultTime, AddEventFragment.this.locationField.toString(), "http://www.google.com", AddEventFragment.this.latitude, AddEventFragment.this.longitude);
			}
		});
		
		mapButton = (Button) view.findViewById(R.id.newevent_mapButton);
		mapButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("clicked the map button");
				System.out.println("lat long is currently at " + Double.toString(latitude) + "," + Double.toString(longitude));
				
				Bundle data = new Bundle();
				((MainActivity) getActivity()).createAddEventMapFrag(data, AddEventFragment.this);
			}
		});
		
		
		
		Calendar cal = Calendar.getInstance();
		
		cal.add(Calendar.MINUTE, NEWEVENT_TIME_OFFSET_DEFAULT);
		cal.set(Calendar.MINUTE, 0);
		
		
		updateCalendarDisplay(cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR), startID);
		updateTimeDisplay(cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE), startID);
		
		return view;
	}
	
	public void setLatitudeLongitude(double lat, double lng) {
		this.latitude = lat;
		this.longitude = lng;
		System.out.println("Location now set to " + Double.toString(lat) + "," + Double.toString(lng));
	}
	
	/**
	 * Update the Ui and the db record the detail fragment is currently on
	 *  with this date
	 * @param date - the new date
	 */
	public void updateDate(Calendar date){			
		//set this detail fragment's date property
		setDate(date);
		//update the display
		updateUi(date);
		//update the row in the db
		// setDbDate(date);
	}
	
	
	
	private void updateUi(Calendar date){		
		//String shortDateStr = DatesDbHelper.SHORT_DATE_FORMAT.format(date.getTime());
		// mShortDateText.setText(shortDateStr);					
		
		//String longDateStr = DatesDbHelper.LONG_DATE_FORMAT.format(date.getTime());
		//mLongDateText.setText(longDateStr);
		
		updateTimeDisplay(date, startDateID);
	}
	
	public void setDate(Calendar newDate){
		this.startYear = newDate.YEAR;
		this.startMonth = newDate.MONTH;
		this.startDay = newDate.DAY_OF_MONTH;
	}
    
    private void showDateDialog(){
    	Calendar currentDate = Calendar.getInstance();
    	currentDate.set(startYear, startMonth, startDay);
    	dateTimeButtonListener.onSetDateButtonClicked(currentDate);
    }
	
	
	
	private void updateCalendarDisplay(Calendar date, int startEndSelect) {
		updateCalendarDisplay(date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.YEAR), startEndSelect);
	}
	/**
	 * Updates the Button text for the user input calendar popup.
	 */
	private void updateCalendarDisplay(int month, int day, int year, int startEndSelect) {
		Button dateButton;
		switch (startEndSelect) {
		case startID:
			dateButton = startDateButton;
			this.startYear = year;
			this.startMonth = month;
			this.startDay = day;
		case endID:
			dateButton = endDateButton;
			this.endYear = year;
			this.endMonth = month;
			this.endDay = day;
		default:
			dateButton = startDateButton;
		}
		
		Calendar cal = Calendar.getInstance();
		
		if ((day == cal.get(Calendar.DAY_OF_MONTH)) && (month == cal.get(Calendar.MONTH)) && (year == cal.get(Calendar.YEAR))) {
			dateButton.setText("Today");
		}
		else {
			cal.add(Calendar.DAY_OF_MONTH, 1); // Set day to tomorrow
			
			if (day == cal.get(Calendar.DAY_OF_MONTH) && month == cal.get(Calendar.MONTH)
					&& year == cal.get(Calendar.YEAR)) {
				dateButton.setText("Tomorrow");
			} else {
				String dayOfWeek = WEEKDAY_ABBREVIATIONS[new GregorianCalendar(year, month, day)
						.get(Calendar.DAY_OF_WEEK) - 1];
				
				dateButton.setText(dayOfWeek + ", " + (month + 1) + "/" + day);
			}
		}
	}
	
	private void updateTimeDisplay(Calendar time, int startEndSelect) {
		updateTimeDisplay(time.get(Calendar.HOUR_OF_DAY),time.get(Calendar.MINUTE), startEndSelect);
	}

	private void updateTimeDisplay(int hour, int minute, int startEndSelect) {
		Button timeButton;
		switch (startEndSelect) {
		case startID:
			timeButton = startTimeButton;
			this.startHour = hour;
			this.startMinute = minute;
		case endID:
			timeButton = endDateButton;
			this.endHour = hour;
			this.endMinute = minute;
		default:
			timeButton = startTimeButton;
		}
		
		String AmPm;
		int displayTime;
		if (hour > 12) {
			AmPm = "PM";
			displayTime = hour - 12;
		} 
		
		else if (hour == 0) {
			displayTime = 12;
			AmPm = "AM";
		} else {
			displayTime = hour;
			if (hour == 12) {
				AmPm = "PM";
			} else {
				AmPm = "AM";
			}
		}
		
		// The weird boolean part adds an extra zero before the int.toString if minute <
		// 10.
		timeButton.setText(displayTime + ":"
				+ (minute < 10 ? "0" + Integer.toString(minute) : Integer.toString(minute)) + AmPm);
	}
	
	
	private DatePickerDialog.OnDateSetListener dateTimeSetListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			
			updateCalendarDisplay(monthOfYear, dayOfMonth, year, startID);
			
		}
	};
	
	/*
	private StartTimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			
			updateTimeDisplay(hourOfDay, minute);
		}
	};
	*/
	@Override
	public void onClick(View v) {
		
	}
	/*
	
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case startDateID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
		case startTimeID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, false);

		}
		return null;

	}
	*/
    public interface AddEventFragmentButtonListener{
    	public void onSetDateButtonClicked(Calendar date);
    }
	@Override
	public void onNetworkSuccess(int requestCode, JSONObject json) {
		// TODO Auto-generated method stub
		System.out.println(json.toString());
		System.out.println("Response after creating event!");
	}

	@Override
	public void onNetworkFailure(int requestCode, JSONObject json) {
		// TODO Auto-generated method stub
		
	}
}
