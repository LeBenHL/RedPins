package com.example.redpins;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class FacebookFragment extends Fragment implements JSONResponseHandler {
	
	private static final String TAG = "MainFragment";
	private UiLifecycleHelper uiHelper;
	
	//The facebook user of our app
	private GraphUser _user;
	//The facebook session of the user using our app
	private Session _session;
	//The LoginButton
	protected LoginButton authButton;
	//Error Codes
	private static final int SUCCESS = 1;
	private static final int ERR_NO_USER_EXISTS = -1;
	private static final int ERR_USER_EXISTS = -2;
	private static final int ERR_BAD_EMAIL = -3;
	private static final int ERR_BAD_FACEBOOK_ID = -4;
	
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
	    View view = inflater.inflate(R.layout.facebook_fragment, container, false);
	    
	    authButton = (LoginButton) view.findViewById(R.id.authButton);
	    authButton.setFragment(this);
	    //authButton.setReadPermissions(Arrays.asList("email", "user_events", "user_interests", "user_likes", "friends_events"));
	    authButton.setReadPermissions(Arrays.asList("email"));
	    
	    return view;
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		((MainActivity) getActivity()).setFacebookSession(session);
		_session = session;
	    if (state.isOpened()) {
	        // Request user data and show the results
	        Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

	            @Override
	            public void onCompleted(GraphUser user, Response response) {
	                if (user != null) {
	                	_user = user;
	                	((MainActivity) getActivity()).setFacebookUser(user);
	                	Log.v("onSessionStateChange", "GOT USER");
	        	        ConnectivityManager connMgr = (ConnectivityManager) 
	        		            getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        		        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        		       if (networkInfo != null && networkInfo.isConnected()) {
        		    	   Utility.loginUser(FacebookFragment.this, _user.getProperty("email").toString(), _user.getProperty("id").toString(), _session.getAccessToken());
        		    	   ((MainActivity) getActivity()).hideFacebookFragment();
        		        } else {
        		        }
        		       ((MainActivity) getActivity()).setFacebookMenuLogout();
        		        Log.i(TAG, "Logged in...");
	                }
	            }
	        });
	    } else if (state.isClosed()) {
	    	_user = null;
	    	((MainActivity) getActivity()).setFacebookUser(null);
	    	((MainActivity) getActivity()).showFacebookFragment();
	    	((MainActivity) getActivity()).setFacebookMenuLogin();
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
	
    
    
    public enum Actions {
        LOGINUSER,
        ADDUSER
    }

	@Override
	public void onNetworkSuccess(int requestCode, JSONObject json) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case Utility.REQUEST_LOGIN_USER:
			try {
				switch (json.getInt("errCode")) {
				  case ERR_NO_USER_EXISTS:
					  System.out.println("REQUEST_LOGIN_USER: User does not exist. Adding user now.");
					  Utility.addUser(FacebookFragment.this, _user.getProperty("email").toString(), _user.getProperty("id").toString(), _session.getAccessToken(), _user.getFirstName(),  _user.getLastName());
					  break;
				  default:
					  break;
				}
			} catch (JSONException e) {
				Toast toast = Toast.makeText(getActivity(), "REQUEST_LOGIN_USER: Could not connect to server", Toast.LENGTH_SHORT);
				toast.show();
			}
			break;
		case Utility.REQUEST_ADD_USER:
			System.out.println("REQUEST_ADD_USER: Created a new user");
			break;
		}
	}

	@Override
	public void onNetworkFailure(int requestCode, JSONObject json) {
		// TODO Auto-generated method stub
		
	}
}
    
