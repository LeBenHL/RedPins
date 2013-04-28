package com.example.redpins;

import com.facebook.Request;
import com.facebook.Response;

import android.os.AsyncTask;

public class FacebookTask extends AsyncTask<Void, Void, Response> {
	
	Request request;
	
	public FacebookTask(Request request) {
		super();
		this.request = request;
	}

	@Override
	protected Response doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		return request.executeAndWait();
	}

}
