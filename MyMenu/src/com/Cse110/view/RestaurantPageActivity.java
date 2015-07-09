package com.Cse110.view;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.Cse110.model.ParseApplication;
import com.Cse110.model.Restaurant;
import com.Cse110.model.User;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.Cse110.R;

public class RestaurantPageActivity extends Activity {


	private ParseApplication search = new ParseApplication();
	private Button menuButton;
	private String restaurantID, restaurantName;
	
	private Restaurant restaurant;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_page);	
		menuButton = (Button) findViewById(R.id.menu_button);
		
		restaurantID = com.Cse110.view.HomePageActivity.RId;
		restaurantName = com.Cse110.view.HomePageActivity.RName;
		ParseObject.registerSubclass(Restaurant.class);
		fillResPage((Restaurant) search.retrieveById(restaurantID));

		
	}
	
	public void fillResPage(final Restaurant restaurant)
	{
		
		TextView resNameTV = (TextView) findViewById(R.id.resName);
		resNameTV.setText(restaurant.getName());

		TextView resCategoryTV = (TextView) findViewById(R.id.resCategory);
		resCategoryTV.setText(restaurant.getCategory());

		TextView resDescriptionTV = (TextView) findViewById(R.id.resDescription);
		resDescriptionTV.setText(restaurant.getDescription());

		TextView resHoursTV = (TextView) findViewById(R.id.resHours);
		resHoursTV.setText(restaurant.getHours());

        
        /*Possibly add calling capability:
         * http://stackoverflow.com/questions/1556987/how-to-make-a-phone-call-in-android-and-come-back-to-my-activity-when-the-call-i
         */
        TextView resPhoneTV = (TextView) findViewById(R.id.resPhone);
		resPhoneTV.setText(restaurant.getPhone());

		ParseImageView resImageView = (ParseImageView) findViewById(R.id.resImage);
		resImageView.setVisibility(View.VISIBLE);
		ParseFile photoFile = restaurant.getPicture();
		if (photoFile != null) {
			resImageView.setParseFile(photoFile);
			resImageView.loadInBackground(new GetDataCallback() {
				@Override
				public void done(byte[] data, ParseException e) {
				}
			});
		}

        
        /*
         * Implement google maps capability:
         * http://stackoverflow.com/questions/5375654/how-to-implement-google-maps-search-by-address-in-android
         */
        TextView resAddressTV = (TextView) findViewById(R.id.resAddress);
		resAddressTV.setText(restaurant.getAddress());

        
        CheckBox favorited = (CheckBox) findViewById(R.id.favorite_check);
        ArrayList<ParseObject> favs = com.Cse110.view.FavoritesPageActivity.searchFavorites(com.Cse110.view.LoginActivity.user.getString("UserName"));
        
        
        System.out.println("Selected restaurant name: " + com.Cse110.view.HomePageActivity.RName);

        
        for(int i = 0; i < favs.size(); i++){
        	System.out.println("Loop " + i);
        	System.out.println("Checking " + favs.get(i).getString("FavoriteName"));
        	
        	//If the ith favorite is equal to the current restaurant
             if(favs.get(i).getString("resID").equals((restaurantID))) {
            	favorited.setChecked(true);
            	System.out.println("SUCCESS");
            	break;
             }
        	
             else 
            	 favorited.setChecked(false);
        }
        
        menuButton.setOnClickListener(new View.OnClickListener() {

		    public void onClick(View v) {
		    	Intent i = new Intent(RestaurantPageActivity.this, MenuPageActivity.class);
		    	Bundle extras = new Bundle();
				
		    	extras.putString("RestaurantName", (String) restaurant.getName());		
		    	extras.putString("restaurantId", restaurant.getObjectId());
				i.putExtras(extras);

                // Open MenuPageActivity.java activity
				startActivity(i);
		    }
		});
        
	}
	
	


            
	
	
	public void onCheckboxClicked(View view) {
	    // Is the view now checked?
	    boolean checked = ((CheckBox) view).isChecked();
	    
	    if(checked){
	    	System.out.println("Checked!");
	    	
	    	ParseObject newFav = new ParseObject("Favorite");
	    	newFav.put("UserID", com.Cse110.view.LoginActivity.user.getString("UserName"));
	    	newFav.put("resID", restaurantID);
	    	newFav.put("FavoriteName", restaurantName);
	    	newFav.saveInBackground();
	    }
	    else{
	    	System.out.println("Unchecked!");
	    	//Retrieves this user's list of favorites
	    	System.out.println("Removing " + com.Cse110.view.HomePageActivity.RName + " from favorites");
	    	
	    	ParseObject thisFav = search.retrieveFavById(restaurantID);
	    	System.out.println("Removing " + thisFav.getObjectId());
	    	thisFav.deleteInBackground();
	    }

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		 switch (item.getItemId()) {
	        case R.id.logout:
	        	Logout();
	            return true;
	        case R.id.home:
	        	Intent i = new Intent(this, HomePageActivity.class);
	        	startActivity(i);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void Logout() {
		
        final Intent intentLogOut = new Intent(this, LoginActivity.class);
        
		new AlertDialog.Builder(this)
		.setMessage("Do you really want to logout?")
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int whichButton) {
		        startActivity(intentLogOut);		    }})
		 .setNegativeButton(android.R.string.no, null).show();
		
	}


}



