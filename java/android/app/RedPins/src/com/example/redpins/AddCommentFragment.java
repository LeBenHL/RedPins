package com.example.redpins;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AddCommentFragment extends Fragment implements OnClickListener, JSONResponseHandler {
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
		MainActivity.utility.addComment(this, getArguments().getString("event_id"), commentText.getText().toString());
		((MainActivity) getActivity()).onBackPressed();
	}

	@Override
	public void onNetworkSuccess(int requestCode, JSONObject json) {
		switch (requestCode) {
			case Utility.REQUEST_ADD_COMMENT:
				System.out.println("Successfully posted a comment");
				break;
			default:
				System.out.println("onNetworkSuccess does not know what to do with result of network request");
		}
	}
	
	@Override
	public void onNetworkFailure(int requestCode, JSONObject json) {
		switch (requestCode) {
			case Utility.REQUEST_ADD_COMMENT:
				System.out.println("Failed to post a comment");
				break;
			default:
				System.out.println("onNetworkSuccess does not know what to do with result of network request");
		}
	}
}