package com.neekle.kunlunandroid.presenter.common;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.neekle.kunlunandroid.common.Constants;
import com.neekle.kunlunandroid.data.AddressBookNode;

class AddsbookXmlContentResolver {
	private static List<Object> mList;

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
		if (parser.getName().equals("xCard")) {
			listItem = new AddressBookNode();

			String jid = parser.getAttributeValue(null, "JID");
			String id = parser.getAttributeValue(null, "ID");
			String name = parser.getAttributeValue(null, "Name");

			listItem.setJid(jid);
			listItem.setParentId(null);
			listItem.setParentType(Constants.DEF_INT_VALUE);
			listItem.setId(id);
			listItem.setName(name);
			listItem.setType(Constants.AddsbookNodeType.XCARD);
		}

		return listItem;
	}

	private static void setAddsbookInfoEndTag(XmlPullParser parser,
			AddressBookNode listItem) {
		if (parser.getName().equals("xCard")) {
			if (listItem != null) {
				mList.add(listItem);
			}
		}
	}

}
