package com.neekle.kunlunandroid.presenter.interf;

import com.neekle.kunlunandroid.screens.TelVideoActivity.ViewType;

public interface ITelVideoActivity {
	public void test();

	public void showErrorHint(String msg);

	public void loadView(ViewType viewType);
}
