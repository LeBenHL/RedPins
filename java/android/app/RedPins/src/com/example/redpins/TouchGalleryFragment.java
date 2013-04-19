package com.example.redpins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;
import ru.truba.touchgallery.GalleryWidget.UrlPagerAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TouchGalleryFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, 
	        Bundle savedInstanceState) {
		Log.i("TouchGalleryFragment On Create", "ON CREATE");
		View view = inflater.inflate(R.layout.touch_gallery_fragment, container, false);
		String[] urls = {
		"http://cs407831.userapi.com/v407831207/18f6/jBaVZFDhXRA.jpg",
		"http://cs407831.userapi.com/v4078f31207/18fe/4Tz8av5Hlvo.jpg", //special url with error
		"http://cs407831.userapi.com/v407831207/1906/oxoP6URjFtA.jpg",
		"http://cs407831.userapi.com/v407831207/190e/2Sz9A774hUc.jpg",
		"http://cs407831.userapi.com/v407831207/1916/Ua52RjnKqjk.jpg",
		"http://cs407831.userapi.com/v407831207/191e/QEQE83Ok0lQ.jpg"
		};
		List<String> items = new ArrayList<String>();
		Collections.addAll(items, urls);
		UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(getActivity(), items);  
		GalleryViewPager mViewPager = (GalleryViewPager)getActivity().findViewById(R.id.viewer);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setAdapter(pagerAdapter);
		
		return view;
	}

}
