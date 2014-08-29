package com.neekle.kunlunandroid.presenter.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.adapter.MeSettingBlacklistAdapter;
import com.neekle.kunlunandroid.adapter.MeSettingWeiboPrivacyNotSeeAdapter;
import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.data.MeSettingBlacklistData;
import com.neekle.kunlunandroid.data.MeSettingWeiboPrivacyNotSeeData;
import com.neekle.kunlunandroid.presenter.interf.IMeSettingWeiboPrivacyActivity;
import com.neekle.kunlunandroid.presenter.interf.IMeSettingWeiboPrivacyNotSeeActivity;
import com.neekle.kunlunandroid.screens.SelectContactActivity;
import com.neekle.kunlunandroid.util.BitmapOperator;
import com.neekle.kunlunandroid.util.DisplayUtil;
import com.neekle.kunlunandroid.web.common.WebserviceConstants;
import com.neekle.kunlunandroid.xmpp.XmppData;
import com.neekle.kunlunandroid.xmpp.XmppOperation;
import com.neekle.kunlunandroid.xmpp.data.XmppFriend;

public class MeSettingWeiboPrivacyNotSeeActivityPresenter {

	private static final int IMAGE_WIDTH_DIP = 40;
	private static final int IMAGE_HEIGHT_DIP = 40;

	private static final String INTENT_FROM_SETTING = "from_setting";
	private static final String INTENT_SELECTED_USER_LIST_KEY = "UserList";

	private Context mContext;
	private Bitmap mDefBitmap;
	private IMeSettingWeiboPrivacyNotSeeActivity mIView;

	private ArrayList<MeSettingWeiboPrivacyNotSeeData> mArrayList = new ArrayList<MeSettingWeiboPrivacyNotSeeData>();
	private MeSettingWeiboPrivacyNotSeeAdapter mAdapter;

	public MeSettingWeiboPrivacyNotSeeActivityPresenter(
			IMeSettingWeiboPrivacyNotSeeActivity view, Context context) {
		mIView = view;
		mContext = context;
	}

	public void init() {
		Resources res = mContext.getResources();
		int id = R.drawable.cm_mini_avatar;
		mDefBitmap = BitmapFactory.decodeResource(res, id);

		constructData();
		updateAdapterAndView();
	}

	private void constructData() {
		initBlacklistData();
		addAddAndDeleteView();
	}

	private void updateAdapterAndView() {
		if (mAdapter == null) {
			mAdapter = new MeSettingWeiboPrivacyNotSeeAdapter(mContext,
					mArrayList);
			mAdapter.setOnAdapterItemClickListener(mOnAdapterItemClickListener);

			mIView.updateAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

	private MeSettingWeiboPrivacyNotSeeAdapter.OnAdapterItemClickListener mOnAdapterItemClickListener = new MeSettingWeiboPrivacyNotSeeAdapter.OnAdapterItemClickListener() {

		@Override
		public void onClick(MeSettingWeiboPrivacyNotSeeData data) {
			int itemType = data.getItemType();

			switch (itemType) {
			case MeSettingWeiboPrivacyNotSeeAdapter.ItemType.ADD: {
				doOnAddItemClick();
				break;
			}
			case MeSettingWeiboPrivacyNotSeeAdapter.ItemType.DELETE: {
				doOnDeleteItemClick();
				break;
			}
			case MeSettingWeiboPrivacyNotSeeAdapter.ItemType.NORMAL: {
				doOnNormalItemClick(data);
				break;
			}
			default:
				break;
			}
		}
	};

	private void doOnAddItemClick() {
		ArrayList<String> blackList = getCurrentBlacklist();

		int requestCode = 0;
		Class<? extends Activity> targetActivity = SelectContactActivity.class;
		doSwitchHuddleSelectContactActivity(mContext, targetActivity,
				blackList, requestCode);
	}

	private void doOnDeleteItemClick() {
		int size = mArrayList.size();
		for (int i = 0; i < size; i++) {
			MeSettingWeiboPrivacyNotSeeData data = mArrayList.get(i);
			int itemType = data.getItemType();
			if (itemType == MeSettingWeiboPrivacyNotSeeAdapter.ItemType.NORMAL) {
				data.setDeleteIconVisible(true);
			}
		}

		updateAdapterAndView();
	}

	private void doOnNormalItemClick(MeSettingWeiboPrivacyNotSeeData data) {
		boolean isCanDelete = data.isDeleteIconVisible();
		if (isCanDelete) {
			mArrayList.remove(data);
			updateAdapterAndView();

			String contactJid = data.getBareJid();
			rmToUserBlackList(contactJid);
		}
	}

	private void initBlacklistData() {
		XmppData xmppData = XmppData.getSingleton();
		Hashtable<String, XmppFriend> hashtable = xmppData.getAllFriends();

		Set<Entry<String, XmppFriend>> set = hashtable.entrySet();
		Iterator<Entry<String, XmppFriend>> iterator = set.iterator();
		while (iterator.hasNext()) {
			Entry<String, XmppFriend> entry = iterator.next();
			XmppFriend xmppFriend = entry.getValue();
			boolean isInBlacklist = getIsIgnoreMicroBlog(xmppFriend);

			if (isInBlacklist) {
				MeSettingWeiboPrivacyNotSeeData data = getMeSettingWeiboPrivacyNotSeeData(xmppFriend);
				mArrayList.add(data);
			}
		}
	}

	private MeSettingWeiboPrivacyNotSeeData getMeSettingWeiboPrivacyNotSeeData(
			XmppFriend xmppFriend) {
		String jid = xmppFriend.getFriendJid();
		String showName = xmppFriend.getShowName();

		String photoPath = xmppFriend.getHeadPortrait();
		Bitmap photoBitmap = getProperPhoto(photoPath, mDefBitmap);

		int itemType = MeSettingWeiboPrivacyNotSeeAdapter.ItemType.NORMAL;
		MeSettingWeiboPrivacyNotSeeData data = getMeSettingWeiboPrivacyNotSeeData(
				photoBitmap, showName, false, itemType, jid);

		return data;
	}

	private boolean getIsIgnoreMicroBlog(XmppFriend data) {
		return data.isIgnoreMicroBlog();
	}

	private void addAddAndDeleteView() {
		Resources res = mContext.getResources();

		int addId = R.drawable.cm_add_bg_normal;
		Bitmap addBitmap = BitmapFactory.decodeResource(res, addId);
		String name = mContext.getString(R.string.add);
		int itemType = MeSettingWeiboPrivacyNotSeeAdapter.ItemType.ADD;
		MeSettingWeiboPrivacyNotSeeData addData = getMeSettingWeiboPrivacyNotSeeData(
				addBitmap, name, false, itemType, null);
		mArrayList.add(addData);

		int deleteId = R.drawable.cm_delete_bg_normal;
		Bitmap deleteBitmap = BitmapFactory.decodeResource(res, deleteId);
		name = mContext.getString(R.string.delete);
		itemType = MeSettingWeiboPrivacyNotSeeAdapter.ItemType.DELETE;
		MeSettingWeiboPrivacyNotSeeData deleteData = getMeSettingWeiboPrivacyNotSeeData(
				deleteBitmap, name, false, itemType, null);
		mArrayList.add(deleteData);
	}

	private MeSettingWeiboPrivacyNotSeeData getMeSettingWeiboPrivacyNotSeeData(
			Bitmap bitmap, String name, boolean isDeleteIconShow, int itemType,
			String bareJid) {
		MeSettingWeiboPrivacyNotSeeData data = new MeSettingWeiboPrivacyNotSeeData();
		data.setBitmap(bitmap);
		data.setName(name);
		data.setDeleteIconVisible(isDeleteIconShow);
		data.setItemType(itemType);
		data.setBareJid(bareJid);

		return data;
	}

	private Bitmap getProperPhoto(String photoPath, Bitmap defPhoto) {
		Bitmap bitmap = defPhoto;
		if (photoPath != null) {
			Bitmap decodedBitmap = BitmapFactory.decodeFile(photoPath);

			if (decodedBitmap != null) {
				bitmap = decodedBitmap;
			}
		}

		int width = DisplayUtil.dip2px(mContext, IMAGE_WIDTH_DIP);
		int height = DisplayUtil.dip2px(mContext, IMAGE_HEIGHT_DIP);
		if (bitmap != null) {
			bitmap = BitmapOperator.getImageThumbnail(bitmap, width, height);
		}

		return bitmap;
	}

	private ArrayList<String> getCurrentBlacklist() {
		ArrayList<String> arrayList = new ArrayList<String>();

		int size = mArrayList.size();
		for (int i = 0; i < size; i++) {
			MeSettingWeiboPrivacyNotSeeData data = mArrayList.get(i);
			int itemType = data.getItemType();
			if (itemType == MeSettingWeiboPrivacyNotSeeAdapter.ItemType.NORMAL) {
				String bareJid = data.getBareJid();
				arrayList.add(bareJid);
			}
		}

		return arrayList;
	}

	private void addToUserBlackList(String contactJid) {
		doUpdateXmppFriendAndWriteDb(contactJid, true);
	}

	private void rmToUserBlackList(String contactJid) {
		doUpdateXmppFriendAndWriteDb(contactJid, false);
	}

	private void doUpdateXmppFriendAndWriteDb(String contactJid,
			boolean isIgnoreMicroBlog) {
		XmppOperation xmppOperation = new XmppOperation();

		XmppData xmppData = XmppData.getSingleton();
		XmppFriend xmppFriend = xmppData.getFriend(contactJid);
		if (xmppFriend != null) {
			xmppFriend.setIgnoreMicroBlog(isIgnoreMicroBlog);
			xmppOperation.writeXmpFriendToDb(xmppFriend);
		}
	}

	public void resolveToGetSelectedContacts(Intent intent) {
		boolean isHaveNewAdded = false;

		ArrayList<String> newArrayList = intent
				.getStringArrayListExtra(INTENT_SELECTED_USER_LIST_KEY);
		if (newArrayList != null) {
			addNewSelectedContactToDataListAndKeepOrder(newArrayList);
			isHaveNewAdded = true;
		}

		updateDataListToResetView();
		updateAdapterAndView();

		if (!isHaveNewAdded) {
			return;
		}

		int size = newArrayList.size();
		for (int i = 0; i < size; i++) {
			String contactJid = newArrayList.get(i);
			addToUserBlackList(contactJid);
		}
	}

	private void updateDataListToResetView() {
		int size = mArrayList.size();
		for (int i = 0; i < size; i++) {
			MeSettingWeiboPrivacyNotSeeData data = mArrayList.get(i);
			int itemType = data.getItemType();

			if (itemType == MeSettingWeiboPrivacyNotSeeAdapter.ItemType.NORMAL) {
				data.setDeleteIconVisible(false);
			}
		}
	}

	private void addNewSelectedContactToDataListAndKeepOrder(
			ArrayList<String> newJidList) {
		int newSize = newJidList.size();
		for (int i = 0; i < newSize; i++) {
			String bareJid = newJidList.get(i);
			XmppData xmppData = XmppData.getSingleton();
			XmppFriend xmppFriend = xmppData.getFriend(bareJid);

			String showName = xmppFriend.getShowName();
			String path = xmppFriend.getHeadPortrait();
			Bitmap bitmap = getProperPhoto(path, mDefBitmap);

			int itemType = MeSettingWeiboPrivacyNotSeeAdapter.ItemType.NORMAL;

			MeSettingWeiboPrivacyNotSeeData data = getMeSettingWeiboPrivacyNotSeeData(
					bitmap, showName, false, itemType, bareJid);

			int index = computeInsertIndex(mArrayList);
			mArrayList.add(index, data);
		}
	}

	private int computeInsertIndex(
			ArrayList<MeSettingWeiboPrivacyNotSeeData> arrayList) {
		int i = 0;
		int size = mArrayList.size();
		for (; i < size; i++) {
			MeSettingWeiboPrivacyNotSeeData data = mArrayList.get(i);
			int itemType = data.getItemType();

			if (itemType != MeSettingBlacklistAdapter.ItemType.NORMAL) {
				break;
			}
		}

		return i;
	}

	public void doSwitchMeSettingWeiboPrivacyActivity(Context context,
			Class<? extends Activity> targetActivity) {
		switchActivity(context, targetActivity, null);
	}

	public void doSwitchHuddleSelectContactActivity(Context context,
			Class<? extends Activity> targetActivity,
			ArrayList<String> blacklist, int requestCode) {
		Bundle bundle = new Bundle();
		bundle.putBoolean(INTENT_FROM_SETTING, true);
		bundle.putStringArrayList(INTENT_SELECTED_USER_LIST_KEY, blacklist);

		switchActivityForResult(context, targetActivity, bundle, requestCode);
	}

	private void switchActivityForResult(Context context,
			Class<? extends Activity> targetActivity, Bundle bundle,
			int requestCode) {
		Activity activity = (Activity) context;
		Intent intent = new Intent();
		intent.setClass(activity, targetActivity);

		if (bundle != null) {
			intent.putExtras(bundle);
		}

		activity.startActivityForResult(intent, requestCode);
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
