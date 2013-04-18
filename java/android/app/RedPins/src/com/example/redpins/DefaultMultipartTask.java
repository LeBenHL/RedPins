package com.example.redpins;

import org.apache.http.entity.mime.MultipartEntity;

import android.os.AsyncTask;

public class DefaultMultipartTask extends AsyncTask<Void, Void, Void> {

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected void executeTask(MultipartResponseHandler fragment, int requestCode, MultipartEntity requestEntity, String requestPath) {
		this.execute();
	}
}
