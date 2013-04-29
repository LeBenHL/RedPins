package com.example.redpins;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class EventFragment extends Fragment implements OnClickListener, JSONResponseHandler {

	private ImageButton homeButton;
	private ImageButton removeEventButton;
	private TextView eventName;
	private TextView eventURL;
	private TextView eventLoc;
	private TextView eventTime;
	private ImageView eventImg;
	private TextView eventLikes;
	private TextView eventDislikes;
	private TextView eventDesc;
	private ListView commentsList;
	private String event_id;
	private int owner_id;
	private ProgressBar progressBar;
	private String urlLink;
	private String linkBack;
	protected ArrayList<JSONObject> commentArr;
	private Button addCommentButton;
	private Context mContext; 
	private ImageButton likeButton;
	private ImageButton dislikeButton;
	private ImageButton bookmarkButton;
	private boolean bookmarked;
	private ImageButton deleteEventButton;
	private Button uploadPhotoButton;
	private ProgressDialog progress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		System.out.println("CREATED EventFragment");
		View view = inflater.inflate(R.layout.event_fragment, container, false);
		eventName = (TextView) view.findViewById(R.id.event_name);
		eventURL = (TextView) view.findViewById(R.id.event_url);
		eventURL.setOnClickListener(this);
		eventLoc = (TextView) view.findViewById(R.id.event_location);
		eventTime = (TextView) view.findViewById(R.id.event_time);
		eventImg = (ImageView) view.findViewById(R.id.event_photo);
		eventDesc = (TextView) view.findViewById(R.id.event_description);
		eventLikes = (TextView) view.findViewById(R.id.event_like);
		eventDislikes = (TextView) view.findViewById(R.id.event_dislike);
		commentsList = (ListView) view.findViewById(R.id.comment_listview);
		addCommentButton = (Button) view.findViewById(R.id.add_comment_button);
		addCommentButton.setOnClickListener(this);
		removeEventButton = (ImageButton) view.findViewById(R.id.removeEventButton);
		removeEventButton.setOnClickListener(this);
		deleteEventButton = (ImageButton) view.findViewById(R.id.deleteEventButton);
		deleteEventButton.setOnClickListener(this);
		uploadPhotoButton = (Button) view.findViewById(R.id.uploadPhoto);
		uploadPhotoButton.setOnClickListener(this);
		
		if(!((MainActivity)getActivity()).getFacebookId().equals(((MainActivity)getActivity()).getFacebookId())){
			removeEventButton.setVisibility(View.INVISIBLE);
			deleteEventButton.setVisibility(View.INVISIBLE);
		}
		
		likeButton = (ImageButton) view.findViewById(R.id.like_button);
		likeButton.setOnClickListener(this);
		dislikeButton = (ImageButton) view.findViewById(R.id.dislike_button);
		dislikeButton.setOnClickListener(this);
		
		if(event_id==null) {
			event_id = getArguments().getString("event_id");
		}
		
		System.out.println("event_id1: "+event_id);
		linkBack = getArguments().getString("prev");
		progressBar = (ProgressBar) view.findViewById(R.id.event_progress);
		mContext = getActivity().getApplicationContext();
		commentArr = new ArrayList<JSONObject>();
		bookmarkButton = (ImageButton) view.findViewById(R.id.bookmark_button);
		bookmarkButton.setOnClickListener(this);
		
		eventImg.setOnClickListener(this);
		
		// API Requests
		progress = MainActivity.utility.addProgressDialog(getActivity(), "Loading", "Loading Event...", 3);
		MainActivity.utility.getEvent(this, event_id);
		MainActivity.utility.getRatings(this, event_id);
		MainActivity.utility.getComments(this, event_id);

		return view;
	}

	public void updateComments() {
		MainActivity.utility.getComments(this, event_id);
	}
	
	public void uploadPhoto(View view) {
		Bundle data = new Bundle();
		data.putString("event_id", event_id);
		((MainActivity) getActivity()).createAddPhotoFrag(data);
	}
	
	public void removeComment(String comment_user_id, String _comment_id, int position) {
		if(comment_user_id.equals(((MainActivity)getActivity()).getFacebookId())){
			final int pos = position;
			final String comment_id = _comment_id;
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Remove Comment")
			.setMessage("Would you like to remove this comment?")
			.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			})
			.setPositiveButton("remove", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					commentArr.remove(pos);
					populateCommentList();
					MainActivity.utility.deleteComment(EventFragment.this, event_id, comment_id);
				}
			});
			AlertDialog alertDialog = builder.create();
			// Set the Icon for the Dialog
			alertDialog.show();
		}else{
			Toast toast = Toast.makeText(getActivity(), "You are not the author of this comment", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	private void bookmarkEvent() {
		if(!bookmarked){
			bookmarked = true;
			MainActivity.utility.addBookmark(this, event_id);
			Toast toast = Toast.makeText(getActivity(), "This event got bookmarked", Toast.LENGTH_SHORT);
			toast.show();
		}else{
			bookmarked = false;
			MainActivity.utility.deleteBookmark(this, event_id);
			Toast toast = Toast.makeText(getActivity(), "Bookmark for this event is removed", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.event_url:
			// takes user to web browser with given link
			Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlLink));
			startActivity(myIntent);
			break;
		case R.id.add_comment_button:
			Bundle bundle = new Bundle();
			bundle.putString("event_id", event_id);
			bundle.putString("callback", linkBack);
			((MainActivity) getActivity()).createAddCommentFrag(bundle);
			break;
		case R.id.like_button:
			System.out.println("LIKE");
			if (likeButton.isSelected()){
				likeButton.setSelected(false);
				MainActivity.utility.deleteLike(this, event_id);
				break;
			} else if (dislikeButton.isSelected()) {
				MainActivity.utility.deleteLike(this, event_id);
			}
			likeButton.setSelected(true);
			dislikeButton.setSelected(false);
			MainActivity.utility.modifyLike(this, event_id, true);
			break;
		case R.id.dislike_button:
			System.out.println("DISLIKE");
			if (dislikeButton.isSelected()) {
				dislikeButton.setSelected(false);
				MainActivity.utility.deleteLike(this, event_id);
				break;
			} else if (likeButton.isSelected()) {
				MainActivity.utility.deleteLike(this, event_id);
			}
			dislikeButton.setSelected(true);
			likeButton.setSelected(false);
			MainActivity.utility.modifyLike(this, event_id, false);
			break;
		case R.id.bookmark_button:
			bookmarkEvent();
			break;
		case R.id.removeEventButton:
			System.out.println("Are you sure you want to remove the event?");
			MainActivity.utility.cancelEvent(this, event_id);
			break;
		case R.id.deleteEventButton:
			System.out.println("Are you sure you want to delete the event?");
			MainActivity.utility.deleteEvent(this, event_id);
			break;
		case R.id.uploadPhoto:
			Log.i("onClick", "UploadPhoto");
			uploadPhoto(v);
			break;
		case R.id.event_photo:
			Bundle data = new Bundle();
			data.putString("event_id", event_id);
			((MainActivity) getActivity()).createTouchGalleryFrag(data);
			break;
		}
	}

	private void populateCommentList() {
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
				return 1;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				Log.v("POPULATECOMMENT", "getView in populateCommentList called");
				LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v;
				if (convertView == null) {
					v = inflater.inflate(R.layout.comment_list, null); 
				} else {
					v = convertView;
				}

				TextView commentUsername = (TextView) v.findViewById(R.id.comment_username);
				TextView commentDate = (TextView) v.findViewById(R.id.comment_date);
				TextView commentContent = (TextView) v.findViewById(R.id.comment_content);
				final int pos = position;
				JSONObject json;
				try {
					json = commentArr.get(position);
					Log.v("POPULATECOMMENT", "getting json object of commentArr");

					commentUsername.setText("" + json.getString("firstname")+" "+json.getString("lastname"));
					commentDate.setText(json.getString("created_at"));
					commentContent.setText(json.getString("comment"));
					final String facebook_id = json.getString("facebook_id");
					final String comment_id = json.getString("comment_id");
					v.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							System.out.println("item clicked: " + facebook_id);
							removeComment(facebook_id, comment_id, pos);
						}
					});
				} catch (JSONException e) {
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
				if(commentArr == null){
					return 0;
				}else{
					return commentArr.size();
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
		commentsList.setAdapter(adapter);
	}
	
	@Override
	public void onNetworkSuccess(int requestCode, JSONObject json) {
		switch (requestCode) {
		case Utility.REQUEST_GET_EVENT:
			try {
				JSONArray responseJSONArray = json.toJSONArray(json.names());
				if (responseJSONArray.getInt(0) == 1) {
					JSONObject responseJSONObject = responseJSONArray.getJSONObject(1);
					String name = responseJSONObject.getString("title");
					String url = responseJSONObject.getString("url");
					String location = responseJSONObject.getString("location");
					String description = responseJSONObject.getString("description");
					String time = responseJSONObject.getString("start_time");
					owner_id = responseJSONObject.getInt("user_id");
					eventName.setText(name);
					urlLink = url;
					eventURL.setText("URL: " + urlLink);
					eventLoc.setText("Location: " + location);
					eventTime.setText("Time: " + time);
					eventDesc.setText("Description: " + description);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		
		case Utility.REQUEST_GET_COMMENTS:
			JSONArray responseJSONArray = MainActivity.utility.lookupJSONArrayFromJSONObject(json, "comments");
			for (int i = 0; i < responseJSONArray.length(); i++) {
				try {
					System.out.println("JSONObj: " + responseJSONArray.getJSONObject(i));
					commentArr.add(responseJSONArray.getJSONObject(i));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			populateCommentList();
			break;
			
		case Utility.REQUEST_MODIFY_LIKE:
			MainActivity.utility.getRatings(this, event_id);
			break;
			
		case Utility.REQUEST_DELETE_LIKE:
			try {
				eventLikes.setText("LIKES: "+json.getInt("likes"));
				eventDislikes.setText("DISLIKES: " + json.getInt("dislikes"));
				if (json.getInt("likes")+json.getInt("dislikes") != 0) {
					progressBar.setProgress((100*json.getInt("likes"))/(json.getInt("likes")+json.getInt("dislikes")));
				} else {
					progressBar.setProgress(50);
				}
				dislikeButton.setBackgroundColor(Color.TRANSPARENT);
				likeButton.setBackgroundColor(Color.TRANSPARENT);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
			
		case Utility.REQUEST_ADD_BOOKMARK:
			System.out.println("Bookmarked event!");
			break;
			
		case Utility.REQUEST_DELETE_BOOKMARK:
			System.out.println("Deleted bookmark for this event.");
			break;
			
		case Utility.REQUEST_DELETE_COMMENT:
			System.out.println("Deleted comment");
			break;
			
		case Utility.REQUEST_GET_RATINGS:
			try {
				eventLikes.setText("LIKES: "+json.getInt("likes"));
				eventDislikes.setText("DISLIKES: " + json.getInt("dislikes"));
				if (json.getInt("likes")+json.getInt("dislikes") != 0) {
					progressBar.setProgress((100*json.getInt("likes"))/(json.getInt("likes")+json.getInt("dislikes")));
				} else {
					progressBar.setProgress(50);
				}
				if (json.getString("alreadyLikedEvent").equals("true")) {
					if (json.getString("rating").equals("true")) {
						likeButton.setBackgroundColor(Color.GREEN);
						dislikeButton.setBackgroundColor(Color.TRANSPARENT);
						likeButton.setSelected(true);
						dislikeButton.setSelected(false);
					} else {
						likeButton.setBackgroundColor(Color.TRANSPARENT);
						dislikeButton.setBackgroundColor(Color.RED);
						likeButton.setSelected(false);
						dislikeButton.setSelected(true);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case Utility.REQUEST_CANCEL_EVENT:
			try {
				if(json.getInt("errCode")==1){
					Toast toast = Toast.makeText(getActivity(), "This event got cancelled", Toast.LENGTH_SHORT);
					toast.show();
				}else{
					Toast toast = Toast.makeText(getActivity(), "You are not authorized to cancel this event", Toast.LENGTH_SHORT);
					toast.show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case Utility.REQUEST_DELETE_EVENT:
			try {
				if(json.getInt("errCode")==1){
					Toast toast = Toast.makeText(getActivity(), "This event got deleted", Toast.LENGTH_SHORT);
					toast.show();
				}else{
					Toast toast = Toast.makeText(getActivity(), "You are not authorized to delete this event", Toast.LENGTH_SHORT);
					toast.show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		default:
			System.out.println("Unknown network request with requestCode: " + Integer.toString(requestCode));
		}
		progress.dismiss();
	}

	@Override
	public void onNetworkFailure(int requestCode, JSONObject json) {
		Toast toast = null;
		switch (requestCode) {
		case Utility.REQUEST_GET_EVENT:
			toast = Toast.makeText(getActivity(), "REQUEST_GET_EVENT: Could not connect to server.", Toast.LENGTH_SHORT);
			break;
		
		case Utility.REQUEST_GET_COMMENTS:
			toast = Toast.makeText(getActivity(), "REQUEST_GET_COMMENTS: Could not connect to server.", Toast.LENGTH_SHORT);
			break;
			
		case Utility.REQUEST_MODIFY_LIKE:
			toast = Toast.makeText(getActivity(), "REQUEST_MODIFY_LIKE: Could not connect to server.", Toast.LENGTH_SHORT);
			break;
			
		case Utility.REQUEST_ADD_BOOKMARK:
			toast = Toast.makeText(getActivity(), "REQUEST_ADD_BOOKMARK: Could not connect to server.", Toast.LENGTH_SHORT);
			break;
			
		case Utility.REQUEST_DELETE_BOOKMARK:
			toast = Toast.makeText(getActivity(), "REQUEST_DELETE_BOOKMARK: Could not connect to server.", Toast.LENGTH_SHORT);
			break;
			
		case Utility.REQUEST_DELETE_COMMENT:
			toast = Toast.makeText(getActivity(), "REQUEST_DELETE_COMMENT: Could not connect to server.", Toast.LENGTH_SHORT);
			break;
			
		case Utility.REQUEST_GET_RATINGS:
			toast = Toast.makeText(getActivity(), "REQUEST_GET_RATINGS: Could not connect to server.", Toast.LENGTH_SHORT);
			break;
			
		case Utility.REQUEST_CANCEL_EVENT:
			toast = Toast.makeText(getActivity(), "REQUEST_CANCEL_EVENT: Could not connect to server.", Toast.LENGTH_SHORT);
			break;
			
		case Utility.REQUEST_DELETE_EVENT:
			toast = Toast.makeText(getActivity(), "REQUEST_DELETE_EVENT: Could not connect to server.", Toast.LENGTH_SHORT);
			break;
			
		default:
			System.out.println("Unknown network request with requestCode: " + Integer.toString(requestCode));
		}
		progress.dismiss();
		toast.show();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v("onBackPressed","Event Destroyed");
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v("onBackPressed","Event Resumed");
	}
}