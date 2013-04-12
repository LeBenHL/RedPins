package com.example.redpins;

import java.util.ArrayList;
import java.util.Stack;

import org.apache.http.HttpClientConnection;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.google.android.gms.maps.GoogleMap;

public class MainActivity extends FragmentActivity{
	
	static Activity activity;
	public FacebookFragment facebookFragment;
	public Fragment appFragment;
	private Fragment searchFragment;
	private Fragment listViewFragment;
	private Fragment googleMapFragment;
	public String mQuery;
	public String mLoc;
	private GoogleMap mMap;
//	private Stack<String> fragStack;

	//Facebook User and Session
	private static GraphUser user;
	private static Session session;

	private Menu _menu;

	// public final static String serverURL = "http://nameless-brook-4178.herokuapp.com";
	public final static String serverURL = "http://10.0.0.20:3000"; //"http://redpins.pagekite.me"; //"http://192.168.5.188:3000";
	// public final static String serverURL = "http://safe-savannah-1864.herokuapp.com";
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("GOT CREATED");
		super.onCreate(savedInstanceState);
		activity = this;
		setContentView(R.layout.main_activity);
//		fragStack = new Stack<String>();
		//fragStack.push("navi");
		if (savedInstanceState == null) {
			// Add the fragment on initial activity setup
			Log.v("MainActivity On Create", "No Restored State Info");
			appFragment = new NavigationFragment();
			getSupportFragmentManager()
			.beginTransaction()
			.add(R.id.mainAppFragment, appFragment)
			.commit();
			facebookFragment = new FacebookFragment();
			getSupportFragmentManager()
			.beginTransaction()
			.add(R.id.facebookFragment, facebookFragment)
			.commit();
			searchFragment = new SearchFragment();
			getSupportFragmentManager()
			.beginTransaction()
			.add(R.id.searchFragment, searchFragment)
			.commit();
			//			getSupportFragmentManager()
			//			.beginTransaction().hide(appFragment).commit();
			//			
			//hideFacebookFragment();//temporary	
		} else {
			// Or set the fragment from restored state info
			Log.v("MainActivity On Create", "Restored State Info");
			appFragment = getSupportFragmentManager()
					.findFragmentById(R.id.mainAppFragment);
			facebookFragment = (FacebookFragment) getSupportFragmentManager()
					.findFragmentById(R.id.facebookFragment);
			searchFragment = getSupportFragmentManager()
					.findFragmentById(R.id.searchFragment);

			//	hideFacebookFragment();//temporary
		}
		//		if(user!=null || session != null){
		//			hideFacebookFragment();
		//		}
		// handle the search intent
		//handleIntent(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		_menu = menu;

		// Get the SearchView and set the searchable configuration
		//		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		//		SearchView searchView = (SearchView) findViewById(R.id.search_view);
		//		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		//		searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
		//		searchView.setSubmitButtonEnabled(true); // Enable a submit button

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
		System.out.println(session.getAccessToken());
		return session.getAccessToken();
	}

	//
	//	@Override
	//	protected void onNewIntent(Intent intent) {
	//		setIntent(intent);
	//		handleIntent(intent);
	//	}
	//
	//	// handle the search query intent
	//	private void handleIntent(Intent intent) {
	//
	//		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	//			String query = intent.getStringExtra(SearchManager.QUERY);
	//			//use the query to search your data somehow
	//			Log.v("MainActivity", "THIS IS THE QUERY: " + query);
	//			mQuery = query;
	//			showListviewFrag();
	//			hideNaviFrag();
	//		}
	//	}

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
		ft.hide(appFragment);
		ft.remove(appFragment).commit();
	}

	public void showNaviFrag(){
		System.out.println("showing navi");
		appFragment = new NavigationFragment();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.mainAppFragment,appFragment).commit();
//		fragStack.push("navi");
	}

	public void hideListviewFrag(){
		System.out.println("hiding listview");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.hide(appFragment);
		ft.remove(appFragment).commit();
	}

	public void createListviewFrag(){
		System.out.println("showing listview");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Bundle data = new Bundle();
		System.out.println("mQuery: "+ mQuery);
		data.putString("query",mQuery);
		data.putString("location",mLoc);
		listViewFragment = new ListviewFragment();
		listViewFragment.setArguments(data);
		Fragment lastAppFragment = appFragment;
		appFragment = listViewFragment;
		if (lastAppFragment instanceof NavigationFragment) {
			ft.replace(R.id.mainAppFragment, appFragment).addToBackStack(null);			
		} else {
			ft.replace(R.id.mainAppFragment, appFragment);
		}
		removeMapFragment();
		ft.commit();
//		fragStack.push("list");
	}
	
	public void toggleListviewFrag() {
		System.out.println("toggling mapview");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		appFragment = listViewFragment;
		ft.replace(R.id.mainAppFragment, appFragment);
		ft.remove(getSupportFragmentManager()
				.findFragmentById(R.id.map));
		ft.commit();
	}


	public void hideMapviewFrag(){
		System.out.println("hiding mapview");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.hide(getSupportFragmentManager().findFragmentById(R.id.map));
		ft.hide(appFragment);
		ft.remove(getSupportFragmentManager().findFragmentById(R.id.map));
		ft.remove(appFragment).commit();
	}

	public void createMapviewFrag(Bundle bundle){
		googleMapFragment = new GoogMapFragment();
		googleMapFragment.setArguments(bundle);
		//		fragStack.push("map");
	}
	
	public void toggleMapviewFrag() {
		System.out.println("toggling mapview");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		appFragment = googleMapFragment;
		ft.replace(R.id.mainAppFragment, appFragment);
		ft.commit();
	}

	public void hideEventFrag(){
		System.out.println("hiding event page");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.hide(appFragment);
		ft.remove(appFragment).commit();
	}

	public void showEventFrag(String eventID, String prev){
		System.out.println("showing event page");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Bundle data = new Bundle();
		System.out.println("eventID: "+eventID + ", prev: "+prev);
		data.putString("prev", prev);
		data.putString("event_id",eventID);
		appFragment = new EventFragment();
		appFragment.setArguments(data);
		ft.replace(R.id.mainAppFragment, appFragment).addToBackStack(null);
		ft.commit();
//		fragStack.push("event");
	}

	public void hideBookmarksFrag(){
		System.out.println("hiding bookmarks page");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.hide(appFragment);
		ft.remove(appFragment).commit();
	}

	public void showBookmarksFrag(){
		System.out.println("showing bookmarks page");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		//		Bundle data = new Bundle();
		//		data.putString("prev", prev);
		//		data.putString("event_id",eventID);
		appFragment = new BookmarksFragment();
		//		appFragment.setArguments(data);
		ft.replace(R.id.mainAppFragment, appFragment).commit();
//		fragStack.push("bookmark");
	}
	
	public void hideAddPhotoFragFrag(){
		System.out.println("hiding add photo page");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.remove(facebookFragment).commit();
	}

	public void showAddPhotoFrag(){
		System.out.println("showing add photo page");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		//		Bundle data = new Bundle();
		//		data.putString("prev", prev);
		//		data.putString("event_id",eventID);
		appFragment = new AddPhotoFragment();
		//		appFragment.setArguments(data);
		ft.replace(R.id.mainAppFragment, appFragment).commit();
//		fragStack.push("bookmark");
	}
	
	public void createAddCommentFrag(Bundle bundle) {
		Log.i("createAddCommentFrag", "Created comment frag");
		AddCommentFragment commFragment = new AddCommentFragment();
		commFragment.setArguments(bundle);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		appFragment = commFragment;
		ft.replace(R.id.mainAppFragment, appFragment).addToBackStack(null);
		ft.commit();
	}
	

	public void logoutFacebook(MenuItem item) {
		facebookFragment.authButton.callOnClick();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
//		if(keyCode == KeyEvent.KEYCODE_BACK){
//			String currPage = fragStack.pop();
//			if(fragStack.isEmpty()){
//				return super.onKeyDown(keyCode, event);
//			}
//			String prevPage = fragStack.peek();
//			System.out.println("PREV PAGE: "+prevPage);
//			if(prevPage.equals("navi")){
//				hideListviewFrag();
//				showNaviFrag();
//			}else if(prevPage.equals("list")){
//				hideEventFrag();
//				showListviewFrag();
//			}else if(prevPage.equals("map")){
//				hideEventFrag();
//				showMapviewFrag();
//			}
////			else if(prevPage.equals("event")){
////				showEventFrag(getArguments().getString("event_id"), getArguments().getString("callback"));
////			}
////			else if(prevPage.equals("bookmark")){
////				hideBookmarksFrag();
////			}
//		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void removeMapFragment() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment mapFragment = getSupportFragmentManager()
				.findFragmentById(R.id.map);
		if (mapFragment == null) {
		} else {
			ft.hide(mapFragment);
			ft.remove(mapFragment);
		}
		ft.commit();
	}
	
	@Override
	public void onBackPressed() {
		Log.i("onBackPressed", "Back Pressed");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		removeMapFragment();
		ft.hide(appFragment);
		ft.remove(appFragment);
		Log.v("onBackPressed", "Old Fragment: " + appFragment.toString());
		super.onBackPressed();
		appFragment = getSupportFragmentManager()
				.findFragmentById(R.id.mainAppFragment);
		ft.replace(R.id.mainAppFragment, appFragment);
		ft.show(appFragment);
		ft.commit();
		Log.v("onBackPressed", "New Fragment: " + appFragment.toString());
	}
}
