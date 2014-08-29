package com.neekle.kunlunandroid.view.specials;

import android.app.ProgressDialog;
import android.content.Context;

import com.neekle.kunlunandroid.R;

public class MyProgressDialog {

	private ProgressDialog mProgressDialog;

	public void showProgressDialog(Context context, String tittle, String msg) {
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setTitle(tittle);
		mProgressDialog.setMessage(msg);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.show();
	}

	public void closeProgressDialog() {
		mProgressDialog.dismiss();
	}
}
