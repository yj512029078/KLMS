package com.neekle.kunlunandroid.screens;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.presenter.impl.LoginActiyPresenter;
import com.neekle.kunlunandroid.presenter.impl.SoftwareServerSettingActiyPresenter;
import com.neekle.kunlunandroid.presenter.interf.ISoftwareServerSettingActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SoftwareServerSettingActivity extends Activity implements
		ISoftwareServerSettingActivity {

	private RelativeLayout mBackRelati;
	private TextView mModuleNameTxtv;
	private RelativeLayout mCompleteRelati;
	private TextView mCompleteTxtv;
	private RadioButton mUseNeekleAddressRadioBtn;
	private EditText mUseNeekleAddressEditxt;
	private RadioButton mUseOwnAddressRadioBtn;
	private EditText mUseOwnAddressEditxt;

	private SoftwareServerSettingActiyPresenter mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.software_server_setting);

		findViewsAndSetAttributes();

		mPresenter = new SoftwareServerSettingActiyPresenter(this, this);
		Intent intent = getIntent();
		mPresenter.init(intent);
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
		switchSoftwareSettingActivity();
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

		mCompleteRelati = (RelativeLayout) findViewById(R.id.relati_operation_right);
		int resid = R.drawable.cm_top_bar_operation_right_bg;
		mCompleteRelati.setBackgroundResource(resid);
		mCompleteRelati.setOnClickListener(mOnClickListener);

		mCompleteTxtv = (TextView) findViewById(R.id.tv_operation_right);
		resId = R.string.complete;
		mCompleteTxtv.setText(resId);

		mUseNeekleAddressRadioBtn = (RadioButton) findViewById(R.id.rbtn_use_neekle_service_address);
		mUseNeekleAddressRadioBtn.setChecked(true);
		mUseNeekleAddressRadioBtn
				.setOnCheckedChangeListener(mOnCheckedChangeListener);

		mUseNeekleAddressEditxt = (EditText) findViewById(R.id.editxt_use_neekle_service_address);

		mUseOwnAddressRadioBtn = (RadioButton) findViewById(R.id.rbtn_use_your_own_address);
		mUseOwnAddressRadioBtn.setChecked(false);
		mUseOwnAddressRadioBtn
				.setOnCheckedChangeListener(mOnCheckedChangeListener);

		mUseOwnAddressEditxt = (EditText) findViewById(R.id.editxt_use_your_own_address);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == mBackRelati) {
				switchSoftwareSettingActivity();
			} else if (v == mCompleteRelati) {
				doOnCompleteClick();
			}
		}
	};

	private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (mUseNeekleAddressRadioBtn == buttonView) {
				if (isChecked) {
					setUseNeekleAddressView();
				}
			} else if (mUseOwnAddressRadioBtn == buttonView) {
				if (isChecked) {
					setUseOwnAddressView();
				}
			}

		}
	};

	private void setUseNeekleAddressView() {
		mUseNeekleAddressRadioBtn.setChecked(true);
		mUseOwnAddressRadioBtn.setChecked(false);
		mUseNeekleAddressEditxt.setEnabled(true);
		mUseNeekleAddressEditxt.requestFocus();
		mUseOwnAddressEditxt.setEnabled(false);
	}

	private void setUseOwnAddressView() {
		mUseNeekleAddressRadioBtn.setChecked(false);
		mUseOwnAddressRadioBtn.setChecked(true);
		mUseNeekleAddressEditxt.setEnabled(false);
		mUseOwnAddressEditxt.setEnabled(true);
		mUseOwnAddressEditxt.requestFocus();
	}

	public void doOnCompleteClick() {
		boolean isInputOk = judgeInputValid();

		if (isInputOk) {
			String string = getServerAddressToSet();
			mPresenter.doOnCompleteClick(string);
		} else {
			String hint = getString(R.string.input_invalid);
			showHint(hint, Toast.LENGTH_SHORT);
		}
	}

	private String getServerAddressToSet() {
		String string = null;

		if (mUseNeekleAddressRadioBtn.isChecked()) {
			string = mUseNeekleAddressEditxt.getText().toString();
		} else if (mUseOwnAddressRadioBtn.isChecked()) {
			string = mUseOwnAddressEditxt.getText().toString();
		}

		return string;
	}

	private boolean judgeInputValid() {
		boolean isOk = true;

		if (mUseNeekleAddressRadioBtn.isChecked()) {
			String string = mUseNeekleAddressEditxt.getText().toString();
			if (string.equals(Constants.EMPTY_STRING)) {
				isOk = false;
			}
		} else if (mUseOwnAddressRadioBtn.isChecked()) {
			String string = mUseOwnAddressEditxt.getText().toString();
			if (string.equals(Constants.EMPTY_STRING)) {
				isOk = false;
			}
		}

		return isOk;
	}

	public void showHint(String hint, int duration) {
		Toast toast = Toast.makeText(this, hint, duration);
		toast.show();
	}

	private void switchSoftwareSettingActivity() {
		Class<? extends Activity> targetActivity = SoftwareSettingActivity.class;
		mPresenter.doSwitchSoftwareSettingActivity(this, targetActivity);
	}

	@Override
	public void test() {

	}

	@Override
	public void showUseNeekleAddress(boolean isShow, String address) {
		if (isShow) {
			mUseNeekleAddressEditxt.setText(address);
			setUseNeekleAddressView();
		} else {
			mUseOwnAddressEditxt.setText(address);
			setUseOwnAddressView();
		}
	}
}
