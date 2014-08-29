package com.neekle.kunlunandroid.screens;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.adapter.CellOneContactAdapter;
import com.neekle.kunlunandroid.data.ContactInfo;
import com.neekle.kunlunandroid.presenter.impl.AddsbookActiyPresenter;
import com.neekle.kunlunandroid.presenter.interf.IAddsbookActivity;
import com.neekle.kunlunandroid.view.specials.AlphabetListView;
import com.neekle.kunlunandroid.view.specials.AlphabetListView.AlphabetPositionListener;
import com.neekle.kunlunandroid.voice.IVoiceRecoginzerCallback;
import com.neekle.kunlunandroid.voice.IflyVoice;

public class AddsbookActivity extends Activity implements IAddsbookActivity,
		IVoiceRecoginzerCallback {

	private static final int MENU_ITEM_ADD_FRIEND = Menu.FIRST;
	private static final int MENU_ITEM_CREATE_HUDDLE = Menu.FIRST + 1;
	private static final int MENU_ITEM_CREATE_TEAM = Menu.FIRST + 2;
	private static final int MENU_ITEM_SHOW_CONTROL = Menu.FIRST + 3;

	private RelativeLayout mBackRelati;
	private TextView mModuleNameTxtv;
	private TextView mFriendSetTxtv;
	private EditText mSearchInputEditxt;
	private RelativeLayout mFriendSetRelati;
	private AlphabetListView mAddsbookLv;
	private ImageView mSpeechImgv;

	private boolean mIsDialogViewShowAll;

	// Later modify it, use it for webservice
	private String mSessionid = "test001@server08.com";

	private AddsbookActiyPresenter mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addsbook);

		mPresenter = new AddsbookActiyPresenter(this, this);
		// mPresenter.doReceiveIntent();

		findViewsAndSetAttributes();
		mPresenter.setIsShowAllFriends(mIsDialogViewShowAll);
		mPresenter.init(mSessionid);
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
		constructMenu(menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		int showAllResId = R.string.show_all;
		int showFriendResId = R.string.show_friend;
		String showAll = getString(showAllResId);
		String showFriend = getString(showFriendResId);

		MenuItem item = menu.findItem(MENU_ITEM_SHOW_CONTROL);
		if (mIsDialogViewShowAll) {
			item.setTitle(showFriend);
		} else {
			item.setTitle(showAll);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		Class<? extends Activity> targetActivity = MainScreenActivity.class;
		mPresenter.doSwitchMainActivity(AddsbookActivity.this, targetActivity);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void findViewsAndSetAttributes() {
		mBackRelati = (RelativeLayout) findViewById(R.id.relati_back);
		mBackRelati.setOnClickListener(mOnClickListener);

		mSpeechImgv = (ImageView) findViewById(R.id.imgv_speech);
		mSpeechImgv.setOnClickListener(mOnClickListener);

		mModuleNameTxtv = (TextView) findViewById(R.id.tv_module_name);
		int addsbookResId = R.string.addsbook;
		mModuleNameTxtv.setText(addsbookResId);

		mSearchInputEditxt = (EditText) findViewById(R.id.edittxt_search_input);
		mSearchInputEditxt.addTextChangedListener(mTextWatcher);

		mFriendSetRelati = (RelativeLayout) findViewById(R.id.relati_operation_right);
		int resid = R.drawable.cm_top_bar_operation_right_bg;
		mFriendSetRelati.setBackgroundResource(resid);
		mFriendSetRelati.setOnClickListener(mOnClickListener);

		mFriendSetTxtv = (TextView) findViewById(R.id.tv_operation_right);
		int ellipsisResId = R.string.addsbook_ellipsis;
		mFriendSetTxtv.setText(ellipsisResId);

		mAddsbookLv = (AlphabetListView) findViewById(R.id.lv_addsbook);
	}

	private void constructMenu(Menu menu) {
		int addFriendResId = R.string.add_friend;
		String addFriend = getString(addFriendResId);
		menu.add(0, MENU_ITEM_ADD_FRIEND, 0, addFriend);

		int createHuddleResId = R.string.create_huddle;
		String createHuddle = getString(createHuddleResId);
		menu.add(0, MENU_ITEM_CREATE_HUDDLE, 0, createHuddle);

		int createTeamResId = R.string.create_team;
		String createTeam = getString(createTeamResId);
		menu.add(0, MENU_ITEM_CREATE_TEAM, 0, createTeam);

		int showAllResId = R.string.show_all;
		String showAll = getString(showAllResId);
		menu.add(0, MENU_ITEM_SHOW_CONTROL, 0, showAll);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();

		switch (itemId) {
		case MENU_ITEM_ADD_FRIEND: {
			Class<? extends Activity> targetActivity = InviteAddFriendActivity.class;
			mPresenter.doSwitchActivity(AddsbookActivity.this, targetActivity);

			break;
		}
		case MENU_ITEM_CREATE_HUDDLE: {
			Class<? extends Activity> targetActivity = SelectContactActivity.class;
			mPresenter.doSwitchHuddleActivity(AddsbookActivity.this,
					targetActivity);

			break;
		}
		case MENU_ITEM_CREATE_TEAM: {
			break;
		}
		case MENU_ITEM_SHOW_CONTROL: {
			updateFilterShow();

			break;
		}
		default: {
			break;
		}

		}

		return super.onOptionsItemSelected(item);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == mFriendSetRelati) {
				int titleResId = R.string.please_choose;
				String title = AddsbookActivity.this.getString(titleResId);
				Drawable icon = null;

				int itemsId;
				if (mIsDialogViewShowAll) {
					itemsId = R.array.friend_operation_items_nofilter;
				} else {
					itemsId = R.array.friend_operation_items_filter;
				}

				showDialog(AddsbookActivity.this, title, icon, itemsId);
			} else if (v == mBackRelati) {
				Class<? extends Activity> targetActivity = MainScreenActivity.class;
				mPresenter.doSwitchMainActivity(AddsbookActivity.this,
						targetActivity);
			} else if (v == mSpeechImgv) {
				IflyVoice iflyVoice = IflyVoice.getInstance();
				iflyVoice.setCallback(AddsbookActivity.this);
				iflyVoice.initRecognizerDialog(AddsbookActivity.this);
				iflyVoice.showRecognizerDialog();
			}

		}
	};

	private void showDialog(Context context, String title, Drawable icon,
			int itemsId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(null);
		builder.setTitle(title);
		builder.setItems(itemsId, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					Class<? extends Activity> targetActivity = InviteAddFriendActivity.class;
					mPresenter.doSwitchActivity(AddsbookActivity.this,
							targetActivity);
				} else if (which == 1) {
					Class<? extends Activity> targetActivity = SelectContactActivity.class;
					mPresenter.doSwitchHuddleActivity(AddsbookActivity.this,
							targetActivity);
				} else if (which == 2) {

				} else if (which == 3) {
					updateFilterShow();
				}

			}
		});

		builder.create();
		builder.show();
	}

	private void updateFilterShow() {
		mIsDialogViewShowAll = !mIsDialogViewShowAll;
		mPresenter.doAddsbookFilter(mIsDialogViewShowAll);
	}

	private TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			mPresenter.doSearchFilter(s);
		}
	};

	@Override
	public void updateAddsbookAdapter(CellOneContactAdapter adapter) {
		mAddsbookLv.setAdapter(adapter);
	}

	@Override
	public void setPositionLinstener(AlphabetPositionListener listener) {
		// TODO Auto-generated method stub
		mAddsbookLv.setAlphabetPositionListener(listener);
	}

	@Override
	public void test() {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyMenuToChange(boolean isShowAll) {

	}

	@Override
	public void onVoiceResultNotify(String text) {
		mSearchInputEditxt.setText(text);
	}

}
