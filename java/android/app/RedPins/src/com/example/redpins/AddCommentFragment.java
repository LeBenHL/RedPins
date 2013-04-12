package com.example.redpins;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.redpins.EventFragment.GetCommentTask;
import com.google.android.gms.maps.SupportMapFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddCommentFragment extends NetworkFragment implements OnClickListener{
	private EditText commentText;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.add_comment_fragment, container, false);
		commentText = (EditText) view.findViewById(R.id.comment_input);
		Button submitButton = (Button) view.findViewById(R.id.comment_submit_button);
		submitButton.setOnClickListener(this);
		return view;
	}
	@Override
	public void onClick(View v) {
		Utility.addComment(this, getArguments().getString("event_id"), commentText.getText().toString());
		((MainActivity)getActivity()).showEventFrag(getArguments().getString("event_id"), getArguments().getString("callback"));
		getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
	}

	@Override
	public void onNetworkSuccess(JSONObject json) {
		System.out.println("Successfully posted a comment");
	}
	@Override
	public void onNetworkFailure(JSONObject json) {
		// TODO Auto-generated method stub
	}
}