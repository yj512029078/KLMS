package com.neekle.kunlunandroid.screens;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.adapter.ContactDetailImageAdapter;
import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.presenter.impl.ContactDetailActiyPresenter;
import com.neekle.kunlunandroid.presenter.interf.IContactDetailActivity;
import com.neekle.kunlunandroid.util.DeviceInfo;
import com.neekle.kunlunandroid.view.specials.ContactDetailBottomPopupMenu;
import com.neekle.kunlunandroid.view.specials.ContactDetailBottomPopupMenu.OnMenuItemClickListener;

public class ContactDetailActivity extends Activity implements
		IContactDetailActivity {

	private static final int CRITICAL_MIN_MARGIN_RIGHT = 20;
	private static final int POPUP_WINDOW_ANCHOR_VIEW_Y_OFFSET = 6;

	private RelativeLayout mBackRelati;
	private TextView mModuleNameTxtv;
	private TextView mEditInfoTxtv;
	private TextView mNameTxtv;
	private ImageView mStarImgv;
	private TextView mAccountIdTxtv;
	private ImageView mPersonalPhotoImgv;
	private RelativeLayout mLocalAddsinfoRelati;
	private TextView mLocalAddsbookInfoContentTxtv;
	private RelativeLayout mLocalAddsbookInfoMoreRelati;
	private TextView mPersonalSignatureContentTxtv;
	private Gallery mPersonalPhotoGallery;
	private TextView mSexContentTxtv;
	private RelativeLayout mMoreInfoRelati;
	private RelativeLayout mEditInfoRelati;
	private RelativeLayout mSendMsgRelati;
	private RelativeLayout mMakePhoneRelati;
	private RelativeLayout mMakeVideoRelati;

	private ContactDetailBottomPopupMenu mBottomPopupMenu;
	private View mCurrentView;

	/* 本地通讯录区域的弹出菜单 */
	private PopupWindow mPopupWindow;
	private RelativeLayout mPopupMenuMakeCallRelati;
	private RelativeLayout mPopupMenuSavePhotoToAddsRelati;

	private int mPopupWindowMeasuredWidth;

	private ContactDetailActiyPresenter mPresenter;
	private Class<? extends Activity> mIntentClass;
	private boolean mIsStartNewActivity = true;
	private boolean mIsFinishSef = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater layoutInflater = getLayoutInflater();
		mCurrentView = layoutInflater.inflate(R.layout.contact_detail, null);
		setContentView(mCurrentView);

		findViewsAndSetAttributes();
		mPresenter = new ContactDetailActiyPresenter(this, this);

		Intent intent = getIntent();
		mPresenter.init(intent);

		initAddsinfoPopupMenu();
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
		mPresenter.doClearNecessaryCallback();
		switchIntentActivity();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void findViewsAndSetAttributes() {
		mBackRelati = (RelativeLayout) findViewById(R.id.relati_back);
		mBackRelati.setOnClickListener(mOnClickListener);

		mModuleNameTxtv = (TextView) findViewById(R.id.tv_module_name);
		int nameResId = R.string.test_name;
		mModuleNameTxtv.setText(nameResId);

		mEditInfoRelati = (RelativeLayout) findViewById(R.id.relati_operation_right);
		int resid = R.drawable.cm_top_bar_operation_right_bg;
		mEditInfoRelati.setBackgroundResource(resid);
		mEditInfoRelati.setOnClickListener(mOnClickListener);

		mEditInfoTxtv = (TextView) findViewById(R.id.tv_operation_right);
		int resId = R.string.edit;
		mEditInfoTxtv.setText(resId);

		mNameTxtv = (TextView) findViewById(R.id.tv_name);
		mStarImgv = (ImageView) findViewById(R.id.imgv_star);
		mAccountIdTxtv = (TextView) findViewById(R.id.tv_account_id);
		mPersonalPhotoImgv = (ImageView) findViewById(R.id.imgv_personal_photo);

		mLocalAddsinfoRelati = (RelativeLayout) findViewById(R.id.relati_local_addsbook_info);
		mLocalAddsbookInfoContentTxtv = (TextView) findViewById(R.id.tv_local_addsbook_info_content);
		mLocalAddsbookInfoMoreRelati = (RelativeLayout) findViewById(R.id.relati_local_addsbook_info_more);
		mLocalAddsbookInfoMoreRelati.setOnClickListener(mOnClickListener);

		mPersonalSignatureContentTxtv = (TextView) findViewById(R.id.tv_personal_signature_content);
		mPersonalPhotoGallery = (Gallery) findViewById(R.id.gallery_personal_photo_ablum);
		mSexContentTxtv = (TextView) findViewById(R.id.tv_sex_content);

		mMoreInfoRelati = (RelativeLayout) findViewById(R.id.relati_more_info);
		mMoreInfoRelati.setOnClickListener(mOnClickListener);

		mSendMsgRelati = (RelativeLayout) findViewById(R.id.relati_send_msg);
		mSendMsgRelati.setOnClickListener(mOnClickListener);
		mMakePhoneRelati = (RelativeLayout) findViewById(R.id.relati_make_phone);
		mMakePhoneRelati.setOnClickListener(mOnClickListener);
		mMakeVideoRelati = (RelativeLayout) findViewById(R.id.relati_make_video);
		mMakeVideoRelati.setOnClickListener(mOnClickListener);
	}

	private void initAddsinfoPopupMenu() {
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = layoutInflater.inflate(
				R.layout.contact_detail_addsinfo_popmenu, null);

		/*
		 * 如果使用Inflater的情况下会出现以上错误，原因是用Inflater渲染组件的时候并没有给其指定父控件，所以渲染器不会去解析width
		 * 和 height属性，就会导致空指针异常。 具体原因，现在不是特别确定，如果没有这行代码，部分手机会崩溃
		 */
		contentView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		/* measure 以后，调用 getMeasuredX 会获得实际测量过的值。 */
		contentView.measure(
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

		mPopupWindowMeasuredWidth = contentView.getMeasuredWidth();
		mPopupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		findAddsinfoPopupMenuView();

		mPopupWindow.setFocusable(true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		BitmapDrawable bitmapDrawable = new BitmapDrawable();
		mPopupWindow.setBackgroundDrawable(bitmapDrawable);
	}

	private void findAddsinfoPopupMenuView() {
		View view = mPopupWindow.getContentView();
		mPopupMenuMakeCallRelati = (RelativeLayout) view
				.findViewById(R.id.relati_make_call);
		mPopupMenuMakeCallRelati.setOnClickListener(mOnClickListener);

		mPopupMenuSavePhotoToAddsRelati = (RelativeLayout) view
				.findViewById(R.id.relati_save_photo_to_addsbook);
		mPopupMenuSavePhotoToAddsRelati.setOnClickListener(mOnClickListener);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == mEditInfoRelati) {
				showBottomPopupMenu();
			} else if (v == mLocalAddsbookInfoMoreRelati) {
				showAddsinfoPopupMenu(v);
			} else if (v == mPopupMenuMakeCallRelati) {
				doOnPopupMenuMakeCallClick();
			} else if (v == mPopupMenuSavePhotoToAddsRelati) {
				doOnPopupMenuSavePhotoToAddsClick();
			} else if (v == mMoreInfoRelati) {
				switchContactDetailMoreActivity();
			} else if (v == mBackRelati) {
				switchAddsbookActivity();
			} else if (v == mSendMsgRelati) {
				switchMessageChatActivity();
			} else if (v == mMakePhoneRelati) {
				mPresenter.doToCallFriend();
			} else if (v == mMakeVideoRelati) {
				mPresenter.doToVideoFriend();
			}
		}
	};

	private ContactDetailBottomPopupMenu.OnMenuItemClickListener mOnMenuItemClickListener = new OnMenuItemClickListener() {

		@Override
		public void onMenuItemClick(int tag) {
			mPresenter.doOnMenuItemClick(tag);
		}
	};

	private void doOnPopupMenuMakeCallClick() {
		mPopupWindow.dismiss();
		mPresenter.doPhoneCall();
	}

	private void doOnPopupMenuSavePhotoToAddsClick() {
		mPopupWindow.dismiss();
		mPresenter.setContactPhoto();
	}

	private void showBottomPopupMenu() {
		mBottomPopupMenu = new ContactDetailBottomPopupMenu(
				ContactDetailActivity.this);
		mBottomPopupMenu.setOnMenuItemClickListener(mOnMenuItemClickListener);
		mBottomPopupMenu.setWindowLayoutMode(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		mBottomPopupMenu.showAtLocation(mCurrentView, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	private void showAddsinfoPopupMenu(View v) {
		// 设置为false，能够使其定位位置完全根据我们的设置，而忽略内部的一系列自动调整的设置
		mPopupWindow.setClippingEnabled(false);
		int xOffset = getProperXOffset();
		mPopupWindow.showAsDropDown(v, xOffset,
				POPUP_WINDOW_ANCHOR_VIEW_Y_OFFSET);
	}

	private int getProperXOffset() {
		int[] location = new int[2];
		mLocalAddsbookInfoMoreRelati.getLocationInWindow(location);
		int popMenuInitStartX = location[0];
		// 以china mobile 为例， 720 * 1280
		int screenWidth = DeviceInfo.getScreenWidth(this);

		int xOffset = computeXOffset(popMenuInitStartX,
				mPopupWindowMeasuredWidth, screenWidth);

		return xOffset;
	}

	private int computeXOffset(int popMenuInitStartX, int popMenuWidth,
			int screenWidth) {
		int desireXOffset = 0;

		int margin = popMenuInitStartX + popMenuWidth
				+ CRITICAL_MIN_MARGIN_RIGHT - screenWidth;
		if (margin > 0) {
			desireXOffset = -margin;
		}

		return desireXOffset;
	}

	private void switchIntentActivity() {
		mPresenter.doSwitchIntentActivity(ContactDetailActivity.this,
				mIntentClass, mIsStartNewActivity, mIsFinishSef);
	}

	private void switchAddsbookActivity() {
		Class<? extends Activity> targetActivity = AddsbookActivity.class;
		mPresenter.doSwitchAddsbookActivity(ContactDetailActivity.this,
				targetActivity);
	}

	private void switchContactDetailMoreActivity() {
		Class<? extends Activity> targetActivity = ContactDetailMoreActivity.class;
		mPresenter.doSwitchContactDetailMoreActivity(
				ContactDetailActivity.this, targetActivity);
	}

	private void switchMessageChatActivity() {
		Class<? extends Activity> targetActivity = MessageChatActivity.class;
		mPresenter.doSwitchMessageChatActivity(ContactDetailActivity.this,
				targetActivity);
	}

	@Override
	public void test() {

	}

	@Override
	public void showBasicInfo(Drawable photoDrawable, String name, String jid,
			boolean isStarSign) {
		mPersonalPhotoImgv.setBackgroundDrawable(photoDrawable);
		mModuleNameTxtv.setText(name);
		mNameTxtv.setText(name);
		mAccountIdTxtv.setText(jid);

		if (isStarSign) {
			mStarImgv.setVisibility(View.VISIBLE);
		} else {
			mStarImgv.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void showLocalAddsInfo(String info) {
		if ((info != null) && (!info.equals(Constants.EMPTY_STRING))) {
			mLocalAddsinfoRelati.setVisibility(View.VISIBLE);
			mLocalAddsbookInfoContentTxtv.setText(info);
		} else {
			mLocalAddsinfoRelati.setVisibility(View.GONE);
		}
	}

	@Override
	public void showHint(String hint, int duration) {
		Toast taost = Toast.makeText(this, hint, duration);
		taost.show();
	}

	@Override
	public void showStarSign(boolean isShow) {
		if (isShow) {
			mStarImgv.setVisibility(View.VISIBLE);
		} else {
			mStarImgv.setVisibility(View.GONE);
		}
	}

	@Override
	public void setIntentClass(Class<? extends Activity> intentClass,
			boolean isStartNewActivity, boolean isFinishSelf) {
		mIntentClass = intentClass;
		mIsStartNewActivity = isStartNewActivity;
		mIsFinishSef = isFinishSelf;
	}

	@Override
	public void showExtraInfo(String label, String sex) {
		mPersonalSignatureContentTxtv.setText(label);
		mSexContentTxtv.setText(sex);
	}

	@Override
	public void updatePhotoAlbumAdapter(ContactDetailImageAdapter adapter) {
		int count = adapter.getCount();
		int index = count / 2;
		mPersonalPhotoGallery.setAdapter(adapter);
		mPersonalPhotoGallery.setSelection(index);
	}
}
