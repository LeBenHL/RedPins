package com.example.redpins;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Response;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class AddPhotoFragment extends Fragment implements OnClickListener, MultipartResponseHandler {
	private static final int SELECT_GALLERY_REQUEST_CODE = 0;
	
	Button uploadBtn, cancelBtn, selectBtn;
	TextView photoPath;
	Uri currImageURI;
	Bitmap bm;
	File finalFile;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_photo_fragment, container, false);
		selectBtn = (Button) view.findViewById(R.id.add_photo_select_btn);
		selectBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				/*
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Photo"), 1);
				*/
				startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), SELECT_GALLERY_REQUEST_CODE);
			}
		});
		cancelBtn = (Button) view.findViewById(R.id.add_photo_cancel_btn);
		uploadBtn = (Button) view.findViewById(R.id.add_photo_upload_btn);
		uploadBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				System.out.println("uploading photo");
				try {
					Utility.addPhoto(AddPhotoFragment.this, getArguments().getString("event_id"), bm, "uploaded with RedPins");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Failed");
					e.printStackTrace();
				}
			}
		});
		
		photoPath = (TextView) view.findViewById(R.id.add_photo_path);
		return view;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SELECT_GALLERY_REQUEST_CODE) {
			Uri selectedImage = data.getData();
			try {
				bm = MediaStore.Images.Media.getBitmap(((MainActivity)getActivity()).getContentResolver(), selectedImage);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNetworkSuccess(int requestCode, HttpResponse response) {
		JSONObject responseJSON = Utility.convertHttpResponseToJSONObject(response); // resulting JSONObject
	}

	@Override
	public void onNetworkFailure(int requestCode, HttpResponse response) {
		// TODO Auto-generated method stub
		
	}
}