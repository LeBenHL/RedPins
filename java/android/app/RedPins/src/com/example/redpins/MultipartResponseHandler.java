package com.example.redpins;

import org.apache.http.HttpResponse;

public interface MultipartResponseHandler {
	public void onNetworkSuccess (int requestCode, HttpResponse response);
	public void onNetworkFailure (int requestCode, HttpResponse response);
}
