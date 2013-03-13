package com.example.redpins;

import java.util.Arrays;

import com.facebook.widget.LoginButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class NavigationFragment extends Fragment{
	
	private View nearbyButton;
	private View recommendationsButton;
	private View addEventButton;
	private View bookmarksButton;
	private View profileButton;
	private View historyButton;
	private View listButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, 
	        Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.navigation_fragment, container, false);
	    nearbyButton = view.findViewById(R.id.Nearby);
	    recommendationsButton = view.findViewById(R.id.Recommendations);
	    addEventButton = view.findViewById(R.id.AddEvent);
	    bookmarksButton = view.findViewById(R.id.Bookmarks);
	    profileButton = view.findViewById(R.id.Profile);
	    historyButton = view.findViewById(R.id.History);
	    listButton = view.findViewById(R.id.list_button);
	    return view;
	}
	
	public void nearbyOnClick(View view) {
		Log.v("buttonClick", "NEARBY");
	}
	
	public void recommendationsOnClick(View view) {
		Log.v("buttonClick", "RECOMMENDATIONS");
	}
	
	public void addEventOnClick(View view) {
		Log.v("buttonClick", "ADDEVENT");
	}
	
	public void bookmarksOnClick(View view) {
		Log.v("buttonClick", "BOOKMARKS");
	}
	
	public void profileOnClick(View view) {
		Log.v("buttonClick", "PROFILE");
	}
	
	public void historyOnClick(View view) {
		Log.v("buttonClick", "HISTORY");
	}
	
}
