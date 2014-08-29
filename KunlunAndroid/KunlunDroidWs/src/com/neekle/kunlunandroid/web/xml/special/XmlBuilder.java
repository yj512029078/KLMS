package com.neekle.kunlunandroid.web.xml.special;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import org.xmlpull.v1.XmlSerializer;

import com.neekle.kunlunandroid.web.data.TypeContact;

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
	public static String buildXML(List<TypeContact> contacts) {
		String xmlString = null;

		try {
			XmlSerializer serializer = Xml.newSerializer();
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			serializer.setOutput(byteArrayOutputStream, "utf-8");

			serializer.startDocument("utf-8", true);
			serializer.startTag(null, "MRUContacts");
			serializer.attribute("", "xsi:noNamespaceSchemaLocation",
					"MRUContacts.xsd");
			serializer.attribute("", "xmlns:xsi",
					"http://www.w3.org/2001/XMLSchema-instance");

			for (TypeContact contact : contacts) {
				serializer.startTag(null, "Contact");
				String jid = contact.getJid();
				serializer.attribute(null, "JID", jid);
				String points = contact.getPoints();
				serializer.attribute(null, "Points", points);

				serializer.endTag(null, "Contact");
			}

			serializer.endTag(null, "MRUContacts");
			serializer.endDocument();
			byteArrayOutputStream.close();
			xmlString = byteArrayOutputStream.toString();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return xmlString;
	}
}