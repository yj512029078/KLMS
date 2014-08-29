package com.neekle.kunlunandroid.web.data.friend;

public class TypeUserOrg {

	private int cityId;
	private String cityName;
	private int current;
	// dateTime to String
	private String dateEnd;
	// dateTime to String
	private String dateStart;
	private String department;
	private String employeeNo;
	private String orgJid;
	private String userEntid;

	public TypeUserOrg() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TypeUserOrg(int cityId, String cityName, int current,
			String dateEnd, String dateStart, String department,
			String employeeNo, String orgJid, String userEntid) {
		super();
		this.cityId = cityId;
		this.cityName = cityName;
		this.current = current;
		this.dateEnd = dateEnd;
		this.dateStart = dateStart;
		this.department = department;
		this.employeeNo = employeeNo;
		this.orgJid = orgJid;
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

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getOrgJid() {
		return orgJid;
	}

	public void setOrgJid(String orgJid) {
		this.orgJid = orgJid;
	}

	public String getUserEntid() {
		return userEntid;
	}

	public void setUserEntid(String userEntid) {
		this.userEntid = userEntid;
	}

}
