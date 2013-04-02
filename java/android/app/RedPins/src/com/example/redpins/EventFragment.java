package com.example.redpins;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.gms.maps.SupportMapFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class EventFragment extends Fragment implements OnClickListener{

	private ImageButton homeButton;
	private TextView eventName;
	private TextView eventURL;
	private TextView eventLoc;
	private TextView eventTime;
	private ImageView eventImg;
	private TextView eventLikes;
	private TextView eventDislikes;
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
	private Button bookmarkButton;
	private boolean bookmarked;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.event_fragment, container, false);
		homeButton = (ImageButton) view.findViewById(R.id.home_button);
		homeButton.setOnClickListener(this);
		eventName = (TextView) view.findViewById(R.id.event_name);
		eventURL = (TextView) view.findViewById(R.id.event_url);
		eventURL.setOnClickListener(this);
		eventLoc = (TextView) view.findViewById(R.id.event_location);
		eventTime = (TextView) view.findViewById(R.id.event_time);
		eventImg = (ImageView) view.findViewById(R.id.event_image);
		eventLikes = (TextView) view.findViewById(R.id.event_like);
		eventDislikes = (TextView) view.findViewById(R.id.event_dislike);
		commentsList = (ListView) view.findViewById(R.id.comment_listview);
		addCommentButton = (Button) view.findViewById(R.id.add_comment_button);
		addCommentButton.setOnClickListener(this);
		likeButton = (ImageButton) view.findViewById(R.id.like_button);
		likeButton.setOnClickListener(this);
		dislikeButton = (ImageButton) view.findViewById(R.id.dislike_button);
		dislikeButton.setOnClickListener(this);
		if(event_id==null){
			event_id = getArguments().getString("event_id");
		}
		linkBack = getArguments().getString("prev");
		progressBar = (ProgressBar) view.findViewById(R.id.event_progress);
		mContext = getActivity().getApplicationContext();
		commentArr = new ArrayList<JSONObject>();
		bookmarkButton = (Button) view.findViewById(R.id.bookmark_button);
		bookmarkButton.setOnClickListener(this);
		GetEventTask task = new GetEventTask();
		task.execute();
		GetLikesTask likesTask = new GetLikesTask();
		likesTask.execute();
		GetCommentTask commentsTask = new GetCommentTask();
		commentsTask.execute();
		GetUserEventRatingTask userRatingTask = new GetUserEventRatingTask();
		userRatingTask.execute();
		return view;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(event_id==null){
			event_id = getArguments().getString("event_id");
		}
		System.out.println("event_id2: "+event_id);
		//		GetCommentTask task = new GetCommentTask();
		//		task.execute();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.home_button:
			if(linkBack.equals("map")){
				((MainActivity) getActivity()).showMapviewFrag();
			}else if(linkBack.equals("list")){
				((MainActivity) getActivity()).showListviewFrag();
			}else{
				((MainActivity) getActivity()).showNaviFrag();
			}
			((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction().hide(((MainActivity) getActivity()).eventFragment).commit();
			break;
		case R.id.event_url:
			//takes user to web browser with given link
			Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlLink));
			startActivity(myIntent);
			break;
		case R.id.add_comment_button:
			AddCommentFragment commFragment = new AddCommentFragment();
			Bundle bundle = new Bundle();
			bundle.putString("event_id", event_id);
			bundle.putString("callback", linkBack);
			commFragment.setArguments(bundle);
			getActivity().getSupportFragmentManager().beginTransaction().add(android.R.id.content,commFragment).commit();
			((MainActivity)getActivity()).hideEventFrag();
			break;
		case R.id.like_button:
			System.out.println("LIKE");
			likeButton.setBackgroundColor(Color.GREEN);
			dislikeButton.setBackgroundColor(Color.TRANSPARENT);
			likeButton.setClickable(false);
			dislikeButton.setClickable(true);
			likeTask task = new likeTask();
			task.execute();
			break;
		case R.id.dislike_button:
			System.out.println("DISLIKE");
			likeButton.setBackgroundColor(Color.TRANSPARENT);
			dislikeButton.setBackgroundColor(Color.RED);
			dislikeButton.setClickable(false);
			likeButton.setClickable(true);
			dislikeTask task2 = new dislikeTask();
			task2.execute();
			break;
		case R.id.bookmark_button:
			bookmarkEvent();
			break;
		}
	}

	//gets the information you need for a given event through server request
	public class GetEventTask extends AsyncTask<Void, Void, JSONObject>{

		@Override
		protected JSONObject doInBackground(Void... arg0) {
			JSONObject json = new JSONObject();
			try {
				//adds input values into JSON data object
				json.put("event_id", event_id);
				json.put("facebook_id", ((MainActivity)getActivity()).getFacebookId());
				json.put("session_token", ((MainActivity)getActivity()).getFacebookSessionToken());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			JSONObject ret = null;
			try {
				//sends requests to server and receive
				ret = Utility.requestServer(MainActivity.serverURL+"/events/getEvent.json", json);
			} catch (Throwable e) {
			}
			return ret;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				System.out.println(result);
				JSONArray jsonArr = result.toJSONArray(result.names());
				System.out.println(jsonArr.getInt(0));
				if(jsonArr.getInt(0) == 1){
					JSONObject json = jsonArr.getJSONObject(1);
					String name = json.getString("title");
					String url = json.getString("url");
					String loc = json.getString("location");
					String time = json.getString("start_time");
					owner_id = json.getInt("user_id");
					eventName.setText(name);
					urlLink = url;
					eventURL.setText(urlLink);
					eventLoc.setText(loc);
					eventTime.setText(time);
				}
				//???eventImg.setImageURI(uri);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			//			GetLikesTask task = new GetLikesTask();
			//			task.execute();
			//	GetCommentTask commentTask = new GetCommentTask();
			//	commentTask.execute();
		}
	}

	public class GetCommentTask extends AsyncTask<Void, Void, JSONArray>{

		@Override
		protected JSONArray doInBackground(Void... arg0) {
			JSONObject json = new JSONObject();
			JSONObject temp;
			JSONArray ret = null;
			try {
				json.put("event_id", event_id);
				temp = Utility.requestServer(MainActivity.serverURL + "/events/getComments.json", json);
				ret = temp.getJSONArray("comments");
				Log.v("EVENTFRAGMENT", "comment json array: " + ret.toString());
				ret.toString().replace("[", "");
				ret.toString().replace("]", "");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("caught exception in getcommentask and error: " + e.toString());
				e.printStackTrace();
			}
			//sends requests to server and receives
			return ret;
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			System.out.println("comment result: " + result.toString());
			for(int i = 0; i<result.length();i++){
				try {
					System.out.println("JSONObj: " +result.getJSONObject(i));
					commentArr.add(result.getJSONObject(i));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			populateCommentList();
		}
	}


	private void populateCommentList(){
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
					v.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							System.out.println("item clicked: " + facebook_id);
							removeComment(facebook_id, pos);
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
				// TODO Auto-generated method stub
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
	public class GetLikesTask extends AsyncTask<Void, Void, JSONObject>{

		@Override
		protected JSONObject doInBackground(Void... arg0) {
			JSONObject json = new JSONObject();
			JSONObject ret = null;
			JSONObject temp;
			try {
				json.put("event_id", event_id);
				// sends requests to server and receives
				ret = Utility.requestServer(MainActivity.serverURL + "/events/getRatings.json", json);
			} catch (Throwable e) {
				e.printStackTrace();
			}

			return ret;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try {
				eventLikes.setText("LIKES: "+ result.getInt("likes"));
				eventDislikes.setText("DISLIKES: " + result.getInt("dislikes"));
				progressBar.setProgress((100*result.getInt("likes"))/(result.getInt("likes")+result.getInt("dislikes")));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public class GetUserEventRatingTask extends AsyncTask<Void, Void, JSONObject>{

		@Override
		protected JSONObject doInBackground(Void... arg0) {
			JSONObject json = new JSONObject();
			JSONObject ret = null;
			try {
				json.put("event_id", event_id);
				json.put("facebook_id",((MainActivity)getActivity()).getFacebookId());
				json.put("session_token", ((MainActivity)getActivity()).getFacebookSessionToken());
				// sends requests to server and receives
				ret = Utility.requestServer(MainActivity.serverURL + "/users/alreadyLikedEvent.json", json);
			} catch (Throwable e) {
				e.printStackTrace();
			}

			return ret;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try {
				if(result.getString("alreadyLikedEvent").equals("true")) {
					if (result.getString("like").equals("true")) {
						likeButton.setBackgroundColor(Color.GREEN);
						dislikeButton.setBackgroundColor(Color.TRANSPARENT);
						likeButton.setClickable(false);
						dislikeButton.setClickable(true);
					} else {
						likeButton.setBackgroundColor(Color.TRANSPARENT);
						dislikeButton.setBackgroundColor(Color.RED);
						dislikeButton.setClickable(false);
						likeButton.setClickable(true);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void removeComment(String comment_user_id, int position){
		if(comment_user_id.equals(((MainActivity)getActivity()).getFacebookId())){
			final int pos = position;
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
					//RemoveCommentTask task = new RemoveCommentTask();
					//task.execute();
				}
			});
			AlertDialog alertDialog = builder.create();
			// Set the Icon for the Dialog
			alertDialog.show();
		}else{
			Toast toast = Toast.makeText(getActivity(), "You are not owner of this comment", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	public void updateComments(){
		GetCommentTask task = new GetCommentTask();
		task.execute();
	}

	public class likeTask extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			JSONObject json = new JSONObject();
			JSONObject ret = null;
			JSONObject temp;
			try {
				json.put("event_id", event_id);
				json.put("facebook_id",((MainActivity)getActivity()).getFacebookId());
				json.put("session_token", ((MainActivity)getActivity()).getFacebookSessionToken());
				//	json.put(name, value)
				// sends requests to server and receives
				if(likeButton.isPressed() || dislikeButton.isPressed()){
					Utility.requestServer(MainActivity.serverURL + "/users/removeLike.json", json);
				}
				json.put("like", true);
				ret = Utility.requestServer(MainActivity.serverURL + "/users/likeEvent.json", json);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			GetLikesTask task = new GetLikesTask();
			task.execute();
		}
	}

	public class dislikeTask extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			JSONObject json = new JSONObject();
			JSONObject ret = null;
			JSONObject temp;
			try {
				json.put("event_id", event_id);
				json.put("facebook_id",((MainActivity)getActivity()).getFacebookId());
				json.put("session_token", ((MainActivity)getActivity()).getFacebookSessionToken());
				//	json.put(name, value)
				// sends requests to server and receives
				if(likeButton.isPressed() || dislikeButton.isPressed()){
					Utility.requestServer(MainActivity.serverURL + "/users/removeLike.json", json);
				}
				json.put("like", false);
				Utility.requestServer(MainActivity.serverURL + "/users/likeEvent.json", json);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			GetLikesTask task = new GetLikesTask();
			task.execute();
		}
	}

	public class BookmarkTask extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			JSONObject json = new JSONObject();
			JSONObject ret = null;
			JSONObject temp;
			try {
				json.put("event_id", event_id);
				json.put("facebook_id",((MainActivity)getActivity()).getFacebookId());
				json.put("session_token", ((MainActivity)getActivity()).getFacebookSessionToken());
				// sends requests to server and receives
				ret = Utility.requestServer(MainActivity.serverURL + "/users/bookmarkEvent.json", json);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public class UnbookmarkTask extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			JSONObject json = new JSONObject();
			JSONObject ret = null;
			JSONObject temp;
			try {
				json.put("event_id", event_id);
				json.put("facebook_id",((MainActivity)getActivity()).getFacebookId());
				json.put("session_token", ((MainActivity)getActivity()).getFacebookSessionToken());
				// sends requests to server and receives
				ret = Utility.requestServer(MainActivity.serverURL + "/users/bookmarkEvent.json", json);//remove bookmark
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private void bookmarkEvent(){
		if(!bookmarked){
			bookmarked = true;
			BookmarkTask task = new BookmarkTask();
			task.execute();
		}else{
			bookmarked = false;
			UnbookmarkTask task = new UnbookmarkTask();
			task.execute();
		}
	}
	
}