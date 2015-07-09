package com.Cse110.view;

import java.util.ArrayList;

import com.Cse110.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;



import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;



public class FavoritesPageActivity extends Activity {
    //Used for search function
    protected EditText FsearchText;
    protected Cursor cursor;
    protected ListView FList;
    

    
    //Allows writing of restaurant names to listview
    ArrayAdapter<String> adapter;

    
    private String[] favArray, IDArray;
    private ListView favoriteListView;

    private ArrayAdapter<String> arrayAdapter;
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Sets the layout according to activity_favorites_page
		setContentView(R.layout.activity_favorites_page);
			
		//Creates a list layout
		favoriteListView = (ListView) findViewById(R.id.FlistView1);
		
		//Retrieve all objects from the database
		ArrayList<ParseObject> list = searchFavorites(com.Cse110.view.LoginActivity.user.getString("UserName"));  
        
		//Creates an array with the same size as the list above
		favArray = new String[list.size()];
		IDArray = new String[list.size()];
		
        //Populates an array with the restaurant names from the Favorite class
        for(int i = 0; i < list.size();i++)
        {
           favArray[i] = list.get(i).getString("FavoriteName");
       	   IDArray[i] = list.get(i).getString("resID");
        }
        
        
    	
        //arrayAdapter allows interaction between the list and the restaurant name array
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.product_name, favArray);
        
        //Sets the adapter for the restaurant list view to arrayAdapter
        favoriteListView.setAdapter(arrayAdapter);

        //Assigns searchBar to be searchText, the EditText item
        FsearchText = (EditText) findViewById(R.id.FsearchBar);
        
        //Adds a text watcher to searchText to filter search results
        FsearchText.addTextChangedListener(new TextWatcher() {
        	
        		//Refilters the restaurant list depending on what is typed into the searchText search bar
        	    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
        	        //When user changed the Text, filters the list with that new string
        	        FavoritesPageActivity.this.arrayAdapter.getFilter().filter(cs);   
        	    }
        	     
        	    
        	    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
        	            int arg3) {
        	        // TODO Auto-generated method stub
        	         
        	    }
        	     
        	    
        	    public void afterTextChanged(Editable arg0) {
        	        // TODO Auto-generated method stub                          
        	    }
        	});
        
        
        /**
         * This section directs the user to a new page when they click a restaurant
         * 
         */
        favoriteListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
            	Intent i = new Intent(FavoritesPageActivity.this, RestaurantPageActivity.class);
            	com.Cse110.view.HomePageActivity.RName = favArray[position];
            	com.Cse110.view.HomePageActivity.RId = IDArray[position];
            	

            	startActivity(i);
            }
        
        });
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



	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_favorites_page,
					container, false);
			return rootView;
		}
	}
	

	//Returns the list of favorites based on the userID passed in as ID
	public static ArrayList<ParseObject> searchFavorites(String ID) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite"); //Table to get data from
	
		//Possibly remove
		if(ID == null){
			ID = com.Cse110.view.LoginActivity.user.getObjectId();
		}
		
		
	    query.whereContains("UserID", ID);                          //Retrieve restaurants where name has search
	    
	    ArrayList<ParseObject> l = null;                                     //Store results from query
		try {
			l = (ArrayList<ParseObject>) query.find();                       //Send query to database
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	    Log.d("score", "got " + l.size() + " favorites");
		return l;
	} 
}
