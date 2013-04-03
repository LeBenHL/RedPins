package com.example.redpins;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class SearchFragment extends Fragment implements OnQueryTextListener{
	private EditText locInput;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.search_fragment, container, false);
		SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) view.findViewById(R.id.search_view);
		searchView.setOnQueryTextListener(this);
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
		searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
		searchView.setSubmitButtonEnabled(true); // Enable a submit button
		locInput = (EditText) view.findViewById(R.id.location_input);

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
		if(((MainActivity)getActivity()).listFragment != null){
			((MainActivity) getActivity()).hideListviewFrag();
		}
		((MainActivity) getActivity()).showListviewFrag();
		((MainActivity) getActivity()).hideNaviFrag();
		return false;
	}
}