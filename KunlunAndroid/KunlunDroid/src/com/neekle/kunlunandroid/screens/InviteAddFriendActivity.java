package com.neekle.kunlunandroid.screens;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.presenter.impl.AddsbookActiyPresenter;
import com.neekle.kunlunandroid.presenter.impl.InviteAddFriendActiyPresenter;
import com.neekle.kunlunandroid.presenter.impl.LoginActiyPresenter;
import com.neekle.kunlunandroid.presenter.interf.IInviteAddFriendActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InviteAddFriendActivity extends Activity implements
		IInviteAddFriendActivity {

	private RelativeLayout mBackRelati;
	private TextView mModuleNameTxtv;
	private EditText mFriendAccountEditxt;
	private EditText mInviteInfoEditxt;
	private RelativeLayout mSendInviteInfoRelati;

	private InviteAddFriendActiyPresenter mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invite_add_friend);

		findViewsAndSetAttributes();

		mPresenter = new InviteAddFriendActiyPresenter(this, this);
		Intent intent = getIntent();
		mPresenter.init(intent);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		doBack();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}

	private void findViewsAndSetAttributes() {
		mBackRelati = (RelativeLayout) findViewById(R.id.relati_back);
		mBackRelati.setOnClickListener(mOnClickListener);

		mModuleNameTxtv = (TextView) findViewById(R.id.tv_module_name);
		int addsbookResId = R.string.invite_add_friend;
		mModuleNameTxtv.setText(addsbookResId);

		mFriendAccountEditxt = (EditText) findViewById(R.id.edittxt_friend_account_info_or_cell_number);
		mInviteInfoEditxt = (EditText) findViewById(R.id.edittxt_invite_info);

		// Then comment it
		String account = "test15@yj-pc";
		mFriendAccountEditxt.setText(account);

		mSendInviteInfoRelati = (RelativeLayout) findViewById(R.id.relati_send_invite);
		mSendInviteInfoRelati.setOnClickListener(mOnClickListener);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == mSendInviteInfoRelati) {
				String inviteAccount = mFriendAccountEditxt.getText()
						.toString();
				String inviteNickName = inviteAccount;
				int index = inviteAccount.lastIndexOf("@");
				if (index != -1) {
					inviteNickName = inviteAccount.subSequence(0, index)
							.toString();
				}
				String inviteInfo = mInviteInfoEditxt.getText().toString();
				mPresenter.doInviteFriend(inviteAccount, inviteNickName,
						inviteInfo);
			} else if (v == mBackRelati) {
				doBack();
			}
		}
	};

	private void doBack() {
		mPresenter.doSwitchActivity(this);
	}

	@Override
	public void test() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showErrorHint(String msg) {
		showToast(msg);
	}

	@Override
	public void showContactAddedHint(String msg) {
		showToast(msg);
	}

	private void showToast(String msg) {
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(this, msg, duration);
		toast.show();
	}

	@Override
	public void setFriendAccount(String account) {
		mFriendAccountEditxt.setText(account);
	}
}
