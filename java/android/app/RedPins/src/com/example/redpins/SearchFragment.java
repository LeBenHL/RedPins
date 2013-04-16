package com.example.redpins;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class SearchFragment extends Fragment implements OnQueryTextListener{
	private EditText locInput;
	private SearchView searchView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.search_fragment, container, false);
		SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
		searchView = (SearchView) view.findViewById(R.id.search_view);
		locInput = (EditText) view.findViewById(R.id.location_input);
		searchView.setOnQueryTextListener(this);
		//searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
		searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
		searchView.setSubmitButtonEnabled(true); // Enable a submit button
		searchView.setFocusable(false);
		searchView.setFocusableInTouchMode(false);
		locInput.setFocusableInTouchMode(false);
		locInput.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				locInput.setFocusable(true);
				locInput.setFocusableInTouchMode(true);
				locInput.setEnabled(true);
				return false;
			}
		});
		locInput.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus){
					locInput.setFocusable(false);
					locInput.setFocusableInTouchMode(false);
				}
			}
		});
		return view;
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		Log.v("MainActivity", "THIS IS THE QUERY: " + query);
		((MainActivity) getActivity()).mQuery = query;
		((MainActivity) getActivity()).mLoc = locInput.getText().toString();
		searchView.setFocusableInTouchMode(false);
		searchView.setFocusable(false);
		searchView.setSelected(false);
		Bundle data = new Bundle();
		data.putString("query",((MainActivity) getActivity()).mQuery);
		data.putString("location", ((MainActivity) getActivity()).mLoc);
		((MainActivity) getActivity()).createListviewFrag(data);
		return false;
	}
}