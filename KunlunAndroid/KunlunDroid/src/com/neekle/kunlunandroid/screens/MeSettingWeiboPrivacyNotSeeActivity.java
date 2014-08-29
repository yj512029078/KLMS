package com.neekle.kunlunandroid.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.adapter.MeSettingWeiboPrivacyNotSeeAdapter;
import com.neekle.kunlunandroid.presenter.impl.MeSettingWeiboPrivacyNotSeeActivityPresenter;
import com.neekle.kunlunandroid.presenter.interf.IMeSettingWeiboPrivacyNotSeeActivity;

public class MeSettingWeiboPrivacyNotSeeActivity extends Activity implements
		IMeSettingWeiboPrivacyNotSeeActivity {

	private RelativeLayout mBackRelati;
	private TextView mModuleNameTxtv;
	private GridView mGridView;

	private MeSettingWeiboPrivacyNotSeeActivityPresenter mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_setting_weibo_privacy_not_see);

		findViewAndSetAttributes();

		mPresenter = new MeSettingWeiboPrivacyNotSeeActivityPresenter(this,
				this);
		mPresenter.init();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		switchMeSettingWeiboPrivacyActivity();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		mPresenter.resolveToGetSelectedContacts(data);
	}

	private void findViewAndSetAttributes() {
		mBackRelati = (RelativeLayout) findViewById(R.id.relati_back);
		mBackRelati.setOnClickListener(mOnClickListener);

		mModuleNameTxtv = (TextView) findViewById(R.id.tv_module_name);
		int resId = R.string.not_see_their_weibo;
		mModuleNameTxtv.setText(resId);

		mGridView = (GridView) findViewById(R.id.gridview);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

		}

	};

	private void switchMeSettingWeiboPrivacyActivity() {
		Class<? extends Activity> targetActivity = MeSettingWeiboPrivacyActivity.class;
		mPresenter.doSwitchMeSettingWeiboPrivacyActivity(this, targetActivity);
	}

	@Override
	public void test() {

	}

	@Override
	public void updateAdapter(MeSettingWeiboPrivacyNotSeeAdapter adapter) {
		mGridView.setAdapter(adapter);
	}

	@Override
	public void showHint(String hint, int time) {
		Toast.makeText(this, hint, time).show();
	}

}
