package com.neekle.kunlunandroid.presenter.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.neekle.kunlunandroid.R;
import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.common.XmlParser;
import com.neekle.kunlunandroid.common.activity.ActivityUtil;
import com.neekle.kunlunandroid.data.SysConfigInfo;
import com.neekle.kunlunandroid.presenter.interf.ISoftwareServerSettingActivity;
import com.neekle.kunlunandroid.presenter.interf.ISoftwareSettingActivity;
import com.neekle.kunlunandroid.presenter.interf.ISoftwareSettingActiyPresenterCb;

public class SoftwareSettingActiyPresenter implements
		ISoftwareSettingActiyPresenterCb {

	private static final String FILE_SYS_CONFIG_INFO_NAME = "sys_config_info.xml";
	private static final String INTENT_SOFTWARE_AND_SERVER_SETTING_SERVER_ADDRESS = "software_and_server_setting_server_address";

	private Context mContext;
	private ISoftwareSettingActivity mIView;

	private SysConfigInfo mSysConfigInfo;

	public SoftwareSettingActiyPresenter(ISoftwareSettingActivity view,
			Context context) {
		mIView = view;
		mContext = context;
	}

	public void init() {
		mSysConfigInfo = readSysConfigFile();

		String serverAddress = null;
		if (mSysConfigInfo != null) {
			serverAddress = mSysConfigInfo.getWebServiceURL();
		}

		if (serverAddress != null) {
			String string = getCompleteAddressToShow(serverAddress);
			mIView.showServerAddress(string);
		}
	}

	private SysConfigInfo readSysConfigFile() {
		SysConfigInfo sysConfigInfo = null;

		File file = mContext.getFileStreamPath(FILE_SYS_CONFIG_INFO_NAME);
		if (!file.exists()) {
			return sysConfigInfo;
		}

		FileInputStream fileInputStream = null;
		String xmlString = null;
		try {
			fileInputStream = new FileInputStream(file);
			int length = (int) file.length();
			byte[] bytes = new byte[length];

			fileInputStream.read(bytes);
			xmlString = new String(bytes);
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (xmlString == null) {
			return sysConfigInfo;
		}

		try {
			List<?> list = XmlParser.parse(xmlString,
					Constants.XmlType.SYS_CONFIG_INFO);
			if ((list != null) && (list.size() != 0)) {
				Object object = list.get(0);
				sysConfigInfo = (SysConfigInfo) object;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sysConfigInfo;
	}

	private String getCompleteAddressToShow(String pureAddress) {
		String moreIdentifier = mContext.getString(R.string.more_identifier);
		String string = pureAddress + " " + moreIdentifier;
		return string;
	}

	public void doSwitchLoginSettingActivity(Context context,
			Class<? extends Activity> targetActivity) {
		// mMyXmppCallback.setMainActiyCallback(null);

		switchActivity(context, targetActivity, null);
	}

	public void doSwitchSoftwareServerSettingActivity(Context context,
			Class<? extends Activity> targetActivity) {
		// mMyXmppCallback.setMainActiyCallback(null);

		Bundle bundle = new Bundle();
		bundle.putParcelable(INTENT_SOFTWARE_AND_SERVER_SETTING_SERVER_ADDRESS,
				mSysConfigInfo);

		switchActivity(context, targetActivity, bundle);
	}

	private void switchActivity(Context context,
			Class<? extends Activity> targetActivity, Bundle bundle) {
		Activity activity = (Activity) context;
		Intent intent = new Intent();
		intent.setClass(activity, targetActivity);

		if (bundle != null) {
			intent.putExtras(bundle);
		}

		ActivityUtil.switchTo(activity, intent);
		activity.finish();
	}

	@Override
	public void testPresenter() {
		// TODO Auto-generated method stub

	}
}
