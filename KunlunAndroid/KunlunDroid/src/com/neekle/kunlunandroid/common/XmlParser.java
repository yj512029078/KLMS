package com.neekle.kunlunandroid.common;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.neekle.kunlunandroid.data.AddressBookNode;
import com.neekle.kunlunandroid.data.SysConfigInfo;
import com.neekle.kunlunandroid.web.common.WebserviceConstants;
import com.neekle.kunlunandroid.web.data.TypeCircle;
import com.neekle.kunlunandroid.web.data.friend.FriendInfo;

public class XmlParser {

	private static List<Object> mList;

	public static List<?> parse(String xml, int xmlType) throws Exception {
		mList = null;
		XmlPullParser parser = setXmlParser(xml);

		SysConfigInfo sysConfigInfo = null;

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT: {
				if (xmlType == Constants.XmlType.SYS_CONFIG_INFO) {
					setCategoryStartDocu();
				}

				break;
			}
			case XmlPullParser.START_TAG: {
				if (xmlType == Constants.XmlType.SYS_CONFIG_INFO) {
					sysConfigInfo = setSysConfigInfoStartTag(parser,
							sysConfigInfo);
				}

				break;
			}
			case XmlPullParser.END_TAG: {
				if (xmlType == Constants.XmlType.SYS_CONFIG_INFO) {
					setSysConfigInfoEndTag(parser, sysConfigInfo);
				}

				break;
			}
			}

			eventType = parser.next();
		}

		return mList;
	}

	private static XmlPullParser setXmlParser(String xml) throws Exception {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser parser = factory.newPullParser();

		StringReader stringReader = new StringReader(xml);
		parser.setInput(stringReader);

		return parser;
	}

	private static void setCategoryStartDocu() {
		mList = new ArrayList<Object>();
	}

	private static SysConfigInfo setSysConfigInfoStartTag(XmlPullParser parser,
			SysConfigInfo listItem) throws Exception {

		if (parser.getName().equals("SysConfig")) {
			listItem = new SysConfigInfo();
		} else if (parser.getName().equals("UpdateHttp")) {
			listItem.setUpdateHttp(parser.nextText());
		} else if (parser.getName().equals("WebServiceURL")) {
			listItem.setWebServiceURL(parser.nextText());
		}

		return listItem;
	}

	private static void setSysConfigInfoEndTag(XmlPullParser parser,
			SysConfigInfo listItem) {
		if (parser.getName().equals("SysConfig")) {
			mList.add(listItem);
		}
	}

}
