package com.neekle.kunlunandroid.presenter.interf;

import com.neekle.kunlunandroid.adapter.MeSettingBlacklistAdapter;
import com.neekle.kunlunandroid.adapter.MeSettingWeiboPrivacyNotSeeAdapter;

public interface IMeSettingWeiboPrivacyNotSeeActivity {
	public void test();

	public void updateAdapter(MeSettingWeiboPrivacyNotSeeAdapter adapter);

	public void showHint(String hint, int time);
}
