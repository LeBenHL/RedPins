package com.example.redpins;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.DataSetObserver;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class ListviewFragment extends ListFragment implements OnClickListener{
	private Button mapviewButton;
	private ListView listView;
	private ImageButton homeButton;
	protected JSONArray jsonArr;
	private String searchTerm;
	private String searchLoc;
	private Double latitude;
	private Double longitude;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("Listview On Create", "ON CREATE");
		View view = inflater.inflate(R.layout.listview_fragment, container, false);
		//((MainActivity) getActivity()).hideNaviFrag();
		homeButton = (ImageButton) view.findViewById(R.id.home_button);
		homeButton.setOnClickListener(this);
		mapviewButton = (Button) view.findViewById(R.id.button_to_mapview);
		mapviewButton.setOnClickListener(this);
		listView = (ListView) view.findViewById(android.R.id.list);
		listView.setClickable(true);
		TextView searchText = (TextView) view.findViewById(R.id.searched_term);
		searchTerm = getArguments().getString("query");
		searchText.setText(searchTerm);
		searchLoc = getArguments().getString("location");
		latitude = getArguments().getDouble("latitude");
		longitude = getArguments().getDouble("longitude");
		GetEventListTask task = new GetEventListTask();
		task.execute();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				// TODO Auto-generated method stub
				View view = (View) listView.getItemAtPosition(position);
				System.out.println("CLICKED");
				((MainActivity) getActivity()).hideListviewFrag();
				((MainActivity) getActivity()).showEventFrag(view.getTag().toString());
			}

		});
		Log.v("Listview OnCreate", "View Returned");
		return view;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_to_mapview:
			//go to mapView
			((MainActivity) getActivity()).toggleMapviewFrag();
			break;
		}
	}

	protected OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			System.out.println("CLICKED");
			((MainActivity) getActivity()).hideListviewFrag();
			//		showMapviewFrag();
			((MainActivity) getActivity()).showEventFrag(v.getTag().toString());
		}
	};

	private void populateList(){
		Log.v("Populate List", "Populate List");
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
				v.setOnClickListener(listener);
				TextView eventName = (TextView) v.findViewById(R.id.event_name);
				ImageView eventImage = (ImageView) v.findViewById(R.id.event_image);
				TextView eventDesc = (TextView) v.findViewById(R.id.event_description);
				TextView eventAddr = (TextView) v.findViewById(R.id.event_address);
				TextView eventTime = (TextView) v.findViewById(R.id.event_time);
				//event tags
				// Likes/Dislikes
				//eventImage.setRes...
				double lat;
				double lng;
				JSONObject json;
				try {
					if (jsonArr == null) {
						System.out.println("jsonarray is null");
					}
					//System.out.println(jsonArr);
					json = jsonArr.getJSONObject(position);
					System.out.println("JSON"+position+": "+json);
					v.setTag(json.getInt("id"));
					eventName.setText(json.getString("title"));
					eventDesc.setText(json.getString("url"));
					eventAddr.setText(json.getString("location"));
					eventTime.setText(json.getString("start_time"));
					// Create Map Fragment version of it
					Bundle bundle = new Bundle();
					bundle.putString("JSONArr", jsonArr.toString());
					((MainActivity)getActivity()).createMapviewFrag(bundle);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
				if(jsonArr == null){
					return 0;
				}else{
					return jsonArr.length();
				}
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
	}


	public class GetEventListTask extends AsyncTask<Void, Void, JSONArray>{

		//how should i save comments

		@Override
		protected JSONArray doInBackground(Void... arg0) {
			JSONObject json = new JSONObject();
			try {
				json.put("search_query", searchTerm);
				if (searchLoc == null) {
					json.put("latitude", latitude);
					json.put("longitude", longitude);
				} else {
					json.put("location_query", searchLoc);
				}
				json.put("facebook_id", ((MainActivity)getActivity()).getFacebookId());
				json.put("session_token", ((MainActivity)getActivity()).getFacebookSessionToken());
				json.put("page", 1);
				System.out.println("INPUT: " + json.toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			JSONArray ret =null;
			try {
				//sends requests to server and receives
				JSONObject jsonObj;
				if (searchLoc == null) {
					jsonObj = Utility.requestServer(MainActivity.serverURL + "/events/searchViaCoordinates.json", json);
				} else {
					jsonObj = Utility.requestServer(MainActivity.serverURL + "/events/search.json", json);
				}
				System.out.println("RESPONSE: " + jsonObj.toString());
				ret = jsonObj.getJSONArray("events");
				System.out.println("RET: " + ret);
				ret.toString().replace("[", "");
				ret.toString().replace("]", "");
			} catch (JSONException e) {
				System.out.println("Caught Exception");
				e.printStackTrace();
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