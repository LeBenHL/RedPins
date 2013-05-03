package com.example.redpins.test;

import android.test.ActivityInstrumentationTestCase2;

import com.example.redpins.MainActivity;
import com.jayway.android.robotium.solo.Solo;

// Unit Tests for MainActivity
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
	MainActivity main;
	private Solo solo;
	
	public MainActivityTest() {
		super(MainActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		main = (MainActivity) getActivity();
		MockUtility mockUtility = new MockUtility();
		main.setUtility(mockUtility);
		solo = new Solo(getInstrumentation(), main);
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
	/*
	public void testAddEventButtonClick() throws Exception {
		solo.clickOnText("Add Event");
		assertTrue(solo.searchText("Cancel"));
		assertTrue(solo.searchText("Create"));
		assertTrue(solo.searchText("Map"));
		solo.goBack();
		assertEquals(solo.getCurrentActivity(), main);
	}
	*/
}
