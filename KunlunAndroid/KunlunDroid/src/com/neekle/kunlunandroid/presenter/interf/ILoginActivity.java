package com.neekle.kunlunandroid.presenter.interf;

public interface ILoginActivity {

	public void test();

	public void showProgressDialog(String tittle, String msg);

	public void shutProgressDialog();

	public void showErrorToast(String msg, boolean isLong);

	// public void updateUIByPrefe(LoginPreference loginPreference);

	// public void autoLogin();

	public void showLoginAccount(String account);
}
