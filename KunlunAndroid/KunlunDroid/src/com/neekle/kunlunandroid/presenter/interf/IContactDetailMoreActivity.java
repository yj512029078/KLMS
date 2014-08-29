package com.neekle.kunlunandroid.presenter.interf;

import com.neekle.kunlunandroid.adapter.ContactDetailMoreAdapter;
import com.neekle.kunlunandroid.web.data.friend.FriendInfo;

public interface IContactDetailMoreActivity {

	public void test();

	public void updateContactDetailMoreAdapter(ContactDetailMoreAdapter adapter);

	public void showPopupWindow(String title, String content);

	public void showName(String showName);

	public void showHint(String hint, int duration);

}
