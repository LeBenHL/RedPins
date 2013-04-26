package com.example.redpins;

import android.app.ProgressDialog;
import android.content.Context;

public class CountingProgressDialog extends ProgressDialog {
	
	Integer count;
	
	public CountingProgressDialog(Context context) {
		super(context);
		this.count = 1;
	}
	
	public CountingProgressDialog(Context context, Integer count) {
		super(context);
		this.count = count;
	}
	
	@Override
	public void dismiss() {
		count--;
		if (count == 0) {
			super.dismiss();
		}
	}

}
