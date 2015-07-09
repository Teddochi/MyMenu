package com.Cse110.model;

import com.parse.*;

@ParseClassName("UserAccount")
public class User extends ParseObject {

	public User() {
		// A default constructor is required.
	}

	public String getID() {
		return getObjectId();
	}

	public String getName() {
		return getString("UserName");
	}

	public void setName(String title) {
		put("UserName", title);
	}

	public String getPassword() {
		return getString("Password");
	}

	public void setPassword(String pw) {
		put("Password", pw);
	}
}