package com.example.redpins;

import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class AddCommentFragment extends Fragment implements OnClickListener, JSONResponseHandler {
	private EditText commentText;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_comment_fragment, container, false);
		commentText = (EditText) view.findViewById(R.id.comment_input);
		commentText.requestFocus();
		InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_IMPLICIT_ONLY);
		Button submitButton = (Button) view.findViewById(R.id.comment_submit_button);
		submitButton.setOnClickListener(this);
		return view;
	}
	@Override
	public void onClick(View v) {
		MainActivity.utility.addComment(this, getArguments().getString("event_id"), commentText.getText().toString());
		InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
	      Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
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