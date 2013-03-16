package com.example.redpins;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		if(savedInstanceState==null){
			View view = inflater.inflate(R.layout.map_fragment, container, false);
			//Fragment mapFrag = (Fragment) getFragmentManager().findFragmentById(R.id.map);
			mMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			//mMap.setOnInfoWindowClickListener(this);
			homeButton = (ImageButton) view.findViewById(R.id.home_button);
			homeButton.setOnClickListener(this);
			listviewButton = (Button) view.findViewById(R.id.button_to_listview);
			listviewButton.setOnClickListener(this);
			JSONArray jsonArr = null;
			System.out.println(getArguments().getString("JSONArr"));
			try {
				jsonArr = new JSONArray(getArguments().getString("JSONArr"));
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for(int i = 0; i < 10 ; i++){
				try {
					addPins(jsonArr.getJSONObject(i));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			mapView = view;
			return view;
//		}else{
//			return getView();
//		}
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		//should take user to event page that corresponds to the event
		//		((MainActivity) getActivity()).hideMapviewFrag();
		//		((MainActivity) getActivity()).showEventFrag(event_id, "map");
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
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//	v.setTag(1, event_id);
				// Returning the view containing InfoWindow contents
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
			((MainActivity) getActivity()).showListviewFrag();
			((MainActivity) getActivity()).hideMapviewFrag();
			break;

		}
	}

}