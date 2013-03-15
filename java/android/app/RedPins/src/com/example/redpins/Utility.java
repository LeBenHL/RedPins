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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class Utility{
	//takes care of sending and receiving json data

	public static JSONObject requestServer(String url,JSONObject jsonInput){
		HttpPost request = new HttpPost(url);
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
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 10000); 
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),10000); 

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
}