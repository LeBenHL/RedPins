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
				response = new JSONObject("{\"errCode\":1,\"events\":[{\"id\":18,\"title\":\"Bookmark Event 1\",\"url\":\"http://www.redpins.com\",\"location\":\"Berkeley\",\"start_time\":\"2013-04-19T13:00:00Z\",\"created_at\":\"2013-04-19T20:00:30Z\",\"updated_at\":\"2013-04-19T20:00:30Z\",\"end_time\":\"2013-04-19T14:00:00Z\",\"user_id\":6,\"canceled\":false,\"latitude\":37.86616569838829,\"longitude\":-122.2582331299782,\"description\":\"\",\"owner\":true,\"bookmark\":true,\"isPhoto\":false,\"likes\":0,\"dislikes\":0},{\"id\":1,\"title\":\"Bookmark Event 2\",\"url\":\"www.google.com\",\"location\":\"2540 Regent St. Berkeley, CA\",\"start_time\":\"2010-09-08T00:00:00Z\",\"created_at\":\"2013-04-10T20:29:32Z\",\"updated_at\":\"2013-04-10T20:29:32Z\",\"end_time\":\"2010-09-10T00:00:00Z\",\"user_id\":5,\"canceled\":false,\"latitude\":37.8635552,\"longitude\":-122.2578662,\"description\":\"It's Victor's birthday!\",\"owner\":false,\"bookmark\":true,\"isPhoto\":false,\"likes\":4,\"dislikes\":1}],\"next_page\":null}");

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
			}else if(url.equals(MainActivity.serverURL+"/events/search" + "ViaCoordinates.json")){
				response = new JSONObject("{\"errCode\":1,\"events\":[{\"id\":18,\"title\":\"Fake Event 1\",\"url\":\"\",\"location\":\" Berkeley, CA 94704 USA\",\"start_time\":\"2013-05-01T00:59:00Z\",\"created_at\":\"2013-05-01T01:01:50Z\",\"updated_at\":\"2013-05-01T01:01:50Z\",\"end_time\":\"2013-05-01T01:59:00Z\",\"user_id\":7,\"canceled\":false,\"latitude\":37.869516768969426,\"longitude\":-122.25994873791932,\"description\":\"\",\"owner\":false,\"bookmark\":false,\"isPhoto\":false,\"photo\":\"/system/event_images/photos/000/000/007/thumbnail/newPhoto.JPEG?1367568813\",\"likes\":1,\"dislikes\":1},{\"id\":19,\"title\":\"Fake Event 2\",\"url\":\"\",\"location\":\" 2400-2498 Le Conte Ave Berkeley, CA 94709 USA\",\"start_time\":\"2013-05-01T00:59:00Z\",\"created_at\":\"2013-05-01T01:02:24Z\",\"updated_at\":\"2013-05-01T01:02:24Z\",\"end_time\":\"2013-05-01T01:59:00Z\",\"user_id\":7,\"canceled\":false,\"latitude\":37.87688615305561,\"longitude\":-122.26096965372562,\"description\":\"\",\"owner\":false,\"bookmark\":false,\"isPhoto\":false,\"photo\":\"/system/event_images/photos/000/000/008/thumbnail/newPhoto.JPEG?1367568944\",\"likes\":0,\"dislikes\":1},{\"id\":20,\"title\":\"Fake Event 3\",\"url\":\"\",\"location\":\" 2776 Hilgard Ave Berkeley, CA 94709 USA\",\"start_time\":\"2013-05-01T01:10:00Z\",\"created_at\":\"2013-05-01T01:13:25Z\",\"updated_at\":\"2013-05-01T01:13:25Z\",\"end_time\":\"2013-05-01T02:10:00Z\",\"user_id\":7,\"canceled\":false,\"latitude\":37.87902471113747,\"longitude\":-122.25566625595093,\"description\":\"\",\"owner\":false,\"bookmark\":false,\"isPhoto\":false,\"photo\":\"/system/event_images/photos/000/000/005/thumbnail/newPhoto.JPEG?1367387096\",\"likes\":1,\"dislikes\":0},{\"id\":21,\"title\":\"Fake Event 4\",\"url\":\"\",\"location\":\" 6021 College Ave Oakland, CA 94618 UK\",\"start_time\":\"2013-05-01T06:12:00Z\",\"created_at\":\"2013-05-01T06:12:37Z\",\"updated_at\":\"2013-05-01T06:12:37Z\",\"end_time\":\"2013-05-01T07:12:00Z\",\"user_id\":6,\"canceled\":false,\"latitude\":37.848592647225644,\"longitude\":-122.25225381553173,\"description\":\"\",\"owner\":false,\"bookmark\":false,\"isPhoto\":false,\"likes\":0,\"dislikes\":0}],\"next_page\":2}");
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
			}else if(url.equals(MainActivity.serverURL+"/users/getRecentEvents.json")){
				response = new JSONObject("{\"errCode\":1,\"events\":[{\"id\":20,\"title\":\"Recent Event 1\",\"url\":\"\",\"location\":\" 2776 Hilgard Ave Berkeley, CA 94709 USA\",\"start_time\":\"2013-05-01T01:10:00Z\",\"created_at\":\"2013-05-01T01:13:25Z\",\"updated_at\":\"2013-05-01T01:13:25Z\",\"end_time\":\"2013-05-01T02:10:00Z\",\"user_id\":7,\"canceled\":false,\"latitude\":37.87902471113747,\"longitude\":-122.25566625595093,\"description\":\"\",\"owner\":false,\"bookmark\":false,\"isPhoto\":false,\"photo\":\"/system/event_images/photos/000/000/005/thumbnail/newPhoto.JPEG?1367387096\",\"likes\":1,\"dislikes\":0},{\"id\":33,\"title\":\"Recent Event 2\",\"url\":\"\",\"location\":\" 2453-2499 Parker St Berkeley, CA 94704 USA\",\"start_time\":\"2013-05-03T08:34:00Z\",\"created_at\":\"2013-05-03T08:34:50Z\",\"updated_at\":\"2013-05-03T08:34:50Z\",\"end_time\":\"2013-05-03T09:34:00Z\",\"user_id\":8,\"canceled\":false,\"latitude\":37.8633938,\"longitude\":-122.2580737,\"description\":\"\",\"owner\":true,\"bookmark\":false,\"isPhoto\":false,\"likes\":0,\"dislikes\":0},{\"id\":32,\"title\":\"Recent Event 3\",\"url\":\"\",\"location\":\" 2453-2499 Parker St Berkeley, CA 94704 USA\",\"start_time\":\"2013-05-03T08:30:00Z\",\"created_at\":\"2013-05-03T08:31:04Z\",\"updated_at\":\"2013-05-03T08:31:04Z\",\"end_time\":\"2013-05-03T09:30:00Z\",\"user_id\":8,\"canceled\":false,\"latitude\":37.8633774,\"longitude\":-122.2580567,\"description\":\"\",\"owner\":true,\"bookmark\":false,\"isPhoto\":false,\"likes\":0,\"dislikes\":0},{\"id\":30,\"title\":\"Recent Event 4\",\"url\":\"\",\"location\":\" 2453-2499 Parker St Berkeley, CA 94704 USA\",\"start_time\":\"2013-05-03T08:20:00Z\",\"created_at\":\"2013-05-03T08:20:49Z\",\"updated_at\":\"2013-05-03T08:20:49Z\",\"end_time\":\"2013-05-03T09:20:00Z\",\"user_id\":8,\"canceled\":false,\"latitude\":37.8633959,\"longitude\":-122.2580751,\"description\":\"\",\"owner\":true,\"bookmark\":false,\"isPhoto\":false,\"likes\":0,\"dislikes\":0},{\"id\":19,\"title\":\"Recent Event 5\",\"url\":\"\",\"location\":\" 2400-2498 Le Conte Ave Berkeley, CA 94709 USA\",\"start_time\":\"2013-05-01T00:59:00Z\",\"created_at\":\"2013-05-01T01:02:24Z\",\"updated_at\":\"2013-05-01T01:02:24Z\",\"end_time\":\"2013-05-01T01:59:00Z\",\"user_id\":7,\"canceled\":false,\"latitude\":37.87688615305561,\"longitude\":-122.26096965372562,\"description\":\"\",\"owner\":false,\"bookmark\":false,\"isPhoto\":false,\"photo\":\"/system/event_images/photos/000/000/008/thumbnail/newPhoto.JPEG?1367568944\",\"likes\":0,\"dislikes\":1},{\"id\":18,\"title\":\"Recent Event 6\",\"url\":\"\",\"location\":\" Berkeley, CA 94704 USA\",\"start_time\":\"2013-05-01T00:59:00Z\",\"created_at\":\"2013-05-01T01:01:50Z\",\"updated_at\":\"2013-05-01T01:01:50Z\",\"end_time\":\"2013-05-01T01:59:00Z\",\"user_id\":7,\"canceled\":false,\"latitude\":37.869516768969426,\"longitude\":-122.25994873791932,\"description\":\"\",\"owner\":false,\"bookmark\":false,\"isPhoto\":false,\"photo\":\"/system/event_images/photos/000/000/007/thumbnail/newPhoto.JPEG?1367568813\",\"likes\":1,\"dislikes\":1}],\"next_page\":null}");
			}else{
				response = new JSONObject("{\"errCode\":1}");
			}
		}catch(JSONException e){
			System.out.println("FAILED SERVER REQUEST");
		}
		return response;
	}
}