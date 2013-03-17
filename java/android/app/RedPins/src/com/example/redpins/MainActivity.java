package com.example.redpins;

import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class MainActivity extends FragmentActivity{

	public FacebookFragment facebookFragment;
	private Fragment appFragment;
	private Fragment listFragment;
	public Fragment mapFragment;
	public Fragment eventFragment;
	private String mQuery;
	private GoogleMap mMap;
	
	//Facebook User and Session
	private static GraphUser user;
	private static Session session;

	private Menu _menu;

	// public final static String serverURL = "http://nameless-brook-4178.herokuapp.com";
	public final static String serverURL = "http://safe-savannah-1864.herokuapp.com";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		if (savedInstanceState == null) {
			// Add the fragment on initial activity setup	
			facebookFragment = new FacebookFragment();
			getSupportFragmentManager()
			.beginTransaction()
			.add(android.R.id.content, facebookFragment)
			.commit();
			appFragment = new NavigationFragment();
			getSupportFragmentManager()
			.beginTransaction()
			.add(R.id.mainAppFragment, appFragment)
			.commit();
			getSupportFragmentManager()
			.beginTransaction().hide(appFragment).commit();
			hideFacebookFragment();//temporary	
		} else {
			// Or set the fragment from restored state info
			facebookFragment = (FacebookFragment) getSupportFragmentManager()
					.findFragmentById(android.R.id.content);
			appFragment = getSupportFragmentManager()
					.findFragmentById(R.id.mainAppFragment);
			hideFacebookFragment();//temporary
		}
		handleIntent(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		_menu = menu;

		// Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) findViewById(R.id.search_view);
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
		searchView.setSubmitButtonEnabled(true); // Enable a submit button

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
		getSupportFragmentManager()
		.beginTransaction().hide(facebookFragment).commit();
		getSupportFragmentManager()
		.beginTransaction()
		.show(appFragment)
		.commit();
	}


	public void showFacebookFragment() {
		getSupportFragmentManager()
		.beginTransaction().show(facebookFragment).commit();
		getSupportFragmentManager()
		.beginTransaction()
		.hide(appFragment)
		.commit();
	}
	
	public void setFacebookUser(GraphUser _user) {
		user = _user;
	}
	
	public void setFacebookSession(Session _session) {
		session = _session;
	}
	
	public String getFacebookId() {
		return user.getProperty("id").toString();
	}
	
	public String getFacebookSessionToken() {
		return session.getAccessToken();
	}


	@Override
	protected void onNewIntent(Intent intent) {
		handleIntent(intent);
	}

	// handle the search query intent
	private void handleIntent(Intent intent) {

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			//use the query to search your data somehow
			Log.v("MainActivity", "THIS IS THE QUERY: " + query);
			mQuery = query;
			showListviewFrag();
			hideNaviFrag();
		}
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

	public void hideNaviFrag(){
		System.out.println("hiding navi");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.hide(appFragment).commit();
	}

	public void showNaviFrag(){
		System.out.println("showing navi");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.show(appFragment).commit();
	}

	public void hideListviewFrag(){
		System.out.println("hiding listview");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.hide(listFragment).commit();
	}

	public void showListviewFrag(){
		System.out.println("showing listview");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Bundle data = new Bundle();
		data.putString("query",mQuery);
		listFragment = new ListviewFragment();
		listFragment.setArguments(data);
		ft.add(android.R.id.content, listFragment);
		ft.show(listFragment).commit();
	}


	public void hideMapviewFrag(){
		System.out.println("hiding mapview");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.remove(getSupportFragmentManager().findFragmentById(R.id.map));
		ft.remove(mapFragment).commit();
	}

	public void showMapviewFrag(){
		System.out.println("showing mapview");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if(mapFragment == null){
			mapFragment = new GoogMapFragment();
			ft.add(android.R.id.content, mapFragment).commit();
		}else{
			ft.show(mapFragment).commit();
		}
	}

	public void hideEventFrag(){
		System.out.println("hiding event page");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.hide(eventFragment).commit();
	}

	public void showEventFrag(String prev, String eventID){
		System.out.println("showing event page");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Bundle data = new Bundle();
		data.putString("prev", prev);
		data.putString("event_id",eventID);
		eventFragment = new EventFragment();
		eventFragment.setArguments(data);
		ft.add(android.R.id.content, eventFragment).commit();
	}


	public void listviewOnClick(View view){
		hideNaviFrag();
		//		showMapviewFrag();
		showListviewFrag();
	}

	public void eventClicked(View view){
		System.out.println("CLICKED");
		hideListviewFrag();
		//		showMapviewFrag();
		showEventFrag("list", "1");
	}

	public void logoutFacebook(MenuItem item) {
		facebookFragment.authButton.callOnClick();
	}
}
