package com.Cse110.view;




import com.Cse110.R;
import com.parse.ParseObject;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class HomePageActivity extends Activity {

	public static String UserID;
	public static String RName;
	public static String RId;
	//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
		
		//Checks for new user
	    //newUserCheck(Session.getActiveSession());
	    
		Button searchButton = (Button) findViewById(R.id.searchButton);
		Button favButton = (Button) findViewById(R.id.favoritesButton);


    /**
     * This section directs the user to a new page when they click on the search button
     * 
     */
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(HomePageActivity.this, SearchPageActivity.class);
				startActivity(i);
			}
		});
    
		favButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(HomePageActivity.this, FavoritesPageActivity.class);
				startActivity(i);
			}
		});
	}
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_home_page, container,
					false);
			return rootView;
		}
	}

	
	

	//Adds the facebook user ID to the UserAccount class in the database 
	//if the user is not already in it
	public static void createNewUser(String ID){
		ParseObject newUser = new ParseObject("UserAccount");
		newUser.put("UserName", ID);
		newUser.saveInBackground();
	
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);		
		return true;
	}
	
	@Override
	public void onBackPressed() {
		
	}
}
