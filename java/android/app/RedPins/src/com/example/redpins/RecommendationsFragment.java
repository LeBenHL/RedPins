package com.example.redpins;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RecommendationsFragment extends Fragment implements OnClickListener, JSONResponseHandler {

	private JSONArray eventsArray = null;
	private ListView eventsList;
	private ProgressDialog progress;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.recommendations_fragment, container, false);
		eventsList = (ListView) view.findViewById(android.R.id.list);
	    progress = MainActivity.utility.addProgressDialog(getActivity(), "Loading", "Loading Recommendations...");
		MainActivity.utility.getSimpleRecommendations(this);
		return view;
	}
	
	@Override
	public void onClick(View arg0) {		
		// TODO Auto-generated method stub
		
	}
	
	private void populateList(){
		Log.v("Populate List", "Populating Recommendations List");
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
					v = inflater.inflate(R.layout.recommendations_list, null); 
				} else {
					v = convertView;
				}
				v.setOnClickListener(listener);
				int id = -1;
				try {
					id = eventsArray.getJSONObject(position).getInt("id");
					v.setTag(id);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				TextView eventName = (TextView) v.findViewById(R.id.event_name);
				ImageView eventImage = (ImageView) v.findViewById(R.id.event_image);
				TextView eventDesc = (TextView) v.findViewById(R.id.event_description);
				TextView eventAddr = (TextView) v.findViewById(R.id.event_address);
				TextView eventTime = (TextView) v.findViewById(R.id.event_time);
				TextView eventLikes = (TextView) v.findViewById(R.id.event_likes);
				TextView eventDislikes = (TextView) v.findViewById(R.id.event_dislikes);
				//event tags
				// Likes/Dislikes
				//eventImage.setRes...
				JSONObject json;
				try {
					if (eventsArray == null) {
						System.out.println("eventsArray is null");
					}
					//System.out.println(eventsArray);
					json = eventsArray.getJSONObject(position);
					System.out.println("JSON"+position+": "+json);
					v.setTag(json.getInt("id"));
					eventName.setText(json.getString("title"));
					eventDesc.setText(json.getString("url"));
					eventAddr.setText(json.getString("location"));
					eventTime.setText(json.getString("start_time"));
					eventLikes.setText(json.getString("likes"));
					eventDislikes.setText(json.getString("dislikes"));
					boolean photo = json.getBoolean("isPhoto");
					if (photo) {
						String photoPath = json.getString("photo");
						MainActivity.utility.getImage(eventImage, photoPath);
					} else {
						eventImage.setImageResource(R.drawable.ic_launcher);
					}
					// Create Map Fragment version of it
					Bundle bundle = new Bundle();
					bundle.putString("eventsArray", eventsArray.toString());
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
				if(eventsArray == null){
					return 0;
				}else{
					return eventsArray.length();
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
		eventsList.setAdapter(adapter);
	}
	
	protected OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			((MainActivity) getActivity()).showEventFrag(v.getTag().toString());
		}
	};
	
	@Override
	public void onNetworkSuccess(int requestCode, JSONObject json) {
		switch (requestCode) {
		case Utility.REQUEST_GET_RECOMMENDATIONSLIST:
			eventsArray = MainActivity.utility.lookupJSONArrayFromJSONObject(json, "events");
			populateList();
			System.out.println("Successfully get recommendations");
			break;
		default:
			System.out.println("Unknown network request with requestCode: " + Integer.toString(requestCode));
		}
		progress.dismiss();
	}

	@Override
	public void onNetworkFailure(int requestCode, JSONObject json) {
		switch (requestCode) {
		case Utility.REQUEST_GET_RECOMMENDATIONSLIST:
			((MainActivity) getActivity()).makeToast("REQUEST_GET_RECOMMENDATIONSLIST: Error: Unable to get recommendations", Toast.LENGTH_SHORT);
			break;
		default:
			System.out.println("Unknown network request with requestCode: " + Integer.toString(requestCode));
		}
		progress.dismiss();
	}
}