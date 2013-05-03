package com.example.redpins;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProfileFragment extends Fragment implements OnClickListener, JSONResponseHandler{

	private ProgressDialog progress;
	private ListView bookmarksListView;
	private ListView myEventsListView;
	private JSONArray likesArr;
	private JSONArray eventsArr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.profile_fragment, container, false);

		progress = MainActivity.utility.addProgressDialog(getActivity(), "Loading", "Loading your profile...");
		bookmarksListView = (ListView) view.findViewById(R.id.bookmarksListView);
		myEventsListView = (ListView) view.findViewById(R.id.myEventsListView);
		MainActivity.utility.getUserProfile(this);
		// MainActivity.utility.getSimpleRecommendations(this);
		return view;
	}

	private void populateLikes(){
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
				if(likesArr.length()==0){
					v = inflater.inflate(R.layout.null_list, null);
					TextView text = (TextView) v.findViewById(R.id.text_view);
					text.setText("You Have No Liked Events");
				}else{
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
					TextView eventLikes = (TextView) v.findViewById(R.id.event_likes);
					TextView eventDislikes = (TextView) v.findViewById(R.id.event_dislikes);
					JSONObject json;
					try {
						if (likesArr == null) {
							System.out.println("jsonarray is null");
						}
						//System.out.println(jsonArr);
						json = likesArr.getJSONObject(position);
						System.out.println("JSON"+position+": "+json);
						v.setTag(json.getInt("id"));
						eventName.setText(json.getString("title"));
						eventDesc.setText(json.getString("description"));
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
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
				if(likesArr.length()==0){
					return 1;
				}else{
					return likesArr.length();
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
		bookmarksListView.setAdapter(adapter);
	}

	private void populateEvents(){
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
				if(eventsArr.length()==0){
					v = inflater.inflate(R.layout.null_list, null);
					TextView text = (TextView) v.findViewById(R.id.text_view);
					text.setText("You Have No Created Events");
				}else{
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
					TextView eventLikes = (TextView) v.findViewById(R.id.event_likes);
					TextView eventDislikes = (TextView) v.findViewById(R.id.event_dislikes);
					JSONObject json;
					try {
						if (eventsArr == null) {
							System.out.println("jsonarray is null");
						}
						//System.out.println(jsonArr);
						json = eventsArr.getJSONObject(position);
						System.out.println("JSON"+position+": "+json);
						v.setTag(json.getInt("id"));
						eventName.setText(json.getString("title"));
						eventDesc.setText(json.getString("description"));
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
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
				if(eventsArr.length()==0){
					return 1;
				}else{
					return eventsArr.length();
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
		myEventsListView.setAdapter(adapter);
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
		case Utility.REQUEST_GET_USER_PROFILE:
			likesArr = MainActivity.utility.lookupJSONArrayFromJSONObject(json, "likedEvents");
			eventsArr = MainActivity.utility.lookupJSONArrayFromJSONObject(json, "myEvents");
			populateLikes();
			populateEvents();
			break;
		default:
			System.out.println("Unknown network request with requestCode: " + Integer.toString(requestCode));
		}
		progress.dismiss();
	}

	@Override
	public void onNetworkFailure(int requestCode, JSONObject json) {

	}

	@Override
	public void onClick(View arg0) {

	}

}
