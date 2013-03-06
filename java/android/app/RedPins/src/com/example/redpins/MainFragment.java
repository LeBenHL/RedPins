package com.example.redpins;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import org.json.JSONObject;

import com.facebook.*;
import com.facebook.model.*;
import com.facebook.widget.LoginButton;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment {
	
	private static final String TAG = "MainFragment";
	private UiLifecycleHelper uiHelper;
	
	//The facebook user of our app
	private GraphUser _user;
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, 
	        Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.activity_main, container, false);
	    
	    LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
	    authButton.setFragment(this);
	    //authButton.setReadPermissions(Arrays.asList("email", "user_events", "user_interests", "user_likes", "friends_events"));
	    authButton.setReadPermissions(Arrays.asList("email"));
	    
	    return view;
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        // Request user data and show the results
	        Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

	            @Override
	            public void onCompleted(GraphUser user, Response response) {
	                if (user != null) {
	                	_user = user;
	                }
	            }
	        });
	        ConnectivityManager connMgr = (ConnectivityManager) 
	            getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	       // if (networkInfo != null && networkInfo.isConnected()) {
	        if (true) {
	            new postJSON(_user).execute("loginUser");
	        } else {
	        }
	        Log.i(TAG, "Logged in...");
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	    }
	}
	
    private String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
	
	@Override
	public void onResume() {
	    super.onResume();	    
	    // For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }

	    
	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
    // Uses AsyncTask to create a task away from the main UI thread. This task takes a 
    // action we want to perform and calls on the appropriate API of our RedPins server to
	// perform the action.
    private class postJSON extends AsyncTask {
    	
    	private static final int SUCCESS = 1;
		private static final int ERR_NO_USER_EXISTS = -1;
		private static final int ERR_USER_EXISTS = -2;
		private static final int ERR_BAD_EMAIL = -3;
		private static final int ERR_BAD_FACEBOOK_ID = -4;
    	
    	//The base URL we are trying to post to
    	private static final String baseUrl = "http://safe-savannah-1864.herokuapp.com";
    	
    	//The facebook session we are working on
    	private GraphUser _user;
    	//The action we are trying to perform
    	private String _action;
    	
    	public postJSON(GraphUser user) {
    		super();
    		_user = user;
    		
    	}
      
    	protected JSONObject doInBackground(Object... actions) {
           // params comes from the execute() call: params[0] is the action we want to perform.
           try {
        	   _action = (String) actions[0];
               return postUrl((String) actions[0]);
           } catch (IOException e) {
               return null;
           }
       }
       // onPostExecute reads JSON Object and responds appropriately to the error messages.
       protected void onPostExecute(Object o) {
		   Log.v("onPostExecute", _action);
    	   try {
	           JSONObject json = (JSONObject) o; 
	    	   if (json != null) {
        		   Log.v("onPostExecute", json.toString());
	    		   switch (Actions.valueOf(_action.toUpperCase())) {
	        		case LOGINUSER:
	 	    		   switch (json.getInt("errCode")) {
		 	    		   case ERR_NO_USER_EXISTS:
		 	    			   new postJSON(_user).execute("addUser");
		 	    			   break;
	 	    			   default:
	 	    				   break;
			    		}
	        			break;
	        		case ADDUSER:
	        			break;
	        		default:
	        			//Should not reach this case EVER
	        			throw new Exception();
	        	}
	    	   }
    	   } catch (Exception e) {
    		   Log.v("onPostExecute", e.getMessage());
    	   }
       }
       
	    // Given a URL, establishes an HttpUrlConnection, POSTS JSON data
	    // and retrieves the JSON Object back from the server
	    private JSONObject postUrl(String action) throws IOException {
	        InputStream is = null;   
	        try {
	        	String myUrl;
	        	switch (Actions.valueOf(action.toUpperCase())) {
	        		case LOGINUSER:
	        			myUrl = baseUrl + "/users/login.json";
	        			break;
	        		case ADDUSER:
	        			myUrl = baseUrl + "/users/add.json";
	        			break;
	        		default:
	        			//Should not reach this case EVER
	        			throw new Exception();
	        	}
	        	Log.v("postUrl", myUrl);
	            URL url = new URL(myUrl);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setDoOutput(true);
	            conn.setReadTimeout(10000 /* milliseconds */);
	            conn.setConnectTimeout(15000 /* milliseconds */);
	            conn.setRequestProperty("Content-Type", "application/json");
	            conn.setRequestProperty("Accept", "application/json");
	            conn.setRequestMethod("POST");
	            conn.setDoInput(true);
	            // Starts the query
	            Log.v("postUrl", "Connecting");
	            conn.connect();
	            Log.v("postUrl", "Done");
	        	// Post JSON data
	            JSONObject jsonToSend = new JSONObject();
	        	switch (Actions.valueOf(action.toUpperCase())) {
	        		case LOGINUSER:
	    	            jsonToSend.put("email", _user.getProperty("email").toString());
	    	            jsonToSend.put("facebook_id", _user.getProperty("id").toString());
	        			break;
	        		case ADDUSER:
	    	            jsonToSend.put("email", _user.getProperty("email").toString());
	    	            jsonToSend.put("facebook_id", _user.getProperty("id").toString());
	        			break;
	        		default:
	        			//Should not reach this case EVER
	        			throw new Exception();
	        	}
	            Log.v("postUrl", _user.getProperty("email").toString());
	            Log.v("postUrl", _user.getProperty("id").toString());
	        	//byte[] outputBytes = jsonString.getBytes("UTF-8");
	        	OutputStream os = conn.getOutputStream();
	        	os.write(jsonToSend.toString().getBytes());
	         	Log.v("postUrl", Integer.valueOf(conn.getResponseCode()).toString());
	            if(conn.getResponseCode() == 200) {
	                // Connection was established. Get the content. 
	            	is = conn.getInputStream();
	            	String jsonString = convertStreamToString(is);
	            	Log.v("postUrl", jsonString);
		            // Convert the InputStream into a JSON Object
		            return new JSONObject(jsonString);
	            } else {
	            	return null;
	            }
	            
	        // Makes sure that the InputStream is closed after the app is
	        // finished using it.
	        } catch (Exception e) {
	        	return null;
	    	} finally {
	            if (is != null) {
	                is.close();
	            } 
	        }
	    }
    }
    
    public enum Actions {

        LOGINUSER,
        ADDUSER
    }
}
    
