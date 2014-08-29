package com.neekle.kunlunandroid.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.presenter.impl.LoginSettingActiyPresenter;
import com.neekle.kunlunandroid.presenter.interf.ILoginSettingActivity;
import com.neekle.kunlunandroid.updateapp.AppUpgradeAssist;
import com.neekle.kunlunandroid.updateapp.AppUpgradeService;

public class LoginSettingActivity extends Activity implements
		ILoginSettingActivity {

	private RelativeLayout mForgetPasswdRelati;
	private RelativeLayout mRegisterAccountRelati;
	private RelativeLayout mSettingRelati;
	private RelativeLayout mChatUseWlanRelati;
	private RelativeLayout mUpdataAppRelati;

	private LoginSettingActiyPresenter mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_setting);

		findViewAndSetAttributes();
		mPresenter = new LoginSettingActiyPresenter(this, this);
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
		switchLoginActivity();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void findViewAndSetAttributes() {
		mForgetPasswdRelati = (RelativeLayout) findViewById(R.id.relati_forget_password);
		mForgetPasswdRelati.setOnClickListener(mOnClickListener);

		mRegisterAccountRelati = (RelativeLayout) findViewById(R.id.relati_register_account);
		mRegisterAccountRelati.setOnClickListener(mOnClickListener);

		mSettingRelati = (RelativeLayout) findViewById(R.id.relati_setting);
		mSettingRelati.setOnClickListener(mOnClickListener);

		mChatUseWlanRelati = (RelativeLayout) findViewById(R.id.relati_chat_use_wlan);
		mChatUseWlanRelati.setOnClickListener(mOnClickListener);

		mUpdataAppRelati = (RelativeLayout) findViewById(R.id.relati_update_app);
		mUpdataAppRelati.setOnClickListener(mOnClickListener);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == mForgetPasswdRelati) {
				doSwitchPasswordResetActivity();
			} else if (v == mRegisterAccountRelati) {
				doSwitchAccountRegisterActivity();
			} else if (v == mSettingRelati) {
				doSwitchSoftwareSettingActivity();
			} else if (v == mChatUseWlanRelati) {

			} else if (v == mUpdataAppRelati) {
				AppUpgradeAssist appUpgradeAssist = new AppUpgradeAssist(
						LoginSettingActivity.this);
				appUpgradeAssist
						.setOnCheckVersionListener(new AppUpgradeAssist.OnCheckVersionListener() {

							@Override
							public void onCheck(boolean flag) {
								if (flag) {
									// 开启更新服务UpdateService
									// 这里为了把update更好模块化，可以传一些updateService依赖的值
									// 如布局ID，资源ID，动态获取的标题,这里以app_name为例
									Intent updateIntent = new Intent(
											LoginSettingActivity.this,
											AppUpgradeService.class);
									startService(updateIntent);
								}

							}
						});
				appUpgradeAssist.checkVersionToUpgrade();
			}
		}
	};

	public void switchLoginActivity() {
		Class<? extends Activity> targetActivity = LoginActivity.class;
		mPresenter.doSwitchLoginActivity(this, targetActivity);
	}

	protected void doSwitchPasswordResetActivity() {
		Class<? extends Activity> targetActivity = PasswordResetActivity.class;
		mPresenter.doSwitchSoftwareSettingActivity(this, targetActivity);

	}

	public void doSwitchSoftwareSettingActivity() {
		Class<? extends Activity> targetActivity = SoftwareSettingActivity.class;
		mPresenter.doSwitchSoftwareSettingActivity(this, targetActivity);
	}

	public void doSwitchAccountRegisterActivity() {
		Class<? extends Activity> targetActivity = AccountRegisterActivity.class;
		mPresenter.doSwitchSoftwareSettingActivity(this, targetActivity);
	}

	@Override
	public void test() {

	}
}
