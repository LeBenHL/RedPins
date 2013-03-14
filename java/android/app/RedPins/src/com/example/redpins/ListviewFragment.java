package com.example.redpins;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.method.HideReturnsTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class ListviewFragment extends ListFragment implements OnClickListener{
	private Button listviewButton;
	private Button mapviewButton;
	private LinearLayout listviewLayout;
	private LinearLayout mapviewLayout;
	private ListView listView;
	private ActionBar actionBar;
	private ImageButton homeButton;
	protected JSONArray jsonArr;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.listview_fragment, container, false);
		homeButton = (ImageButton) view.findViewById(R.id.home_button);
		homeButton.setOnClickListener(this);
		mapviewButton = (Button) view.findViewById(R.id.button_to_mapview);
		mapviewButton.setOnClickListener(this);
		listView = (ListView) view.findViewById(android.R.id.list);
		GetEventListTask task = new GetEventListTask();
		task.execute();
		return view;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_button:
			((MainActivity) getActivity()).hideListviewFrag();
			((MainActivity) getActivity()).showNaviFrag();
			break;
		case R.id.button_to_mapview:
			//go to mapView
			((MainActivity) getActivity()).hideListviewFrag();
			((MainActivity) getActivity()).showMapviewFrag();
			break;
		}
	}

	private void populateList(){
		ListAdapter adapter = new ListAdapter() {

			@Override
			public void unregisterDataSetObserver(DataSetObserver observer) {
				// TODO Auto-generated method stub

			}

			@Override
			public void registerDataSetObserver(DataSetObserver observer) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public int getViewTypeCount() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v;
				if (convertView == null) {
					v = inflater.inflate(R.layout.event_list, null); 
				} else {
					v = convertView;
				}

				TextView eventName = (TextView) v.findViewById(R.id.event_name);
				ImageView eventImage = (ImageView) v.findViewById(R.id.event_image);
				TextView eventDesc = (TextView) v.findViewById(R.id.event_description);
				TextView eventAddr = (TextView) v.findViewById(R.id.event_address);
				TextView eventTime = (TextView) v.findViewById(R.id.event_time);
				//event tags
				// Likes/Dislikes

				eventName.setText("HIyo "+position);
				//eventImage.setRes...

				double lat;
				double lng;
				JSONObject json;
				try {
					json = jsonArr.getJSONObject(position);
					eventName.setText(json.getString("title"));
					eventDesc.setText(json.getString("url"));
					lat = json.getDouble("lat");
					lng = json.getDouble("lng");
					eventAddr.setText(json.getString("address"));
					String loc =  json.getString("l");//??
					eventTime.setText(json.getString("time"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//				String img =  result.getString("");
				//	String likes =  result.getString("");
				//String dislikes =  result.getString("");
				//				eventName.setText(name);
				//				eventDesc.setText(desc);
				//				eventLoc.setText(loc);
				//				eventTime.setText(time);
				//???eventImg.setImageURI(uri);
				//				eventLikes.setText("LIKES "+likes);
				//				eventDislikes.setText("DISLIKES " + dislikes);
				return v;
			}

			@Override
			public int getItemViewType(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 10;
			}

			@Override
			public boolean isEnabled(int position) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean areAllItemsEnabled() {
				// TODO Auto-generated method stub
				return false;
			}
		};
		listView.setAdapter(adapter);
		//		GetEventListTask task = new GetEventListTask();
		//		task.execute();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		((MainActivity) getActivity()).showEventFrag();
	}

	public class GetEventListTask extends AsyncTask<Void, Void, JSONArray>{

		//how should i save comments

		@Override
		protected JSONArray doInBackground(Void... arg0) {
			JSONObject json = new JSONObject();
			//			try {
			//				
			//			} catch (JSONException e1) {
			//				e1.printStackTrace();
			//			}
			JSONArray ret = null;
			try {
				//sends requests to server and receives
				ret = Utility.requestServerArr("http://dry-wave-1707.herokuapp.com/events/find", json);
			} catch (Throwable e) {
			}
			return ret;
		}
		@Override
		protected void onPostExecute(JSONArray result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			jsonArr = result;
			populateList();

		}
	}


	private Location currLoc;
	private LocationManager mLocationManager;
	public void getCurrLocation(){
		currLoc = null;
		mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		LocationListener locListener = new LocationListener() {
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				currLoc = mLocationManager.getLastKnownLocation(provider);
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				currLoc = mLocationManager.getLastKnownLocation(provider);
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				currLoc = mLocationManager.getLastKnownLocation(provider);

			}

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				currLoc = location;

			}
		};
		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locListener);
	}
}