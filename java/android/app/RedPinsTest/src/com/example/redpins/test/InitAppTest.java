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

	public void testOpenLegalActivity() throws Throwable{
		solo.clickOnMenuItem("Legal Notice");
		solo.assertCurrentActivity("Expected LegalActivity", "LegalActivity");
		solo.goBack();
		solo.assertCurrentActivity("Expected MainActivity", "MainActivity");
	}
	
	public void testLogin() throws Throwable{
		
//		solo.clickOnText("Log In");
//		solo.enterText(0, "redpins.berkeley@gmail.com");
//		solo.enterText(1, "lolnebkcuf");
//		solo.clickOnText("Log In");
	}
	
	public void testSearch() throws Throwable{
//		solo.enterText(0, "Korean");
//		solo.clickOnScreen(730, 150);
		//"To the Map"
		//click search button
		//check that current fragment is ListviewFragment
	}
}
