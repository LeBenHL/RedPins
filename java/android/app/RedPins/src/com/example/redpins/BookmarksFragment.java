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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BookmarksFragment extends Fragment implements OnClickListener, JSONResponseHandler {

	private ListView listview;
	private JSONArray jsonArr;
	private ArrayList<Integer> remList;
	private ProgressDialog progress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		remList = new ArrayList<Integer>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.bookmarks_fragment, container, false);
		listview = (ListView) view.findViewById(android.R.id.list);
	    progress = MainActivity.utility.addProgressDialog(getActivity(), "Loading", "Loading Bookmarks...");
		MainActivity.utility.getBookmarks(this, 1);
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	private void populateList(){
		Log.v("Populate List", "Populating Bookmark List");
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
					v = inflater.inflate(R.layout.bookmark_list, null); 
				} else {
					v = convertView;
				}
				v.setOnClickListener(listener);
				CheckBox checkbox = (CheckBox) v.findViewById(R.id.checkbox);
				int id = -1;
				try {
					id = jsonArr.getJSONObject(position).getInt("id");
					v.setTag(id);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				checkbox.setTag(id);
				checkbox.setChecked(true);
				checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						int id = (Integer) buttonView.getTag();
						if(!isChecked){
							remList.add(id);
						}else{
							remList.remove(new Integer(id));
						}
					}
				});
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
		listview.setAdapter(adapter);
	}

	protected OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			System.out.println("CLICKED");
			((MainActivity) getActivity()).showEventFrag(v.getTag().toString());
		}
	};

	@Override
	public void onNetworkSuccess(int requestCode, JSONObject json) {
		switch (requestCode) {
		case Utility.REQUEST_GET_BOOKMARKS:
			jsonArr = MainActivity.utility.lookupJSONArrayFromJSONObject(json, "events");
			populateList();
			System.out.println("Successfully get bookmarks");
			break;
		default:
			System.out.println("Unknown network request with requestCode: " + Integer.toString(requestCode));
		}
		progress.dismiss();
	}

	@Override
	public void onNetworkFailure(int requestCode, JSONObject json) {
		switch (requestCode) {
		case Utility.REQUEST_GET_BOOKMARKS:
			System.out.println("Failed to get bookmarks");
			break;
		default:
			System.out.println("Unknown network request with requestCode: " + Integer.toString(requestCode));
		}
		progress.dismiss();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		for(int i = 0; i < remList.size();i++){
			MainActivity.utility.deleteBookmark(this, remList.get(i).toString());
		}
	}
}
