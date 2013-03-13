package com.example.redpins;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class EventActivity extends Activity implements OnClickListener{
	
	private TextView eventName;
	private TextView eventURL;
	private TextView eventLoc;
	private TextView eventTime;
	private ImageView eventImg;
	private TextView eventLikes;
	private TextView eventDislikes;
	private ListView commentsList;
	private String event_id;
	private ProgressBar progressBar;
	private String urlLink;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		eventName = (TextView) findViewById(R.id.event_name);
		eventURL = (TextView) findViewById(R.id.event_url);
		eventURL.setOnClickListener(this);
		eventLoc = (TextView) findViewById(R.id.event_location);
		eventTime = (TextView) findViewById(R.id.event_time);
		eventImg = (ImageView) findViewById(R.id.event_image);
		eventLikes = (TextView) findViewById(R.id.event_like);
		eventDislikes = (TextView) findViewById(R.id.event_dislike);
//		commentsList = (ListView) findViewById(R.id.comment_listview);
		event_id = "";//getIntent().getExtras().getString("event_id");
		progressBar = (ProgressBar) findViewById(R.id.event_progress);
 		GetEventTask task = new GetEventTask();
		task.execute();
	}
	
	
	public class GetEventTask extends AsyncTask<Void, Void, JSONObject>{

		//how should i save comments
		
		@Override
		protected JSONObject doInBackground(Void... arg0) {
			JSONObject json = new JSONObject();
			try {
				//adds input values into JSON data object
				json.put("event_id", "hi");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			JSONObject ret = null;
			try {
				//sends requests to server and receives
				ret = Utility.requestServer("http://dry-wave-1707.herokuapp.com/events/get.json", json);
			} catch (Throwable e) {
			}
			return ret;
		}
		
		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				System.out.println(""+result);
				JSONObject replace = new JSONObject();
				replace.put("title", "Doc's Party");
				replace.put("url", "http://www.google.com");
				replace.put("location", "Starbucks");
				replace.put("time", "5:30AM");
				String name = replace.getString("title");//result.getString("title");
				String url = replace.getString("url"); //result.getString("url");
				String loc = replace.getString("location"); //result.getString("location");
				String time = replace.getString("time"); //result.getString("time");
//				String img =  result.getString("");
				String likes =  "20";//result.getString("");
				String dislikes =  "10";//result.getString("");
				eventName.setText(name);
				urlLink = url;
				eventURL.setText(urlLink);
				eventLoc.setText(loc);
				eventTime.setText(time);
				//???eventImg.setImageURI(uri);
				eventLikes.setText("LIKES: "+likes);
				eventDislikes.setText("DISLIKES: " + dislikes);
				progressBar.setProgress((int) (Double.parseDouble(likes)/(Double.parseDouble(likes)+Double.parseDouble(dislikes))*100));
			} catch (JSONException e) {
				e.printStackTrace();
			}
//			GetCommentTask commentTask = new GetCommentTask();
//			commentTask.execute();
			
		}
	}
	
	public class GetCommentTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlLink));
		startActivity(myIntent);
	}
}