package com.example.redpins.test;

import org.json.JSONObject;

import com.example.redpins.*;

public class MockUtility extends Utility {

	public static JSONObject requestServer(String url, JSONObject jsonInput) {
		// switch statement between request URLs, return customized JSONs 
		JSONObject response = new JSONObject();
		if(url.equals(MainActivity.serverURL+"/users/postComment.json")){
			
		}else if(url.equals(MainActivity.serverURL+"/users/BookmarkEvent.json")){
			
		}else if(url.equals(MainActivity.serverURL+"/users/uploadPhoto")){
			
		}else if(url.equals(MainActivity.serverURL+"/events/getComments.json")){
			
		}else if(url.equals(MainActivity.serverURL+"/events/get.json")){
			
		}else if(url.equals(MainActivity.serverURL+"/users/getBookmarks.json")){
			
		}else if(url.equals(MainActivity.serverURL+"/users/alreadyLikedEvent.json")){
		
		}else if(url.equals(MainActivity.serverURL+"/events/search.json")){
			
		}else if(url.equals(MainActivity.serverURL+"/users/cancelEvent.json")){
			
		}else if(url.equals(MainActivity.serverURL+"/users/removeComment.json")){
			
		}else if(url.equals(MainActivity.serverURL+"/users/deleteEvent.json")){
			
		}else if(url.equals(MainActivity.serverURL+"/users/removeBookmark.json")){
			
		}else if(url.equals(MainActivity.serverURL+"/users/likeEvent.json")){
			
		}
		return response;
	}
}