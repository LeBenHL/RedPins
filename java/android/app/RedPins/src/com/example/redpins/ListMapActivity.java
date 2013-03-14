package com.example.redpins;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ListMapActivity extends Activity implements OnClickListener, OnInfoWindowClickListener{
	private GoogleMap mMap;
	private Button listviewButton;
	private Button mapviewButton;
	private LinearLayout listviewLayout;
	private LinearLayout mapviewLayout;
	private ListView listView;
	private ActionBar actionBar;
	private ImageButton homeButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_list_map);
		//setup map
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.setMyLocationEnabled(true);
		
		actionBar = getActionBar();
		actionBar.hide();
		
		homeButton = (ImageButton) findViewById(R.id.home_button);
		homeButton.setOnClickListener(this);
		listviewButton = (Button) findViewById(R.id.button_to_listview);
		listviewButton.setOnClickListener(this);
		mapviewButton = (Button) findViewById(R.id.button_to_mapview);
		mapviewButton.setOnClickListener(this);
		
		mapviewLayout = (LinearLayout) findViewById(R.id.map_layer);
		listviewLayout = (LinearLayout) findViewById(R.id.list_layer);
		listView = (ListView) findViewById(R.id.listview);
		getCurrLocation();
		currLoc = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		mMap.addMarker(new MarkerOptions().position(new LatLng(currLoc.getLatitude(),currLoc.getLongitude())).title("MY LOCATION").snippet("desc"+"\n"+"address"));
		populateList(); //move to asynctask
		mMap.setOnInfoWindowClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.menu_legal:
			Intent i = new Intent(getApplicationContext(),LegalActivity.class);
			startActivity(i);
			break;
		}
		return false;
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
				View v = getLayoutInflater().inflate(R.layout.event_info, null);

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
		switch (v.getId()) {
		case R.id.home_button:
		//	Intent i = new Intent(getApplicationContext(), MainActivity.class);
		//	startActivity(i);
			finish();
			break;
		case R.id.button_to_listview:
			//go to viewView
			listviewLayout.setVisibility(View.VISIBLE);
			mapviewLayout.setVisibility(View.GONE);
			listviewButton.setPressed(true);
			mapviewButton.setPressed(false);
			break;
		case R.id.button_to_mapview:
			//go to mapView
			listviewLayout.setVisibility(View.GONE);
			mapviewLayout.setVisibility(View.VISIBLE);
			mapviewButton.setPressed(true);
			listviewButton.setPressed(false);
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
				LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v;
				if (convertView == null) {
					v = inflater.inflate(R.layout.list_events, null); 
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
				eventDesc.setText("desc" + position);
				eventAddr.setText("addr" + position);
				eventTime.setText("time" + position);

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

	public void eventClicked(View v){
		//should take user to event page that corresponds to the event
		Intent i = new Intent(getApplicationContext(),EventActivity.class);
//		i.putExtra("event_id", v.getTag().toString());
		startActivity(i);
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		//should take user to event page that corresponds to the event
		Intent i = new Intent(getApplicationContext(),EventActivity.class);
		//cross reference between the list of event_id given the name of event or something
		//i.putExtra("event_id", );
		startActivity(i);
	}
	
	public class GetEventListTask extends AsyncTask<Void, Void, JSONObject>{

		//how should i save comments
		
		@Override
		protected JSONObject doInBackground(Void... arg0) {
			JSONObject json = new JSONObject();
			try {
				//adds input values into JSON data object
				json.put("event_id", "");//event_id);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			JSONObject ret = null;
			try {
				//sends requests to server and receives
				ret = Utility.requestServer("http://dry-wave-1707.herokuapp.com/events/find", json);
			} catch (Throwable e) {
			}
			return ret;
		}
		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				populateList();
				String name =  result.getString("title");
				String desc =  result.getString("url");
				String loc =  result.getString("location");
				String time =  result.getString("time");
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
				for(int i = 0;i < 10;i++){
					addPins(i);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
	}
	

	private Location currLoc;
	private LocationManager mLocationManager;
	public void getCurrLocation(){
		currLoc = null;
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
		//mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locListener);
		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locListener);
		// this made it work
	}
}