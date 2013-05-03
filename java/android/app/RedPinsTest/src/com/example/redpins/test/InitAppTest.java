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
		solo.sleep(2000);
	}
	
	public void test00_nearby() throws Throwable {
		solo.sleep(2000);
		ImageView nearbyView = (ImageView) solo.getCurrentActivity().findViewById(R.id.NearbyImg);
		solo.clickOnView(nearbyView);
		solo.sleep(2000);
		assertTrue(solo.searchText("Fake Event 1"));
		assertTrue(solo.searchText("Fake Event 2"));
		assertTrue(solo.searchText("Fake Event 3"));
		assertTrue(solo.searchText("Fake Event 4"));
	}
	
		/*
		solo.clickOnScreen(500, 150);
		solo.enterText(0, "Korean");
		solo.clickOnScreen(730, 150);
		assertTrue(solo.searchText("To the Map"));
		assertTrue(solo.searchText("Eric's BBQ'"));
		assertTrue(solo.searchText("Off The Grid"));
		*/
		//"To the Map"
		//click search button
		//check that current fragment is ListviewFragment
		//}
	/*
	public void test3Event() throws Throwable{
		solo.clickOnScreen(500, 150);
		solo.enterText(0, "Korean");
		solo.clickOnScreen(730, 150);
		solo.clickOnText("Eric's BBQ");
		assertTrue(solo.searchText("Add Comment"));
		solo.clickOnText("Add Comment");
		assertTrue(solo.searchText("Write Comments"));
		solo.enterText(2, "HIYO");
		solo.clickOnText("SUBMIT");
		assertTrue(solo.searchText("HIYO"));
		//"To the Map"
		//click search button
		//check that current fragment is ListviewFragment
	}
	
	public void test4Bookmark() throws Throwable{
		solo.clickOnScreen(500, 150);
		solo.enterText(0, "Korean");
		solo.clickOnScreen(730, 150);
		solo.clickOnText("Danceworks");
		solo.clickOnScreen(50, 350);
		solo.goBack();
		solo.goBack();
		solo.clickOnScreen(700, 900);
		assertTrue(solo.searchText("Danceworks"));
		solo.clickOnScreen(200, 50);
		solo.goBack();
		solo.clickOnScreen(700, 900);
		assertFalse(solo.searchText("Danceworks"));
	}
	public void test5Map() throws Throwable{
		solo.clickOnScreen(500, 150);
		solo.enterText(0, "Korean");
		solo.clickOnScreen(730, 150);
		solo.clickOnText("To the Map");
		assertTrue(solo.searchText("To Search List"));
		//"To the Map"
		//click search button
		//check that current fragment is ListviewFragment
	}
	
	public void test6DislikeEvent() throws Throwable {
		solo.clickOnScreen(500, 150);
		solo.enterText(0, "food");
		solo.clickOnScreen(730, 150);
		solo.clickOnText("Off the Grid");
		assertTrue(solo.searchText("Off The Grid"));
		ImageButton dislikeButton = (ImageButton) solo.getCurrentActivity().findViewById(R.id.dislike_button);
		solo.clickOnView(dislikeButton);
		assertTrue(dislikeButton.isSelected());
	}
	
	public void test7LikeEvent() throws Throwable {
		solo.clickOnScreen(500, 150);
		solo.enterText(0, "burger");
		solo.clickOnScreen(730, 150);
		solo.clickOnText("Eric's BBQ");
		ImageButton dislikeButton = (ImageButton) solo.getCurrentActivity().findViewById(R.id.dislike_button);
		ImageButton likeButton = (ImageButton) solo.getCurrentActivity().findViewById(R.id.like_button);
		solo.clickOnView(likeButton);
		assertTrue(likeButton.isSelected());
		assertFalse(dislikeButton.isSelected());
	}
	*/
}
