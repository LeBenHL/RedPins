package com.example.redpins;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

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
	
	public static final int REQUEST_ADD_COMMENT = 100;
	public static final int REQUEST_ADD_EVENT= 101;
	public static final int REQUEST_ADD_PHOTO = 102;
	public static final int REQUEST_ADD_BOOKMARK = 103;
	public static final int REQUEST_GET_EVENT = 200;
	
	public static JSONObject requestServer(String url,JSONObject jsonInput){
		HttpPost request = new HttpPost(url);
		Log.v("UTILITY", "API REQUEST: " + url);
		Log.v("UTILITY", "JSON INPUT: " + jsonInput.toString());
		JSONStringer jsonString = new JSONStringer();
		if (jsonInput!=null){
			Iterator<String> iter = jsonInput.keys();
			if(iter.hasNext())
				try {
					jsonString.object();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			while (iter.hasNext()){
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
		entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
		request.setEntity(entity); 

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 50000); 
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),50000); 

		HttpResponse response =null;
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
		StringBuilder sBuilder =new StringBuilder();
		try {
			String line = buffReader.readLine();
			while(line != null){
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
	
	public static void getEvent(NetworkFragment networkFragment, String eventID) {
		JSONObject requestJSON = new JSONObject();
		try {
			requestJSON.put("event_id", eventID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		DefaultJSONTask jsonTask = new DefaultJSONTask();
		jsonTask.executeTask(networkFragment, REQUEST_GET_EVENT, requestJSON, "/events/get.json");
	}
}
	
