package com.Cse110.model;

import com.parse.*;

@ParseClassName("Dish")
public class Dish extends ParseObject {

	public Dish() {
		// A default constructor is required.
	}

	public String getID() {
		return getObjectId();
	}

	public String getName() {
		return getString("DishName");
	}

	public String getCategory() {
		return getString("Category");
	}

	public ParseFile getPicture() {
		return getParseFile("Picture");
	}

	public String getDescription() {
		return getString("Description");
	}

	public String getRestaurantId() {
		return getString("RestaurantId");
	}

	public int getCalories() {
		return (Integer) getNumber("Calories");
	}

	public double getPrice() {
		return (Double) getNumber("Price");
	}

	public int getCarbs() {
		return (Integer) getNumber("Carbs");
	}

	public int getProtein() {
		return (Integer) getNumber("Protein");
	}

	public int getFat() {
		return (Integer) getNumber("Fat");
	}

	public String getNotes() {
		return getString("Notes");
	}
}
