package com.Cse110.view;

import java.util.ArrayList;








//Imported files for search function
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;





import com.Cse110.R;
import com.Cse110.model.ParseApplication;
import com.parse.ParseObject;


public class SearchPageActivity extends Activity {

  
    
    //Used for search function
    protected EditText searchText;
    protected Cursor cursor;
    protected ListView RList;
    
    //Allows writing of restaurant names to listview
    ArrayAdapter<String> adapter;

    
    
    private String[] ResArray, IDArray;
    private ListView RestaurantListView;
    private ParseApplication search = new ParseApplication();
    private ArrayAdapter<String> arrayAdapter;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Sets the layout according to activity_home_page
		setContentView(R.layout.activity_search_page);
			
		RestaurantListView = (ListView) findViewById(R.id.listView1);
		
		//Retrieve all objects from the database
		ArrayList<ParseObject> list = search.retrieveByName("");  
        
		//Creates an array with the same size as the list above
		ResArray = new String[list.size()];
		IDArray = new String[list.size()];
		
        //Populates an array with the restaurant names
        for(int i = 0; i < list.size();i++)
        {
        	IDArray[i] = list.get(i).getObjectId();
           ResArray[i] = list.get(i).getString("RestaurantName");
           
           
        }
        
        //arrayAdapter allows interaction between the list and the restaurant name array
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.product_name, ResArray);
        
        //Sets the adapter for the restaurant list view to arrayAdapter
        RestaurantListView.setAdapter(arrayAdapter);

        //Assigns searchBar to be searchText, the EditText item
        searchText = (EditText) findViewById(R.id.searchBar);
        
        //Adds a text watcher to searchText to filter search results
        searchText.addTextChangedListener(new TextWatcher() {
        	
        		//Refilters the restaurant list depending on what is typed into the searchText search bar
        	    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
        	        //When user changed the Text, filters the list with that new string
        	        SearchPageActivity.this.arrayAdapter.getFilter().filter(cs);   
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
        RestaurantListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
            	Intent i = new Intent(SearchPageActivity.this, RestaurantPageActivity.class);
            	com.Cse110.view.HomePageActivity.RId = IDArray[position];
            	com.Cse110.view.HomePageActivity.RName = ResArray[position];

            	startActivity(i);
            }
        
        });
       
	}

	
    public static void Save(String restaurantName, String zipCode, String category)
    {
    	ParseObject newRestaurant = new ParseObject("Restaurant");
    	newRestaurant.put("RestaurantName", restaurantName);
    	newRestaurant.put("ZipCode", zipCode);
    	newRestaurant.put("Category", category);
    	newRestaurant.saveInBackground();
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
			View rootView = inflater.inflate(R.layout.fragment_home_page,
					container, false);
			return rootView;
		}
	}
}
