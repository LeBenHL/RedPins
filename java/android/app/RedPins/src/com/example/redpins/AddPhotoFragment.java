package com.example.redpins;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Photo"), 1);
			}
		});
		cancelBtn = (Button) view.findViewById(R.id.add_photo_cancel_btn);
		uploadBtn = (Button) view.findViewById(R.id.add_photo_upload_btn);
		uploadBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				System.out.println("uploading photo");
				try {
					bm = BitmapFactory.decodeFile(currImageURI.getEncodedPath());
					executeMultipartPost();
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
	        /*
		Bitmap photo = (Bitmap) data.getExtras().get("data"); 
        


        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
		
        Uri tempUri = getImageUri((((MainActivity)getActivity()).getApplicationContext()), photo);

        // CALL THIS METHOD TO GET THE ACTUAL PATH
        finalFile = new File(getRealPathFromURI(tempUri));
        //System.out.println(finalFile.getPath());
        //System.out.println(mImageCaptureUri);
	  	*/
		System.out.println("Received intent data");
		currImageURI = data.getData();
		// currImageURI = tempUri;
		//((MainActivity)getActivity()).getSupportLoaderManager().initLoader(0, null, (LoaderCallbacks<D>) ((MainActivity)getActivity()));
		photoPath.setText(currImageURI.getPath());//getRealPathFromURI(currImageURI));
		System.out.println("content://com.google.android.gallery3d.provider/" + currImageURI.getPath());
	}
	
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	public void executeMultipartPost() throws Exception {
		try {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.JPEG, 75, bos);
		byte[] data = bos.toByteArray();
		HttpClient httpClient = new DefaultHttpClient();
		System.out.println("Here");
		HttpPost postRequest = new HttpPost("http://10.0.1.10:3000/users/uploadPhoto");
		ByteArrayBody bab = new ByteArrayBody(data, currImageURI.getPath());
		//ByteArrayBody bab = new ByteArrayBody(data, getRealPathFromURI(currImageURI));
		System.out.println("executing upload");
		
		// File file= new File(“/mnt/sdcard/forest.png”);
		// FileBody bin = new FileBody(file);
		MultipartEntity reqEntity = new MultipartEntity(
		HttpMultipartMode.BROWSER_COMPATIBLE);
		reqEntity.addPart("photo", bab);
		reqEntity.addPart("photoCaption", new StringBody("Uploaded with RedPins Android"));
		reqEntity.addPart("facebook_id", new StringBody(((MainActivity)getActivity()).getFacebookId()));
		reqEntity.addPart("session_token", new StringBody(((MainActivity)getActivity()).getFacebookSessionToken()));
		postRequest.setEntity(reqEntity);
		HttpResponse response = httpClient.execute(postRequest);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
		response.getEntity().getContent(), "UTF-8"));
		String sResponse;
		StringBuilder s = new StringBuilder();

		while ((sResponse = reader.readLine()) != null) {
		s = s.append(sResponse);
		}
		// System.out.println("Response:—————————————————————————————————————————-> " + s);
		} catch (Exception e) {
			// handle exception here
			// Log.e(e.getClass().getName(), e.getMessage());
		}
	}

	
	public Uri getImageUri(Context inContext, Bitmap inImage) {
	    //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	    //inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
	    String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
	    return Uri.parse(path);
	}

	public String getRealPathFromURI(Uri uri) {
	    Cursor cursor = ((MainActivity)getActivity()).getContentResolver().query(uri, null, null, null, null); 
	    cursor.moveToFirst(); 
	    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
	    return cursor.getString(idx); 
	}
	
	/*
	private String getRealPathFromURI(Uri contentUri) {
		String [] proj={MediaStore.Images.Media.DATA};
		Cursor cursor = (((MainActivity)getActivity()).getContentResolver()).query(contentUri,
                        proj, // Which columns to return
                        null,       // WHERE clause; which rows to return (all rows)
                        null,       // WHERE clause selection arguments (none)
                        null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
	}
	*/
	
	
	public class AddPhotoTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
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
			
		}
		
	}
}