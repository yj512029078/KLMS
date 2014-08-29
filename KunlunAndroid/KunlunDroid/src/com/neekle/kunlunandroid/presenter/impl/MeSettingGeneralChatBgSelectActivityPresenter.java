package com.neekle.kunlunandroid.presenter.impl;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.adapter.MeSettingBlacklistAdapter;
import com.neekle.kunlunandroid.adapter.MeSettingGeneralChatBgSelectAdapter;
import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.data.MeSettingBlacklistData;
import com.neekle.kunlunandroid.data.MeSettingGeneralChatBgSelectData;
import com.neekle.kunlunandroid.data.UserGlobalPartInfo;
import com.neekle.kunlunandroid.presenter.interf.IMeSettingGeneralChatBgActivity;
import com.neekle.kunlunandroid.presenter.interf.IMeSettingGeneralChatBgSelectActivity;
import com.neekle.kunlunandroid.util.FileOperation;

public class MeSettingGeneralChatBgSelectActivityPresenter {

	private static final String INTENT_SELECTED_DRAWABLE_BG_ID = "selected_drawable_bg_id";

	private static final String SDCARD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	private static final String PRE_PLACE_BGS_FOLDER_RELATIVE_PATH_TO_SDCARD = "KunlunDroid/img/def_bgs";
	private static final String PRE_PLACE_BGS_FOLDER_ABSOLUTE_PATH = SDCARD_PATH
			+ "/" + PRE_PLACE_BGS_FOLDER_RELATIVE_PATH_TO_SDCARD;

	private Context mContext;
	private IMeSettingGeneralChatBgSelectActivity mIView;

	private int mSelectedDrawableId = Constants.DEF_INT_VALUE;
	private ArrayList<MeSettingGeneralChatBgSelectData> mArrayList = new ArrayList<MeSettingGeneralChatBgSelectData>();
	private MeSettingGeneralChatBgSelectAdapter mAdapter;

	private int[] mPrePlaceBgs = {
			R.raw.me_setting_general_chat_bg_select_pre_place_1_1_thumb,
			R.raw.me_setting_general_chat_bg_select_pre_place_2_1_thumb,
			R.raw.me_setting_general_chat_bg_select_pre_place_3_1_thumb,
			R.raw.me_setting_general_chat_bg_select_pre_place_4_1_thumb,
			R.raw.me_setting_general_chat_bg_select_pre_place_5_1_thumb,
			R.raw.me_setting_general_chat_bg_select_pre_place_6_1_thumb,
			R.raw.me_setting_general_chat_bg_select_pre_place_7_1_thumb,
			R.raw.me_setting_general_chat_bg_select_pre_place_8_1_thumb };

	public MeSettingGeneralChatBgSelectActivityPresenter(
			IMeSettingGeneralChatBgSelectActivity view, Context context) {
		mIView = view;
		mContext = context;
	}

	public void init(Intent intent) {
		mSelectedDrawableId = intent.getIntExtra(
				INTENT_SELECTED_DRAWABLE_BG_ID, Constants.DEF_INT_VALUE);
		initData(mSelectedDrawableId);

		updateAdapterAndView();
	}

	private void updateAdapterAndView() {
		if (mAdapter == null) {
			mAdapter = new MeSettingGeneralChatBgSelectAdapter(mContext,
					mArrayList);
			mIView.updateAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

	private void initData(int selectedDrawableId) {
		Resources res = mContext.getResources();

		int length = mPrePlaceBgs.length;
		for (int i = 0; i < length; i++) {
			int id = mPrePlaceBgs[i];

			Bitmap bitmap = BitmapFactory.decodeResource(res, id);
			boolean isSelected = false;
			if (id == selectedDrawableId) {
				isSelected = true;
			}

			MeSettingGeneralChatBgSelectData data = new MeSettingGeneralChatBgSelectData();
			data.setBitmap(bitmap);
			data.setSelected(isSelected);

			mArrayList.add(data);
		}
	}

	public void doOnItemSelected(int position) {
		doUpdateDataAndView(position);
		mSelectedDrawableId = getProperSelectedDrawableId(position);
	}

	private void doUpdateDataAndView(int selectedPosition) {
		int size = mArrayList.size();
		for (int i = 0; i < size; i++) {
			MeSettingGeneralChatBgSelectData data = mArrayList.get(i);

			boolean isSelected = false;
			if (i == selectedPosition) {
				isSelected = true;
			}

			data.setSelected(isSelected);
		}

		updateAdapterAndView();
	}

	private int getProperSelectedDrawableId(int selectedPosition) {
		int drawableId = Constants.DEF_INT_VALUE;

		int length = mPrePlaceBgs.length;
		if ((selectedPosition >= length) || (selectedPosition < 0)) {
			return drawableId;
		}

		drawableId = mPrePlaceBgs[selectedPosition];
		return drawableId;
	}

	public void doSwitchMeSettingActivity(Context context,
			Class<? extends Activity> targetActivity) {
		switchActivity(context, targetActivity, null);
	}

	public void doSwitchMeSettingPrivacyActivity(Context context,
			Class<? extends Activity> targetActivity) {
		switchActivity(context, targetActivity, null);
	}

	public void doSwitchMeSettingGeneralChatBgActivity(Context context,
			Class<? extends Activity> targetActivity) {
		Intent intent = new Intent();
		intent.setClass(context, targetActivity);
		intent.putExtra(INTENT_SELECTED_DRAWABLE_BG_ID, mSelectedDrawableId);

		Activity activity = (Activity) context;
		activity.setResult(Activity.RESULT_OK, intent);
	}

	private void switchActivityWithNoFinish(Context context,
			Class<? extends Activity> targetActivity, Bundle bundle) {
		Activity activity = (Activity) context;
		Intent intent = new Intent();
		intent.setClass(activity, targetActivity);

		if (bundle != null) {
			intent.putExtras(bundle);
		}

		ActivityUtil.switchTo(activity, intent);
	}

	private void switchActivity(Context context,
			Class<? extends Activity> targetActivity, Bundle bundle) {
		switchActivityWithNoFinish(context, targetActivity, bundle);

		Activity activity = (Activity) context;
		activity.finish();
	}

}
