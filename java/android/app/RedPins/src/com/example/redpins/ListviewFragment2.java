package com.example.redpins;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateUtils;
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
import android.widget.Toast;


public class ListviewFragment2 extends ListFragment implements OnClickListener, JSONResponseHandler {
	private Button mapviewButton;
	private PullToRefreshListView listView;
	private ImageButton homeButton;
	protected JSONArray jsonArr;
	private String searchTerm;
	private String searchLoc;
	private Double latitude;
	private Double longitude;
	private ProgressDialog progress;
	private int page;
	protected ArrayList<JSONObject> jsonList;
	protected Context mContext;
	protected ListFragment fragment;
	private boolean yes;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaZnceState) {
		Log.i("Listview On Create", "ON CREATE");
		View view = inflater.inflate(R.layout.listview_fragment2, container, false);
		//((MainActivity) getActivity()).hideNaviFrag();
		mapviewButton = (Button) view.findViewById(R.id.button_to_mapview);
		mapviewButton.setOnClickListener(this);
		jsonList = new ArrayList<JSONObject>();
		page = 1;
		yes = true;
		fragment = this;
		mContext = getActivity().getApplicationContext();
		listView = (PullToRefreshListView) view.findViewById(R.id.events_listview);
		listView.setClickable(true);		
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				MainActivity.utility.getEventList((JSONResponseHandler) fragment, searchTerm, searchLoc, page);
				//				if(success){
				//					page++;
				//				}
			}
		});
		TextView searchText = (TextView) view.findViewById(R.id.searched_term);
		searchTerm = getArguments().getString("query");
		searchText.setText(searchTerm);
		searchLoc = getArguments().getString("location");
		latitude = getArguments().getDouble("latitude");
		longitude = getArguments().getDouble("longitude");

		progress = MainActivity.utility.addProgressDialog(getActivity(), "Searching", "Searching For Events...");
		if (searchLoc == null) {
			MainActivity.utility.getNearbyEventList(this, searchTerm, latitude, longitude, page);
			page++;
		} else {
			MainActivity.utility.getEventList(this, searchTerm, searchLoc, page);
			page++;
		}

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				// TODO Auto-generated method stub
				View view = (View) listView.getChildAt(position);
				System.out.println("CLICKED");
				//((MainActivity) getActivity()).hideListviewFrag();
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
			// Create Map Fragment version of it
			Bundle bundle = new Bundle();
			bundle.putString("JSONArr", jsonArr.toString());
			bundle.putString("searchTerm", searchTerm);
			((MainActivity)getActivity()).createMapviewFrag(bundle);
			((MainActivity) getActivity()).toggleMapviewFrag();
			break;
		}
	}

	protected OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			System.out.println("CLICKED");
			((MainActivity) getActivity()).toggleListviewFrag();
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
					json = jsonList.get(position);
					System.out.println("JSON"+position+": "+json);
					v.setTag(json.getInt("id"));
					eventName.setText(json.getString("title"));
					eventDesc.setText(json.getString("url"));
					eventAddr.setText(json.getString("location"));
					eventTime.setText(json.getString("start_time"));
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
				if(jsonList == null){
					return 0;
				}else{
					return jsonList.size();
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
			for(int i = 0; i < jsonArr.length();i++){
				try {
					jsonList.add(jsonArr.getJSONObject(i));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(yes){
				populateList();
				yes = false;
			}
			listView.onRefreshComplete();
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
		listView.onRefreshComplete();
		progress.dismiss();
	}
}