package com.example.redpins.test;

import com.example.redpins.*;
import com.jayway.android.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
	MainActivity main;
	android.support.v4.app.Fragment appFrag;
	private Solo solo;
	
	public MainActivityTest() {
		super(MainActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
		main = (MainActivity) getActivity();
		appFrag = main.appFragment;
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		solo.finishOpenedActivities();
	}
	
	public void testButtonsExist() throws Exception {
		assertTrue(solo.searchText("Nearby"));
		assertTrue(solo.searchText("Profile"));
		assertTrue(solo.searchText("Add Event"));
		assertTrue(solo.searchText("History"));
		assertTrue(solo.searchText("Bookmarks"));
		assertTrue(solo.searchText("Recommended Events"));
	}

}
