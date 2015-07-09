/*package com.parse.starter;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import com.parse.ParseUser;

import android.app.Application;
import android.util.Log;

public class ParseApplication extends Application {

	private String AppId = "PFeVg2evGtAWYkKlIe11myOGl2Wsw4bpygimtWcT";
	private String ClientKey = "AmbMohWlGph7KEZfZMl4LnbVBj8M20fThtOIzn7P";
	private ArrayList<ParseObject> list = new ArrayList<ParseObject>();
	
	@Override
	public void onCreate() {
		super.onCreate();

		// Add your initialization code here
		Parse.initialize(this, AppId, ClientKey);


		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
	}
	
	public ArrayList<ParseObject> retrieveByName(String name)
	{
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Restaurant");    //Table to get data from
	    query.whereContains("RestaurantName", name);                          //Retrieve restaurants where name has search
	    
	    ArrayList<ParseObject> l = null;                                     //Store results from query
		try {
			l = (ArrayList<ParseObject>) query.find();                       //Send query to database
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	    Log.d("score", "got " + l.size() + " restaurants");
		return l;
	}
	
	/*
	 * To retrieve list of dishes from restaurant,
	 * find selected restaurant's object id
	 * Something like:
	 * ParseQuery<ParseObject> query = ParseQuery.getQuery("Dish")
	 * query.whereEquals("ObjectID", <SelectedRestaurantObjectId>) *obtain with parse_object.getObjectId()
	 * query.find()
	 *//*

}

*/

package com.Cse110.model;

import java.util.ArrayList;
import java.util.List;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Application;
import android.util.Log;

public class ParseApplication extends Application {

	private String AppId = "PFeVg2evGtAWYkKlIe11myOGl2Wsw4bpygimtWcT";
	private String ClientKey = "AmbMohWlGph7KEZfZMl4LnbVBj8M20fThtOIzn7P";
	
	@Override
	public void onCreate() {
		super.onCreate();

		// Add your initialization code here
		Parse.initialize(this, AppId, ClientKey);


		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
	}
	
	public ArrayList<ParseObject> retrieveByName(String name)
	{
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Restaurant");    //Table to get data from
	    query.whereContains("RestaurantName", name);                          //Retrieve restaurants where name has search
	    
	    ArrayList<ParseObject> l = null;                                     //Store results from query
		try {
			l = (ArrayList<ParseObject>) query.find();                       //Send query to database
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	    Log.d("score", "got " + l.size() + " restaurants");
		return l;
	}
	
	public ParseObject retrieveFavById(String ID)
	{
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");    //Table to get data from
	    query.whereContains("resID", ID);                          //Retrieve restaurants where name has search
	    query.whereContains("UserID", com.Cse110.view.LoginActivity.user.getString("UserName"));

	    ArrayList<ParseObject> l = null;                                     //Store results from query
		try {
			l = (ArrayList<ParseObject>) query.find();                       //Send query to database
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	    Log.d("score", "got " + l.size() + " favorites");
	    
	    System.out.println("OBJECT ID TO BE DELETED IS " + l.get(0).getObjectId());
		return l.get(0);
	}
	
	public ParseObject retrieveById(String id)
	{
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Restaurant");    //Table to get data from
	    query.whereEqualTo("objectId", id);                          //Retrieve restaurants where name has search
	    
	    ArrayList<ParseObject> list = null;                                     //Store results from query
		try {
			list = (ArrayList<ParseObject>) query.find();                       //Send query to database
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	    //Log.d("score", "got " + l.size() + " restaurants");
		return list.get(0);
	}
	
	public ArrayList<ParseObject> retrieveFavorites(String uid)
	{
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");    //Table to get data from
	    query.whereEqualTo("UserID", uid);                          //Retrieve restaurants where name has search
	    
	    ArrayList<ParseObject> list = null;                                     //Store results from query
		try {
			list = (ArrayList<ParseObject>) query.find();                       //Send query to database
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return list;
	}
	

	public ArrayList<ParseObject> retrieveDish(String id)
	{
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Dish");    //Table to get data from
	    query.whereEqualTo("RestaurantId", id);                //Retrieve dishes from restaurant
	    
	    ArrayList<ParseObject> l = null;                                     //Store results from query
		try {
			l = (ArrayList<ParseObject>) query.find();                       //Send query to database
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	    Log.d("score", "got " + l.size() + " dishes");
		return l;
	}

	public List<ParseObject> retrieveDishes(String id)
	{
		// Locate the class table named "Dish" in Parse.com
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                 "Dish");
        query.orderByAscending("Category");
        
        // Retrieve only the dishes from the selected restaurant.
		query.whereContains("RestaurantId", id);
		
		List<ParseObject> l = null;
		try {
            l = query.find();
        } catch (ParseException e) {
             e.printStackTrace();
        }
		Log.d("score", "got " + l.size() + " dishes");
        return l;
	}
}
