package com.neekle.kunlunandroid.presenter.common;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.data.AddressBookNode;

class AddsbookXmlLevelResolver {
	private static List<Object> mList;

	private static String mRootIdName;
	private static String mRootIdValue;

	private static int mRootDepth;
	private static boolean mIsCanStartToResolve;
	private static String mRootId;
	private static int mRootType;
	private static boolean mIsShouldStopResolve;

	public static List<?> parse(String xml, int xmlType, String rootIdName,
			String rootIdValue) throws Exception {
		if (mList != null) {
			mList.clear();
			mList = null;
		}
		mRootIdName = rootIdName;
		mRootIdValue = rootIdValue;
		mRootDepth = Constants.DEF_INT_VALUE;
		mIsCanStartToResolve = false;
		mRootId = null;
		mRootType = Constants.DEF_INT_VALUE;
		mIsShouldStopResolve = false;

		List<?> list = parse(xml, xmlType);
		return list;
	}

	public static List<?> parse(String xml, int xmlType) throws Exception {
		mList = null;
		XmlPullParser parser = setXmlParser(xml);

		AddressBookNode addressBookNode = null;

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT: {
				if (xmlType == Constants.XmlType.ADDSBOOK) {
					setAddsbookInfoStartDocu();
				}

				break;
			}
			case XmlPullParser.START_TAG: {
				if (xmlType == Constants.XmlType.ADDSBOOK) {
					addressBookNode = setAddsbookInfoStartTag(parser,
							addressBookNode);
				}

				break;
			}
			case XmlPullParser.END_TAG: {
				if (xmlType == Constants.XmlType.ADDSBOOK) {
					setAddsbookInfoEndTag(parser, addressBookNode);
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

	private static void setAddsbookInfoStartDocu() {
		mList = new ArrayList<Object>();
	}

	private static AddressBookNode setAddsbookInfoStartTag(
			XmlPullParser parser, AddressBookNode listItem) throws Exception {
		if (mIsShouldStopResolve) {
			return listItem;
		}

		if (!mIsCanStartToResolve) {
			mIsCanStartToResolve = getIsCanStartToResolve(parser);

			if (mIsCanStartToResolve) {
				mRootDepth = parser.getDepth();
				mRootId = parser.getAttributeValue(null, "ID");
				String nodeName = parser.getName();
				mRootType = getType(nodeName);
			}
		}

		if (!mIsCanStartToResolve) {
			return listItem;
		}

		int nowDepth = parser.getDepth();
		if (nowDepth != (mRootDepth + 1)) {
			return listItem;
		}

		String id = parser.getAttributeValue(null, "ID");
		String name = parser.getAttributeValue(null, "Name");

		if (parser.getName().equals("AddressBook")) {
			listItem = new AddressBookNode();

			listItem.setParentId(mRootId);
			listItem.setParentType(mRootType);
			listItem.setId(id);
			listItem.setName(name);
			listItem.setType(Constants.AddsbookNodeType.ADDSBOOK);
		} else if (parser.getName().equals("Folder")) {
			listItem = new AddressBookNode();

			listItem.setParentId(mRootId);
			listItem.setParentType(mRootType);
			listItem.setId(id);
			listItem.setName(name);
			listItem.setType(Constants.AddsbookNodeType.FOLDER);
		} else if (parser.getName().equals("xCard")) {
			listItem = new AddressBookNode();

			String jid = parser.getAttributeValue(null, "JID");

			listItem.setJid(jid);
			listItem.setParentId(mRootId);
			listItem.setParentType(mRootType);
			listItem.setId(id);
			listItem.setName(name);
			listItem.setType(Constants.AddsbookNodeType.XCARD);
		}

		return listItem;
	}

	private static int getType(String name) {
		int type = Constants.AddsbookNodeType.ADDSBOOK;
		if (name.equals("AddressBook")) {
			type = Constants.AddsbookNodeType.ADDSBOOK;
		} else if (name.equals("Folder")) {
			type = Constants.AddsbookNodeType.FOLDER;
		} else if (name.equals("xCard")) {
			type = Constants.AddsbookNodeType.XCARD;
		}

		return type;
	}

	private static boolean getIsCanStartToResolve(XmlPullParser parser) {
		boolean isCanStartToResolve = false;

		String idAttriValue = parser.getAttributeValue(null, mRootIdName);
		if ((idAttriValue != null) && (idAttriValue.equals(mRootIdValue))) {
			isCanStartToResolve = true;
		}

		return isCanStartToResolve;
	}

	private static void setAddsbookInfoEndTag(XmlPullParser parser,
			AddressBookNode listItem) {
		if (mIsShouldStopResolve) {
			return;
		}

		int depth = parser.getDepth();
		if (depth == (mRootDepth + 1)) {
			if (listItem != null) {
				Log.i("listItemtest",
						"listItem post endTag " + parser.getName());
				mList.add(listItem);
			}
		} else if (depth == mRootDepth) {
			mIsShouldStopResolve = true;
		}

	}
}
