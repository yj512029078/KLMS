package com.neekle.kunlunandroid.screens;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.presenter.impl.MeSettingWeiboPrivacyActivityPresenter;
import com.neekle.kunlunandroid.presenter.interf.IMeSettingWeiboPrivacyActivity;

public class MeSettingWeiboPrivacyActivity extends Activity implements
		IMeSettingWeiboPrivacyActivity {

	private RelativeLayout mBackRelati;
	private TextView mModuleNameTxtv;
	private RelativeLayout mNotSeeTheirWeiboRelati;
	private RelativeLayout mDeleteTempFilesRelati;

	private MeSettingWeiboPrivacyActivityPresenter mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_setting_weibo_privacy);

		findViewAndSetAttributes();

		mPresenter = new MeSettingWeiboPrivacyActivityPresenter(this, this);
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
		switchMeSettingPrivacyActivity();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void findViewAndSetAttributes() {
		mBackRelati = (RelativeLayout) findViewById(R.id.relati_back);
		mBackRelati.setOnClickListener(mOnClickListener);

		mModuleNameTxtv = (TextView) findViewById(R.id.tv_module_name);
		int resId = R.string.weibo_privacy_setting;
		mModuleNameTxtv.setText(resId);

		mNotSeeTheirWeiboRelati = (RelativeLayout) findViewById(R.id.relati_not_see_their_weibo);
		mNotSeeTheirWeiboRelati.setOnClickListener(mOnClickListener);

		mDeleteTempFilesRelati = (RelativeLayout) findViewById(R.id.relati_delete_temp_files);
		mDeleteTempFilesRelati.setOnClickListener(mOnClickListener);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == mNotSeeTheirWeiboRelati) {
				switchMeSettingWeiboPrivacyNotSeeActivity();
			} else if (v == mDeleteTempFilesRelati) {
				doOnDeleteTempFilesClick();
			}
		}

	};

	private void switchMeSettingWeiboPrivacyNotSeeActivity() {
		Class<? extends Activity> targetActivity = MeSettingWeiboPrivacyNotSeeActivity.class;
		mPresenter.doSwitchMeSettingWeiboPrivacyNotSeeActivity(this,
				targetActivity);
	}

	private void switchMeSettingPrivacyActivity() {
		Class<? extends Activity> targetActivity = MeSettingPrivacyActivity.class;
		mPresenter.doSwitchMeSettingPrivacyActivity(this, targetActivity);
	}

	public void doOnDeleteTempFilesClick() {
		mPresenter.doOnDeleteTempFilesClick();
	}

	@Override
	public void test() {

	}
}
