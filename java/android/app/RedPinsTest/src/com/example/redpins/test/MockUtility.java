package com.example.redpins.test;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.redpins.*;

public class MockUtility extends Utility {

	public JSONObject requestServer(String url, JSONObject jsonInput) {
		// switch statement between request URLs, return customized JSONs 
		JSONObject response = null;
		try{
			if(url.equals(MainActivity.serverURL+"/users/login.json")){
				response = new JSONObject("{\"errCode\":1}");
			}else if(url.equals(MainActivity.serverURL+"/users/postComment.json")){
				response = new JSONObject("{\"errCode\":1}");
			}else if(url.equals(MainActivity.serverURL+"/users/bookmarkEvent.json")){
				response = new JSONObject("{\"errCode\":1}");
			}else if(url.equals(MainActivity.serverURL+"/users/uploadPhoto")){
				response = new JSONObject("{\"errCode\":1}");
			}else if(url.equals(MainActivity.serverURL+"/events/getComments.json")){
				response = new JSONObject("{\"comments\":[{\"comment_id\":8,\"facebook_id\":\"2\",\"created_at\":\"2013-04-16T19:17:17Z\",\"firstname\":\"Jerry\",\"lastname\":\"Chen\",\"comment\":\"EXPENSIVE FOOD\"}," +
						"{\"comment_id\":14,\"facebook_id\":\"3\",\"created_at\":\"2013-04-16T19:17:17Z\",\"firstname\":\"Andy\",\"lastname\":\"Lee\",\"comment\":\"THIS FOOD SO DA EXPENSIVE\"}," +
						"{\"comment_id\":23,\"facebook_id\":\"5\",\"created_at\":\"2013-04-16T19:17:17Z\",\"firstname\":\"Victor\",\"lastname\":\"Chang\",\"comment\":\"I LOVE TRUCK FOOD\"}],\"errCode\":1}");

			}else if(url.equals(MainActivity.serverURL+"/events/getEvent.json")){
				if(jsonInput.getString("event_id").equals("6")){
					response = new JSONObject("{\"errCode\":1,\"event\":{\"id\":6,\"title\":\"Off The Grid\",\"url\":\"www.google.com\",\"location\":\"2450 Haste St. Berkeley, CA\",\"start_time\":\"2013-04-27T00:00:00Z\",\"created_at\":\"2013-04-16T19:17:14Z\",\"updated_at\":\"2013-04-16T19:17:14Z\",\"end_time\":\"2013-04-28T00:00:00Z\",\"user_id\":1,\"canceled\":false,\"latitude\":37.8659538,\"longitude\":-122.2590753,\"description\":\"Great food! Though super expensive as fuck. I hope Korean Tacos are there!\",\"owner\":false}}");
				}else if(jsonInput.getString("event_id").equals("8")){
					response = new JSONObject("{\"errCode\":1,\"event\":{\"id\":8,\"title\":\"Holi Party\",\"url\":\"www.google.com\",\"location\":\"UC Berkeley. Berkeley, CA\",\"start_time\":\"2013-01-11T00:00:00Z\",\"created_at\":\"2013-04-16T19:17:14Z\",\"updated_at\":\"2013-04-16T19:17:14Z\",\"end_time\":\"2013-01-12T00:00:00Z\",\"user_id\":3,\"canceled\":false,\"latitude\":37.8717477,\"longitude\":-122.2609626,\"description\":\"Holi Celebration at Berkeley! Buy your colors at the table this week!\",\"owner\":false}}");

				}else if(jsonInput.getString("event_id").equals("3")){
					response = new JSONObject("{\"errCode\":1,\"event\":{\"id\":3,\"title\":\"Eric's BBQ'\",\"url\":\"www.google.com\",\"location\":\"2520 College Ave. Berkeley, CA\",\"start_time\":\"2013-03-13T00:00:00Z\",\"created_at\":\"2013-04-16T19:17:13Z\",\"updated_at\":\"2013-04-16T19:17:13Z\",\"end_time\":\"2013-03-14T00:00:00Z\",\"user_id\":4,\"canceled\":false,\"latitude\":37.864826,\"longitude\":-122.254198,\"description\":\"Meat, Steaks, Korean BBQ. No Vegatables needed. This is a man party.\",\"owner\":false}}");
				}
			}else if(url.equals(MainActivity.serverURL+"/users/getBookmarks.json")){
				response = new JSONObject("{\"errCode\":1," +
						"\"events\":[{\"id\":6,\"title\":\"Off The Grid\",\"url\":\"www.google.com\",\"location\":\"2450 Haste St. Berkeley, CA\",\"start_time\":\"2013-04-27T00:00:00Z\",\"created_at\":\"2013-04-16T19:17:14Z\",\"updated_at\":\"2013-04-16T19:17:14Z\",\"end_time\":\"2013-04-28T00:00:00Z\",\"user_id\":1,\"canceled\":false,\"latitude\":37.8659538,\"longitude\":-122.2590753,\"description\":\"Great food! Though super expensive as fuck. I hope Korean Tacos are there!\",\"owner\":false}," +
						"{\"id\":3,\"title\":\"Eric's BBQ'\",\"url\":\"www.google.com\",\"location\":\"2520 College Ave. Berkeley, CA\",\"start_time\":\"2013-03-13T00:00:00Z\",\"created_at\":\"2013-04-16T19:17:13Z\",\"updated_at\":\"2013-04-16T19:17:13Z\",\"end_time\":\"2013-03-14T00:00:00Z\",\"user_id\":4,\"canceled\":false,\"latitude\":37.864826,\"longitude\":-122.254198,\"description\":\"Meat, Steaks, Korean BBQ. No Vegatables needed. This is a man party.\",\"owner\":false}],\"next_page\":null}");

			}else if(url.equals(MainActivity.serverURL+"/users/alreadyLikedEvent.json")){//getRatings
				if(jsonInput.getString("event_id").equals("6")){
					response = new JSONObject("{\"likes\":3,\"dislikes\":0,\"errCode\":1,\"alreadyLikedEvent\":false}");
				}else if(jsonInput.getString("event_id").equals("8")){
					response = new JSONObject("{\"likes\":1,\"dislikes\":0,\"errCode\":1,\"alreadyLikedEvent\":false}");
				}else if(jsonInput.getString("event_id").equals("3")){
					response = new JSONObject("{\"likes\":1,\"dislikes\":1,\"errCode\":1,\"alreadyLikedEvent\":false}");
				}else{
					response = new JSONObject("{\"likes\":0,\"dislikes\":0,\"errCode\":1,\"alreadyLikedEvent\":false}");
				}
			}else if(url.equals(MainActivity.serverURL+"/events/search.json")){
				if(jsonInput.getString("search_query").equals("Berkeley")){
					response = new JSONObject("{\"errCode\":1,\"events\":[{\"id\":8,\"title\":\"Holi Party\",\"url\":\"www.google.com\",\"location\":\"UC Berkeley. Berkeley, CA\",\"start_time\":\"2013-01-11T00:00:00Z\",\"created_at\":\"2013-04-16T19:17:14Z\",\"updated_at\":\"2013-04-16T19:17:14Z\",\"end_time\":\"2013-01-12T00:00:00Z\",\"user_id\":3,\"canceled\":false,\"latitude\":37.8717477,\"longitude\":-122.2609626,\"description\":\"Holi Celebration at Berkeley! Buy your colors at the table this week!\",\"owner\":false}],\"next_page\":null}");
				}else if(jsonInput.getString("search_query").equals("Korean")){
					response = new JSONObject("{\"errCode\":1," +
							"\"events\":[{\"id\":3,\"title\":\"Eric's BBQ'\",\"url\":\"www.google.com\",\"location\":\"2520 College Ave. Berkeley, CA\",\"start_time\":\"2013-03-13T00:00:00Z\",\"created_at\":\"2013-04-16T19:17:13Z\",\"updated_at\":\"2013-04-16T19:17:13Z\",\"end_time\":\"2013-03-14T00:00:00Z\",\"user_id\":4,\"canceled\":false,\"latitude\":37.864826,\"longitude\":-122.254198,\"description\":\"Meat, Steaks, Korean BBQ. No Vegatables needed. This is a man party.\",\"owner\":false}" +
							",{\"id\":6,\"title\":\"Off The Grid\",\"url\":\"www.google.com\",\"location\":\"2450 Haste St. Berkeley, CA\",\"start_time\":\"2013-04-27T00:00:00Z\",\"created_at\":\"2013-04-16T19:17:14Z\",\"updated_at\":\"2013-04-16T19:17:14Z\",\"end_time\":\"2013-04-28T00:00:00Z\",\"user_id\":1,\"canceled\":false,\"latitude\":37.8659538,\"longitude\":-122.2590753,\"description\":\"Great food! Though super expensive as fuck. I hope Korean Tacos are there!\",\"owner\":false}," +
							"{\"id\":9,\"title\":\"Danceworks Workshop\",\"url\":\"www.google.com\",\"location\":\"Lower Sproul. Berkeley, CA\",\"start_time\":\"2013-02-16T00:00:00Z\",\"created_at\":\"2013-04-16T19:17:14Z\",\"updated_at\":\"2013-04-16T19:17:14Z\",\"end_time\":\"2013-02-17T00:00:00Z\",\"user_id\":4,\"canceled\":false,\"latitude\":37.8691454,\"longitude\":-122.2602313,\"description\":\"We will be teaching Hip hop and Korean Pop right here on Sproul!\",\"owner\":false}," +
							"{\"id\":16,\"title\":\"Eric's BBQ2'\",\"url\":\"www.google.com\",\"location\":\"2520 College Ave. Berkeley, CA\",\"start_time\":\"2013-03-13T00:00:00Z\",\"created_at\":\"2013-04-16T19:17:16Z\",\"updated_at\":\"2013-04-16T19:17:16Z\",\"end_time\":\"2013-03-14T00:00:00Z\",\"user_id\":4,\"canceled\":false,\"latitude\":37.864826,\"longitude\":-122.254198,\"description\":\"Meat, Steaks, Korean BBQ. No Vegatables needed. This is a man party.\",\"owner\":false}," +
							"{\"id\":17,\"title\":\"Eric's BBQ3'\",\"url\":\"www.google.com\",\"location\":\"2520 College Ave. Berkeley, CA\",\"start_time\":\"2013-03-13T00:00:00Z\",\"created_at\":\"2013-04-16T19:17:16Z\",\"updated_at\":\"2013-04-16T19:17:16Z\",\"end_time\":\"2013-03-14T00:00:00Z\",\"user_id\":4,\"canceled\":false,\"latitude\":37.864826,\"longitude\":-122.254198,\"description\":\"Meat, Steaks, Korean BBQ. No Vegatables needed. This is a man party.\",\"owner\":false},\"next_page\":null}");
				}else{
					response = new JSONObject("{\"errCode\":1,\"events\":[{\"id\":8,\"title\":\"Holi Party\",\"url\":\"www.google.com\",\"location\":\"UC Berkeley. Berkeley, CA\",\"start_time\":\"2013-01-11T00:00:00Z\",\"created_at\":\"2013-04-16T19:17:14Z\",\"updated_at\":\"2013-04-16T19:17:14Z\",\"end_time\":\"2013-01-12T00:00:00Z\",\"user_id\":3,\"canceled\":false,\"latitude\":37.8717477,\"longitude\":-122.2609626,\"description\":\"Holi Celebration at Berkeley! Buy your colors at the table this week!\",\"owner\":false}],\"next_page\":null}");
				}
			}else if(url.equals(MainActivity.serverURL+"/users/cancelEvent.json")){
				response = new JSONObject("{\"errCode\":1}");
			}else if(url.equals(MainActivity.serverURL+"/users/removeComment.json")){
				response = new JSONObject("{\"errCode\":1}");
			}else if(url.equals(MainActivity.serverURL+"/users/deleteEvent.json")){
				response = new JSONObject("{\"errCode\":1}");
			}else if(url.equals(MainActivity.serverURL+"/users/removeBookmark.json")){
				response = new JSONObject("{\"errCode\":1}");
			}else if(url.equals(MainActivity.serverURL+"/users/likeEvent.json")){
				response = new JSONObject("{\"errCode\":1}");
			}else{
				response = new JSONObject("{\"errCode\":1}");
			}
		}catch(JSONException e){
			System.out.println("FAILED SERVER REQUEST");
		}
		return response;
	}
}