package com.neekle.kunlunandroid.web.data.friend;


public class TypeFriendDetails {

	private TypeAddsbook addbooks;
	private TypeUser userInfor;
	private TypeUserOrg userOrgs;
	private TypeUserSchool userSchools;

	public TypeFriendDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TypeFriendDetails(TypeAddsbook addbooks, TypeUser userInfor,
			TypeUserOrg userOrgs, TypeUserSchool userSchools) {
		super();
		this.addbooks = addbooks;
		this.userInfor = userInfor;
		this.userOrgs = userOrgs;
		this.userSchools = userSchools;
	}

	public TypeAddsbook getAddbooks() {
		return addbooks;
	}

	public void setAddbooks(TypeAddsbook addbooks) {
		this.addbooks = addbooks;
	}

	public TypeUser getUserInfor() {
		return userInfor;
	}

	public void setUserInfor(TypeUser userInfor) {
		this.userInfor = userInfor;
	}

	public TypeUserOrg getUserOrgs() {
		return userOrgs;
	}

	public void setUserOrgs(TypeUserOrg userOrgs) {
		this.userOrgs = userOrgs;
	}

	public TypeUserSchool getUserSchools() {
		return userSchools;
	}

	public void setUserSchools(TypeUserSchool userSchools) {
		this.userSchools = userSchools;
	}

}
