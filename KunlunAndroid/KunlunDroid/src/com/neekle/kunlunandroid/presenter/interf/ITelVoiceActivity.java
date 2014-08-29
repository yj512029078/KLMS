package com.neekle.kunlunandroid.presenter.interf;

import com.neekle.kunlunandroid.screens.TelVoiceActivity.ViewType;

public interface ITelVoiceActivity {
	public void test();

	public void showErrorHint(String msg);

	public void loadView(ViewType viewType);
}
