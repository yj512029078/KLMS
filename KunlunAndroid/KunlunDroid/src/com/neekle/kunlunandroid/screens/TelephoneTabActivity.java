package com.neekle.kunlunandroid.screens;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.common.Constants;

public class TelephoneTabActivity extends TabActivity implements
		OnCheckedChangeListener {

	private static final String DIAL_TAB = "dial_tab";
	private static final String UN_ANSWER_TAB = "un_answer_tab";
	private static final String ALL_TAB = "all_tab";

	// 定义Intent对象
	private Intent mDialIntent, mUnAnswerIntent, mAllIntent;

	// 定义TabHost对象
	private TabHost mTabHost;

	// 定义单选按钮对象
	private RadioButton mDialRb, mUnAnswerRb, mAllRb;
	private RelativeLayout mBackRelati;
	private TextView mModuleNameTxtv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.telephone_tab);

		initView();
		initData();
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

	// 分析发现：从TAB所管理的界面退出时，没有执行此方法，执行了实际的Activity的onBackPressed方法
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		// 得到TabHost
		mTabHost = getTabHost();

		// 得到Intent对象
		mDialIntent = new Intent(this, TelephoneDialActivity.class);
		mUnAnswerIntent = new Intent(this, TelephoneUnAnswerActivity.class);
		mAllIntent = new Intent(this, TelephoneAllActivity.class);

		// 得到单选按钮对象
		mDialRb = ((RadioButton) findViewById(R.id.radio_dial));
		mUnAnswerRb = ((RadioButton) findViewById(R.id.radio_unanswer));
		mAllRb = ((RadioButton) findViewById(R.id.radio_all));

		mBackRelati = (RelativeLayout) findViewById(R.id.relati_back);
		mBackRelati.setOnClickListener(mOnClickListener);

		mModuleNameTxtv = (TextView) findViewById(R.id.tv_module_name);
		int resId = R.string.telephone;
		mModuleNameTxtv.setText(resId);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// 给单选按钮设置监听
		mDialRb.setOnCheckedChangeListener(this);
		mUnAnswerRb.setOnCheckedChangeListener(this);
		mAllRb.setOnCheckedChangeListener(this);

		// 添加进Tab选项卡
		mTabHost.addTab(buildTabSpec(DIAL_TAB, mDialIntent));
		mTabHost.addTab(buildTabSpec(UN_ANSWER_TAB, mUnAnswerIntent));
		mTabHost.addTab(buildTabSpec(ALL_TAB, mAllIntent));

		// 设置当前默认的Tab选项卡页面
		mDialRb.setChecked(true);
		mTabHost.setCurrentTabByTag(DIAL_TAB);
	}

	private TabHost.TabSpec buildTabSpec(String tag, Intent intent) {
		TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tag);
		tabSpec.setContent(intent).setIndicator(Constants.EMPTY_STRING);

		return tabSpec;
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}

	};

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			switch (buttonView.getId()) {
			case R.id.radio_dial: {
				mTabHost.setCurrentTabByTag(DIAL_TAB);
				// TelephoneTabActivity.this.finish();
				break;
			}
			case R.id.radio_unanswer: {
				mTabHost.setCurrentTabByTag(UN_ANSWER_TAB);
				// TelephoneTabActivity.this.finish();
				break;
			}
			case R.id.radio_all: {
				mTabHost.setCurrentTabByTag(ALL_TAB);
				// TelephoneTabActivity.this.finish();
				break;
			}
			default: {
				break;
			}

			}
		}
	}

}
