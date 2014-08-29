package com.neekle.kunlunandroid.presenter.interf;

import android.os.Message;

public interface IAccountRegisterActivity {

	public enum FragmentPage {
		ServerOptFragment, AccountFragment, DetailFragment, ConfirmFragment
	}

	public class MessageType {
		public static final int GET_HUMAN_VALIDATE_CODE_PICTURE = 0;
		public static final int HUMAN_VALIDATE = 1;
		public static final int ACCOUNT_REG = 2;
	}

	public void cancel();

	public void back();

	public void next();

	public void handleMessage(Message msg);

}
