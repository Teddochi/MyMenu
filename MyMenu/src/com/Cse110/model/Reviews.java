package com.Cse110.model;

import com.parse.*;

@ParseClassName("Reviews")
public class Reviews extends ParseObject {

	public Reviews() {
		// A default constructor is required.
	}

	public String getID() {
		return getObjectId();
	}

	public String getName() {
		return getString("Name");
	}
	
	public void setName(String n){
		put("Name", n);
	}

	public String getDescription() {
		return getString("ReviewDescription");
	}
	
	public void setDescription(String n){
		put("ReviewDescription", n);
	}

	public String getDishId() {
		return getString("dishID");
	}
	
	public void setDishId(String n){
		put("dishID", n);
	}
	
	public String getUserId(){
		return getString("userID");
	}
	
	public void setUserId(String n){
		put("userID", n);
	}


}
