package com.example.redpins;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;

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
