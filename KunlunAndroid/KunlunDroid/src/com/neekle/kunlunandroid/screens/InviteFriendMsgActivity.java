package com.neekle.kunlunandroid.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.presenter.impl.InviteAddFriendActiyPresenter;
import com.neekle.kunlunandroid.presenter.impl.InviteFriendMsgActiyPresenter;
import com.neekle.kunlunandroid.presenter.interf.IInviteFriendMsgActivity;
import com.neekle.kunlunandroid.xmpp.XmppOperation;
import com.neekle.kunlunandroid.xmpp.data.XmppSingleChatMessage;

public class InviteFriendMsgActivity extends Activity implements
		IInviteFriendMsgActivity {

	public static final int INVITE_STATE_PARTENER_ON_INVITING = 0;
	public static final int INVITE_STATE_PARTENER_ON_REFUSE = 1;
	public static final int INVITE_STATE_PARTENER_ON_APPROVE = 2;
	public static final int INVITE_STATE_SELF_REV_ECHO = 3;
	public static final int INVITE_STATE_SELF_REV_APPROVED = 4;
	public static final int INVITE_STATE_SELF_REV_REFUSED = 5;

	private RelativeLayout mBackRelati;
	private TextView mModuleNameTxtv;
	private ImageView mPartenerPhotoImgv;
	private TextView mPartenerNameTv;
	private TextView mInviteMsgTv;
	private TextView mInviteHandleResultTv;
	private RelativeLayout mHandleInviteRelati;
	private RelativeLayout mApproveRelati;
	private RelativeLayout mRefuseRelati;
	private TextView mInviteSysEchoTv;
	private TextView mInviteTimeTv;
	private RelativeLayout mSendInviteRelati;
	private RelativeLayout mInvitePartenerEchoRelati;
	private TextView mInviteStatePartenerEchoTv;

	private InviteFriendMsgActiyPresenter mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invite_friend_msg);

		mPresenter = new InviteFriendMsgActiyPresenter(this, this);
		findViewsAndSetAttributes();

		int category = INVITE_STATE_PARTENER_ON_INVITING;
		doShow(category);

		Intent intent = getIntent();
		mPresenter.init(intent);
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
	public void onBackPressed() {
		super.onBackPressed();
		mPresenter.doSwitchActivity(this, MessageSessionActivity.class);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void findViewsAndSetAttributes() {
		mBackRelati = (RelativeLayout) findViewById(R.id.relati_back);
		mBackRelati.setOnClickListener(mOnClickListener);

		mModuleNameTxtv = (TextView) findViewById(R.id.tv_module_name);
		int resId = R.string.friend_invite_msg;
		mModuleNameTxtv.setText(resId);

		mPartenerPhotoImgv = (ImageView) findViewById(R.id.imgv_partner_photo);

		mPartenerNameTv = (TextView) findViewById(R.id.tv_partner_name);

		mInviteMsgTv = (TextView) findViewById(R.id.tv_invite_note_msg);

		mInviteHandleResultTv = (TextView) findViewById(R.id.tv_invite_handle_result);

		mHandleInviteRelati = (RelativeLayout) findViewById(R.id.relati_handle_invite);

		mApproveRelati = (RelativeLayout) findViewById(R.id.relati_approve);
		mApproveRelati.setOnClickListener(mOnClickListener);

		mRefuseRelati = (RelativeLayout) findViewById(R.id.relati_refuse);
		mRefuseRelati.setOnClickListener(mOnClickListener);

		mInviteSysEchoTv = (TextView) findViewById(R.id.tv_invite_echo_from_sys);

		mInviteTimeTv = (TextView) findViewById(R.id.tv_invite_time);

		mSendInviteRelati = (RelativeLayout) findViewById(R.id.relati_send_invite);
		mSendInviteRelati.setOnClickListener(mOnClickListener);

		mInvitePartenerEchoRelati = (RelativeLayout) findViewById(R.id.relati_invite_echo_from_partener);

		mInviteStatePartenerEchoTv = (TextView) findViewById(R.id.tv_invite_state_echo_from_partener);
	}

	private void doShow(int category) {
		if (category == INVITE_STATE_PARTENER_ON_INVITING) {
			mInviteHandleResultTv.setVisibility(View.GONE);
			mHandleInviteRelati.setVisibility(View.VISIBLE);
			mInviteSysEchoTv.setVisibility(View.GONE);
			mSendInviteRelati.setVisibility(View.GONE);
			mInvitePartenerEchoRelati.setVisibility(View.GONE);
		} else if (category == INVITE_STATE_PARTENER_ON_REFUSE) {
			mInviteHandleResultTv.setVisibility(View.VISIBLE);
			String text = getString(R.string.has_denied);
			mInviteHandleResultTv.setText(text);
			mHandleInviteRelati.setVisibility(View.GONE);
			mInviteSysEchoTv.setVisibility(View.GONE);
			mSendInviteRelati.setVisibility(View.GONE);
			mInvitePartenerEchoRelati.setVisibility(View.GONE);
		} else if (category == INVITE_STATE_PARTENER_ON_APPROVE) {
			mInviteHandleResultTv.setVisibility(View.VISIBLE);
			String text = getString(R.string.has_added);
			mInviteHandleResultTv.setText(text);
			mHandleInviteRelati.setVisibility(View.GONE);
			mInviteSysEchoTv.setVisibility(View.GONE);
			mSendInviteRelati.setVisibility(View.VISIBLE);
			mInvitePartenerEchoRelati.setVisibility(View.GONE);
		} else if (category == INVITE_STATE_SELF_REV_ECHO) {
			mInviteHandleResultTv.setVisibility(View.GONE);
			mHandleInviteRelati.setVisibility(View.GONE);
			mInviteSysEchoTv.setVisibility(View.VISIBLE);
			mSendInviteRelati.setVisibility(View.GONE);
			mInvitePartenerEchoRelati.setVisibility(View.GONE);
		} else if (category == INVITE_STATE_SELF_REV_REFUSED) {
			mInviteHandleResultTv.setVisibility(View.GONE);
			mHandleInviteRelati.setVisibility(View.GONE);
			mInviteSysEchoTv.setVisibility(View.VISIBLE);
			mSendInviteRelati.setVisibility(View.GONE);
			mInvitePartenerEchoRelati.setVisibility(View.VISIBLE);
		} else if (category == INVITE_STATE_SELF_REV_APPROVED) {
			mInviteHandleResultTv.setVisibility(View.GONE);
			mHandleInviteRelati.setVisibility(View.GONE);
			mInviteSysEchoTv.setVisibility(View.VISIBLE);
			mSendInviteRelati.setVisibility(View.GONE);
			mInvitePartenerEchoRelati.setVisibility(View.VISIBLE);
		}
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == mApproveRelati) {
				mPresenter.approveSubscription();
				doShow(INVITE_STATE_PARTENER_ON_APPROVE);
			} else if (v == mRefuseRelati) {
				mPresenter.denySubscription();
				doShow(INVITE_STATE_PARTENER_ON_REFUSE);
			} else if (v == mSendInviteRelati) {
				mPresenter
						.doSwitchMsgChatActivity(InviteFriendMsgActivity.this,
								MessageChatActivity.class);
			}
		}
	};

	@Override
	public void test() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getMessage(XmppSingleChatMessage ownerHandleMsg,
			XmppSingleChatMessage partnerHandleNotifyMsg, int style) {
		doShow(style);

		String showName = ownerHandleMsg.getFromName();
		String sendOrRevTag = ownerHandleMsg.getSendOrRcvTag();
		if (sendOrRevTag.equals(Constants.MsgSendOrRcvTag.REV)) {
			showName = ownerHandleMsg.getFromName();
		} else {
			showName = ownerHandleMsg.getToName();
		}

		String msgBody = ownerHandleMsg.getMsgBody();
		String time = ownerHandleMsg.getTimeStamp();

		mPartenerNameTv.setText(showName);
		mInviteMsgTv.setText(msgBody);
		mInviteTimeTv.setText(time);

		setSysEcho(showName, style);

		if (partnerHandleNotifyMsg != null) {
			String partnerHandleResult = partnerHandleNotifyMsg.getMsgBody();
			mInviteStatePartenerEchoTv.setText(partnerHandleResult);
		}
	}

	private void setSysEcho(String showName, int style) {
		boolean isShouldSetSysEcho = false;
		switch (style) {
		case INVITE_STATE_SELF_REV_ECHO: {
			isShouldSetSysEcho = true;
			break;
		}
		case INVITE_STATE_SELF_REV_APPROVED: {
			isShouldSetSysEcho = true;
			break;
		}
		case INVITE_STATE_SELF_REV_REFUSED: {
			isShouldSetSysEcho = true;
			break;
		}
		}

		if (isShouldSetSysEcho) {
			String sysEcho = getString(R.string.you_have_send_ainvite_to);
			sysEcho += showName;
			mInviteSysEchoTv.setText(sysEcho);
		}
	}
}
