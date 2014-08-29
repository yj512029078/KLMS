package com.neekle.kunlunandroid.screens;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.adapter.CellPhoneContactAdapter;
import com.neekle.kunlunandroid.presenter.impl.TelephoneAllActivityPresenter;
import com.neekle.kunlunandroid.presenter.impl.TelephoneDialActivityPresenter;
import com.neekle.kunlunandroid.presenter.interf.ITelephoneAllActivity;
import com.neekle.kunlunandroid.presenter.interf.ITelephoneDialActivity;

public class TelephoneAllActivity extends Activity implements
		ITelephoneAllActivity {

	private ListView mAllPhoneContactLv;

	private TelephoneAllActivityPresenter mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.telephone_all);

		findViewAndSetAttributes();

		mPresenter = new TelephoneAllActivityPresenter(this, this);
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
		switchMainActivity();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void findViewAndSetAttributes() {
		mAllPhoneContactLv = (ListView) findViewById(R.id.lv_active_phone_contact);
	}

	@Override
	public void updateAdapter(CellPhoneContactAdapter adapter) {
		mAllPhoneContactLv.setAdapter(adapter);
	}

	private void switchMainActivity() {
		Class<? extends Activity> targetActivity = MainScreenActivity.class;
		mPresenter.doSwitchMainActivity(this, targetActivity);
	}
}
