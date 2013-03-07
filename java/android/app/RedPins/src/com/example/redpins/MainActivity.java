package com.example.redpins;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.Session;

public class MainActivity extends FragmentActivity {
	
	private FacebookFragment facebookFragment;
	private Fragment appFragment;

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
	    } else {
	        // Or set the fragment from restored state info
	        facebookFragment = (FacebookFragment) getSupportFragmentManager()
	        .findFragmentById(android.R.id.content);
	        appFragment = getSupportFragmentManager()
	    	        .findFragmentById(R.id.mainAppFragment);
	    }
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
    
    public void logoutFacebook(MenuItem item) {
    	facebookFragment.authButton.callOnClick();
    }
}
