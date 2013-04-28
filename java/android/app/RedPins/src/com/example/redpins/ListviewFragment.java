package com.example.redpins;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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


public class ListviewFragment extends ListFragment implements JSONResponseHandler {
	private Button mapviewButton;
	private ListView listView;
	private ImageButton homeButton;
	protected JSONArray jsonArr;
	private String searchTerm;
	private String searchLoc;
	private Double latitude;
	private Double longitude;
	private ProgressDialog progress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaZnceState) {
		Log.i("Listview On Create", "ON CREATE");
		View view = inflater.inflate(R.layout.listview_fragment, container, false);
		//((MainActivity) getActivity()).hideNaviFrag();
		homeButton = (ImageButton) view.findViewById(R.id.home_button);
		mapviewButton = (Button) view.findViewById(R.id.button_to_mapview);
		mapviewButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
				((MainActivity) getActivity()).toggleMapviewFrag();
		      }
		    });
		listView = (ListView) view.findViewById(android.R.id.list);
		listView.setClickable(true);
		TextView searchText = (TextView) view.findViewById(R.id.searched_term);
		searchTerm = getArguments().getString("query");
		searchText.setText(searchTerm);
		searchLoc = getArguments().getString("location");
		latitude = getArguments().getDouble("latitude");
		longitude = getArguments().getDouble("longitude");
		
		progress = MainActivity.utility.addProgressDialog(getActivity(), "Searching", "Searching For Events...");
		if (searchLoc == null) {
			MainActivity.utility.getNearbyEventList(this, searchTerm, latitude, longitude, 1);
		} else {
			MainActivity.utility.getEventList(this, searchTerm, searchLoc, 1);
		}
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				// TODO Auto-generated method stub
				View view = (View) listView.getItemAtPosition(position);
				System.out.println("CLICKED");
				//((MainActivity) getActivity()).hideListviewFrag();
				((MainActivity) getActivity()).showEventFrag(view.getTag().toString());
			}

		});
		Log.v("Listview OnCreate", "View Returned");
		return view;
	}

	protected OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
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


	@Override
	public void onNetworkSuccess(int requestCode, JSONObject json) {
		switch (requestCode) {
		case Utility.REQUEST_GET_EVENTLIST: case Utility.REQUEST_GET_NEARBYEVENTLIST:
			jsonArr = MainActivity.utility.lookupJSONArrayFromJSONObject(json, "events");
			populateList();
			break;
		default:
			System.out.println("Unknown network request with requestCode: " + Integer.toString(requestCode));
		}
		progress.dismiss();
	}

	@Override
	public void onNetworkFailure(int requestCode, JSONObject json) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case Utility.REQUEST_GET_EVENTLIST: case Utility.REQUEST_GET_NEARBYEVENTLIST:
			System.out.println("Error loading list of events");
			break;
		default:
			System.out.println("Unknown network request with requestCode: " + Integer.toString(requestCode));
		}
		progress.dismiss();
	}
}