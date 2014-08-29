package com.neekle.kunlunandroid.web.xml.special;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.R.string;

import com.neekle.kunlunandroid.web.common.WebserviceConstants;
import com.neekle.kunlunandroid.web.data.TypeCircle;
import com.neekle.kunlunandroid.web.data.friend.FriendInfo;

/**
 * Parser for data in xml format
 * 
 * @author yj
 * 
 */
public class XmlParser {

	private static List<Object> mList;

	public static List<?> parse(String xml, int xmlType) throws Exception {
		mList = null;
		XmlPullParser parser = setXmlParser(xml);

		FriendInfo friendItem = null;
		ArrayList<String> groupList = new ArrayList<String>();
		ArrayList<TypeCircle> circleList = new ArrayList<TypeCircle>();

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT: {
				if (xmlType == WebserviceConstants.WebserviceType.TYPE_XML_FRIENDS
						.ordinal()) {
					setCategoryStartDocu();
				}

				break;
			}
			case XmlPullParser.START_TAG: {
				if (xmlType == WebserviceConstants.WebserviceType.TYPE_XML_FRIENDS
						.ordinal()) {
					friendItem = setFriendsStartTag(parser, friendItem,
							groupList, circleList);
				}

				break;
			}
			case XmlPullParser.END_TAG: {
				if (xmlType == WebserviceConstants.WebserviceType.TYPE_XML_FRIENDS
						.ordinal()) {
					setFriendsEndTag(parser, friendItem, groupList, circleList);
				}

				break;
			}
			}

			eventType = parser.next();
		}

		return mList;
	}

	public static boolean parseResultCode(String xml) throws Exception {
		boolean flag = false;
		XmlPullParser parser = setXmlParser(xml);

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT: {
				break;
			}
			case XmlPullParser.START_TAG: {
				if (parser.getName().equals("result")) {
					if (parser.getAttributeCount() > 0) {
						String attriName = parser.getAttributeName(0);
						if (attriName.equals("type")) {
							String value = parser.getAttributeValue(0);
							if (value.equals("success")) {
								flag = true;
							} else {
								flag = false;
							}

							return flag;
						}
					}
				}

				break;
			}
			case XmlPullParser.END_TAG: {
				break;
			}
			}

			eventType = parser.next();
		}

		return flag;
	}

	public static String parseResultError(String xml) throws Exception {
		boolean successFlag = true;
		String errorText = null;
		XmlPullParser parser = setXmlParser(xml);

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT: {
				break;
			}
			case XmlPullParser.START_TAG: {
				if (parser.getName().equals("result")) {
					if (parser.getAttributeCount() > 0) {
						String attriName = parser.getAttributeName(0);
						if (attriName.equals("type")) {
							String value = parser.getAttributeValue(0);
							if (value.equals("success")) {
								successFlag = true;
							} else {
								successFlag = false;
							}

						}
					}
				}

				if ((!successFlag)
						&& (parser.getName().equals("error-message"))) {
					errorText = parser.nextText();
				}

				break;
			}
			case XmlPullParser.END_TAG: {
				break;
			}
			}

			eventType = parser.next();
		}

		return errorText;
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

	private static FriendInfo setFriendsStartTag(XmlPullParser parser,
			FriendInfo listItem, ArrayList<String> groupList,
			ArrayList<TypeCircle> circleList) throws Exception {
		if (parser.getName().equals("Friend")) {
			listItem = new FriendInfo();
		} else if (parser.getName().equals("FriendJID")) {
			listItem.setFriendJID(parser.nextText());
		} else if (parser.getName().equals("DisplayName")) {
			listItem.setDisplayName(parser.nextText());
		} else if (parser.getName().equals("Phone")) {
			listItem.setPhone(parser.nextText());
		} else if (parser.getName().equals("Mobile")) {
			listItem.setMobile(parser.nextText());
		} else if (parser.getName().equals("Email")) {
			listItem.setEmail(parser.nextText());
		} else if (parser.getName().equals("AddTime")) {
			listItem.setAddTime(parser.nextText());
		} else if (parser.getName().equals("AutoEnterRoom")) {
			listItem.setAddTime(parser.nextText());
		} else if (parser.getName().equals("Group")) {
			groupList.add(parser.nextText());
		} else if (parser.getName().equals("Circle")) {
			TypeCircle circle = constructCircle(parser);
			if (circle != null) {
				circleList.add(circle);
			}
		}

		return listItem;
	}

	private static TypeCircle constructCircle(XmlPullParser parser) {
		TypeCircle circle = null;
		int count = parser.getAttributeCount();

		if (count != 0) {
			circle = new TypeCircle();
		}

		for (int i = 0; i < count; i++) {
			String name = parser.getAttributeName(i);
			String value = parser.getAttributeValue(i);

			if (name.equals("ID")) {
				circle.setId(value);
			} else if (name.equals("Name")) {
				circle.setName(value);
			} else if (name.equals("TempletID")) {
				circle.setTempletId(value);
			} else if (name.equals("Comment")) {
				circle.setComment(value);
			}
		}

		return circle;
	}

	private static void setFriendsEndTag(XmlPullParser parser,
			FriendInfo listItem, ArrayList<String> groupList,
			ArrayList<TypeCircle> circleList) {
		if (parser.getName().equals("Groups")) {
			ArrayList<String> arrayList = (ArrayList<String>) groupList.clone();
			listItem.setGroupList(arrayList);
			groupList.clear();
		} else if (parser.getName().equals("Circles")) {
			ArrayList<TypeCircle> arrayList = (ArrayList<TypeCircle>) circleList
					.clone();
			listItem.setCircleList(arrayList);
			circleList.clear();
		} else if (parser.getName().equals("Friend")) {
			mList.add(listItem);
		}
	}

}
