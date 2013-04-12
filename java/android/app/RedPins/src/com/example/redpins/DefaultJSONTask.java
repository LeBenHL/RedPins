package com.example.redpins;

import org.json.JSONObject;

import android.os.AsyncTask;

public class DefaultJSONTask extends AsyncTask<Void, Void, JSONObject>{
	NetworkFragment networkFragment;
	JSONObject requestJSON;
	String requestPath;
	
	@Override
	protected JSONObject doInBackground(Void... params) {
		JSONObject responseJSON = null;
		try {
			responseJSON = Utility.requestServer(MainActivity.serverURL + requestPath, requestJSON);
		} catch (Throwable e) {
			// TODO Exception: Handle the exception for the server request.
			networkFragment.onNetworkFailure(responseJSON);
		}
		return responseJSON;
	}
	
	@Override
	protected void onPostExecute(JSONObject responseJSON) {
		super.onPostExecute(responseJSON);
		networkFragment.onNetworkSuccess(responseJSON);
	}
	
	protected void executeTask(NetworkFragment networkFragment, JSONObject requestJSON, String requestPath) {
		this.networkFragment = networkFragment;
		this.requestJSON = requestJSON;
		this.requestPath = requestPath;
		this.execute();
	}
}