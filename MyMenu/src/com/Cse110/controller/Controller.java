package com.Cse110.controller;

import com.Cse110.model.Dish;
import com.Cse110.model.Rating;
import com.Cse110.model.Restaurant;
import com.Cse110.model.Reviews;
import com.Cse110.model.User;

public class Controller {

	private Dish currentDish;
	private User currentUser;
	private Restaurant currentRestaurant;
	private Reviews currentReviews;
	private Rating currentRating;
	
	public Restaurant getCurrentRestaurant() {
		return currentRestaurant;
	}

	public void setCurrentRestaurant(Restaurant currentRestaurant) {
		this.currentRestaurant = currentRestaurant;
	}

	public Reviews getCurrentReviews() {
		return currentReviews;
	}

	public void setCurrentReviews(Reviews currentReviews) {
		this.currentReviews = currentReviews;
	}

	public Rating getCurrentRating() {
		return currentRating;
	}

	public void setCurrentRating(Rating currentRating) {
		this.currentRating = currentRating;
	}

	public Dish getCurrentDish() {
		return currentDish;
	}

	public void setCurrentDish(Dish currentDish) {
		this.currentDish = currentDish;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

}
