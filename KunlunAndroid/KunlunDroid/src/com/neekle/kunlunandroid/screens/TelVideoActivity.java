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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.presenter.impl.TelVideoActivityPresenter;
import com.neekle.kunlunandroid.presenter.impl.TelVideoActivityPresenter.OnDataUpdateListener;
import com.neekle.kunlunandroid.presenter.impl.TelVoiceActivityPresenter;
import com.neekle.kunlunandroid.presenter.interf.ITelVideoActivity;

public class TelVideoActivity extends Activity implements ITelVideoActivity {

	public static enum ViewType {
		ViewDef, ViewInCalling, ViewInComing, ViewInCall
	}

	private RelativeLayout mMainRelati;
	private LayoutInflater mLayoutInflater;

	private TelVideoActivityPresenter mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tel_video);

		mMainRelati = (RelativeLayout) findViewById(R.id.relati_tel_voice);
		mLayoutInflater = LayoutInflater.from(this);

		mPresenter = new TelVideoActivityPresenter(this, this);
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
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
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

		default: {
			break;
		}

		}
	}

	private void loadInCallingView() {
		View view = mLayoutInflater
				.inflate(R.layout.tel_video_in_calling, null);

		findInCallingView(view);

		mMainRelati.removeAllViews();
		mMainRelati.addView(view);
	}

	private void loadInComingView() {
		View view = mLayoutInflater.inflate(R.layout.tel_video_in_coming, null);

		findInComingView(view);

		mMainRelati.removeAllViews();
		mMainRelati.addView(view);
	}

	private void loadInCallView() {
		View view = mLayoutInflater.inflate(R.layout.tel_video_in_call, null);

		findInCallView(view);

		mMainRelati.removeAllViews();
		mMainRelati.addView(view);
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
				default:
					break;
				}
			}
		};

		/* 标题部分 */
		RelativeLayout backRelati = (RelativeLayout) view
				.findViewById(R.id.relati_back);
		backRelati.setVisibility(View.INVISIBLE);
		final TextView moduleNameTxtv = (TextView) view
				.findViewById(R.id.tv_module_name);

		final FrameLayout localVideoPreview = (FrameLayout) view
				.findViewById(R.id.frame_call_local_video);
		final FrameLayout remoteVideoPreview = (FrameLayout) view
				.findViewById(R.id.frame_call_remote_video);
		final View viewSecure = view
				.findViewById(R.id.imgv_call_remote_video_secure);
		mPresenter.setPreviewView(localVideoPreview, remoteVideoPreview);

		/* 操作区 */
		RadioButton clickToRecordRb = (RadioButton) view
				.findViewById(R.id.rb_click_to_record);
		clickToRecordRb.setVisibility(View.GONE);
		RelativeLayout hangupRelati = (RelativeLayout) view
				.findViewById(R.id.relati_hang_up);
		hangupRelati.setOnClickListener(onClickListener);
		RelativeLayout doUnusualOperationRelati = (RelativeLayout) view
				.findViewById(R.id.relati_tel_do_unusual_operation);
		doUnusualOperationRelati.setVisibility(View.GONE);

		OnDataUpdateListener onDataUpdateListener = new OnDataUpdateListener() {

			@Override
			public void onPhotoUpdate(Bitmap bitmap) {

			}

			@Override
			public void onNameUpdate(String name) {
				int resId = R.string.video_with_sb;
				String format = getString(resId);
				format = String.format(format, name);
				moduleNameTxtv.setText(format);
			}

			@Override
			public void onIncallTimeUpdate(String time) {

			}
		};
		mPresenter.setOnDataUpdateListener(onDataUpdateListener);
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
		int resId = R.string.in_calling;
		moduleNameTxtv.setText(resId);

		/* 正在呼叫显示 */
		// ... do judge, 需要根据网络情况具体设置UI显示
		ImageView phoneIndicatorImgv = (ImageView) view
				.findViewById(R.id.imgv_phone_indicator);
		phoneIndicatorImgv.setVisibility(View.VISIBLE);
		resId = R.drawable.cm_tel_going_hint_indicator_video_phone_big;
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
		TextView partnerLocationTxtv = (TextView) view
				.findViewById(R.id.txtv_partner_location);
		partnerLocationTxtv.setVisibility(View.GONE);

		/* 操作区 */
		RadioButton clickToRecordRb = (RadioButton) view
				.findViewById(R.id.rb_click_to_record);
		clickToRecordRb.setVisibility(View.GONE);
		RelativeLayout hangupRelati = (RelativeLayout) view
				.findViewById(R.id.relati_hang_up);
		hangupRelati.setOnClickListener(onClickListener);
		RelativeLayout doUnusualOperationRelati = (RelativeLayout) view
				.findViewById(R.id.relati_tel_do_unusual_operation);
		doUnusualOperationRelati.setVisibility(View.GONE);

		OnDataUpdateListener onDataUpdateListener = new OnDataUpdateListener() {

			@Override
			public void onPhotoUpdate(Bitmap bitmap) {
				BitmapDrawable drawable = new BitmapDrawable(bitmap);
				partnerPhotoImgv.setBackgroundDrawable(drawable);
			}

			@Override
			public void onNameUpdate(String name) {
				partnerNameTxtv.setText(name);
			}

			@Override
			public void onIncallTimeUpdate(String time) {

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
		int resId = R.string.invite_video_call;
		moduleNameTxtv.setText(resId);

		/* 正在呼叫显示 */
		ImageView phoneIndicatorImgv = (ImageView) view
				.findViewById(R.id.imgv_phone_indicator);
		phoneIndicatorImgv.setVisibility(View.VISIBLE);
		resId = R.drawable.cm_tel_going_hint_indicator_video_phone_small;
		phoneIndicatorImgv.setBackgroundResource(resId);

		TextView networkTxtv = (TextView) view.findViewById(R.id.txtv_network);
		networkTxtv.setVisibility(View.GONE);
		final ImageView partnerPhotoImgv = (ImageView) view
				.findViewById(R.id.imgv_partner_photo);
		TextView sessionStateTxtv = (TextView) view
				.findViewById(R.id.txtv_session_state);
		sessionStateTxtv.setVisibility(View.GONE);
		TextView durationTxtv = (TextView) view
				.findViewById(R.id.txtv_duration);
		durationTxtv.setVisibility(View.GONE);
		final TextView partnerNameTxtv = (TextView) view
				.findViewById(R.id.txtv_partner_name);
		TextView partnerLocationTxtv = (TextView) view
				.findViewById(R.id.txtv_partner_location);
		partnerLocationTxtv.setVisibility(View.GONE);

		/* 操作区 */
		RelativeLayout handleComingCallRevRelati = (RelativeLayout) view
				.findViewById(R.id.relati_tel_handle_coming_call_rev);
		handleComingCallRevRelati.setOnClickListener(onClickListener);
		RelativeLayout handleComingCallRefuseRelati = (RelativeLayout) view
				.findViewById(R.id.relati_tel_handle_coming_call_refuse);
		handleComingCallRefuseRelati.setOnClickListener(onClickListener);

		OnDataUpdateListener onDataUpdateListener = new OnDataUpdateListener() {

			@Override
			public void onPhotoUpdate(Bitmap bitmap) {
				BitmapDrawable drawable = new BitmapDrawable(bitmap);
				partnerPhotoImgv.setBackgroundDrawable(drawable);
			}

			@Override
			public void onNameUpdate(String name) {
				partnerNameTxtv.setText(name);
			}

			@Override
			public void onIncallTimeUpdate(String time) {

			}
		};
		mPresenter.setOnDataUpdateListener(onDataUpdateListener);
	}

	@Override
	public void test() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showErrorHint(String msg) {
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
		toast.show();
	}

	@Override
	public void loadView(ViewType viewType) {
		showProperView(viewType);
	}
}
