package com.example.redpins;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;
import ru.truba.touchgallery.GalleryWidget.UrlPagerAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;

public class TouchGalleryFragment extends Fragment implements JSONResponseHandler {
	
	GalleryViewPager viewer;
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, 
	        Bundle savedInstanceState) {
		Log.i("TouchGalleryFragment On Create", "ON CREATE");
		View view = inflater.inflate(R.layout.touch_gallery_fragment, container, false);
		viewer = (GalleryViewPager) view.findViewById(R.id.viewer);
		Utility.getPhotos(this, getArguments().getString("event_id"));
		return view;
	}

	@Override
	public void onNetworkSuccess(int requestCode, JSONObject json) {
		//JSONArray jsonArray = Utility.lookupJSONArrayFromJSONObject(json, "urls");
		JSONArray jsonArray;
		List<String> urlsList = new ArrayList<String>();
		try {
			jsonArray = json.getJSONArray("urls");
			for(int i = 0, count = jsonArray.length(); i< count; i++) {
		        String path = jsonArray.getString(i);
		        String fullUrl = MainActivity.serverURL + path;
		        Log.i("Touch Gallery", fullUrl);
		        urlsList.add(fullUrl);
			}
		} catch (JSONException e){
			
		}
		Log.i("TouchGalleryFragment On Create", "GOT ALL URLS");
		if (getActivity() != null) {
			if (urlsList.size() != 0) {
				UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(getActivity(), urlsList);  
				GalleryViewPager mViewPager = (GalleryViewPager) getView().findViewById(R.id.viewer);
				mViewPager.setOffscreenPageLimit(3);
				mViewPager.setAdapter(pagerAdapter);
			} else {
				((ViewManager) viewer.getParent()).removeView(viewer);
			}
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNetworkFailure(int requestCode, JSONObject json) {
		// TODO Auto-generated method stub
		
	}

}
