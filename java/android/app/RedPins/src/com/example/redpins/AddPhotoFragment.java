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


public class AddPhotoFragment extends Fragment implements OnClickListener{
	private static final int SELECT_GALLERY_REQUEST_CODE = 0;
	private static final int RESULT_OK = 1;
	
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
					//bm = BitmapFactory.decodeFile(currImageURI.getEncodedPath());
					//executeMultipartPost();
					//AddCommentTask task = new AddCommentTask();
					AddPhotoTask task = new AddPhotoTask();
					task.execute();
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
				System.out.println("set bm");
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
	
	public class AddPhotoTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			System.out.println("executing multipart post now!");
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(MainActivity.serverURL + "/users/uploadPhoto");
			try {
				  MultipartEntity entity = new MultipartEntity();
				 
				  entity.addPart("type", new StringBody("photo"));
				  //File f = new File(context.getCacheDir(), "uploadPhoto.jpeg");
				  
				  
				  ByteArrayOutputStream bos = new ByteArrayOutputStream();
				  bm.compress(Bitmap.CompressFormat.PNG, 85, bos);
				  byte[] data = bos.toByteArray();
				  
				  String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/RedPins_temp";
				  File dir = new File(file_path);
				  if(!dir.exists()) {
				      dir.mkdirs();
				  }
				  File file = new File(dir, "sample001" + ".png");
				  FileOutputStream fOut = new FileOutputStream(file);
				  fOut.write(data);
				  fOut.flush();
				  fOut.close();
				  
				  
				  // ByteArrayOutputStream bos = new ByteArrayOutputStream();
				  // bm.compress(CompressFormat.JPEG, 100, bos);
				  
				  //FileOutputStream fos = new FileOutputStream();
				  entity.addPart("photo", new FileBody(file));
				  entity.addPart("photoCaption", new StringBody("Uploaded with RedPins Android"));
				  entity.addPart("facebook_id", new StringBody(((MainActivity)getActivity()).getFacebookId()));
				  entity.addPart("session_token", new StringBody(((MainActivity)getActivity()).getFacebookSessionToken()));
				  httppost.setEntity(entity);
				  HttpResponse postResponse = httpclient.execute(httppost);
				} catch (ClientProtocolException e) {
					System.out.println("Client Protocol Exception");
				} catch (IOException e) {
					System.out.println("IOException");
				}
				System.out.println("Finished post request");
			/*
			// TODO Auto-generated method stub
			JSONObject json = new JSONObject();
			try {
				//adds input values into JSON data object
				json.put("facebook_id", ((MainActivity)getActivity()).getFacebookId());
				json.put("session_token", ((MainActivity)getActivity()).getFacebookSessionToken());
				json.put("event_id", "1");
				
				System.out.println(getArguments().getString("event_id"));
				
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			JSONObject ret = null;
			try {
				//sends requests to server and receives
				ret = Utility.requestServer(MainActivity.serverURL+"/users/uploadPhoto.json", json);
			System.out.println(ret.toString());
			} catch (Throwable e) {
				
			}
			return null;
			*/
			return null;
		}
		
	}
}