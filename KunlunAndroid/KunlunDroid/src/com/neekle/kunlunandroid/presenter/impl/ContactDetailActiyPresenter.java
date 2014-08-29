package com.neekle.kunlunandroid.presenter.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.ContactsContract;

import android.util.Log;
import android.widget.Toast;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.adapter.ContactDetailImageAdapter;
import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.data.CellOneContactData;
import com.neekle.kunlunandroid.data.ContactInfo;
import com.neekle.kunlunandroid.db.DbConstants;
import com.neekle.kunlunandroid.presenter.common.FriendOperation;
import com.neekle.kunlunandroid.presenter.common.WebservicePresenter;
import com.neekle.kunlunandroid.presenter.interf.IContactDetailActivity;
import com.neekle.kunlunandroid.presenter.interf.IContactDetailActiyPresenterCb;
import com.neekle.kunlunandroid.screens.AddsbookActivity;
import com.neekle.kunlunandroid.screens.InviteAddFriendActivity;
import com.neekle.kunlunandroid.screens.MessageSessionActivity;
import com.neekle.kunlunandroid.screens.MicroblogListActivity;
import com.neekle.kunlunandroid.screens.TelVideoActivity;
import com.neekle.kunlunandroid.screens.TelVoiceActivity;
import com.neekle.kunlunandroid.screens.TelephoneTabActivity;
import com.neekle.kunlunandroid.sip.common.SipConstants;
import com.neekle.kunlunandroid.sip.common.SipConstants.TelAction;
import com.neekle.kunlunandroid.util.BitmapOperator;
import com.neekle.kunlunandroid.util.DeviceInfo;
import com.neekle.kunlunandroid.util.DisplayUtil;
import com.neekle.kunlunandroid.view.specials.ContactDetailBottomPopupMenu;
import com.neekle.kunlunandroid.web.common.WebserviceConstants;
import com.neekle.kunlunandroid.web.data.common.TypeReturn;
import com.neekle.kunlunandroid.web.data.common.WebserviceParamsObject;
import com.neekle.kunlunandroid.web.data.friend.FriendInfo;
import com.neekle.kunlunandroid.web.data.friend.TypeFriend;
import com.neekle.kunlunandroid.web.data.friend.TypeFriendDetails;
import com.neekle.kunlunandroid.web.data.friend.TypeUser;
import com.neekle.kunlunandroid.web.webservices.ISoapReceivedCallback;
import com.neekle.kunlunandroid.xmpp.MyXmppCallback;
import com.neekle.kunlunandroid.xmpp.XmppData;
import com.neekle.kunlunandroid.xmpp.XmppOperation;
import com.neekle.kunlunandroid.xmpp.XmppService;
import com.neekle.kunlunandroid.xmpp.data.XmppFriend;
import com.neekle.kunlunandroid.xmpp.data.XmppJid;

public class ContactDetailActiyPresenter implements
		IContactDetailActiyPresenterCb, ISoapReceivedCallback {

	private static final int IMAGE_WIDTH_DIP = 120;
	private static final int IMAGE_HEIGHT_DIP = 120;

	private static final int MSG_WS_lOGIN_COMPLETED = 650;
	private static final int MSG_WS_USER_BLACKLIST_COMPLETED = 651;
	private static final int MSG_LOAD_USERINFO_COMPLETED = 652;

	private static final String BACK_SLASH = "/";
	private static final String SDCARD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	private static final String BASE_FOLDER_RELATIVE_PATH_TO_SDCARD = "Kunlun/";
	private static final String BASE_FOLDER_ABSOLUTE_PATH = SDCARD_PATH
			+ BACK_SLASH + BASE_FOLDER_RELATIVE_PATH_TO_SDCARD;
	private static final String MICRO_BLOG = "MicroBlog";
	private static final String IMG_FILTER_SU8FFIX_JPG = ".jpg";

	private static final String MESSAGE_TO_PASS = "message_to_pass";
	private static final String INTENT_MICROBLOGLISTACTIVITY_CONTACT_INFO_KEY = "MicroBlogListActivity_contact_info_key";
	private static final String INTENT_MESSAGESESSION_CONTACT_INFO_KEY = "messagesession_contact_info_key";
	private static final String INTENT_ADDSBOOK_CONTACT_INFO_KEY = "addsbook_contact_info_key";
	private static final String INTENT_CONTACT_DETAIL_MESSAGE_CHAT_KEY = "contact_detail_message_chat_key";
	private static final String INTENT_CONTACT_DETAIL_AND_MORE_KEY = "contact_detail_and_more_key";
	private static final String INTENT_CONTACT_DETAIL_INVITE_ADD_FRIEND_KEY = "contact_detail_invite_add_friend_key";
	private static final String INTENT_CONTACT_DETAIL_TELEPHONE_KEY = "contact_detail_telephone_key";

	private Context mContext;
	private MyXmppCallback mMyXmppCallback;
	private XmppService mXmppService;
	private IContactDetailActivity mIView;
	private WebservicePresenter mWebsPresenter;
	private XmppOperation mXmppOperation;
	private FriendOperation mFriendOperation;

	private XmppFriend mXmppFriend;
	private String mPartnerBareJid;
	private Bitmap mDefPhotoBp;

	// 这个后面要改掉，这个应该是从数据库中获取，后面还是要确认到底是读取联系人的号码，还是自己设置的号码
	private String mPhoneNumber = "15216896542";
	private String mContactId;

	public ContactDetailActiyPresenter(IContactDetailActivity view,
			Context context) {
		mIView = view;
		mContext = context;

		mXmppOperation = new XmppOperation();
		mXmppService = XmppService.getSingleton();
		mMyXmppCallback = MyXmppCallback.getSingleton();
		// mMyXmppCallback.setAddsbookActiyCallback(this);
		mFriendOperation = new FriendOperation();
		mFriendOperation.setContactDetailActiyCallback(this);

		mWebsPresenter = new WebservicePresenter(context);
		mWebsPresenter.setCallback(this);
	}

	public void init(Intent intent) {
		Parcelable microbloglistParcelable = intent
				.getParcelableExtra(INTENT_MICROBLOGLISTACTIVITY_CONTACT_INFO_KEY);
		Parcelable addsbookParcelable = intent
				.getParcelableExtra(INTENT_ADDSBOOK_CONTACT_INFO_KEY);
		Parcelable messageSessionParcelable = intent
				.getParcelableExtra(INTENT_MESSAGESESSION_CONTACT_INFO_KEY);
		Parcelable messageChatParcelable = intent
				.getParcelableExtra(INTENT_CONTACT_DETAIL_MESSAGE_CHAT_KEY);
		Parcelable contactDetailMoreParcelable = intent
				.getParcelableExtra(INTENT_CONTACT_DETAIL_AND_MORE_KEY);
		Parcelable inviteAddFriendParcelable = intent
				.getParcelableExtra(INTENT_CONTACT_DETAIL_INVITE_ADD_FRIEND_KEY);
		Parcelable telephoneParcelable = intent
				.getParcelableExtra(INTENT_CONTACT_DETAIL_TELEPHONE_KEY);
		if (addsbookParcelable != null) {
			mXmppFriend = (XmppFriend) addsbookParcelable;
			mIView.setIntentClass(AddsbookActivity.class, true, true);
		} else if (messageSessionParcelable != null) {
			mXmppFriend = (XmppFriend) messageSessionParcelable;
			mIView.setIntentClass(MicroblogListActivity.class, true, true);
		} else if (microbloglistParcelable != null) {
			mXmppFriend = (XmppFriend) microbloglistParcelable;
			mIView.setIntentClass(MessageSessionActivity.class, true, true);
		} else if (messageChatParcelable != null) {
			mXmppFriend = (XmppFriend) messageChatParcelable;
			mIView.setIntentClass(AddsbookActivity.class, true, true);
		} else if (contactDetailMoreParcelable != null) {
			mXmppFriend = (XmppFriend) contactDetailMoreParcelable;
			mIView.setIntentClass(AddsbookActivity.class, true, true);
		} else if (inviteAddFriendParcelable != null) {
			mXmppFriend = (XmppFriend) inviteAddFriendParcelable;
			mIView.setIntentClass(AddsbookActivity.class, true, true);
		} else if (telephoneParcelable != null) {
			mXmppFriend = (XmppFriend) telephoneParcelable;
			mIView.setIntentClass(TelephoneTabActivity.class, false, true);
		}

		if (mXmppFriend != null) {
			mPartnerBareJid = mXmppFriend.getFriendJid();
			mFriendOperation
					.doLoadUserinfoToUpdateXmppFriendAndDb(mPartnerBareJid);
			showPersonalInfo(mXmppFriend);
			loadPersonalDynamicImg();
		}

		String localAddsInfo = readPhoneInfo(mPhoneNumber);
		mIView.showLocalAddsInfo(localAddsInfo);
	}

	private String readPhoneInfo(String phoneNumber) {
		String string = null;

		ContentResolver contentResolver = mContext.getContentResolver();

		Uri lookupUri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));
		String[] projection = new String[] { ContactsContract.PhoneLookup._ID,
				ContactsContract.PhoneLookup.NUMBER,
				ContactsContract.PhoneLookup.DISPLAY_NAME };
		/* 测试发现，这种方式能够自动将号码归一化，然后进行查找匹配,可能是在 content provider 里面做掉了 */
		Cursor cursor = contentResolver.query(lookupUri, projection,
				ContactsContract.PhoneLookup.NUMBER + " =?",
				new String[] { phoneNumber }, null);
		if (cursor != null) {
			int numberIndex = cursor
					.getColumnIndex(ContactsContract.PhoneLookup.NUMBER);
			int displayNameIndex = cursor
					.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
			int contactIdIndex = cursor
					.getColumnIndex(ContactsContract.PhoneLookup._ID);

			while (cursor.moveToNext()) {
				mContactId = cursor.getString(contactIdIndex);
				// 查询出来的数据， 1 521-689-6542 格式
				String number = cursor.getString(numberIndex);
				String displayName = cursor.getString(displayNameIndex);
				string = displayName + "  " + phoneNumber;

				break;
			}

			cursor.close();
		}

		/* 针对部分手机（模拟器）会将号码自动按照国际标准分段问题，这种方式无法使用，因为其不支持号码归一化查找 */
		// cursor = contentResolver.query(
		// ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
		// ContactsContract.CommonDataKinds.Phone.NUMBER + " =?",
		// new String[] { phoneNumber }, null);

		return string;
	}

	private void tempKeep() {
		// String[] projection = new String[] {
		// ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
		// ContactsContract.CommonDataKinds.Phone.NUMBER,
		// ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
		// ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
		// ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID };
		// cursor = contentResolver.query(
		// ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
		// ContactsContract.CommonDataKinds.Phone.NUMBER + " =?",
		// new String[] { phoneNumber }, null);
		// if (cursor != null) {
		// while (cursor.moveToNext()) {
		// int displayNameIndex = cursor
		// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
		// int rawContactIdIndex = cursor
		// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID);
		//
		// mRawContactId = cursor.getString(rawContactIdIndex);
		// String displayName = cursor.getString(displayNameIndex);
		// string = displayName + "  " + phoneNumber;
		//
		// break;
		// }
		// }
	}

	public void setContactPhoto() {
		if (mDefPhotoBp == null) {
			mDefPhotoBp = getBitmap(R.drawable.addsbook_def_photo);
		}

		String photoPath = mXmppFriend.getHeadPortrait();
		Bitmap sourceBitmap = getProperPhoto(photoPath, mDefPhotoBp);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		sourceBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
		byte[] avatar = os.toByteArray();

		String[] projection = new String[] { ContactsContract.RawContacts._ID };
		Cursor cursor = mContext.getContentResolver().query(
				ContactsContract.RawContacts.CONTENT_URI, projection,
				ContactsContract.RawContacts.CONTACT_ID + " =?",
				new String[] { mContactId }, null);

		if (cursor != null) {
			int rawContactIdIndex = cursor
					.getColumnIndex(ContactsContract.RawContacts._ID);

			while (cursor.moveToNext()) {
				String rawContactId = cursor.getString(rawContactIdIndex);
				setContactPhoto(rawContactId, avatar);
			}
		}

		cursor.close();

		String hint = mContext.getString(R.string.save_photo_ok);
		mIView.showHint(hint, Toast.LENGTH_SHORT);
	}

	private void setContactPhoto(String rawContactId, byte[] bytes) {
		int photoRow = Constants.DEF_INT_VALUE;
		String where = ContactsContract.Data.RAW_CONTACT_ID + " = "
				+ rawContactId + " AND " + ContactsContract.Data.MIMETYPE
				+ "=='"
				+ ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
				+ "'";

		Cursor cursor = mContext.getContentResolver().query(
				ContactsContract.Data.CONTENT_URI, null, where, null, null);
		int idIndex = cursor.getColumnIndexOrThrow(ContactsContract.Data._ID);
		if (cursor.moveToFirst()) {
			photoRow = cursor.getInt(idIndex);
		}
		cursor.close();

		// 修改联系人的头像
		ContentValues values = new ContentValues();
		values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
		values.put(ContactsContract.Data.IS_SUPER_PRIMARY, 1);
		values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, bytes);
		values.put(ContactsContract.Data.MIMETYPE,
				ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);

		if (photoRow >= 0) {
			mContext.getContentResolver().update(
					ContactsContract.Data.CONTENT_URI, values,
					ContactsContract.Data._ID + " = " + photoRow, null);
		} else {
			/*
			 * 测试发现，如果使用 values.put(ContactsContract.Data.CONTACT_ID,
			 * contactId); content provider 底层拋出NPE问题, 原因不是特别确证，可能是 data表没有
			 * contact_id 字段的原因
			 */
			mContext.getContentResolver().insert(
					ContactsContract.Data.CONTENT_URI, values);
		}
	}

	private void showPersonalInfo(XmppFriend xmppFriend) {
		if (mDefPhotoBp == null) {
			mDefPhotoBp = getBitmap(R.drawable.addsbook_def_photo);
		}

		String photoPath = xmppFriend.getHeadPortrait();
		Bitmap bitmap = getProperPhoto(photoPath, mDefPhotoBp);
		// int density = bitmap.getDensity();
		// Log.i("bmdensity", "density: " + density);
		// density = (int) DisplayUtil.getDensity(mContext);
		// Log.i("bmdensity", "display density: " + density);
		// bitmap.setDensity(density);
		BitmapDrawable photoDrawable = new BitmapDrawable(bitmap);

		String jid = xmppFriend.getFriendJid();
		String showName = xmppFriend.getShowName();
		boolean isStarSign = xmppFriend.isStarSign();

		String label = xmppFriend.getSignature();
		String sex = xmppFriend.getSex();

		mIView.showBasicInfo(photoDrawable, showName, jid, isStarSign);
		mIView.showExtraInfo(label, sex);
	}

	private Bitmap getBitmap(int resid) {
		Resources resources = mContext.getResources();
		Bitmap bitmap = BitmapFactory.decodeResource(resources, resid);

		return bitmap;
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

	private void loadPersonalDynamicImg() {
		String myBareJid = XmppOperation.getMyBareJid();
		String basePath = BASE_FOLDER_ABSOLUTE_PATH + myBareJid + BACK_SLASH
				+ MICRO_BLOG + BACK_SLASH;

		ArrayList<String> arrayList = new ArrayList<String>();
		File file = new File(basePath);
		if (file.exists()) {
			String[] fileStrings = file.list();
			int length = fileStrings.length;

			for (int i = 0; i < length; i++) {
				if (fileStrings[i].endsWith(IMG_FILTER_SU8FFIX_JPG)) {
					String absolutePath = basePath + fileStrings[i];
					arrayList.add(absolutePath);
				}
			}
		}

		ContactDetailImageAdapter adapter = new ContactDetailImageAdapter(
				mContext, arrayList);
		mIView.updatePhotoAlbumAdapter(adapter);
	}

	public void doClearNecessaryCallback() {
		mFriendOperation.setContactDetailActiyCallback(null);
	}

	public void doPhoneCall() {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
				+ mPhoneNumber));
		mContext.startActivity(intent);
	}

	public void doToCallFriend() {
		// 关于网络不好的问题，暂时忽略,后面需要考虑 *******
		String jid = mXmppFriend.getFriendJid();
		XmppJid xmppJid = new XmppJid(jid);
		String partnerSipUserName = xmppJid.getUserName();

		Class<? extends Activity> targetActivity = TelVoiceActivity.class;
		doSwitchTelVoiceActivity(mContext, targetActivity, partnerSipUserName);
	}

	public void doToVideoFriend() {
		// 关于网络不好的问题，暂时忽略,后面需要考虑 *******
		String jid = mXmppFriend.getFriendJid();
		XmppJid xmppJid = new XmppJid(jid);
		String partnerSipUserName = xmppJid.getUserName();

		Class<? extends Activity> targetActivity = TelVideoActivity.class;
		doSwitchTelVideoActivity(mContext, targetActivity, partnerSipUserName);
	}

	public void doOnMenuItemClick(int tag) {
		switch (tag) {
		case ContactDetailBottomPopupMenu.MenuItem.MENU_DO_STAR: {
			doOnMenuDoStarClick();
			break;
		}
		case ContactDetailBottomPopupMenu.MenuItem.MENU_ADD_FRIEND: {
			doOnMenuAddFriendClick();
			break;
		}
		case ContactDetailBottomPopupMenu.MenuItem.MENU_ADD_BLACKLIST: {
			doOnMenuAddBlacklistClick();
			break;
		}
		case ContactDetailBottomPopupMenu.MenuItem.MENU_SET_WEIBO_PERMISSION: {
			doOnMenuSetWeiboClick();
			break;
		}
		case ContactDetailBottomPopupMenu.MenuItem.MENU_SEND_NAME_CARD: {
			doOnMenuSendNameCardClick();
			break;
		}
		case ContactDetailBottomPopupMenu.MenuItem.MENU_DELETE: {
			doOnMenuDeleteClick();
			break;
		}
		default: {
			break;
		}

		}
	}

	private void doOnMenuDoStarClick() {
		boolean isStar = mXmppFriend.isStarSign();
		isStar = !isStar;

		mXmppOperation.updateXmpFriendAndWriteToDb(mPartnerBareJid, isStar);
		mXmppFriend.setStarSign(isStar);
		mIView.showStarSign(isStar);
	}

	private void doOnMenuAddFriendClick() {
		Class<? extends Activity> targetActivity = InviteAddFriendActivity.class;
		doSwitchInviteAddFriendActivity(mContext, targetActivity);
	}

	private void doOnMenuAddBlacklistClick() {
		boolean isInBlacklist = mXmppFriend.isInBlacklist();
		if (isInBlacklist) {
			String hint = mContext
					.getString(R.string.have_been_blacklist_member);
			mIView.showHint(hint, Toast.LENGTH_SHORT);
			return;
		}

		doWsRequestLogin();
	}

	private void doOnMenuSetWeiboClick() {

	}

	private void doOnMenuSendNameCardClick() {

	}

	private void doOnMenuDeleteClick() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

		String positiveText = mContext.getString(R.string.confirm);
		String negativeText = mContext.getString(R.string.cancle);
		String message = mContext
				.getString(R.string.are_you_sure_to_delete_this_friend);
		builder.setMessage(message);

		builder.setPositiveButton(positiveText,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mXmppService.deleteFriend(mPartnerBareJid);

						Class<? extends Activity> targetActivity = AddsbookActivity.class;
						doSwitchAddsbookActivity(mContext, targetActivity);
					}
				});

		builder.setNegativeButton(negativeText,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});

		builder.create().show();
	}

	public void doSwitchContactDetailMoreActivity(Context context,
			Class<? extends Activity> targetActivity) {
		Bundle bundle = new Bundle();
		bundle.putParcelable(INTENT_CONTACT_DETAIL_AND_MORE_KEY, mXmppFriend);

		switchActivity(context, targetActivity, bundle);
	}

	public void doSwitchInviteAddFriendActivity(Context context,
			Class<? extends Activity> targetActivity) {
		Bundle bundle = new Bundle();
		bundle.putParcelable(INTENT_CONTACT_DETAIL_INVITE_ADD_FRIEND_KEY,
				mXmppFriend);

		switchActivity(context, targetActivity, bundle);
	}

	public void doSwitchAddsbookActivity(Context context,
			Class<? extends Activity> targetActivity) {
		switchActivity(context, targetActivity, null);
	}

	public void doSwitchIntentActivity(Context context,
			Class<? extends Activity> targetActivity,
			boolean isStartNewActivity, boolean isFinishSelf) {
		if (!isStartNewActivity && isFinishSelf) {
			Activity activity = (Activity) context;
			activity.finish();
		} else if (isStartNewActivity && !isFinishSelf) {
			switchActivityWithNoFinish(context, targetActivity, null);
		} else {
			switchActivity(context, targetActivity, null);
		}
	}

	public void doSwitchMessageChatActivity(Context context,
			Class<? extends Activity> targetActivity) {
		Bundle bundle = new Bundle();
		bundle.putParcelable(INTENT_CONTACT_DETAIL_MESSAGE_CHAT_KEY,
				mXmppFriend);

		switchActivity(context, targetActivity, bundle);
	}

	public void doSwitchTelVoiceActivity(Context context,
			Class<? extends Activity> targetActivity, String partnerSipUserName) {
		Bundle bundle = new Bundle();
		bundle.putString(SipConstants.INTENT_SIP_USERNAME_PARTNER_KEY,
				partnerSipUserName);
		String telAction = TelAction.OUTGOING;
		bundle.putString(SipConstants.INTENT_TEL_ACTION_KEY, telAction);

		switchActivityWithNoFinish(context, targetActivity, bundle);
	}

	public void doSwitchTelVideoActivity(Context context,
			Class<? extends Activity> targetActivity, String partnerSipUserName) {
		Bundle bundle = new Bundle();
		bundle.putString(SipConstants.INTENT_SIP_USERNAME_PARTNER_KEY,
				partnerSipUserName);
		String telAction = TelAction.OUTGOING;
		bundle.putString(SipConstants.INTENT_TEL_ACTION_KEY, telAction);

		switchActivityWithNoFinish(context, targetActivity, bundle);
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

	private void addToUserBlackList(String sessionId, String contactJid) {
		doUpdateXmppFriendAndWriteDb(contactJid, true);

		int opType = WebserviceConstants.USER_METHOD_USER_BLACKLIST_ARGS_OP_TYPE_ADD;
		doWsRequestUserBlackList(sessionId, opType, contactJid);
	}

	private void rmToUserBlackList(String sessionId, String contactJid) {
		doUpdateXmppFriendAndWriteDb(contactJid, false);

		int opType = WebserviceConstants.USER_METHOD_USER_BLACKLIST_ARGS_OP_TYPE_DELETE;
		doWsRequestUserBlackList(sessionId, opType, contactJid);
	}

	private void doUpdateXmppFriendAndWriteDb(String contactJid,
			boolean isInBlacklist) {
		XmppOperation xmppOperation = new XmppOperation();

		XmppData xmppData = XmppData.getSingleton();
		XmppFriend xmppFriend = xmppData.getFriend(contactJid);
		if (xmppFriend != null) {
			xmppFriend.setInBlacklist(isInBlacklist);
			xmppOperation.writeXmpFriendToDb(xmppFriend);
		}
	}

	private void doWsRequestLogin() {
		String nameSpace = WebserviceConstants.NAME_SPACE;
		String endPoint = WebserviceConstants.END_POINT_GLOCAL;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;

		String myJid = XmppOperation.getMyBareJid();

		int handleTypeSync = WebserviceConstants.TaskThreadType.ASYNC_TASK_TYPE;
		wsRequestLogin(nameSpace, endPoint, prefix, myJid, handleTypeSync);
	}

	private void wsRequestLogin(String nameSpace, String endPoint,
			String prefix, String myJid, int handleType) {
		String methodName = WebserviceConstants.GLOCAL_METHOD_LOGIN;
		String soapAction = prefix + methodName;
		LinkedHashMap<String, Object> hashMap = mWebsPresenter.getArgus(myJid);

		WebserviceParamsObject webserviceParamsObject = new WebserviceParamsObject(
				nameSpace, methodName, endPoint, soapAction, hashMap);
		mWebsPresenter.action(webserviceParamsObject, handleType);
	}

	private void doWsRequestUserBlackList(String sessionid, int opType,
			String contactJid) {
		String nameSpace = WebserviceConstants.NAME_SPACE;
		String endPoint = WebserviceConstants.END_POINT_USER;
		String prefix = WebserviceConstants.SOAP_ACTION_PREFIX;

		int handleTypeSync = WebserviceConstants.TaskThreadType.ASYNC_TASK_TYPE;
		wsRequestUserBlackList(nameSpace, endPoint, prefix, sessionid, opType,
				contactJid, handleTypeSync);
	}

	private void wsRequestUserBlackList(String nameSpace, String endPoint,
			String prefix, String sessionid, int opType, String contactJid,
			int handleType) {
		String methodName = WebserviceConstants.USER_METHOD_USER_BLACKLIST;
		String soapAction = prefix + methodName;
		LinkedHashMap<String, Object> hashMap = mWebsPresenter.getArgus(
				sessionid, opType, contactJid);

		WebserviceParamsObject webserviceParamsObject = new WebserviceParamsObject(
				nameSpace, methodName, endPoint, soapAction, hashMap);
		mWebsPresenter.action(webserviceParamsObject, handleType);
	}

	private void sendMsgToHandler(int what) {
		Message message = Message.obtain();
		message.what = what;

		mHandler.sendMessage(message);
	}

	private void sendMsgToHandler(int what, String string) {
		Message message = Message.obtain();
		message.what = what;
		if (string != null) {
			Bundle bundle = new Bundle();
			bundle.putString(MESSAGE_TO_PASS, string);
			message.setData(bundle);
		}
		mHandler.sendMessage(message);
	}

	private void sendMsgToHandler(int what, Parcelable parcelable) {
		Message message = Message.obtain();
		message.what = what;
		if (parcelable != null) {
			Bundle bundle = new Bundle();
			bundle.putParcelable(MESSAGE_TO_PASS, parcelable);
			message.setData(bundle);
		}
		mHandler.sendMessage(message);
	}

	private void sendMsgToHandler(int what, ArrayList<? extends Parcelable> list) {
		Message message = Message.obtain();
		message.what = what;
		if (list != null) {
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList(MESSAGE_TO_PASS, list);
			message.setData(bundle);
		}
		mHandler.sendMessage(message);
	}

	private void sendMsgToHandler(int what, LinkedHashMap<String, ?> list) {
		Message message = Message.obtain();
		message.what = what;
		if (list != null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable(MESSAGE_TO_PASS, list);
			message.setData(bundle);
		}
		mHandler.sendMessage(message);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			Bundle bundle = msg.getData();
			int what = msg.what;
			switch (what) {
			case MSG_WS_lOGIN_COMPLETED: {
				handleMsgWsLogin(bundle);
				break;
			}
			case MSG_WS_USER_BLACKLIST_COMPLETED: {
				handleMsgWsUserBlacklist(bundle);
				break;
			}
			case MSG_LOAD_USERINFO_COMPLETED: {
				handleMsgLoadUserinfo(bundle);
				break;
			}
			default: {
				break;
			}

			}
		}

	};

	private void handleMsgWsLogin(Bundle bundle) {
		String sessionId = bundle.getString(MESSAGE_TO_PASS);

		if (sessionId == null) {
			String hint = mContext.getString(R.string.ws_login_fail);
			mIView.showHint(hint, Toast.LENGTH_SHORT);
			return;
		}

		String contactJid = mXmppFriend.getFriendJid();
		addToUserBlackList(sessionId, contactJid);
	}

	private void handleMsgWsUserBlacklist(Bundle bundle) {
		Parcelable parcelable = (Parcelable) bundle
				.getParcelable(MESSAGE_TO_PASS);
		TypeReturn typeReturn = (TypeReturn) parcelable;

		int code = Constants.DEF_INT_VALUE;
		String codeString = typeReturn.getCode();
		if (codeString != null) {
			code = Integer.valueOf(codeString);
		}

		boolean isSuccess = mWebsPresenter.judgeIfSuccess(code);
		String hint = null;
		if (isSuccess) {
			hint = mContext.getString(R.string.add_to_blacklist_success);
		} else {
			hint = mContext.getString(R.string.add_to_blacklist_failure);
		}

		mIView.showHint(hint, Toast.LENGTH_SHORT);
	}

	private void handleMsgLoadUserinfo(Bundle bundle) {
		Parcelable parcelable = (Parcelable) bundle
				.getParcelable(MESSAGE_TO_PASS);
		XmppFriend xmppFriend = (XmppFriend) parcelable;

		if (xmppFriend != null) {
			String updateJid = xmppFriend.getFriendJid();
			String currentJid = mXmppFriend.getFriendJid();

			if (currentJid.equals(updateJid)) {
				mXmppFriend = xmppFriend;
				showPersonalInfo(mXmppFriend);
			}
		}
	}

	private void resolveWsResult(Object object) {
		Object resolvedObject = mWebsPresenter.resolveObject(object);
		String method = mWebsPresenter.getMethod(resolvedObject);
		Object dataObject = mWebsPresenter.getDataObject(resolvedObject);

		if (method.equals(WebserviceConstants.GLOCAL_METHOD_LOGIN)) {
			String sessionId = null;
			if (dataObject != null) {
				sessionId = (String) dataObject;
			}

			sendMsgToHandler(MSG_WS_lOGIN_COMPLETED, sessionId);
		} else if (method
				.equals(WebserviceConstants.USER_METHOD_USER_BLACKLIST)) {
			TypeReturn typeReturn = null;
			if (dataObject != null) {
				typeReturn = (TypeReturn) dataObject;
			}

			sendMsgToHandler(MSG_WS_USER_BLACKLIST_COMPLETED, typeReturn);
		}
	}

	@Override
	public void onWsResultNotify(Object object) {
		resolveWsResult(object);
	}

	@Override
	public void testPresenter() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadUserinfoCompleted(XmppFriend xmppFriend) {
		sendMsgToHandler(MSG_LOAD_USERINFO_COMPLETED, xmppFriend);
	}

}
