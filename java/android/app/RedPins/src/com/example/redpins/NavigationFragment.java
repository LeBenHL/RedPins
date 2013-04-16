package com.example.redpins;

import java.util.Arrays;

import com.facebook.widget.LoginButton;

import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
		Log.i("NavigationFragment On Create", "ON CREATE");
	    View view = inflater.inflate(R.layout.navigation_fragment, container, false);
	    nearbyButton = view.findViewById(R.id.Nearby);
	    recommendationsButton = view.findViewById(R.id.Recommendations);
	    addEventButton = view.findViewById(R.id.AddEvent);
	    bookmarksButton = view.findViewById(R.id.Bookmarks);
	    profileButton = view.findViewById(R.id.Profile);
	    historyButton = view.findViewById(R.id.History);
	    return view;
	}
	
	public void nearbyOnClick(View view) {
		Log.v("buttonClick", "NEARBY");
	    Criteria locationCritera = new Criteria();
        locationCritera.setAccuracy(Criteria.ACCURACY_COARSE);
        locationCritera.setAltitudeRequired(false);
        locationCritera.setBearingRequired(false);
        locationCritera.setCostAllowed(true);
        locationCritera.setPowerRequirement(Criteria.NO_REQUIREMENT);
        //String providerName = ((MainActivity) getActivity()).locationManager.getBestProvider(locationCritera, true);
        String providerName = LocationManager.NETWORK_PROVIDER;
        Log.v("buttonClick", "Provider: " + providerName);
        
        if (providerName != null && ((MainActivity) getActivity()).locationManager.isProviderEnabled(providerName)) {
            // Provider is enabled
        	Log.v("buttonClick", "Provider Enabled");
        	((MainActivity) getActivity()).locationManager.requestLocationUpdates(providerName, 100, 1, locationListener);
        	Toast.makeText(getActivity(), "Getting Location Data.....", Toast.LENGTH_SHORT).show();
        } else {
            // Provider not enabled, prompt user to enable it
        	Log.v("buttonClick", "Provider Not Enabled");
            Toast.makeText(getActivity(), "Please turn on Wi-Fi & mobile network location so we can get your location data", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            getActivity().startActivity(myIntent);
        }
	}
	
	public void recommendationsOnClick(View view) {
		Log.v("buttonClick", "RECOMMENDATIONS");
	}
	
	public void addEventOnClick(View view) {
		Log.v("buttonClick", "ADDEVENT");
	}
	
	public void bookmarksOnClick(View view) {
		Log.v("buttonClick", "BOOKMARKS");
		((MainActivity)getActivity()).hideNaviFrag();
		((MainActivity)getActivity()).showBookmarksFrag();
	}
	
	public void profileOnClick(View view) {
		Log.v("buttonClick", "PROFILE");
		((MainActivity)getActivity()).hideNaviFrag();
		((MainActivity)getActivity()).showAddPhotoFrag();
	}
	
	public void historyOnClick(View view) {
		Log.v("buttonClick", "HISTORY");
	}
	
	private final LocationListener locationListener = new LocationListener() {

	    @Override
	    public void onLocationChanged(Location location) {
	    	    Log.v("Location Listener", "Location Changed");
            	Bundle data = new Bundle();
        		((MainActivity) getActivity()).locationManager.removeUpdates(locationListener);
        		data.putString("query", "Everything");
        		data.putDouble("latitude", location.getLatitude());
        		data.putDouble("longitude", location.getLongitude());
        		((MainActivity) getActivity()).createListviewFrag(data);
	    }

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}

	};
	
}
