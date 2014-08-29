package com.neekle.kunlunandroid.screens;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.presenter.impl.AddsbookActiyPresenter;
import com.neekle.kunlunandroid.presenter.impl.SoftwareSettingActiyPresenter;
import com.neekle.kunlunandroid.presenter.interf.ISoftwareSettingActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SoftwareSettingActivity extends Activity implements
		ISoftwareSettingActivity {

	private RelativeLayout mBackRelati;
	private TextView mModuleNameTxtv;
	private RelativeLayout mServerRelati;
	private TextView mServerMoreTxtv;

	private SoftwareSettingActiyPresenter mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.software_setting);

		findViewsAndSetAttributes();

		mPresenter = new SoftwareSettingActiyPresenter(this, this);
		mPresenter.init();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
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
		switchLoginSettingActivity();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void findViewsAndSetAttributes() {
		mBackRelati = (RelativeLayout) findViewById(R.id.relati_back);
		mBackRelati.setOnClickListener(mOnClickListener);

		mModuleNameTxtv = (TextView) findViewById(R.id.tv_module_name);
		int resId = R.string.software_setting;
		mModuleNameTxtv.setText(resId);

		mServerRelati = (RelativeLayout) findViewById(R.id.relati_server_address);
		mServerRelati.setOnClickListener(mOnClickListener);

		mServerMoreTxtv = (TextView) findViewById(R.id.tv_server_more);
		mServerMoreTxtv.setOnClickListener(mOnClickListener);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == mBackRelati) {
				switchLoginSettingActivity();
			} else if (v == mServerRelati) {
				switchSoftwareServerSettingActivity();
			}
		}
	};

	private void switchLoginSettingActivity() {
		Class<? extends Activity> targetActivity = LoginSettingActivity.class;
		mPresenter.doSwitchLoginSettingActivity(this, targetActivity);
	}

	private void switchSoftwareServerSettingActivity() {
		Class<? extends Activity> targetActivity = SoftwareServerSettingActivity.class;
		mPresenter.doSwitchSoftwareServerSettingActivity(this, targetActivity);
	}

	@Override
	public void test() {

	}

	@Override
	public void showServerAddress(String address) {
		mServerMoreTxtv.setText(address);
	}

}
