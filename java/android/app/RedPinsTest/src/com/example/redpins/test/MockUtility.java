//package com.example.redpins.test;
//
//import com.example.redpins.*;
//import java.io.BufferedReader;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.util.Iterator;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicHeader;
//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.protocol.HTTP;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.json.JSONStringer;
//
//import android.graphics.Bitmap;
//import android.os.Environment;
//import android.util.Log;
//
//public class MockUtility{
//	
//	/**
//	 * Request Codes
//	 * REQUEST_ADD_[A-Z]+ = 1[0-9][0-9]
//	 * REQUEST_GET_[A-Z]+ = 2[0-9][0-9]
//	 * REQUEST_CANCEL_[A-Z]+ = 3[0-9][0-9]
//	 * REQUEST_DELETE_[A-Z]+ = 4[0-9][0-9]
//	 * REQUEST_MODIFY_[A-Z]+ = 5[0-9][0-9]
//	 */
//	public static final int REQUEST_ADD_COMMENT = 100;
//	public static final int REQUEST_ADD_EVENT= 101;
//	public static final int REQUEST_ADD_PHOTO = 102;
//	public static final int REQUEST_ADD_BOOKMARK = 103;
//	public static final int REQUEST_GET_COMMENTS = 200;
//	public static final int REQUEST_GET_EVENT = 201;
//	public static final int REQUEST_GET_PHOTO = 202;
//	public static final int REQUEST_GET_BOOKMARKS = 203;
//	public static final int REQUEST_GET_RATINGS = 204;
//	public static final int REQUEST_GET_EVENTLIST = 205;
//	public static final int REQUEST_CANCEL_EVENT = 301;
//	public static final int REQUEST_DELETE_COMMENT = 400;
//	public static final int REQUEST_DELETE_EVENT = 401;
//	public static final int REQUEST_DELETE_BOOKMARK = 403;
//	public static final int REQUEST_MODIFY_LIKE = 504;
//	
//	// Helper methods
//	
//	public static JSONObject createJSONObjectWithFacebookIDAndSessionToken() {
//		JSONObject requestJSON = new JSONObject();
//		try {
//			requestJSON.put("facebook_id", ((MainActivity) MainActivity.activity).getFacebookId());
//			requestJSON.put("session_token", ((MainActivity) MainActivity.activity).getFacebookSessionToken());
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return requestJSON;
//	}
//	
//	public static MultipartEntity createMultipartEntityWithFacebookIDAndSessionToken() {
//		MultipartEntity requestEntity = new MultipartEntity();
//		try {
//			requestEntity.addPart("facebook_id", new StringBody(((MainActivity) MainActivity.activity).getFacebookId()));
//			requestEntity.addPart("session_token", new StringBody(((MainActivity) MainActivity.activity).getFacebookSessionToken()));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return requestEntity;
//	}
//	
//	
//	// Methods for REQUEST_ADD_[A-Z]+ = 1[0-9][0-9]
//	public static void addComment(JSONResponseHandler fragment, String eventID, String comment) {
//		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
//		try {
//			requestJSON.put("event_id", eventID);
//			requestJSON.put("comment", comment);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		DefaultJSONTask jsonTask = new DefaultJSONTask();
////		jsonTask.executeTask(fragment, REQUEST_ADD_COMMENT, requestJSON, "/users/postComment.json");
//	}
//	
//	public static void addBookmark(JSONResponseHandler fragment, String eventID) {
//		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
//		try {
//			requestJSON.put("event_id", eventID);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		DefaultJSONTask jsonTask = new DefaultJSONTask();
////		jsonTask.executeTask(fragment, REQUEST_ADD_BOOKMARK, requestJSON, "/users/bookmarkEvent.json");
//	}
//	
////	public static void addPhoto(MultipartResponseHandler fragment, String eventID, Bitmap bitmap, String captions) {
////		MultipartEntity requestEntity = createMultipartEntityWithFacebookIDAndSessionToken();
////		File photoFile = convertBitmapToFile(bitmap, 85, "newPhoto", "/RedPins/uploads");
////		try {
////			requestEntity.addPart("photo", new FileBody(photoFile));
////			requestEntity.addPart("photo_caption", new StringBody(captions));
////			requestEntity.addPart("type", new StringBody("photo"));
////			requestEntity.addPart("event_id", new StringBody(eventID));
////		} catch (UnsupportedEncodingException e) {
////			fragment.onNetworkFailure(REQUEST_ADD_PHOTO, null);
////		}
////		DefaultMultipartTask multipartTask = new DefaultMultipartTask();
//////		multipartTask.executeTask(fragment, REQUEST_ADD_PHOTO, requestEntity, "/users/uploadPhoto");
////		
////	}
//	
//	// Methods for REQUEST_GET_[A-Z]+ = 2[0-9][0-9]
//	public static void getComments(JSONResponseHandler fragment, String eventID) {
//		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
//		try {
//			requestJSON.put("event_id", eventID);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		DefaultJSONTask jsonTask = new DefaultJSONTask();
////		jsonTask.executeTask(fragment, REQUEST_GET_COMMENTS, requestJSON, "/events/getComments.json");
//	}
//	
//	public static void getEvent(JSONResponseHandler fragment, String eventID) {
//		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
//		try {
//			requestJSON.put("event_id", eventID);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		DefaultJSONTask jsonTask = new DefaultJSONTask();
////		jsonTask.executeTask(fragment, REQUEST_GET_EVENT, requestJSON, "/events/get.json");
//	}
//	
//	public static void getBookmarks(JSONResponseHandler fragment, int page_num) {
//		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
//		try {
//			requestJSON.put("page", page_num);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		DefaultJSONTask jsonTask = new DefaultJSONTask();
////		jsonTask.executeTask(fragment, REQUEST_GET_BOOKMARKS, requestJSON, "/users/getBookmarks.json");
//	}
//	
//	public static void getRatings(JSONResponseHandler fragment, String eventID) {
//		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
//		try {
//			requestJSON.put("event_id", eventID);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		DefaultJSONTask jsonTask = new DefaultJSONTask();
////		jsonTask.executeTask(fragment, REQUEST_GET_RATINGS, requestJSON, "/users/alreadyLikedEvent.json");
//	}
//	
//	public static void getEventList(JSONResponseHandler fragment, String searchQuery, String locationQuery, int pageOffset) {
//		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
//		try {
//			requestJSON.put("search_query", searchQuery);
//			requestJSON.put("location_query", locationQuery);
//			requestJSON.put("page", pageOffset);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		DefaultJSONTask jsonTask = new DefaultJSONTask();
////		jsonTask.executeTask(fragment, REQUEST_GET_RATINGS, requestJSON, "/users/alreadyLikedEvent.json");
//	}
//	
//	// Methods for REQUEST_CANCEL_[A-Z]+ = 3[0-9][0-9]
//	public static void cancelEvent(JSONResponseHandler fragment, String eventID) {
//		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
//		try {
//			requestJSON.put("event_id", eventID);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		DefaultJSONTask jsonTask = new DefaultJSONTask();
////		jsonTask.executeTask(fragment, REQUEST_CANCEL_EVENT, requestJSON, "/users/cancelEvent.json");
//	}
//	
//	// Methods for REQUEST_DELETE_[A-Z]+ = 4[0-9][0-9]
//	public static void deleteComment(JSONResponseHandler fragment, String eventID, String commentID) {
//		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
//		try {
//			requestJSON.put("event_id", eventID);
//			requestJSON.put("comment_id", commentID);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		DefaultJSONTask jsonTask = new DefaultJSONTask();
////		jsonTask.executeTask(fragment, REQUEST_DELETE_COMMENT, requestJSON, "/users/removeComment.json");
//	}
//	
//	public static void deleteEvent(JSONResponseHandler fragment, String eventID) {
//		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
//		try {
//			requestJSON.put("event_id", eventID);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		DefaultJSONTask jsonTask = new DefaultJSONTask();
////		jsonTask.executeTask(fragment, REQUEST_DELETE_EVENT, requestJSON, "/users/deleteEvent.json");
//	}
//	
//	public static void deleteBookmark(JSONResponseHandler fragment, String eventID) {
//		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
//		try {
//			requestJSON.put("event_id", eventID);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		DefaultJSONTask jsonTask = new DefaultJSONTask();
////		jsonTask.executeTask(fragment, REQUEST_DELETE_BOOKMARK, requestJSON, "/users/removeBookmark.json");
//	}
//	
//	// Methods for REQUEST_MODIFY_[A-Z]+ = 5[0-9][0-9]
//	public static void modifyLike(JSONResponseHandler fragment, String eventID, boolean likeStatus) {
//		JSONObject requestJSON = createJSONObjectWithFacebookIDAndSessionToken();
//		try {
//			requestJSON.put("event_id", eventID);
//			requestJSON.put("like", likeStatus);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		DefaultJSONTask jsonTask = new DefaultJSONTask();
////		jsonTask.executeTask(fragment, REQUEST_MODIFY_LIKE, requestJSON, "/users/likeEvent.json");
//	}
//}