package com.example.redpins;

import com.google.android.gms.common.GooglePlayServicesUtil;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class LegalActivity extends Activity {

	private TextView legal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_legal);
		legal = (TextView) findViewById(R.id.legal_text);
		legal.setText(GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(getApplicationContext()));
	}

}
