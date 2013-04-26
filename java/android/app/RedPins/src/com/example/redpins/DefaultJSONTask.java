package com.example.redpins;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class DefaultJSONTask extends AsyncTask<Void, Void, JSONObject> {
	JSONResponseHandler fragment;
	int requestCode;
	JSONObject requestJSON;
	String requestPath;
	
	@Override
	protected JSONObject doInBackground(Void... params) {
		JSONObject responseJSON = null;
		try {
			responseJSON = MainActivity.utility.requestServer(MainActivity.serverURL + requestPath, requestJSON);
		} catch (Throwable e) {
			responseJSON = null;
		}
		return responseJSON;
	}
	
	@Override
	protected void onPostExecute(JSONObject responseJSON) {
		if (responseJSON != null ) {
			Log.i("JSON RESPONSE", responseJSON.toString());
			super.onPostExecute(responseJSON);
			fragment.onNetworkSuccess(requestCode, responseJSON);
		} else {
			fragment.onNetworkFailure(requestCode, responseJSON);
		}
	}
	
	protected void executeTask(JSONResponseHandler fragment, int requestCode, JSONObject requestJSON, String requestPath) {
		this.fragment = fragment;
		this.requestCode = requestCode;
		this.requestJSON = requestJSON;
		this.requestPath = requestPath;
		this.execute();
	}
}