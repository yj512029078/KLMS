package com.neekle.kunlunandroid.screens;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.presenter.impl.TelVoiceActivityPresenter;
import com.neekle.kunlunandroid.presenter.impl.TelVoiceActivityPresenter.OnDataUpdateListener;
import com.neekle.kunlunandroid.presenter.interf.ITelVoiceActivity;
import com.neekle.kunlunandroid.sip.utils.DialerUtils;
import com.neekle.kunlunandroid.util.NetworkState;

public class TelVoiceActivity extends Activity implements ITelVoiceActivity {

	public static enum ViewType {
		ViewDef, ViewInCalling, ViewInComing, ViewInCall, ViewInCallShowKeyboard
	}

	private RelativeLayout mMainRelati;
	private LayoutInflater mLayoutInflater;

	private TelVoiceActivityPresenter mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tel_voice);

		mMainRelati = (RelativeLayout) findViewById(R.id.relati_tel_voice);
		mLayoutInflater = LayoutInflater.from(this);

		mPresenter = new TelVoiceActivityPresenter(this, this);
		Intent intent = getIntent();
		mPresenter.init(intent);
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
		mPresenter.doClearResource();
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		mPresenter.doClearResource();
		super.onDestroy();
	}

	public void showProperView(ViewType viewType) {
		switch (viewType) {
		case ViewInCalling: {
			loadInCallingView();
			break;
		}
		case ViewInComing: {
			loadInComingView();
			break;
		}
		case ViewInCall: {
			loadInCallView();
			break;
		}
		case ViewInCallShowKeyboard: {
			loadInCallShowKeyboardView();
			break;
		}
		default: {
			break;
		}

		}
	}

	private void loadInCallingView() {
		View view = mLayoutInflater
				.inflate(R.layout.tel_voice_in_calling, null);

		findInCallingView(view);

		mMainRelati.removeAllViews();
		mMainRelati.addView(view);
	}

	private void loadInComingView() {
		View view = mLayoutInflater.inflate(R.layout.tel_voice_in_coming, null);

		findInComingView(view);

		mMainRelati.removeAllViews();
		mMainRelati.addView(view);
	}

	private void loadInCallView() {
		View view = mLayoutInflater.inflate(R.layout.tel_voice_in_call, null);

		findInCallView(view);

		mMainRelati.removeAllViews();
		mMainRelati.addView(view);
	}

	private void loadInCallShowKeyboardView() {
		View view = mLayoutInflater.inflate(
				R.layout.tel_voice_in_call_show_keyboard, null);

		findInCallShowKeyboardView(view);

		mMainRelati.removeAllViews();
		mMainRelati.addView(view);
	}

	private void findInCallingView(View view) {

		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.relati_hang_up: {
					mPresenter.hangUpCall();
					break;
				}
				case R.id.relati_silent_voice: {
					mPresenter.toggleMicrophoneMute();
					break;
				}
				case R.id.relati_hands_free: {
					mPresenter.toggleSpeakerphone();
					break;
				}
				default:
					break;
				}
			}
		};

		/* 界面顶部 */
		RelativeLayout backRelati = (RelativeLayout) view
				.findViewById(R.id.relati_back);
		TextView backTxtv = (TextView) view.findViewById(R.id.tv_back);
		String string = getString(R.string.cancle);
		backTxtv.setText(string);
		backRelati.setOnClickListener(onClickListener);
		TextView moduleNameTxtv = (TextView) view
				.findViewById(R.id.tv_module_name);
		int resId = R.string.in_calling;
		moduleNameTxtv.setText(resId);

		/* 正在呼叫显示 */
		// 这里先直接判定，其实应该判定呼叫时刻网络情况
		final ImageView phoneIndicatorImgv = (ImageView) view
				.findViewById(R.id.imgv_phone_indicator);
		phoneIndicatorImgv.setVisibility(View.VISIBLE);
		boolean isWifi = mPresenter.getIsWifiUsed();
		if (isWifi) {
			resId = R.drawable.cm_tel_going_hint_indicator_wifi;
		} else {
			resId = R.drawable.cm_tel_going_hint_indicator_3g;
		}
		phoneIndicatorImgv.setBackgroundResource(resId);

		TextView networkTxtv = (TextView) view.findViewById(R.id.txtv_network);
		networkTxtv.setVisibility(View.GONE);
		final ImageView partnerPhotoImgv = (ImageView) view
				.findViewById(R.id.imgv_partner_photo);
		// 需要设置具体头像
		TextView sessionStateTxtv = (TextView) view
				.findViewById(R.id.txtv_session_state);
		sessionStateTxtv.setVisibility(View.GONE);
		TextView durationTxtv = (TextView) view
				.findViewById(R.id.txtv_duration);
		durationTxtv.setVisibility(View.GONE);
		final TextView partnerNameTxtv = (TextView) view
				.findViewById(R.id.txtv_partner_name);
		// 需要设置具体名字
		final TextView partnerLocationTxtv = (TextView) view
				.findViewById(R.id.txtv_partner_location);
		partnerLocationTxtv.setVisibility(View.GONE);

		/* 费用显示区 */
		// ... do judge， 需要根据网络情况进行UI显示
		final TextView feeChargeTxtv = (TextView) view
				.findViewById(R.id.txtv_fee_charge_hint);
		feeChargeTxtv.setVisibility(View.VISIBLE);
		if (isWifi) {
			resId = R.string.no_fee_charge;
		} else {
			resId = R.string.fee_charge_for_net;
		}
		feeChargeTxtv.setText(resId);

		/* 操作区 */
		RadioButton clickToRecordRb = (RadioButton) view
				.findViewById(R.id.rb_click_to_record);
		// ... should set linstener，需要设置监听
		RelativeLayout hangupRelati = (RelativeLayout) view
				.findViewById(R.id.relati_hang_up);
		hangupRelati.setOnClickListener(onClickListener);
		RelativeLayout addToDialRelati = (RelativeLayout) view
				.findViewById(R.id.relati_add_to_dial);
		addToDialRelati.setOnClickListener(onClickListener);
		RelativeLayout dialKeyboardRelati = (RelativeLayout) view
				.findViewById(R.id.relati_dial_keyboard);
		dialKeyboardRelati.setOnClickListener(onClickListener);
		RelativeLayout silentVoiceRelati = (RelativeLayout) view
				.findViewById(R.id.relati_silent_voice);
		silentVoiceRelati.setOnClickListener(onClickListener);
		RelativeLayout handsFreeRelati = (RelativeLayout) view
				.findViewById(R.id.relati_hands_free);
		handsFreeRelati.setOnClickListener(onClickListener);

		OnDataUpdateListener onDataUpdateListener = new OnDataUpdateListener() {

			@Override
			public void onNetworkUpdate(boolean isWifi) {

				int imgResId;
				int txtResId;
				if (isWifi) {
					imgResId = R.drawable.cm_tel_going_hint_indicator_wifi;
					txtResId = R.string.no_fee_charge;
				} else {
					imgResId = R.drawable.cm_tel_going_hint_indicator_3g;
					txtResId = R.string.fee_charge_for_net;
				}

				phoneIndicatorImgv.setBackgroundResource(imgResId);
				feeChargeTxtv.setText(txtResId);
			}

			@Override
			public void onPhotoUpdate(Bitmap bitmap) {
				BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
				partnerPhotoImgv.setBackgroundDrawable(bitmapDrawable);
			}

			@Override
			public void onIncallTimeUpdate(String time) {

			}

			@Override
			public void onNameUpdate(String name) {
				partnerNameTxtv.setText(name);
			}

			@Override
			public void onLocationUpdate(String location) {

			}

		};
		mPresenter.setOnDataUpdateListener(onDataUpdateListener);
	}

	private void findInComingView(View view) {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.relati_tel_handle_coming_call_rev: {
					mPresenter.acceptCall();
					break;
				}
				case R.id.relati_tel_handle_coming_call_refuse: {
					mPresenter.hangUpCall();
					break;
				}
				default:
					break;
				}
			}
		};

		/* 界面顶部 */
		RelativeLayout backRelati = (RelativeLayout) view
				.findViewById(R.id.relati_back);
		backRelati.setOnClickListener(onClickListener);
		TextView moduleNameTxtv = (TextView) view
				.findViewById(R.id.tv_module_name);
		int resId = R.string.coming_call;
		moduleNameTxtv.setText(resId);

		/* 正在呼叫显示 */
		ImageView phoneIndicatorImgv = (ImageView) view
				.findViewById(R.id.imgv_phone_indicator);
		phoneIndicatorImgv.setVisibility(View.VISIBLE);
		resId = R.drawable.cm_tel_going_hint_indicator_voice_phone_small;
		phoneIndicatorImgv.setBackgroundResource(resId);

		TextView networkTxtv = (TextView) view.findViewById(R.id.txtv_network);
		networkTxtv.setVisibility(View.GONE);
		final ImageView partnerPhotoImgv = (ImageView) view
				.findViewById(R.id.imgv_partner_photo);
		// 需要设置具体头像
		TextView sessionStateTxtv = (TextView) view
				.findViewById(R.id.txtv_session_state);
		sessionStateTxtv.setVisibility(View.GONE);
		TextView durationTxtv = (TextView) view
				.findViewById(R.id.txtv_duration);
		durationTxtv.setVisibility(View.GONE);
		final TextView partnerNameTxtv = (TextView) view
				.findViewById(R.id.txtv_partner_name);
		// 需要设置具体名字
		final TextView partnerLocationTxtv = (TextView) view
				.findViewById(R.id.txtv_partner_location);
		// 需要设置具体位置

		/* 操作区 */
		RelativeLayout handleComingCallRevRelati = (RelativeLayout) view
				.findViewById(R.id.relati_tel_handle_coming_call_rev);
		handleComingCallRevRelati.setOnClickListener(onClickListener);
		RelativeLayout handleComingCallRefuseRelati = (RelativeLayout) view
				.findViewById(R.id.relati_tel_handle_coming_call_refuse);
		handleComingCallRefuseRelati.setOnClickListener(onClickListener);

		OnDataUpdateListener onDataUpdateListener = new OnDataUpdateListener() {

			@Override
			public void onNetworkUpdate(boolean isWifi) {

			}

			@Override
			public void onPhotoUpdate(Bitmap bitmap) {
				BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
				partnerPhotoImgv.setBackgroundDrawable(bitmapDrawable);
			}

			@Override
			public void onIncallTimeUpdate(String time) {

			}

			@Override
			public void onNameUpdate(String name) {
				partnerNameTxtv.setText(name);
			}

			@Override
			public void onLocationUpdate(String location) {
				partnerLocationTxtv.setText(location);
			}

		};
		mPresenter.setOnDataUpdateListener(onDataUpdateListener);
	}

	private void findInCallView(View view) {

		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.relati_hang_up: {
					mPresenter.hangUpCall();
					break;
				}
				case R.id.relati_silent_voice: {
					mPresenter.toggleMicrophoneMute();
					break;
				}
				case R.id.relati_hands_free: {
					mPresenter.toggleSpeakerphone();
					break;
				}
				default:
					break;
				}
			}
		};

		/* 界面顶部 */
		RelativeLayout backRelati = (RelativeLayout) view
				.findViewById(R.id.relati_back);
		backRelati.setOnClickListener(onClickListener);
		final TextView moduleNameTxtv = (TextView) view
				.findViewById(R.id.tv_module_name);
		int resId = R.string.phone_with_sb;
		String format = getString(resId);
		format = String.format(format, Constants.EMPTY_STRING);
		// 具体后面要修改。。。
		moduleNameTxtv.setText(format);

		/* 正在通话显示 */
		// ... do judge, 需要根据网络情况具体设置UI显示
		RelativeLayout phoneIndicatorRelati = (RelativeLayout) view
				.findViewById(R.id.relati_phone_indicator);
		phoneIndicatorRelati.setVisibility(View.GONE);

		final TextView networkTxtv = (TextView) view
				.findViewById(R.id.txtv_network);
		boolean isWifi = mPresenter.getIsWifiUsed();
		int txtResId;
		if (isWifi) {
			txtResId = R.string.wifi;
		} else {
			txtResId = R.string.lte_3g;
		}
		networkTxtv.setText(txtResId);
		final ImageView partnerPhotoImgv = (ImageView) view
				.findViewById(R.id.imgv_partner_photo);
		// 需要设置具体头像
		TextView sessionStateTxtv = (TextView) view
				.findViewById(R.id.txtv_session_state);
		resId = R.string.in_the_call;
		sessionStateTxtv.setText(resId);

		final TextView durationTxtv = (TextView) view
				.findViewById(R.id.txtv_duration);
		// 需要设置时长，要在外面更新的。。。
		final TextView partnerNameTxtv = (TextView) view
				.findViewById(R.id.txtv_partner_name);
		// 需要设置具体名字
		final TextView partnerLocationTxtv = (TextView) view
				.findViewById(R.id.txtv_partner_location);
		// 需要设置具体位置

		/* 操作区 */
		RadioButton clickToRecordRb = (RadioButton) view
				.findViewById(R.id.rb_click_to_record);
		// ... should set linstener，需要设置监听
		RelativeLayout hangupRelati = (RelativeLayout) view
				.findViewById(R.id.relati_hang_up);
		hangupRelati.setOnClickListener(onClickListener);
		RelativeLayout addToDialRelati = (RelativeLayout) view
				.findViewById(R.id.relati_add_to_dial);
		addToDialRelati.setOnClickListener(onClickListener);
		RelativeLayout dialKeyboardRelati = (RelativeLayout) view
				.findViewById(R.id.relati_dial_keyboard);
		dialKeyboardRelati.setOnClickListener(onClickListener);
		RelativeLayout silentVoiceRelati = (RelativeLayout) view
				.findViewById(R.id.relati_silent_voice);
		silentVoiceRelati.setOnClickListener(onClickListener);
		RelativeLayout handsFreeRelati = (RelativeLayout) view
				.findViewById(R.id.relati_hands_free);
		handsFreeRelati.setOnClickListener(onClickListener);

		OnDataUpdateListener onDataUpdateListener = new OnDataUpdateListener() {

			@Override
			public void onNetworkUpdate(boolean isWifi) {

				int txtResId;
				if (isWifi) {
					txtResId = R.string.wifi;
				} else {
					txtResId = R.string.lte_3g;
				}
				networkTxtv.setText(txtResId);
			}

			@Override
			public void onPhotoUpdate(Bitmap bitmap) {
				BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
				partnerPhotoImgv.setBackgroundDrawable(bitmapDrawable);
			}

			@Override
			public void onIncallTimeUpdate(String time) {
				durationTxtv.setText(time);
			}

			@Override
			public void onNameUpdate(String name) {
				int resId = R.string.phone_with_sb;
				String format = getString(resId);
				format = String.format(format, name);
				moduleNameTxtv.setText(format);

				partnerNameTxtv.setText(name);
			}

			@Override
			public void onLocationUpdate(String location) {
				partnerLocationTxtv.setText(location);
			}

		};
		mPresenter.setOnDataUpdateListener(onDataUpdateListener);
	}

	private void findInCallShowKeyboardView(View view) {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		};

		/* 界面顶部 */
		RelativeLayout backRelati = (RelativeLayout) view
				.findViewById(R.id.relati_back);
		backRelati.setOnClickListener(onClickListener);
		TextView moduleNameTxtv = (TextView) view
				.findViewById(R.id.tv_module_name);
		int resId = R.string.phone_with_sb;
		String format = getString(resId);
		format = String.format(format, Constants.EMPTY_STRING);
		// 具体后面要修改。。。
		moduleNameTxtv.setText(format);

		/* 键盘显示区域 */
		setDialShow(view);

		/* 操作区 */
		RadioButton clickToRecordRb = (RadioButton) view
				.findViewById(R.id.rb_click_to_record);
		clickToRecordRb.setVisibility(View.GONE);
		RelativeLayout hangupRelati = (RelativeLayout) view
				.findViewById(R.id.relati_hang_up);
		hangupRelati.setOnClickListener(onClickListener);
		RelativeLayout addToDialRelati = (RelativeLayout) view
				.findViewById(R.id.relati_add_to_dial);
		addToDialRelati.setOnClickListener(onClickListener);
		RelativeLayout dialKeyboardRelati = (RelativeLayout) view
				.findViewById(R.id.relati_dial_keyboard);
		dialKeyboardRelati.setOnClickListener(onClickListener);
		RelativeLayout silentVoiceRelati = (RelativeLayout) view
				.findViewById(R.id.relati_silent_voice);
		silentVoiceRelati.setOnClickListener(onClickListener);
		RelativeLayout handsFreeRelati = (RelativeLayout) view
				.findViewById(R.id.relati_hands_free);
		handsFreeRelati.setOnClickListener(onClickListener);
	}

	private void setDialShow(View view) {
		View.OnClickListener mOnDialerClick = new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}

		};

		DialerUtils.setDialerTextButton(view, R.id.screen_tab_dialer_button_0,
				"0", "+", DialerUtils.TAG_0, mOnDialerClick);
		DialerUtils.setDialerTextButton(view, R.id.screen_tab_dialer_button_1,
				"1", "", DialerUtils.TAG_1, mOnDialerClick);
		DialerUtils.setDialerTextButton(view, R.id.screen_tab_dialer_button_2,
				"2", "ABC", DialerUtils.TAG_2, mOnDialerClick);
		DialerUtils.setDialerTextButton(view, R.id.screen_tab_dialer_button_3,
				"3", "DEF", DialerUtils.TAG_3, mOnDialerClick);
		DialerUtils.setDialerTextButton(view, R.id.screen_tab_dialer_button_4,
				"4", "GHI", DialerUtils.TAG_4, mOnDialerClick);
		DialerUtils.setDialerTextButton(view, R.id.screen_tab_dialer_button_5,
				"5", "JKL", DialerUtils.TAG_5, mOnDialerClick);
		DialerUtils.setDialerTextButton(view, R.id.screen_tab_dialer_button_6,
				"6", "MNO", DialerUtils.TAG_6, mOnDialerClick);
		DialerUtils.setDialerTextButton(view, R.id.screen_tab_dialer_button_7,
				"7", "PQRS", DialerUtils.TAG_7, mOnDialerClick);
		DialerUtils.setDialerTextButton(view, R.id.screen_tab_dialer_button_8,
				"8", "TUV", DialerUtils.TAG_8, mOnDialerClick);
		DialerUtils.setDialerTextButton(view, R.id.screen_tab_dialer_button_9,
				"9", "WXYZ", DialerUtils.TAG_9, mOnDialerClick);
		DialerUtils.setDialerTextButton(view,
				R.id.screen_tab_dialer_button_star, "*", "",
				DialerUtils.TAG_STAR, mOnDialerClick);
		DialerUtils.setDialerTextButton(view,
				R.id.screen_tab_dialer_button_sharp, "#", "",
				DialerUtils.TAG_SHARP, mOnDialerClick);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

		}
	};

	@Override
	public void test() {

	}

	@Override
	public void loadView(ViewType viewType) {
		showProperView(viewType);
	}

	@Override
	public void showErrorHint(String msg) {
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
		toast.show();
	}

}
