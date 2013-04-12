package com.example.redpins;

import org.json.JSONObject;

import android.support.v4.app.Fragment;

public abstract class NetworkFragment extends Fragment{
	
	public abstract void onNetworkSuccess (JSONObject json);
	public abstract void onNetworkFailure (JSONObject json);
	
}
