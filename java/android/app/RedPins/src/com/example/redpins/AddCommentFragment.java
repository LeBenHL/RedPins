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

public class AddCommentFragment extends Fragment implements OnClickListener{
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
		// TODO Auto-generated method stub
		AddCommentTask task = new AddCommentTask();
		task.execute();
		getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
	}

	public class AddCommentTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONObject json = new JSONObject();
			try {
				//adds input values into JSON data object
				json.put("facebook_id", ((MainActivity)getActivity()).facebook_id);
				json.put("event_id", getArguments().getString("event_id"));
				json.put("comment", commentText.getText().toString());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			JSONArray ret = null;
			try {
				//sends requests to server and receives
				ret = Utility.requestServerArr(MainActivity.serverURL+"/users/postComment.json", json);
			} catch (Throwable e) {
			}
			return null;
		}
	}
}