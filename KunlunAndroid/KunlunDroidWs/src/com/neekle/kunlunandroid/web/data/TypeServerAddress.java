package com.neekle.kunlunandroid.web.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class TypeServerAddress implements Parcelable {
	private String serverAddress;
	private String serverPort;
	private String serverTypeCode;
	private String serverTypeName;

	public TypeServerAddress() {
		super();
	}

	public TypeServerAddress(String serverAddress, String serverPort,
			String serverTypeCode, String serverTypeName) {
		super();
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.serverTypeCode = serverTypeCode;
		this.serverTypeName = serverTypeName;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	public String getServerTypeCode() {
		return serverTypeCode;
	}

	public void setServerTypeCode(String serverTypeCode) {
		this.serverTypeCode = serverTypeCode;
	}

	public String getServerTypeName() {
		return serverTypeName;
	}

	public void setServerTypeName(String serverTypeName) {
		this.serverTypeName = serverTypeName;
	}

	public static final Parcelable.Creator<TypeServerAddress> CREATOR = new Creator<TypeServerAddress>() {
		public TypeServerAddress createFromParcel(Parcel source) {
			TypeServerAddress data = new TypeServerAddress();
			data.serverAddress = source.readString();
			data.serverPort = source.readString();
			data.serverTypeCode = source.readString();
			data.serverTypeName = source.readString();

			return data;
		}

		public TypeServerAddress[] newArray(int size) {
			return new TypeServerAddress[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(serverAddress);
		dest.writeString(serverPort);
		dest.writeString(serverTypeCode);
		dest.writeString(serverTypeName);
	}

}
