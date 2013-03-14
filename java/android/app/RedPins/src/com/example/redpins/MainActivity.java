package com.example.redpins;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.Session;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class MainActivity extends FragmentActivity{

	private FacebookFragment facebookFragment;
	private Fragment appFragment;
	private Fragment listFragment;
	private Fragment mapFragment;
	private Fragment eventFragment;
	private GoogleMap mMap;

	public final static String serverURL = "http://dry-wave-1707.herokuapp.com";

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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.hide(appFragment).commit();
	}

	public void showNaviFrag(){
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.show(appFragment).commit();
	}
	
	public void hideListviewFrag(){
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.remove(listFragment).commit();
	}

	public void showListviewFrag(){
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Bundle data = new Bundle();
		data.putString("query","");
		if(listFragment == null){
			listFragment = new ListviewFragment();
			listFragment.setArguments(data);
			ft.add(android.R.id.content, listFragment);
		}
		ft.show(listFragment).commit();
	}


	public void hideMapviewFrag(){
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.remove(mapFragment).commit();
	}

	public void showMapviewFrag(){
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if(mapFragment == null){
			mapFragment = new GoogMapFragment();
			ft.add(android.R.id.content, mapFragment);
		}
		ft.show(mapFragment).commit();
	}

	public void hideEventFrag(){
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.remove(eventFragment).commit();
	}

	public void showEventFrag(){
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Bundle data = new Bundle();
		data.putString("event_id","");//event id!!!!!!!
		if(eventFragment == null){
			eventFragment = new EventFragment();
			eventFragment.setArguments(data);
			ft.add(android.R.id.content, eventFragment);
		}
		ft.show(eventFragment).commit();
	}

	public void eventClicked(View v){
		hideListviewFrag();
		showEventFrag();
	}
	
	public void listviewOnClick(View view){
		hideNaviFrag();
		//		showMapviewFrag();
		showListviewFrag();
	}

	public void logoutFacebook(MenuItem item) {
		facebookFragment.authButton.callOnClick();
	}
}
