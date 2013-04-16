package com.example.redpins;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
	private View mapView;
	private HashMap<String, String> hash;
	private ArrayList <LatLng> locArray;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		hash = new HashMap<String, String>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("GoogMapFragment On Create", "ON CREATE");
		View view = inflater.inflate(R.layout.map_fragment, container, false);
		mMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.setOnInfoWindowClickListener(this);
		homeButton = (ImageButton) view.findViewById(R.id.home_button);
		homeButton.setOnClickListener(this);
		listviewButton = (Button) view.findViewById(R.id.button_to_listview);
		listviewButton.setOnClickListener(this);
		locArray = new ArrayList<LatLng>();
		JSONArray jsonArr = null;
		System.out.println(getArguments().getString("JSONArr"));
		try {
			jsonArr = new JSONArray(getArguments().getString("JSONArr"));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i = 0; i < jsonArr.length() ; i++){
			try {
				addPins(jsonArr.getJSONObject(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		double sumLat = 0;
		double sumLng = 0;
		for(int i = 0; i < jsonArr.length();i++){
			LatLng locInfo = locArray.get(i);
			sumLat += locInfo.latitude;
			sumLng += locInfo.longitude;
		}
		double avgLat = sumLat/(double) jsonArr.length();
		double avgLng = sumLng/(double) jsonArr.length();
		mapView = view;
		mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(avgLat,avgLng) , 14.0f) );
		return view;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		System.out.println("WINDOW CLICKED");
		String event_id = hash.get(marker.getId());
		System.out.println("event_idMAP: "+event_id);
		((MainActivity) getActivity()).showEventFrag(event_id);
	}

	private void addPins(JSONObject json){
		Location eventLoc = new Location(Context.LOCATION_SERVICE);
		LatLng loc = null;
		final JSONObject jsonObj = json;
		try {
			loc = new LatLng(json.getDouble("latitude"),json.getDouble("longitude"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		locArray.add(loc);
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
				try {
					eventName.setText(jsonObj.getString("title"));
					//eventImage.setImageResource(R.drawable.ic_launcher);
					eventDesc.setText(jsonObj.getString("url"));
					eventAddr.setText(jsonObj.getString("location"));
					eventTime.setText(jsonObj.getString("start_time")+"~"+ jsonObj.getString("end_time"));
					String event_id = jsonObj.getString("id");
					System.out.println("MARKER ID: "+marker.getId());
					hash.put(marker.getId(), event_id);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return v;
			}
		});
		mMap.addMarker(new MarkerOptions().position(loc).title("LOC: ").snippet("desc"+"\n"+"address"));
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.home_button:
			((MainActivity) getActivity()).hideMapviewFrag();
			((MainActivity) getActivity()).showNaviFrag();
			break;
		case R.id.button_to_listview:
			//go to viewView
			((MainActivity) getActivity()).removeMapFragment();
			((MainActivity) getActivity()).toggleListviewFrag();
//			((MainActivity) getActivity()).toggleListviewFrag();
			break;

		}
	}

}