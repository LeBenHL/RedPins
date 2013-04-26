package com.example.redpins;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEventFragment extends Fragment implements OnClickListener, TimePick, DatePick, JSONResponseHandler, MapPicker{
	private String startTimestamp, endTimestamp;
	private EditText titleField;
	private AutoCompleteTextView locationField;
	private double latitude = -360.0;
	private double longitude = -360.0;
	private Button startDateButton, endDateButton, startTimeButton, endTimeButton, mapButton, createButton;
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
	
	static final int startDateID = 0;
	static final int startTimeID = 1;
	static final int endDateID = 2;
	static final int endTimeID = 3;
	static final int NEWEVENT_DEFAULT_END_HOUR_OFFSET = 1;
	static final String[] WEEKDAY_ABBREVIATIONS = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_event_fragment, container, false);
		titleField = (EditText) view.findViewById(R.id.newevent_title_field);
		locationField = (AutoCompleteTextView) view.findViewById(R.id.newevent_locationField);
		
		// Attaching onClickListeners to Buttons
		startDateButton = (Button) view.findViewById(R.id.newevent_startDatePicker);
		startDateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("clicked the start date button");
				((MainActivity) getActivity()).showDatePickerDialog(AddEventFragment.this, startDateID, startYear, startMonth, startDay);
			}
		});
		startTimeButton = (Button) view.findViewById(R.id.newevent_startTimePicker);
		startTimeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("clicked the start time button");
				((MainActivity) getActivity()).showTimePickerDialog(AddEventFragment.this, startTimeID, startHour, startMinute);
			}
		});
		endDateButton = (Button) view.findViewById(R.id.newevent_endDatePicker);
		endDateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).showDatePickerDialog(AddEventFragment.this, endDateID, endYear, endMonth, endDay);
			}
		});
		endTimeButton = (Button) view.findViewById(R.id.newevent_endTimePicker);
		endTimeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("clicked the end time button");
				((MainActivity) getActivity()).showTimePickerDialog(AddEventFragment.this, endTimeID, endHour, endMinute);
			}
		});
		createButton = (Button) view.findViewById(R.id.newevent_create_btn);
		createButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("clicked the create button");
				updateTimestamps();
				MainActivity.utility.addEvent(AddEventFragment.this, titleField.getText().toString(), startTimestamp, endTimestamp, locationField.getText().toString(), "http://www.redpins.com", latitude, longitude, "");
			}
		});
		mapButton = (Button) view.findViewById(R.id.newevent_mapButton);
		mapButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("lat long is currently at " + Double.toString(latitude) + "," + Double.toString(longitude));
				Bundle data = new Bundle();
				((MainActivity) getActivity()).createAddEventMapFrag(data, AddEventFragment.this);
			}
		});
		initDefaultDateTime();
		return view;
	}
	
	private void initDefaultDateTime() {
		System.out.println("AddEventFragment: Initializing default date time values");
		Calendar currentDateTime = Calendar.getInstance();
		setDate(currentDateTime, startDateID);
		setTime(currentDateTime, startTimeID);
		currentDateTime.add(Calendar.HOUR_OF_DAY, NEWEVENT_DEFAULT_END_HOUR_OFFSET);
		setDate(currentDateTime, endDateID);
		setTime(currentDateTime, endTimeID);
		updateTimestamps();
	}
	
	private void updateTimestamps() {
		// TODO: Establish UTC standard later on.
		// In ISO8601 Format
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
		Calendar newCalendar = Calendar.getInstance();
		newCalendar.set(startYear, startMonth, startDay, startHour, startMinute, 0);
		newCalendar = MainActivity.utility.convertLocalCalendarToUTCCalendar(newCalendar);
		startTimestamp = df.format(newCalendar.getTime());
		newCalendar = Calendar.getInstance();
		newCalendar.set(endYear, endMonth, endDay, endHour, endMinute, 0);
		newCalendar = MainActivity.utility.convertLocalCalendarToUTCCalendar(newCalendar);
		endTimestamp = df.format(newCalendar.getTime());
	}
	
	private void setDate(Calendar newDateTime, int startEndSelect) {
		if (startEndSelect == startDateID) {
			this.startYear = newDateTime.get(Calendar.YEAR);
			this.startMonth = newDateTime.get(Calendar.MONTH);
			this.startDay = newDateTime.get(Calendar.DAY_OF_MONTH);
		} else if (startEndSelect == endDateID) {
			this.endYear = newDateTime.get(Calendar.YEAR);
			this.endMonth = newDateTime.get(Calendar.MONTH);
			this.endDay = newDateTime.get(Calendar.DAY_OF_MONTH);
		}
		updateDateButtonText(startEndSelect);
	}
	
	private void setTime(Calendar newDateTime, int startEndSelect) {
		if (startEndSelect == startTimeID) {
			this.startHour = newDateTime.get(Calendar.HOUR_OF_DAY);
			this.startMinute = newDateTime.get(Calendar.MINUTE);
		} else if (startEndSelect == endTimeID) {
			this.endHour = newDateTime.get(Calendar.HOUR_OF_DAY);
			this.endMinute = newDateTime.get(Calendar.MINUTE);
		}
		updateTimeButtonText(startEndSelect);
	}
	
	private void updateDateButtonText(int startEndSelect) {
		if ((startEndSelect != startDateID) && (startEndSelect != endDateID)) {
			return;
		}
		Button dateButton;
		int year, month, day;
		if (startEndSelect == startDateID) {
			year = startYear;
			month = startMonth;
			day = startDay;
			dateButton = startDateButton;
		} else {
			year = endYear;
			month = endMonth;
			day = endDay;
			dateButton = endDateButton;
		}
		Calendar currentCalendar = Calendar.getInstance();
		if ((day == currentCalendar.get(Calendar.DAY_OF_MONTH)) && (month == currentCalendar.get(Calendar.MONTH)) && (year == currentCalendar.get(Calendar.YEAR))) {
			dateButton.setText("Today");
		} else {
			currentCalendar.add(Calendar.DAY_OF_MONTH, 1);
			if (day == currentCalendar.get(Calendar.DAY_OF_MONTH) && month == currentCalendar.get(Calendar.MONTH) && year == currentCalendar.get(Calendar.YEAR)) {
				dateButton.setText("Tomorrow");
			} else {
				String dayOfWeek = WEEKDAY_ABBREVIATIONS[new GregorianCalendar(year, month, day).get(Calendar.DAY_OF_WEEK) - 1];
				dateButton.setText(dayOfWeek + ", " + (month + 1) + "/" + day);
			}
		}
	}
	
	private void updateTimeButtonText(int startEndSelect) {
		if ((startEndSelect != startTimeID) && (startEndSelect != endTimeID)) {
			return;
		}
		Button timeButton;
		int hour, minute;
		if (startEndSelect == startTimeID) {
			hour = startHour;
			minute = startMinute;
			timeButton = startTimeButton;
		} else {
			hour = endHour;
			minute = endMinute;
			timeButton = endTimeButton;
		}
		timeButton.setText((hour < 10 ? "0" + Integer.toString(hour) : Integer.toString(hour)) + ":" + (minute < 10 ? "0" + Integer.toString(minute) : Integer.toString(minute)));
	}
	
	public int compareCalendar(Calendar calendar1, Calendar calendar2) {
		Date date1 = calendar1.getTime();
		Date date2 = calendar2.getTime();
		if (date1.getTime() < date2.getTime()) {
			return -1;
		} else if (date1.getTime() == date2.getTime()) {
			return 0;
		} else {
			return 1;
		}
	}
	
	@Override
	public void onClick(View v) {
		
	}
	
	@Override
	public void onTimeSet(int hourOfDay, int minute, int id) {
		Calendar currentCalendar = Calendar.getInstance();
		currentCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		currentCalendar.set(Calendar.MINUTE, minute);
		Toast toast = null;
		if (id == startTimeID) {
			currentCalendar.set(Calendar.YEAR, startYear);
			currentCalendar.set(Calendar.MONTH, startMonth);
			currentCalendar.set(Calendar.DAY_OF_MONTH, startDay);
			Calendar maxCalendar = getEndCalendar();
			if (compareCalendar(currentCalendar, maxCalendar) > 0) {
				toast = Toast.makeText(getActivity(), "REQUEST_ADD_EVENT: Start time must occur before end time.", Toast.LENGTH_SHORT);
			} else {
				setTime(currentCalendar, id);
			}
		} else if (id == endTimeID) {
			currentCalendar.set(Calendar.YEAR, endYear);
			currentCalendar.set(Calendar.MONTH, endMonth);
			currentCalendar.set(Calendar.DAY_OF_MONTH, endDay);
			Calendar minCalendar = getStartCalendar();
			if (compareCalendar(minCalendar, currentCalendar) > 0) {
				toast = Toast.makeText(getActivity(), "REQUEST_ADD_EVENT: End time must occur after start time.", Toast.LENGTH_SHORT);
			} else {
				setTime(currentCalendar, id);
			}
		}
		if (toast != null) {
			toast.show();
		}
	}

	@Override
	public void onDateSet(int year, int month, int day, int id) {
		Calendar currentCalendar = Calendar.getInstance();
		currentCalendar.set(Calendar.YEAR, year);
		currentCalendar.set(Calendar.MONTH, month);
		currentCalendar.set(Calendar.DAY_OF_MONTH, day);
		Toast toast = null;
		if (id == startDateID) {
			currentCalendar.set(Calendar.HOUR_OF_DAY, startHour);
			currentCalendar.set(Calendar.MINUTE, startMinute);
			Calendar maxCalendar = getEndCalendar();
			if (compareCalendar(currentCalendar, maxCalendar) > 0) {
				toast = Toast.makeText(getActivity(), "REQUEST_ADD_EVENT: Start date must occur before end date.", Toast.LENGTH_SHORT);
			} else {
				setDate(currentCalendar, id);
			}
		} else if (id == endDateID) {
			currentCalendar.set(Calendar.HOUR_OF_DAY, endHour);
			currentCalendar.set(Calendar.MINUTE, endMinute);
			Calendar minCalendar = getStartCalendar();
			if (compareCalendar(minCalendar, currentCalendar) > 0) {
				toast = Toast.makeText(getActivity(), "REQUEST_ADD_EVENT: End date must occur after start date.", Toast.LENGTH_SHORT);
			} else {
				setDate(currentCalendar, id);
			}
		}
		if (toast != null) {
			toast.show();
		}
	}
	
	public Calendar getStartCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(startYear, startMonth, startDay, startHour, startMinute);
		return calendar;
	}
	
	public Calendar getEndCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(endYear, endMonth, endDay, endHour, endMinute);
		return calendar;
	}

	@Override
	public void setLatitudeLongitude(double lat, double lng) {
		this.latitude = lat;
		this.longitude = lng;
		System.out.println("Location now set to " + Double.toString(lat) + "," + Double.toString(lng));
	}

	@Override
	public void onNetworkSuccess(int requestCode, JSONObject json) {
		switch (requestCode) {
		case Utility.REQUEST_ADD_EVENT:
			System.out.println(json.toString());
			System.out.println("Response after creating event!");	
			break;
		}
	}

	@Override
	public void onNetworkFailure(int requestCode, JSONObject json) {
		// TODO Auto-generated method stub
		
	}
}