package com.neekle.kunlunandroid.screens;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.adapter.ContactDetailMoreAdapter;
import com.neekle.kunlunandroid.presenter.impl.ContactDetailActiyPresenter;
import com.neekle.kunlunandroid.presenter.impl.ContactDetailMoreActiyPresenter;
import com.neekle.kunlunandroid.presenter.interf.IContactDetailMoreActivity;
import com.neekle.kunlunandroid.web.data.friend.FriendInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class ContactDetailMoreActivity extends Activity implements
		IContactDetailMoreActivity {

	private TextView mModuleNameTxtv;
	private ListView mDetailMoreLv;

	private Button mPopWindowCancleBtn;
	private Button mPopWindowConfirmBtn;
	private TextView mPopWindowTitleTxtv;
	private EditText mPopWindowContentEditxt;

	private View mMainView;
	private PopupWindow mPopupWindow;

	private ContactDetailMoreActiyPresenter mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMainView = layoutInflater.inflate(R.layout.contact_detail_more, null);
		setContentView(mMainView);

		findViewsAndSetAttributes();

		Intent intent = getIntent();
		mPresenter = new ContactDetailMoreActiyPresenter(this, this);
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
		doBack();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void findViewsAndSetAttributes() {
		mModuleNameTxtv = (TextView) findViewById(R.id.tv_module_name);
		int nameResId = R.string.test_name;
		mModuleNameTxtv.setText(nameResId);

		mDetailMoreLv = (ListView) findViewById(R.id.lv_contact_detail_more);
		mDetailMoreLv.setOnItemClickListener(mOnItemClickListener);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == mPopWindowCancleBtn) {
				doOnPopWindowCancleBtnClick();
			} else if (v == mPopWindowConfirmBtn) {
				doOnPopWindowConfirmBtnClick();
			}
		}
	};

	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			mPresenter.doShowDetail(arg2);
		}
	};

	private void doOnPopWindowCancleBtnClick() {
		mPopupWindow.dismiss();
	}

	private void doOnPopWindowConfirmBtnClick() {
		mPopupWindow.dismiss();

		String titleText = mPopWindowTitleTxtv.getText().toString();
		String contentText = mPopWindowContentEditxt.getText().toString();
		mPresenter.doOnPopWindowConfirmBtnClick(titleText, contentText);
	}

	private void showPopupWindow() {
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = layoutInflater.inflate(R.layout.cm_confirm_dialog,
				null);

		mPopupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		findPopupView(contentView);

		// 以下代码，经过测试发现对PopupWindow自带的window外点击消失无效，原因暂时没有去查，关系不大
		mPopupWindow.setFocusable(true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		BitmapDrawable bitmapDrawable = new BitmapDrawable();
		mPopupWindow.setBackgroundDrawable(bitmapDrawable);

		mPopupWindow.showAtLocation(mMainView, Gravity.CENTER, 0, 0);
	}

	private void findPopupView(View contentView) {
		mPopWindowCancleBtn = (Button) contentView
				.findViewById(R.id.btn_cancle);
		mPopWindowConfirmBtn = (Button) contentView
				.findViewById(R.id.btn_confirm);

		mPopWindowCancleBtn.setOnClickListener(mOnClickListener);
		mPopWindowConfirmBtn.setOnClickListener(mOnClickListener);

		mPopWindowTitleTxtv = (TextView) contentView
				.findViewById(R.id.confirm_dialog_title_tv);
		mPopWindowContentEditxt = (EditText) contentView
				.findViewById(R.id.confirm_dialog_text_et);
	}

	private void doBack() {
		mPresenter.doWriteToDb();
		switchContactDetailActivity();
	}

	private void switchContactDetailActivity() {
		Class<? extends Activity> targetActivity = ContactDetailActivity.class;
		mPresenter.doSwitchContactDetailActivity(this, targetActivity);
	}

	@Override
	public void test() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateContactDetailMoreAdapter(ContactDetailMoreAdapter adapter) {
		mDetailMoreLv.setAdapter(adapter);
	}

	@Override
	public void showPopupWindow(String title, String content) {
		showPopupWindow();

		mPopWindowTitleTxtv.setText(title);
		mPopWindowContentEditxt.setText(content);
	}

	@Override
	public void showName(String showName) {
		mModuleNameTxtv.setText(showName);
	}

	@Override
	public void showHint(String hint, int duration) {
		Toast taost = Toast.makeText(this, hint, duration);
		taost.show();
	}


}
