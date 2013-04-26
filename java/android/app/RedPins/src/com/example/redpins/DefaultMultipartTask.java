package com.example.redpins;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class DefaultMultipartTask extends AsyncTask<Void, Void, HttpResponse> {
	MultipartResponseHandler fragment;
	int requestCode;
	MultipartEntity requestEntity;
	String requestPath;
	
	@Override
	protected HttpResponse doInBackground(Void... params) {
		HttpPost httpPost = new HttpPost(MainActivity.serverURL + requestPath);
		httpPost.setEntity(requestEntity);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse postResponse = null;
		try {
			postResponse = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			//Do nothing
		} catch (IOException e) {
			//Do Nothing
		}
		return postResponse;
	}
	
	@Override
	protected void onPostExecute(HttpResponse response) {
		if (response == null) {
			fragment.onNetworkFailure(requestCode, response);
		} else {
			fragment.onNetworkSuccess(requestCode, response);
		}
	}
	
	protected void executeTask(MultipartResponseHandler fragment, int requestCode, MultipartEntity requestEntity, String requestPath) {
		this.fragment = fragment;
		this.requestCode = requestCode;
		this.requestEntity = requestEntity;
		this.requestPath = requestPath;
		this.execute();
	}
}
