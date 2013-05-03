package com.example.redpins.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.redpins.MainActivity;
import com.example.redpins.R;
import com.jayway.android.robotium.solo.Solo;

// Integration Tests for the app
public class InitAppTest extends ActivityInstrumentationTestCase2<MainActivity>{
	private Solo solo;
	
	public InitAppTest() {
		super(MainActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		MainActivity main = getActivity();
		main.setUtility(new MockUtility());
		solo = new Solo(getInstrumentation(), main);
	}
	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

	public void test01_OpenLegalActivity() throws Throwable {
		solo.clickOnMenuItem("Legal Notice");
		solo.assertCurrentActivity("Expected LegalActivity", "LegalActivity");
		solo.goBack();
		solo.assertCurrentActivity("Expected MainActivity", "MainActivity");
	}
	
	public void test02_AddEvent() throws Throwable {
		solo.sleep(2000);
		ImageView addEventView = (ImageView) solo.getCurrentActivity().findViewById(R.id.AddEventImg);
		solo.clickOnView(addEventView);
		solo.sleep(2000);
		EditText titleField = (EditText) solo.getCurrentActivity().findViewById(R.id.newevent_title_field);
		solo.enterText(titleField, "Ridin' solo");
		solo.sleep(5000);
		Button mapButton = (Button) solo.getCurrentActivity().findViewById(R.id.newevent_mapButton);
		solo.clickOnView(mapButton);
		solo.sleep(2000);
		solo.goBack();
		solo.sleep(1000);
		Button createButton = (Button) solo.getCurrentActivity().findViewById(R.id.newevent_create_btn);
		solo.clickOnView(createButton);
	}
	
	public void test03_nearby() throws Throwable {
		solo.sleep(2000);
		ImageView nearbyView = (ImageView) solo.getCurrentActivity().findViewById(R.id.NearbyImg);
		solo.clickOnView(nearbyView);
		solo.sleep(2000);
		assertTrue(solo.searchText("Fake Event 1"));
		assertTrue(solo.searchText("Fake Event 2"));
		assertTrue(solo.searchText("Fake Event 3"));
		assertTrue(solo.searchText("Fake Event 4"));
	}
	
	public void test04_history() throws Throwable{
		solo.sleep(2000);
		ImageView historyView = (ImageView) solo.getCurrentActivity().findViewById(R.id.HistoryImg);
		solo.clickOnView(historyView);
		solo.sleep(2000);
		assertTrue(solo.searchText("Recent Event 1"));
		assertTrue(solo.searchText("Recent Event 2"));
		assertTrue(solo.searchText("Recent Event 3"));
		assertTrue(solo.searchText("Recent Event 4"));
		assertTrue(solo.searchText("Recent Event 5"));
		assertTrue(solo.searchText("Recent Event 6"));
	}
	
	public void test05_bookmarks() throws Throwable{
		solo.sleep(2000);
		ImageView bookmarksView = (ImageView) solo.getCurrentActivity().findViewById(R.id.BookmarksImg);
		solo.clickOnView(bookmarksView);
		solo.sleep(2000);
		assertTrue(solo.searchText("Bookmark Event 1"));
		assertTrue(solo.searchText("Bookmark Event 2"));
	}
	
	public void test06_recommended() throws Throwable{
		solo.sleep(2000);
		ImageView recommendationsView = (ImageView) solo.getCurrentActivity().findViewById(R.id.RecommendationsImg);
		solo.clickOnView(recommendationsView);
		solo.sleep(2000);
		assertTrue(solo.searchText("Recommended Event 1"));
		assertTrue(solo.searchText("Recommended Event 2"));
		assertTrue(solo.searchText("Recommended Event 3"));
	}
}