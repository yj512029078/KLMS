package com.neekle.kunlunandroid.web.data.friend;

public class TypeUserSchool {

	private int cityId;
	private String cityName;
	private String classHuddleJid;
	private int current;
	// dateTime to String
	private String dateEnd;
	// dateTime to String
	private String dateStart;
	private int educationId;
	private String educationName;
	private String schoolJid;
	private String userEntid;

	public TypeUserSchool() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TypeUserSchool(int cityId, String cityName, String classHuddleJid,
			int current, String dateEnd, String dateStart, int educationId,
			String educationName, String schoolJid, String userEntid) {
		super();
		this.cityId = cityId;
		this.cityName = cityName;
		this.classHuddleJid = classHuddleJid;
		this.current = current;
		this.dateEnd = dateEnd;
		this.dateStart = dateStart;
		this.educationId = educationId;
		this.educationName = educationName;
		this.schoolJid = schoolJid;
		this.userEntid = userEntid;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getClassHuddleJid() {
		return classHuddleJid;
	}

	public void setClassHuddleJid(String classHuddleJid) {
		this.classHuddleJid = classHuddleJid;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getDateStart() {
		return dateStart;
	}

	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}

	public int getEducationId() {
		return educationId;
	}

	public void setEducationId(int educationId) {
		this.educationId = educationId;
	}

	public String getEducationName() {
		return educationName;
	}

	public void setEducationName(String educationName) {
		this.educationName = educationName;
	}

	public String getSchoolJid() {
		return schoolJid;
	}

	public void setSchoolJid(String schoolJid) {
		this.schoolJid = schoolJid;
	}

	public String getUserEntid() {
		return userEntid;
	}

	public void setUserEntid(String userEntid) {
		this.userEntid = userEntid;
	}

}
