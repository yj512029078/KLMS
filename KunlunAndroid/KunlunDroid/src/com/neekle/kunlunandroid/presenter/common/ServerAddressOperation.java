package com.neekle.kunlunandroid.presenter.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.common.XmlParser;
import com.neekle.kunlunandroid.data.SysConfigInfo;
import com.neekle.kunlunandroid.util.StringOperator;
import com.neekle.kunlunandroid.web.data.TypeServerAddress;

public class ServerAddressOperation {

	private static final String SERVER_ADDRESS_SPLIT_IDENTIFIER = ":";
	private static final String FILE_SYS_CONFIG_INFO_NAME = "sys_config_info.xml";

	public static String getHostIp(String serverAddress) {
		String string = Constants.EMPTY_STRING;
		int index = serverAddress.lastIndexOf(SERVER_ADDRESS_SPLIT_IDENTIFIER);
		if (index != -1) {
			string = serverAddress.substring(0, index);
		}

		return string;
	}

	public static int getPort(String serverAddress) {
		int port = Constants.DEF_INT_VALUE;
		String string = Constants.EMPTY_STRING;
		int index = serverAddress.lastIndexOf(SERVER_ADDRESS_SPLIT_IDENTIFIER);
		if (index != -1) {
			string = serverAddress.substring(index + 1);
		}

		if (string != null) {
			boolean isNumeric = StringOperator.isNumeric(string);
			if (isNumeric) {
				port = Integer.valueOf(string);
			}
		}

		return port;
	}

	public static String readServerAddressFromSysConfigFile(Context context) {
		String serverAddress = null;
		SysConfigInfo sysConfigInfo = null;

		File file = context.getFileStreamPath(FILE_SYS_CONFIG_INFO_NAME);
		if (!file.exists()) {
			return serverAddress;
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
			return serverAddress;
		}

		try {
			List<?> list = XmlParser.parse(xmlString,
					Constants.XmlType.SYS_CONFIG_INFO);
			if ((list != null) && (list.size() != 0)) {
				Object object = list.get(0);
				sysConfigInfo = (SysConfigInfo) object;
				serverAddress = sysConfigInfo.getWebServiceURL();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return serverAddress;
	}

	public static TypeServerAddress getTypeXmppServerAddress(
			ArrayList<TypeServerAddress> list) {
		TypeServerAddress data = null;

		if (list == null) {
			return data;
		}

		int size = list.size();
		if (size == 0) {
			return data;
		}

		for (int i = 0; i < size; i++) {
			TypeServerAddress typeServerAddress = list.get(i);
			String code = typeServerAddress.getServerTypeCode();

			if ((code != null) && (code.equals(Constants.ServerTypeCode.XMPP))) {
				data = typeServerAddress;
				break;
			}
		}

		return data;
	}

	public static TypeServerAddress getTypeSipServerAddress(
			ArrayList<TypeServerAddress> list) {
		TypeServerAddress data = null;

		if (list == null) {
			return data;
		}

		int size = list.size();
		if (size == 0) {
			return data;
		}

		for (int i = 0; i < size; i++) {
			TypeServerAddress typeServerAddress = list.get(i);
			String code = typeServerAddress.getServerTypeCode();

			if ((code != null) && (code.equals(Constants.ServerTypeCode.SIP))) {
				data = typeServerAddress;
				break;
			}
		}

		return data;
	}

	public static String getProperHostIp(TypeServerAddress typeServerAddress) {
		String hostIp = Constants.EMPTY_STRING;
		if (typeServerAddress == null) {
			return hostIp;
		}

		hostIp = typeServerAddress.getServerAddress();
		if (hostIp == null) {
			hostIp = Constants.EMPTY_STRING;
		}

		return hostIp;
	}

	public static int getProperHostPort(TypeServerAddress typeServerAddress) {
		int serverPort = Constants.DEF_INT_VALUE;
		if (typeServerAddress == null) {
			return serverPort;
		}

		String serverPortString = typeServerAddress.getServerPort();
		if ((serverPortString != null)
				&& (!serverPortString.equals(Constants.EMPTY_STRING))) {
			serverPort = Integer.valueOf(serverPortString);
		}

		return serverPort;
	}
}
