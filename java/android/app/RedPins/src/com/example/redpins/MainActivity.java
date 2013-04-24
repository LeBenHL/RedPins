package com.example.redpins;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.Session;

public class MainActivity extends FragmentActivity{

	static Activity activity;
	public FacebookFragment facebookFragment;
	public Fragment appFragment;
	private Fragment searchFragment;
	private Fragment listViewFragment;
	private Fragment googleMapFragment;
	public String mQuery;
	public String mLoc;
	public LocationManager locationManager;

	private Menu _menu;

	public static String serverURL = "http://kantas92.pagekite.me"; //"http://redpins.pagekite.me"; //"http://192.168.5.188:3000";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("GOT CREATED");
		super.onCreate(savedInstanceState);
		activity = this;
		setContentView(R.layout.main_activity);
		if (savedInstanceState == null) {
			// Add the fragment on initial activity setup
			Log.v("MainActivity On Create", "No Restored State Info");
			appFragment = new NavigationFragment();
			getSupportFragmentManager()
			.beginTransaction()
			.add(R.id.mainAppFragment, appFragment)
			//.addToBackStack(null)
			.disallowAddToBackStack()
			.commit();
			facebookFragment = new FacebookFragment();
			getSupportFragmentManager()
			.beginTransaction()
			.add(R.id.facebookFragment, facebookFragment)
			.disallowAddToBackStack()
			.commit();
			searchFragment = new SearchFragment();
			getSupportFragmentManager()
			.beginTransaction()
			.add(R.id.searchFragment, searchFragment)
			.disallowAddToBackStack()
			.commit();
		} else {
			// Or set the fragment from restored state info
			Log.v("MainActivity On Create", "Restored State Info");
			appFragment = getSupportFragmentManager()
					.findFragmentById(R.id.mainAppFragment);
			facebookFragment = (FacebookFragment) getSupportFragmentManager()
					.findFragmentById(R.id.facebookFragment);
			searchFragment = getSupportFragmentManager()
					.findFragmentById(R.id.searchFragment);
		}
		showFacebookFragment();
		locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		_menu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.menu_legal:
			Intent i = new Intent(getApplicationContext(),LegalActivity.class);
			startActivity(i);
			break;
		}
		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

	public void setFacebookMenuLogin() {
		MenuItem facebookMenuItem = _menu.findItem(R.id.logout);
		facebookMenuItem.setTitle("Login");
	}

	public void setFacebookMenuLogout() {
		MenuItem facebookMenuItem = _menu.findItem(R.id.logout);
		facebookMenuItem.setTitle("Logout");
	}

	public void hideFacebookFragment() {
		FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
		tx.hide(facebookFragment);
		tx.show(appFragment);
		tx.show(searchFragment);
		tx.commit();
	}


	public void showFacebookFragment() {
		FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
		tx.show(facebookFragment);
		tx.hide(appFragment);
		tx.hide(searchFragment);
		tx.commit();
	}

	public String getFacebookId() {
		if (facebookFragment._user.getProperty("id") == null) {
			return null;
		}
		return facebookFragment._user.getProperty("id").toString();
	}

	public String getFacebookSessionToken() {
		System.out.println(facebookFragment._session.getAccessToken());
		return facebookFragment._session.getAccessToken();
	}

	public void nearbyOnClick(View view) {
		((NavigationFragment) appFragment).nearbyOnClick(view); 
	}

	public void recommendationsOnClick(View view) {
		((NavigationFragment) appFragment).recommendationsOnClick(view); 
	}

	public void addEventOnClick(View view) {
		((NavigationFragment) appFragment).addEventOnClick(view); 
	}

	public void bookmarksOnClick(View view) {
		((NavigationFragment) appFragment).bookmarksOnClick(view); 
	}

	public void profileOnClick(View view) {
		((NavigationFragment) appFragment).profileOnClick(view); 
	}

	public void historyOnClick(View view) {
		((NavigationFragment) appFragment).historyOnClick(view); 
	}

	public void createListviewFrag(Bundle data){
		System.out.println("showing listview");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		listViewFragment = new ListviewFragment();
		listViewFragment.setArguments(data);
		//Fragment lastAppFragment = appFragment;
		appFragment = listViewFragment;
		//		if (lastAppFragment instanceof NavigationFragment) {
		ft.replace(R.id.mainAppFragment, appFragment);//.addToBackStack(null);			
		//		} else {
		//			ft.replace(R.id.mainAppFragment, appFragment);
		//		}
		ft.addToBackStack("list");
		ft.commit();
		Log.v("onBackPressed","backstack count after adding createListview: "+getSupportFragmentManager().getBackStackEntryCount());
	}

	public void toggleListviewFrag() {
		System.out.println("toggling mapview");
		getSupportFragmentManager().popBackStack("map", FragmentManager.POP_BACK_STACK_INCLUSIVE);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		appFragment = listViewFragment;
		ft.replace(R.id.mainAppFragment, appFragment);
		ft.addToBackStack("list");
		ft.commit();
		Log.v("onBackPressed","backstack count after adding toggle: "+getSupportFragmentManager().getBackStackEntryCount());
	}

	public void createMapviewFrag(Bundle bundle){
		googleMapFragment = new GoogMapFragment();
		googleMapFragment.setArguments(bundle);
	}

	public void toggleMapviewFrag() {
		System.out.println("toggling mapview");
		getSupportFragmentManager().popBackStack("list", FragmentManager.POP_BACK_STACK_INCLUSIVE);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		appFragment = googleMapFragment;
		ft.replace(R.id.mainAppFragment, appFragment);
		ft.addToBackStack("map");
		ft.commit();
		Log.v("onBackPressed","backstack count after adding: "+getSupportFragmentManager().getBackStackEntryCount());
	}

	public void showEventFrag(String eventID){
		System.out.println("showing event page");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Bundle data = new Bundle();
		data.putString("event_id",eventID);
		appFragment = new EventFragment();
		appFragment.setArguments(data);
		ft.replace(R.id.mainAppFragment, appFragment);
		ft.addToBackStack("event");
		ft.commit();
		Log.v("onBackPressed","backstack count after adding: "+getSupportFragmentManager().getBackStackEntryCount());
	}

	public void showBookmarksFrag(){
		System.out.println("showing bookmarks page");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		appFragment = new BookmarksFragment();
		ft.replace(R.id.mainAppFragment, appFragment);
		ft.addToBackStack("bookmark");
		ft.commit();
		Log.v("onBackPressed","backstack count after adding: "+getSupportFragmentManager().getBackStackEntryCount());
	}

	public void hideAddPhotoFragFrag(){
		System.out.println("hiding add photo page");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.remove(facebookFragment).commit();
	}

	public void createAddPhotoFrag(Bundle data) {
		System.out.println("showing add photo page");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		//		Bundle data = new Bundle();
		//		data.putString("prev", prev);
		//		data.putString("event_id",eventID);
		appFragment = new AddPhotoFragment();
		appFragment.setArguments(data);
		ft.replace(R.id.mainAppFragment, appFragment).addToBackStack(null);
		ft.commit();
		//		fragStack.push("bookmark");
	}

	public void createTouchGalleryFrag(Bundle data) {
		Log.i("createTouchGalleryFrag", "Created touch gallery");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		appFragment = new TouchGalleryFragment();
		appFragment.setArguments(data);
		ft.replace(R.id.mainAppFragment, appFragment).addToBackStack(null);
		ft.commit();
	}

	public void createAddEventFrag(Bundle data) {
		Log.i("Create Add Event Frag", "Showing Add Event page");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		appFragment = new AddEventFragment();
		appFragment.setArguments(data);
		ft.replace(R.id.mainAppFragment, appFragment).addToBackStack(null);
		ft.commit();
	}
	
	public void createAddEventMapFrag(Bundle bundle, AddEventFragment addEventFragment) {
		Log.i("createAddEventMapFrag", "Created addEventMap frag");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		appFragment = new AddEventMapFragment();
		((AddEventMapFragment) appFragment).parent = addEventFragment;
		appFragment.setArguments(bundle);
		ft.replace(R.id.mainAppFragment, appFragment).addToBackStack(null);
		ft.commit();
	}

	public void createAddCommentFrag(Bundle bundle) {
		Log.i("createAddCommentFrag", "Created comment frag");
		AddCommentFragment commFragment = new AddCommentFragment();
		commFragment.setArguments(bundle);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		appFragment = commFragment;
		ft.replace(R.id.mainAppFragment, appFragment);
		ft.addToBackStack(null);
		Log.v("onBackPressed","backstack count after adding: "+getSupportFragmentManager().getBackStackEntryCount());
		ft.commit();
	}
	
	public void logoutFacebook(MenuItem item) {
		facebookFragment.authButton.callOnClick();
	}

	public void setRailsAppUrlDialog(MenuItem item) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Set Rails App URL");
		alert.setMessage("URL");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				serverURL = input.getText().toString();
				Log.i("serverURL", serverURL);
				// Do something with value!
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});

		alert.show();
	}

	public void removeMapFragment() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment mapFragment = getSupportFragmentManager()
				.findFragmentById(R.id.map);
		if (mapFragment != null) {
			ft.hide(mapFragment);
			ft.remove(mapFragment);
		}
		ft.commit();
	}

	public void showTimePickerDialog(TimePick fragment, int id) {
		Bundle data = new Bundle();
		data.putInt("id", id);
		TimePickerFragment timeFragment = new TimePickerFragment();
		timeFragment.setArguments(data);
		timeFragment.fragment = fragment;
		timeFragment.show(getSupportFragmentManager(), "timePicker");
	}

	public void showDatePickerDialog(DatePick fragment, int id) {
		Bundle data = new Bundle();
		data.putInt("id", id);
		DatePickerFragment dateFragment = new DatePickerFragment();
		dateFragment.setArguments(data);
		dateFragment.fragment = fragment;
		dateFragment.show(getSupportFragmentManager(), "datePicker");
	}
	
	public void showTimePickerDialog(TimePick fragment, int id, int currentHour, int currentMinute) {
		Bundle data = new Bundle();
		data.putInt("id", id);
		data.putInt("hour", currentHour);
		data.putInt("minute", currentMinute);
	    TimePickerFragment timeFragment = new TimePickerFragment();
	    timeFragment.setArguments(data);
	    timeFragment.fragment = fragment;
	    timeFragment.show(getSupportFragmentManager(), "timePicker");
	}
	
	public void showDatePickerDialog(DatePick fragment, int id, int currentYear, int currentMonth, int currentDay) {
		Bundle data = new Bundle();
		data.putInt("id", id);
		data.putInt("year", currentYear);
		data.putInt("month", currentMonth);
		data.putInt("day", currentDay);
	    DatePickerFragment dateFragment = new DatePickerFragment();
	    dateFragment.setArguments(data);
	    dateFragment.fragment = fragment;
	    dateFragment.show(getSupportFragmentManager(), "datePicker");
	}
	
	public void makeToast(String text, Integer length) {
		Toast toast = Toast.makeText(this, "REQUEST_LOGIN_USER: Could not connect to server", Toast.LENGTH_SHORT);
		toast.show();
	}

	@Override
	public void onBackPressed() {
		Log.i("onBackPressed", "Back Pressed");
		Log.v("onBackPressed", "Old Fragment: " + appFragment.toString());
		super.onBackPressed();
		getSupportFragmentManager().beginTransaction().hide(appFragment).remove(appFragment).commit();//hide(appFragment).remove(appFragment).commit();
		//getSupportFragmentManager().popBackStack();
		appFragment = getSupportFragmentManager()
				.findFragmentById(R.id.mainAppFragment);
		getSupportFragmentManager().beginTransaction().show(appFragment).add(R.id.mainAppFragment,appFragment).commit();
		//getSupportFragmentManager().beginTransaction().show(appFragment).commit();
		Log.v("onBackPressed","backstack count after: "+getSupportFragmentManager().getBackStackEntryCount());
		Log.v("onBackPressed", "New Fragment: " + appFragment.toString());
	}
}
