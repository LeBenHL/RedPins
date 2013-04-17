package com.example.redpins;

import org.json.JSONObject;

public interface JSONResponseHandler {
	public void onNetworkSuccess (int requestCode, JSONObject json);
	public void onNetworkFailure (int requestCode, JSONObject json);
}
