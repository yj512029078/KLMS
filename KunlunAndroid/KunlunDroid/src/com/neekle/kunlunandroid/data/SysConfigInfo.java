package com.neekle.kunlunandroid.data;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class SysConfigInfo implements Parcelable {
	private String updateHttp;
	private String webServiceURL;
	private ArrayList<XmppURL> xmppURLList;

	public String getUpdateHttp() {
		return updateHttp;
	}

	public void setUpdateHttp(String updateHttp) {
		this.updateHttp = updateHttp;
	}

	public String getWebServiceURL() {
		return webServiceURL;
	}

	public void setWebServiceURL(String webServiceURL) {
		this.webServiceURL = webServiceURL;
	}

	public ArrayList<XmppURL> getXmppURLList() {
		return xmppURLList;
	}

	public void setXmppURLList(ArrayList<XmppURL> xmppURLList) {
		this.xmppURLList = xmppURLList;
	}

	public static class XmppURL implements Parcelable {
		private String ip;
		private String port;
		private String type;
		private String xh;

		public XmppURL() {

		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public String getPort() {
			return port;
		}

		public void setPort(String port) {
			this.port = port;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getXh() {
			return xh;
		}

		public void setXh(String xh) {
			this.xh = xh;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		public static final Parcelable.Creator<XmppURL> CREATOR = new Creator<XmppURL>() {
			public XmppURL createFromParcel(Parcel source) {
				XmppURL data = new XmppURL();
				data.ip = source.readString();
				data.port = source.readString();
				data.type = source.readString();
				data.xh = source.readString();

				return data;
			}

			public XmppURL[] newArray(int size) {
				return new XmppURL[size];
			}
		};

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(ip);
			dest.writeString(port);
			dest.writeString(type);
			dest.writeString(xh);
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<SysConfigInfo> CREATOR = new Creator<SysConfigInfo>() {
		public SysConfigInfo createFromParcel(Parcel source) {
			SysConfigInfo data = new SysConfigInfo();
			data.updateHttp = source.readString();
			data.webServiceURL = source.readString();
			data.xmppURLList = new ArrayList<XmppURL>();
			source.readList(data.xmppURLList, XmppURL.class.getClassLoader());

			return data;
		}

		public SysConfigInfo[] newArray(int size) {
			return new SysConfigInfo[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(updateHttp);
		dest.writeString(webServiceURL);
		dest.writeList(xmppURLList);
	}
}
