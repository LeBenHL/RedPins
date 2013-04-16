package com.example.redpins;

import org.json.JSONObject;

import android.support.v4.app.Fragment;

public abstract class NetworkFragment extends Fragment{
	
	public abstract void onNetworkSuccess (int requestCode, JSONObject json);
	public abstract void onNetworkFailure (int requestCode, JSONObject json);
	
}
