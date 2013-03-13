package com.example.redpins;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class GoogMapFragment extends Fragment implements OnClickListener,OnInfoWindowClickListener{
	
	private Button listviewButton;
	private ImageButton homeButton;
	private GoogleMap mMap;
	
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
////		MapFragment map = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
////		map.onAttach(getActivity());
//	}
//	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.map_fragment, container, false);
		Fragment mapFrag = (Fragment) getFragmentManager().findFragmentById(R.id.map);
		mMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.setOnInfoWindowClickListener(this);
		homeButton = (ImageButton) view.findViewById(R.id.home_button);
		homeButton.setOnClickListener(this);
		listviewButton = (Button) view.findViewById(R.id.button_to_listview);
		listviewButton.setOnClickListener(this);
		for(int i = 0; i < 10 ; i++){
			addPins(i*10);
		}
		return view;
	}
	
	@Override
	public void onInfoWindowClick(Marker marker) {
		//should take user to event page that corresponds to the event
//		Intent i = new Intent(getActivity().getApplicationContext(),EventActivity.class);
		//i.putExtra("event_id", );
//		startActivity(i);
		((MainActivity) getActivity()).showEventFrag();
	}
    
	private void addPins(int x){
		Location eventLoc = new Location(Context.LOCATION_SERVICE);
		LatLng loc = new LatLng(eventLoc.getLatitude(),(x*10)+eventLoc.getLongitude());
		mMap.setInfoWindowAdapter(new InfoWindowAdapter() {

			@Override
			public View getInfoWindow(Marker marker) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public View getInfoContents(Marker marker) {
				// TODO Auto-generated method stub
				// Getting view from the layout file info_window_layout
				View v = getActivity().getLayoutInflater().inflate(R.layout.event_info, null);

				TextView eventName = (TextView) v.findViewById(R.id.event_name);
				ImageView eventImage = (ImageView) v.findViewById(R.id.event_image);
				TextView eventDesc = (TextView) v.findViewById(R.id.event_description);
				TextView eventAddr = (TextView) v.findViewById(R.id.event_address);
				TextView eventTime = (TextView) v.findViewById(R.id.event_time);
				//event tags
				// Likes/Dislikes

				eventName.setText("MY EVENT:");
				eventImage.setImageResource(R.drawable.ic_launcher);
				eventDesc.setText("Desc:");
				eventAddr.setText("Addr:");
				eventTime.setText("Time:");

				// Returning the view containing InfoWindow contents
				return v;
			}
		});
		mMap.addMarker(new MarkerOptions().position(loc).title("LOC: " + x).snippet("desc"+"\n"+"address"));
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
switch (v.getId()) {
case R.id.home_button:
	((MainActivity) getActivity()).hideMapviewFrag();
	break;
case R.id.button_to_listview:
	//go to viewView
	((MainActivity)getActivity()).showListviewFrag();
	((MainActivity)getActivity()).hideMapviewFrag();
	break;
	
}
	}
}