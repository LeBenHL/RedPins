package com.example.redpins.test;

import com.example.redpins.*;
import com.jayway.android.robotium.solo.Solo;
import android.test.ActivityInstrumentationTestCase2;

public class InitAppTest extends ActivityInstrumentationTestCase2<MainActivity>{
	private Solo solo;
	
	public InitAppTest() {
		super(MainActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}
	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

	public void test1OpenLegalActivity() throws Throwable{
		solo.clickOnMenuItem("Legal Notice");
		solo.assertCurrentActivity("Expected LegalActivity", "LegalActivity");
		solo.goBack();
		solo.assertCurrentActivity("Expected MainActivity", "MainActivity");
	}
	
	public void test2Search() throws Throwable{
		solo.clickOnScreen(500, 150);
		solo.enterText(0, "Korean");
		solo.clickOnScreen(730, 150);
		assertTrue(solo.searchText("To the Map"));
		assertTrue(solo.searchText("Eric's BBQ'"));
		assertTrue(solo.searchText("Off The Grid"));
		//"To the Map"
		//click search button
		//check that current fragment is ListviewFragment
	}
	
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

}
