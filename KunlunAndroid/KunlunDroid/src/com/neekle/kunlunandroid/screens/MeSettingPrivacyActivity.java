package com.neekle.kunlunandroid.screens;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.presenter.impl.MeSettingPrivacyActivityPresenter;
import com.neekle.kunlunandroid.presenter.interf.IMeSettingPrivacyActivity;

public class MeSettingPrivacyActivity extends Activity implements
		IMeSettingPrivacyActivity {

	private RelativeLayout mBackRelati;
	private TextView mModuleNameTxtv;
	private CheckBox mNeedToInvitedFriendWithMeCb;
	private RelativeLayout mBlacklistRelati;
	private RelativeLayout mWeiboPrivacySettingRelati;

	private MeSettingPrivacyActivityPresenter mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_setting_privacy);

		findViewAndSetAttributes();

		mPresenter = new MeSettingPrivacyActivityPresenter(this, this);
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
		switchMeSettingActivity();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void findViewAndSetAttributes() {
		mBackRelati = (RelativeLayout) findViewById(R.id.relati_back);
		mBackRelati.setOnClickListener(mOnClickListener);

		mModuleNameTxtv = (TextView) findViewById(R.id.tv_module_name);
		int resId = R.string.privacy;
		mModuleNameTxtv.setText(resId);

		mNeedToInvitedFriendWithMeCb = (CheckBox) findViewById(R.id.cb_need_to_be_invited_when_add_me_as_friend);
		mNeedToInvitedFriendWithMeCb
				.setOnCheckedChangeListener(mOnCheckedChangeListener);

		mBlacklistRelati = (RelativeLayout) findViewById(R.id.relati_blasklist);
		mBlacklistRelati.setOnClickListener(mOnClickListener);

		mWeiboPrivacySettingRelati = (RelativeLayout) findViewById(R.id.relati_weibo_privacy_setting);
		mWeiboPrivacySettingRelati.setOnClickListener(mOnClickListener);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == mBackRelati) {
				switchMeSettingActivity();
			} else if (v == mBlacklistRelati) {
				switchMeSettingBlacklistActivity();
			} else if (v == mWeiboPrivacySettingRelati) {
				switchMeSettingWeiboPrivacyActivity();
			}
		}
	};

	private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (buttonView == mNeedToInvitedFriendWithMeCb) {
				doOnRevNeedToInvitedFriendWithMeChanged(isChecked);
			}

		}
	};

	private void switchMeSettingActivity() {
		Class<? extends Activity> targetActivity = MeSettingActivity.class;
		mPresenter.doSwitchMeSettingActivity(this, targetActivity);
	}

	private void switchMeSettingBlacklistActivity() {
		Class<? extends Activity> targetActivity = MeSettingBlacklistActivity.class;
		mPresenter.doSwitchMeSettingBlacklistActivity(this, targetActivity);
	}

	private void switchMeSettingWeiboPrivacyActivity() {
		Class<? extends Activity> targetActivity = MeSettingWeiboPrivacyActivity.class;
		mPresenter.doSwitchMeSettingWeiboPrivacyActivity(this, targetActivity);
	}

	private void doOnRevNeedToInvitedFriendWithMeChanged(boolean isChecked) {
		mPresenter.doUpdateOnRevNeedToInvitedFriendWithMeCheck(isChecked);
	}

	@Override
	public void test() {

	}

	@Override
	public void showCheck(boolean isNeedInviteToFriendWithMe) {
		mNeedToInvitedFriendWithMeCb.setChecked(isNeedInviteToFriendWithMe);
	}
}
