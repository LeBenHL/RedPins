package com.example.redpins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class AddPhotoFragment extends Fragment implements OnClickListener, MultipartResponseHandler {
	private static final int SELECT_GALLERY_REQUEST_CODE = 0;
	
	Button uploadBtn, cancelBtn, selectBtn;
	TextView photoPath;
	Bitmap bm;
	File finalFile;
	File outputFileName;
	private ProgressDialog progress;
	private ImageView photo;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_photo_fragment, container, false);
		selectBtn = (Button) view.findViewById(R.id.add_photo_select_btn);
		photo = (ImageView) view.findViewById(R.id.photo); 
		selectBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				/*
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Photo"), 1);
				*/
				//startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), SELECT_GALLERY_REQUEST_CODE);
				Intent pickIntent = new Intent();
				pickIntent.setType("image/*");
				pickIntent.setAction(Intent.ACTION_GET_CONTENT);

				try {
					outputFileName = createImageFile(".tmp");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFileName));

				String pickTitle = "Select or take a new Picture"; // Or get from strings.xml
				Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
				chooserIntent.putExtra
				(
				  Intent.EXTRA_INITIAL_INTENTS, 
				  new Intent[] { takePhotoIntent }
				);

				startActivityForResult(chooserIntent, SELECT_GALLERY_REQUEST_CODE);
			}
		});
		cancelBtn = (Button) view.findViewById(R.id.add_photo_cancel_btn);
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				bm = null;
				photo.setImageResource(R.drawable.content_attachment);
			}
		});
		uploadBtn = (Button) view.findViewById(R.id.add_photo_upload_btn);
		uploadBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				System.out.println("uploading photo");
				try {
					progress = MainActivity.utility.addProgressDialog(getActivity(), "Uploading", "Uploading Photo...");
					MainActivity.utility.addPhoto(AddPhotoFragment.this, getArguments().getString("event_id"), bm, "uploaded with RedPins");
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SELECT_GALLERY_REQUEST_CODE) {
			if (data != null && data.getData() != null) {
				Uri selectedImage = data.getData();
				try {
					bm = MediaStore.Images.Media.getBitmap(((MainActivity)getActivity()).getContentResolver(), selectedImage);
					photo.setImageBitmap(bm); 
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (outputFileName != null){
				try {
					bm = MediaStore.Images.Media.getBitmap(((MainActivity)getActivity()).getContentResolver(), Uri.fromFile(outputFileName));
					photo.setImageBitmap(bm); 
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private File createImageFile(String fileExtensionToUse) throws IOException {

	    File storageDir = new File(
	            Environment.getExternalStoragePublicDirectory(
	                Environment.DIRECTORY_PICTURES
	            ), 
	            "MyImages"
	        );      

	    if(!storageDir.exists())
	    {
	        if (!storageDir.mkdir())
	        {
	            Log.d("createImageFile","was not able to create it");
	        }
	    }
	    if (!storageDir.isDirectory())
	    {
	        Log.d("createImageFile","Don't think there is a dir there.");
	    }

	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "FOO_" + timeStamp + "_image";

	    File image = File.createTempFile(
	        imageFileName, 
	        fileExtensionToUse, 
	        storageDir
	    );

	    return image;
	}    
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNetworkSuccess(int requestCode, HttpResponse response) {
		JSONObject responseJSON = MainActivity.utility.convertHttpResponseToJSONObject(response); // resulting JSONObject
		progress.dismiss();
		bm = null;
		photo.setImageResource(R.drawable.content_attachment);
	}

	@Override
	public void onNetworkFailure(int requestCode, HttpResponse response) {
		progress.dismiss();
		// TODO Auto-generated method stub
		
	}
}