package com.neekle.kunlunandroid.screens;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.adapter.CellPhoneContactAdapter;
import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.presenter.impl.TelephoneDialActivityPresenter;
import com.neekle.kunlunandroid.presenter.interf.ITelephoneDialActivity;
import com.neekle.kunlunandroid.sip.common.SipConstants;
import com.neekle.kunlunandroid.sip.utils.DialerUtils;

public class TelephoneDialActivity extends Activity implements
		ITelephoneDialActivity {

	/* 列表部分 */
	private ListView mActivePhoneContactLv;

	/* 拨号盘部分 */
	private EditText mDialInfoEditText;
	private ImageView mDelImgv;
	private LinearLayout mZeroOrPlusLinear;
	private RelativeLayout mDialCallRelati;
	private RelativeLayout mDialControlKeyboardRelati;
	private LinearLayout mDialNumberLinear;
	private RelativeLayout mKeyboardRelative;

	private TelephoneDialActivityPresenter mPresenter;

	public static HashMap<String, String> mHashMap = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.telephone_dial);

		findViewAndSetAttributes();

		mPresenter = new TelephoneDialActivityPresenter(this, this);
		mPresenter.init();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
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
		switchMainActivity();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void findViewAndSetAttributes() {
		mActivePhoneContactLv = (ListView) findViewById(R.id.lv_active_phone_contact);

		constructKeyboardMap();
		setDialShow();

		mZeroOrPlusLinear = (LinearLayout) findViewById(R.id.screen_tab_dialer_button_0);
		findViewById(R.id.screen_tab_dialer_button_0).setOnLongClickListener(
				mOnLongClickListener);

		mDialInfoEditText = (EditText) findViewById(R.id.edittxt_input);
		mDialInfoEditText.setInputType(InputType.TYPE_NULL);
		mDialInfoEditText.setFocusable(false);
		mDialInfoEditText.setFocusableInTouchMode(false);
		mDialInfoEditText.addTextChangedListener(mTextWatcher);
		// mDialInfoEditText.setText(SipConstants.SIP_USER_NAME_PARTNER);

		mDelImgv = (ImageView) findViewById(R.id.imgv_delete);
		mDelImgv.setOnClickListener(mOnClickListener);
		mDialCallRelati = (RelativeLayout) findViewById(R.id.relati_dial_call);
		mDialCallRelati.setOnClickListener(mOnClickListener);
		mDialControlKeyboardRelati = (RelativeLayout) findViewById(R.id.relati_dial_control_keyboard);
		mDialControlKeyboardRelati.setOnClickListener(mOnClickListener);
		mDialNumberLinear = (LinearLayout) findViewById(R.id.linear_dial_number);
		mDialNumberLinear.setOnClickListener(mOnClickListener);
		mKeyboardRelative = (RelativeLayout) findViewById(R.id.relati_keyboard);
	}

	private void constructKeyboardMap() {
		mHashMap.put("0", "+");
		mHashMap.put("1", "");
		mHashMap.put("2", "ABC");
		mHashMap.put("3", "DEF");
		mHashMap.put("4", "GHI");
		mHashMap.put("5", "JKL");
		mHashMap.put("6", "MNO");
		mHashMap.put("7", "PQRS");
		mHashMap.put("8", "TUV");
		mHashMap.put("9", "WXYZ");
		mHashMap.put("*", "");
		mHashMap.put("#", "");
	}

	private void setDialShow() {
		String number = "0";
		String correspondString = mHashMap.get(number);
		DialerUtils.setDialerTextButton(this, R.id.screen_tab_dialer_button_0,
				number, correspondString, DialerUtils.TAG_0, mOnDialerClick);

		number = "1";
		correspondString = mHashMap.get(number);
		DialerUtils.setDialerTextButton(this, R.id.screen_tab_dialer_button_1,
				number, correspondString, DialerUtils.TAG_1, mOnDialerClick);

		number = "2";
		correspondString = mHashMap.get(number);
		DialerUtils.setDialerTextButton(this, R.id.screen_tab_dialer_button_2,
				number, correspondString, DialerUtils.TAG_2, mOnDialerClick);

		number = "3";
		correspondString = mHashMap.get(number);
		DialerUtils.setDialerTextButton(this, R.id.screen_tab_dialer_button_3,
				number, correspondString, DialerUtils.TAG_3, mOnDialerClick);

		number = "4";
		correspondString = mHashMap.get(number);
		DialerUtils.setDialerTextButton(this, R.id.screen_tab_dialer_button_4,
				number, correspondString, DialerUtils.TAG_4, mOnDialerClick);

		number = "5";
		correspondString = mHashMap.get(number);
		DialerUtils.setDialerTextButton(this, R.id.screen_tab_dialer_button_5,
				number, correspondString, DialerUtils.TAG_5, mOnDialerClick);

		number = "6";
		correspondString = mHashMap.get(number);
		DialerUtils.setDialerTextButton(this, R.id.screen_tab_dialer_button_6,
				number, correspondString, DialerUtils.TAG_6, mOnDialerClick);

		number = "7";
		correspondString = mHashMap.get(number);
		DialerUtils.setDialerTextButton(this, R.id.screen_tab_dialer_button_7,
				number, correspondString, DialerUtils.TAG_7, mOnDialerClick);

		number = "8";
		correspondString = mHashMap.get(number);
		DialerUtils.setDialerTextButton(this, R.id.screen_tab_dialer_button_8,
				number, correspondString, DialerUtils.TAG_8, mOnDialerClick);

		number = "9";
		correspondString = mHashMap.get(number);
		DialerUtils.setDialerTextButton(this, R.id.screen_tab_dialer_button_9,
				number, correspondString, DialerUtils.TAG_9, mOnDialerClick);

		number = "*";
		correspondString = mHashMap.get(number);
		DialerUtils.setDialerTextButton(this,
				R.id.screen_tab_dialer_button_star, number, correspondString,
				DialerUtils.TAG_STAR, mOnDialerClick);

		number = "#";
		correspondString = mHashMap.get(number);
		DialerUtils.setDialerTextButton(this,
				R.id.screen_tab_dialer_button_sharp, number, correspondString,
				DialerUtils.TAG_SHARP, mOnDialerClick);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == mDelImgv) {
				doOnDelImgvClick();
			} else if (v == mDialCallRelati) {
				doOnDialCallRelatiClick();
			} else if (v == mDialControlKeyboardRelati) {
				doOnDialControlKeyboardRelatiClick();
			} else if (v == mDialNumberLinear) {
				doOnDialNumberLinearClick();
			}
		}

	};

	private void doOnDelImgvClick() {
		final String number = mDialInfoEditText.getText().toString();
		final int selStart = mDialInfoEditText.getSelectionStart();
		if (selStart > 0) {
			final StringBuffer sb = new StringBuffer(number);
			sb.delete(selStart - 1, selStart);
			mDialInfoEditText.setText(sb.toString());
			mDialInfoEditText.setSelection(selStart - 1);
		}
	}

	private void doOnDialCallRelatiClick() {
		switchTelVoiceActivity();
		// switchTelVideoActivity();
	}

	private void doOnDialControlKeyboardRelatiClick() {
		int visible = mKeyboardRelative.getVisibility();
		if (visible == View.VISIBLE) {
			mKeyboardRelative.setVisibility(View.GONE);
		} else {
			mKeyboardRelative.setVisibility(View.VISIBLE);
		}
	}

	private void doOnDialNumberLinearClick() {

	}

	private OnLongClickListener mOnLongClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			if (v == mZeroOrPlusLinear) {
				appendText("+");
			}

			return true;
		}
	};

	private TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			int length = mDialInfoEditText.getText().toString().length();
			final boolean bShowCaret = (length > 0);
			mDialInfoEditText.setFocusableInTouchMode(bShowCaret);
			mDialInfoEditText.setFocusable(bShowCaret);

			if (length > 0) {
				mDialInfoEditText.setSelection(length - 1);
			}

			if (mPresenter != null) {
				String string = s.toString();
				mPresenter.doFilter(string);
			}
		}
	};

	private final View.OnClickListener mOnDialerClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			int tag = Integer.parseInt(v.getTag().toString());
			final String number = mDialInfoEditText.getText().toString();
			final String textToAppend = tag == DialerUtils.TAG_STAR ? "*"
					: (tag == DialerUtils.TAG_SHARP ? "#" : Integer
							.toString(tag));
			appendText(textToAppend);
		}

	};

	private void appendText(String textToAppend) {
		final int selStart = mDialInfoEditText.getSelectionStart();
		final StringBuffer sb = new StringBuffer(mDialInfoEditText.getText()
				.toString());
		sb.insert(selStart, textToAppend);
		mDialInfoEditText.setText(sb.toString());
		mDialInfoEditText.setSelection(selStart + 1);
	}

	private void switchMainActivity() {
		Class<? extends Activity> targetActivity = MainScreenActivity.class;
		mPresenter.doSwitchMainActivity(this, targetActivity);
	}

	private void switchTelVoiceActivity() {
		Class<? extends Activity> targetActivity = TelVoiceActivity.class;
		// mPresenter.doSwitchTelVoiceActivity(this, targetActivity,
		// SipConstants.SIP_USER_NAME_PARTNER);
	}

	private void switchTelVideoActivity() {
		Class<? extends Activity> targetActivity = TelVideoActivity.class;
		// mPresenter.doSwitchTelVideoActivity(this, targetActivity,
		// SipConstants.SIP_USER_NAME_PARTNER);
	}

	@Override
	public void test() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAdapter(CellPhoneContactAdapter adapter) {
		mActivePhoneContactLv.setAdapter(adapter);
	}
}
