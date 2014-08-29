package com.neekle.kunlunandroid.common;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlSerializer;

import com.neekle.kunlunandroid.data.SysConfigInfo;
import com.neekle.kunlunandroid.data.SysConfigInfo.XmppURL;
import com.neekle.kunlunandroid.web.data.TypeContact;

import android.provider.ContactsContract.Contacts.Data;
import android.util.Xml;

/**
 * 采用PULL 生成XML数据
 * 
 * @author Administrator
 * 
 */
public class XmlBuilder {
	/**
	 * 
	 * @param persons
	 * @param outputStream
	 * @throws Exception
	 */
	public static String buildXML(SysConfigInfo sysConfigInfo) {
		String xmlString = null;

		try {
			XmlSerializer serializer = Xml.newSerializer();
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			serializer.setOutput(byteArrayOutputStream, "utf-8");

			serializer.startDocument("utf-8", true);
			serializer.startTag(null, "SysConfig");

			serializer.startTag(null, "UpdateHttp");
			String updateHttpUrl = sysConfigInfo.getUpdateHttp();
			updateHttpUrl = getProperData(updateHttpUrl);
			serializer.text(updateHttpUrl);
			serializer.endTag(null, "UpdateHttp");

			serializer.startTag(null, "WebServiceURL");
			String webservicepUrl = sysConfigInfo.getWebServiceURL();
			webservicepUrl = getProperData(webservicepUrl);
			serializer.text(webservicepUrl);
			serializer.endTag(null, "WebServiceURL");

			serializer.startTag(null, "XmppURLs");
			ArrayList<XmppURL> xmppURLs = sysConfigInfo.getXmppURLList();
			if (xmppURLs != null) {
				for (XmppURL xmppURL : xmppURLs) {
					serializer.startTag(null, "item");

					serializer.startTag(null, "IP");
					String ip = xmppURL.getIp();
					ip = getProperData(ip);
					serializer.text(ip);
					serializer.endTag(null, "IP");

					serializer.startTag(null, "Port");
					String port = xmppURL.getPort();
					port = getProperData(port);
					serializer.text(port);
					serializer.endTag(null, "Port");

					serializer.startTag(null, "Type");
					String type = xmppURL.getType();
					type = getProperData(type);
					serializer.endTag(null, "Type");

					serializer.startTag(null, "XH");
					String xh = xmppURL.getXh();
					xh = getProperData(xh);
					serializer.endTag(null, "XH");

					serializer.endTag(null, "item");
				}
			}
			serializer.endTag(null, "XmppURLs");

			serializer.endTag(null, "SysConfig");
			serializer.endDocument();
			byteArrayOutputStream.close();
			xmlString = byteArrayOutputStream.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return xmlString;
	}

	private static String getProperData(String string) {
		if (string == null) {
			string = Constants.EMPTY_STRING;
		}

		return string;
	}
}