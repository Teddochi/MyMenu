package com.Cse110.model;

import com.parse.*;

@ParseClassName("Restaurant")
public class Restaurant extends ParseObject {

	public Restaurant() {
		// A default constructor is required.
	}

	public String getID() {
		return getObjectId();
	}

	public String getName() {
		return getString("RestaurantName");
	}

	public void setName(String title) {
		put("RestaurantName", title);
	}

	public String getCategory() {
		return getString("Category");
	}

	public void setCategory(String category) {
		put("Category", category);
	}

	public ParseFile getPicture() {
		return getParseFile("RestaurantPicture");
	}

	public void setPicture(ParseFile pic) {
		put("RestaurantPicture", pic);
	}

	public String getDescription() {
		return getString("Description");
	}

	public void setDescription(String des) {
		put("Description", des);
	}

	public String getHours() {
		return getString("Hours");
	}

	public void setHours(String hours) {
		put("Hours", hours);
	}

	public String getPhone() {
		return getString("Phone");
	}

	public void setPhone(String phone) {
		put("Phone", phone);
	}

	public String getAddress() {
		return getString("Address");
	}

	public void setAddress(String add) {
		put("Address", add);
	}
}