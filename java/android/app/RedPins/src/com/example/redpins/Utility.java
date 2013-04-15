package com.example.redpins;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.util.Log;

public class Utility{
	
	
	/**
	 * Request Codes
	 * REQUEST_ADD_[A-Z]+ = 1[0-9][0-9]
	 * REQUEST_GET_[A-Z]+ = 2[0-9][0-9]
	 * REQUEST_CANCEL_[A-Z]+ = 3[0-9][0-9]
	 * REQUEST_DELETE_[A-Z]+ = 4[0-9][0-9]
	 * REQUEST_MODIFY_[A-Z]+ = 5[0-9][0-9]
	 */
	public static final int REQUEST_ADD_COMMENT = 100;
	public static final int REQUEST_ADD_EVENT= 101;
	public static final int REQUEST_ADD_PHOTO = 102;
	public static final int REQUEST_ADD_BOOKMARK = 103;
	public static final int REQUEST_GET_COMMENTS = 200;
	public static final int REQUEST_GET_EVENT = 201;
	public static final int REQUEST_GET_PHOTO = 202;
	public static final int REQUEST_GET_RATINGS = 203;
	public static final int REQUEST_GET_BOOKMARKS = 204;
	public static final int REQUEST_CANCEL_EVENT = 301;
	public static final int REQUEST_DELETE_COMMENT = 400;
	public static final int REQUEST_DELETE_EVENT = 401;
	public static final int REQUEST_DELETE_BOOKMARK = 403;
	public static final int REQUEST_MODIFY_LIKE = 504;
	
	public static JSONObject requestServer(String url, JSONObject jsonInput) {
		HttpPost request = new HttpPost(url);
		Log.v("UTILITY", "API REQUEST: " + url);
		Log.v("UTILITY", "JSON INPUT: " + jsonInput.toString());
		JSONStringer jsonString = new JSONStringer();
		if (jsonInput!=null) {
			Iterator<String> iter = jsonInput.keys();
			if (iter.hasNext()) {
				try {
					jsonString.object();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			while (iter.hasNext()) {
				String s =iter.next();
				try {
					jsonString.key(s).value(jsonInput.get(s));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}             
		}
		try {
			jsonString.endObject();
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		StringEntity entity = null;
		try {
			entity = new StringEntity(jsonString.toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		entity.setContentType("application/json;charset=UTF-8");
		entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
		request.setEntity(entity); 

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 50000); 
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 50000); 

		HttpResponse response = null;
		try {
			response = httpClient.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStreamReader read = null;
		try {
			read = new InputStreamReader(response.getEntity().getContent());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader buffReader = new BufferedReader(read);
		StringBuilder sBuilder = new StringBuilder();
		try {
			String line = buffReader.readLine();
			while(line != null) {
				sBuilder.append(line);
				line = buffReader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject jsonOutput = null;
		try {
			System.out.println("RESPONSE STRING: "+sBuilder.toString());
			jsonOutput = new JSONObject(sBuilder.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonOutput;
	}
	
	// Methods for REQUEST_ADD_[A-Z]+ = 1[0-9][0-9]
	public static void addComment(NetworkFragment networkFragment, String eventID, String comment) {
		JSONObject requestJSON = new JSONObject();
		try {
			requestJSON.put("facebook_id", ((MainActivity) MainActivity.activity).getFacebookId());
			requestJSON.put("session_token", ((MainActivity) MainActivity.activity).getFacebookSessionToken());
			requestJSON.put("event_id", eventID);
			requestJSON.put("comment", comment);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(networkFragment, REQUEST_ADD_COMMENT, requestJSON, "/users/postComment.json");
	}
	
	public static void addBookmark(NetworkFragment networkFragment, String eventID) {
		JSONObject requestJSON = new JSONObject();
		try {
			requestJSON.put("facebook_id", ((MainActivity) MainActivity.activity).getFacebookId());
			requestJSON.put("session_token", ((MainActivity) MainActivity.activity).getFacebookSessionToken());
			requestJSON.put("event_id", eventID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(networkFragment, REQUEST_ADD_BOOKMARK, requestJSON, "/users/bookmarkEvent.json");
	}
	
	// Methods for REQUEST_GET_[A-Z]+ = 2[0-9][0-9]
	public static void getComments(NetworkFragment networkFragment, String eventID) {
		JSONObject requestJSON = new JSONObject();
		try {
			requestJSON.put("facebook_id", ((MainActivity) MainActivity.activity).getFacebookId());
			requestJSON.put("session_token", ((MainActivity) MainActivity.activity).getFacebookSessionToken());
			requestJSON.put("event_id", eventID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(networkFragment, REQUEST_GET_COMMENTS, requestJSON, "/events/getComments.json");
	}
	
	public static void getEvent(NetworkFragment networkFragment, String eventID) {
		JSONObject requestJSON = new JSONObject();
		try {
			requestJSON.put("facebook_id", ((MainActivity) MainActivity.activity).getFacebookId());
			requestJSON.put("session_token", ((MainActivity) MainActivity.activity).getFacebookSessionToken());
			requestJSON.put("event_id", eventID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(networkFragment, REQUEST_GET_EVENT, requestJSON, "/events/get.json");
	}
	
	public static void getRatings(NetworkFragment networkFragment, String eventID) {
		JSONObject requestJSON = new JSONObject();
		try {
			requestJSON.put("facebook_id", ((MainActivity) MainActivity.activity).getFacebookId());
			requestJSON.put("session_token", ((MainActivity) MainActivity.activity).getFacebookSessionToken());
			requestJSON.put("event_id", eventID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(networkFragment, REQUEST_GET_RATINGS, requestJSON, "/users/alreadyLikedEvent.json");
	}
	
	// Methods for REQUEST_CANCEL_[A-Z]+ = 3[0-9][0-9]
	public static void cancelEvent(NetworkFragment networkFragment, String eventID) {
		JSONObject requestJSON = new JSONObject();
		try {
			requestJSON.put("facebook_id", ((MainActivity) MainActivity.activity).getFacebookId());
			requestJSON.put("session_token", ((MainActivity) MainActivity.activity).getFacebookSessionToken());
			requestJSON.put("event_id", eventID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(networkFragment, REQUEST_CANCEL_EVENT, requestJSON, "/users/cancelEvent.json");
	}
	
	// Methods for REQUEST_DELETE_[A-Z]+ = 4[0-9][0-9]
	public static void deleteComment(NetworkFragment networkFragment, String eventID) {
		JSONObject requestJSON = new JSONObject();
		try {
			requestJSON.put("facebook_id", ((MainActivity) MainActivity.activity).getFacebookId());
			requestJSON.put("session_token", ((MainActivity) MainActivity.activity).getFacebookSessionToken());
			requestJSON.put("event_id", eventID);
			//TODO: Require comment_id?
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(networkFragment, REQUEST_DELETE_COMMENT, requestJSON, "/users/removeComment.json");
	}
	
	public static void deleteEvent(NetworkFragment networkFragment, String eventID) {
		JSONObject requestJSON = new JSONObject();
		try {
			requestJSON.put("facebook_id", ((MainActivity) MainActivity.activity).getFacebookId());
			requestJSON.put("session_token", ((MainActivity) MainActivity.activity).getFacebookSessionToken());
			requestJSON.put("event_id", eventID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(networkFragment, REQUEST_DELETE_EVENT, requestJSON, "/users/deleteEvent.json");
	}
	
	public static void deleteBookmark(NetworkFragment networkFragment, String eventID) {
		JSONObject requestJSON = new JSONObject();
		try {
			requestJSON.put("facebook_id", ((MainActivity) MainActivity.activity).getFacebookId());
			requestJSON.put("session_token", ((MainActivity) MainActivity.activity).getFacebookSessionToken());
			requestJSON.put("event_id", eventID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(networkFragment, REQUEST_DELETE_BOOKMARK, requestJSON, "/users/removeBookmark.json");
	}
	
	// Methods for REQUEST_MODIFY_[A-Z]+ = 5[0-9][0-9]
	public static void modifyLike(NetworkFragment networkFragment, String eventID, boolean likeStatus) {
		JSONObject requestJSON = new JSONObject();
		try {
			requestJSON.put("facebook_id", ((MainActivity) MainActivity.activity).getFacebookId());
			requestJSON.put("session_token", ((MainActivity) MainActivity.activity).getFacebookSessionToken());
			requestJSON.put("event_id", eventID);
			requestJSON.put("like", likeStatus);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(networkFragment, REQUEST_MODIFY_LIKE, requestJSON, "/users/likeEvent.json");
	}
	
	public static JSONObject getBookmarks(NetworkFragment networkFragment, int page_num) {
		JSONObject requestJSON = new JSONObject();
		try {
			requestJSON.put("facebook_id", ((MainActivity) MainActivity.activity).getFacebookId());
			requestJSON.put("session_token", ((MainActivity) MainActivity.activity).getFacebookSessionToken());
			requestJSON.put("page", page_num);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(networkFragment, REQUEST_GET_BOOKMARKS, requestJSON, "/users/getBookmarks.json");
		try {
			return jsonTask.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
	
