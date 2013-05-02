package com.example.redpins;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.facebook.Request;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

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
	public static final int REQUEST_ADD_USER = 107;
	public static final int REQUEST_GET_COMMENTS = 200;
	public static final int REQUEST_GET_EVENT = 201;
	public static final int REQUEST_GET_PHOTOS = 202;
	public static final int REQUEST_GET_BOOKMARKS = 203;
	public static final int REQUEST_GET_RATINGS = 204;
	public static final int REQUEST_GET_EVENTLIST = 205;
	public static final int REQUEST_GET_NEARBYEVENTLIST = 206;
	public static final int REQUEST_GET_RECENTEVENTLIST = 207;
	public static final int REQUEST_GET_RECOMMENDATIONSLIST = 208;
	public static final int REQUEST_CANCEL_EVENT = 301;
	public static final int REQUEST_DELETE_COMMENT = 400;
	public static final int REQUEST_DELETE_EVENT = 401;
	public static final int REQUEST_DELETE_BOOKMARK = 403;
	public static final int REQUEST_DELETE_LIKE = 404;
	public static final int REQUEST_MODIFY_LIKE = 504;
	public static final int REQUEST_LOGIN_USER = 600;
	
	// Helper methods
	public Calendar convertLocalCalendarToUTCCalendar(Calendar newCalendar) {
		// Find the date and timezone from the calendar
		Date newDate = newCalendar.getTime();
		TimeZone tz = newCalendar.getTimeZone();
		long msFromUnixTime = newDate.getTime();
		int offsetFromUTC = tz.getOffset(msFromUnixTime);
		
		// Create a new calendar in GMT and account for offsets
		Calendar gmtCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		gmtCalendar.setTime(newDate);
		gmtCalendar.add(Calendar.MILLISECOND, -offsetFromUTC);
		return gmtCalendar;
	}
	
	//TODO: UNTESTED
	public Calendar convertUTCCalendarToLocalCalendar(Calendar newCalendar) {
		// Find the date and timezone from the calendar
		Date newDate = newCalendar.getTime();
		TimeZone tz = newCalendar.getTimeZone();
		long msFromUnixTime = newDate.getTime();
		int offsetFromUTC = tz.getOffset(msFromUnixTime);
		
		// Create a new calendar in local timezone and account for offsets
		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
		localCalendar.setTime(newDate);
		localCalendar.add(Calendar.MILLISECOND, -offsetFromUTC);
		return localCalendar;
	}
	
	public JSONObject requestServer(String url, JSONObject jsonInput) {
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
		return convertHttpResponseToJSONObject(response);
	}
	
	public JSONObject convertHttpResponseToJSONObject(HttpResponse httpResponse) {
		InputStreamReader read = null;
		try {
			read = new InputStreamReader(httpResponse.getEntity().getContent());
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
			System.out.println("Converted RESPONSE STRING: "+sBuilder.toString());
			jsonOutput = new JSONObject(sBuilder.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonOutput;
	}
	
	public JSONArray lookupJSONArrayFromJSONObject(JSONObject jsonInput, String key) {
		JSONArray jsonArrayInput = null;
		try {
			jsonArrayInput = jsonInput.getJSONArray(key);
			jsonArrayInput.toString().replace("[", "");
			jsonArrayInput.toString().replace("]", "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonArrayInput;
	}
	
	public File convertBitmapToFile(Bitmap bitmap, int quality, String filename, String path) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, quality, bos);
		byte[] data = bos.toByteArray();
		String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + path;
		File dir = new File(filePath);
			if(!dir.exists()) {
				dir.mkdirs();
		 }
		File file = new File(dir, filename);
		FileOutputStream fOut;
		try {
			fOut = new FileOutputStream(file);
			fOut.write(data);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}
	
	public JSONObject createJSONObjectWithFacebookIDAndSessionToken() {
		JSONObject requestJSON = new JSONObject();
		try {
			requestJSON.put("facebook_id", ((MainActivity) MainActivity.activity).getFacebookId());
			requestJSON.put("session_token", ((MainActivity) MainActivity.activity).getFacebookSessionToken());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return requestJSON;
	}
	
	public MultipartEntity createMultipartEntityWithFacebookIDAndSessionToken() {
		MultipartEntity requestEntity = new MultipartEntity();
		try {
			requestEntity.addPart("facebook_id", new StringBody(((MainActivity) MainActivity.activity).getFacebookId()));
			requestEntity.addPart("session_token", new StringBody(((MainActivity) MainActivity.activity).getFacebookSessionToken()));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestEntity;
	}
	
	
	// Methods for REQUEST_ADD_[A-Z]+ = 1[0-9][0-9]
	public void addComment(JSONResponseHandler fragment, String eventID, String comment) {
		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
		try {
			requestJSON.put("event_id", eventID);
			requestJSON.put("comment", comment);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(fragment, REQUEST_ADD_COMMENT, requestJSON, "/users/postComment.json");
	}
	
	public void addEvent(JSONResponseHandler fragment, String title, String startTime, String endTime, String location, String url, double latitude, double longitude, String description) {
		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
		try {
			requestJSON.put("title", title);
			requestJSON.put("start_time", startTime);
			requestJSON.put("end_time", endTime);
			requestJSON.put("location", location);
			requestJSON.put("url", url);
			requestJSON.put("description", description);
			if ((latitude != -360.0) && (longitude != -360.0)) {
				requestJSON.put("latitude", latitude);
				requestJSON.put("longitude", longitude);	
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(fragment, REQUEST_ADD_EVENT, requestJSON, "/events/add.json");
	}
	
	public void addUser(JSONResponseHandler fragment, String email, String facebookID, String sessionToken, String firstname, String lastname) {
		JSONObject requestJSON = new JSONObject();
		try {
			 requestJSON.put("email", email);
			 requestJSON.put("facebook_id", facebookID);
			 requestJSON.put("firstname", firstname);
			 requestJSON.put("lastname", lastname);
			 requestJSON.put("session_token", sessionToken);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(fragment, REQUEST_ADD_USER, requestJSON, "/users/add.json");
	}
	
	public void addBookmark(JSONResponseHandler fragment, String eventID) {
		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
		try {
			requestJSON.put("event_id", eventID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(fragment, REQUEST_ADD_BOOKMARK, requestJSON, "/users/bookmarkEvent.json");
	}
	
	public void addPhoto(MultipartResponseHandler fragment, String eventID, Bitmap bitmap, String captions) {
		MultipartEntity requestEntity = createMultipartEntityWithFacebookIDAndSessionToken();
		File photoFile = convertBitmapToFile(bitmap, 85, "newPhoto.JPEG", "/RedPins/uploads");
		try {
			requestEntity.addPart("photo", new FileBody(photoFile));
			requestEntity.addPart("caption", new StringBody(captions));
			requestEntity.addPart("event_id", new StringBody(eventID));
		} catch (UnsupportedEncodingException e) {
			fragment.onNetworkFailure(REQUEST_ADD_PHOTO, null);
		}
		DefaultMultipartTask multipartTask = new DefaultMultipartTask();
		multipartTask.executeTask(fragment, REQUEST_ADD_PHOTO, requestEntity, "/users/uploadPhoto");
		
	}
	
	// Methods for REQUEST_GET_[A-Z]+ = 2[0-9][0-9]
	public void getComments(JSONResponseHandler fragment, String eventID) {
		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
		try {
			requestJSON.put("event_id", eventID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(fragment, REQUEST_GET_COMMENTS, requestJSON, "/events/getComments.json");
	}
	
	public void getEvent(JSONResponseHandler fragment, String eventID) {
		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
		try {
			requestJSON.put("event_id", eventID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(fragment, REQUEST_GET_EVENT, requestJSON, "/events/getEvent.json");
	}
	
	public void getPhotos(JSONResponseHandler fragment, String eventID) {
		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
		try {
			requestJSON.put("event_id", eventID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(fragment, REQUEST_GET_PHOTOS, requestJSON, "/events/getPhotos.json");
	}
	
	public void getBookmarks(JSONResponseHandler fragment, int page_num) {
		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
		try {
			requestJSON.put("page", page_num);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(fragment, REQUEST_GET_BOOKMARKS, requestJSON, "/users/getBookmarks.json");
	}
	
	public void getRatings(JSONResponseHandler fragment, String eventID) {
		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
		try {
			requestJSON.put("event_id", eventID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(fragment, REQUEST_GET_RATINGS, requestJSON, "/users/alreadyLikedEvent.json");
	}
	
	public void getEventList(JSONResponseHandler fragment, String searchQuery, String locationQuery, int pageOffset) {
		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
		try {
			requestJSON.put("search_query", searchQuery);
			requestJSON.put("location_query", locationQuery);
			requestJSON.put("page", pageOffset);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(fragment, REQUEST_GET_EVENTLIST, requestJSON, "/events/search.json");
	}
	
	public void getNearbyEventList(JSONResponseHandler fragment, String searchQuery, double latitude, double longitude, int pageOffset) {
		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
		try {
			requestJSON.put("search_query", searchQuery);
			requestJSON.put("latitude", latitude);
			requestJSON.put("longitude", longitude);
			requestJSON.put("page", pageOffset);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(fragment, REQUEST_GET_NEARBYEVENTLIST, requestJSON, "/events/searchViaCoordinates.json");
	}
	
	// Methods for REQUEST_CANCEL_[A-Z]+ = 3[0-9][0-9]
	public void cancelEvent(JSONResponseHandler fragment, String eventID) {
		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
		try {
			requestJSON.put("event_id", eventID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(fragment, REQUEST_CANCEL_EVENT, requestJSON, "/users/cancelEvent.json");
	}
	
	// Methods for REQUEST_DELETE_[A-Z]+ = 4[0-9][0-9]
	public void deleteComment(JSONResponseHandler fragment, String eventID, String commentID) {
		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
		try {
			requestJSON.put("event_id", eventID);
			requestJSON.put("comment_id", commentID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(fragment, REQUEST_DELETE_COMMENT, requestJSON, "/users/removeComment.json");
	}
	
	public void deleteEvent(JSONResponseHandler fragment, String eventID) {
		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
		try {
			requestJSON.put("event_id", eventID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(fragment, REQUEST_DELETE_EVENT, requestJSON, "/users/deleteEvent.json");
	}
	
	public void deleteBookmark(JSONResponseHandler fragment, String eventID) {
		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
		try {
			requestJSON.put("event_id", eventID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(fragment, REQUEST_DELETE_BOOKMARK, requestJSON, "/users/removeBookmark.json");
	}
	
	public void deleteLike(JSONResponseHandler fragment, String eventID) {
		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
		try {
			requestJSON.put("event_id", eventID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(fragment, REQUEST_DELETE_LIKE, requestJSON, "/users/removeLike.json");
	}
	
	// Methods for REQUEST_MODIFY_[A-Z]+ = 5[0-9][0-9]
	public void modifyLike(JSONResponseHandler fragment, String eventID, boolean likeStatus) {
		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
		try {
			requestJSON.put("event_id", eventID);
			requestJSON.put("like", likeStatus);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(fragment, REQUEST_MODIFY_LIKE, requestJSON, "/users/likeEvent.json");
	}
	
	// Methods for REQUEST_LOGIN_[A-Z]+ = 6[0-9][0-9]
	public void loginUser(JSONResponseHandler fragment, String email, String facebookID, String sessionToken) {
		JSONObject requestJSON = new JSONObject();
		try {
			 requestJSON.put("email", email);
			 requestJSON.put("facebook_id", facebookID);
			 requestJSON.put("session_token", sessionToken);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(fragment, REQUEST_LOGIN_USER, requestJSON, "/users/login.json");
	}
	
	public void getRecentEventList(JSONResponseHandler fragment, Integer page) {
		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
		try {
			 requestJSON.put("page", page);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(fragment, REQUEST_GET_RECENTEVENTLIST, requestJSON, "/users/getRecentEvents.json");
	}
	
	public void getImage(ImageView image, String requestPath) {
		DownloadImageTask task = new DownloadImageTask(image, requestPath);
		task.execute();
	}
	
	public void getSimpleRecommendations(JSONResponseHandler fragment) {
		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(fragment, REQUEST_GET_RECOMMENDATIONSLIST, requestJSON, "/users/getSimpleRecommendations.json");
	}
	
	public void executeFacebookRequest(Request request) {
		FacebookTask task = new FacebookTask(request);
		task.execute();
	}
	
	public ProgressDialog addProgressDialog(Context context, String title, String message, Integer count) {
		CountingProgressDialog progress = new CountingProgressDialog(context, count);
		progress.setTitle(title);
		progress.setMessage(message);
		progress.show();
		return progress;
	}
	
	public ProgressDialog addProgressDialog(Context context, String title, String message) {
		Log.i("Progress Dialog", "CREATING DIALOG");
		CountingProgressDialog progress = new CountingProgressDialog(context);
		progress.setTitle(title);
		progress.setMessage(message);
		progress.show();
		return progress;
	}
}