package com.neekle.kunlunandroid.data;

import android.graphics.Bitmap;

public class CellMicroBlogData {

	private int userState;
	private Bitmap userPhoto;
	private String name;
	private String publishTime;
	private String publishCity;
	private int microBlogState;
	private String microBlogContent;
	private String microBlogStateDescri;
	private String device;

	public CellMicroBlogData() {
		// TODO Auto-generated constructor stub
	}

	public CellMicroBlogData(int userState, Bitmap userPhoto, String name,
			String publishTime, String publishCity, int microBlogState,
			String microBlogContent, String microBlogStateDescri, String device) {
		super();
		this.userState = userState;
		this.userPhoto = userPhoto;
		this.name = name;
		this.publishTime = publishTime;
		this.publishCity = publishCity;
		this.microBlogState = microBlogState;
		this.microBlogContent = microBlogContent;
		this.microBlogStateDescri = microBlogStateDescri;
		this.device = device;
	}

	public int getUserState() {
		return userState;
	}

	public void setUserState(int userState) {
		this.userState = userState;
	}

	public Bitmap getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(Bitmap userPhoto) {
		this.userPhoto = userPhoto;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getPublishCity() {
		return publishCity;
	}

	public void setPublishCity(String publishCity) {
		this.publishCity = publishCity;
	}

	public int getMicroBlogState() {
		return microBlogState;
	}

	public void setMicroBlogState(int microBlogState) {
		this.microBlogState = microBlogState;
	}

	public String getMicroBlogContent() {
		return microBlogContent;
	}

	public void setMicroBlogContent(String microBlogContent) {
		this.microBlogContent = microBlogContent;
	}

	public String getMicroBlogStateDescri() {
		return microBlogStateDescri;
	}

	public void setMicroBlogStateDescri(String microBlogStateDescri) {
		this.microBlogStateDescri = microBlogStateDescri;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

}
