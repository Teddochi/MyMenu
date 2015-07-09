package com.Cse110.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.Cse110.R;
import com.Cse110.model.ParseApplication;
import com.parse.ParseObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;

public class MenuPageActivity extends Activity {
	
	private String restaurantName;
	private String restaurantId;
	private List<ParseObject> ob;
	private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private TextView name;
    private ParseApplication search = new ParseApplication();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Sets the layout according to activity_menu_page
		setContentView(R.layout.activity_menu_page);
		
		// Retrieve the Restaurant name and Id from HomePageActivity
		restaurantId = com.Cse110.view.HomePageActivity.RId;
		
		restaurantName = com.Cse110.view.HomePageActivity.RName;		
		// Show "Restaurant Menu"
		name = (TextView) findViewById(R.id.menuTv);
		name.setText(restaurantName + " Menu");
		
		ob = search.retrieveDishes(restaurantId);
		
		listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
		
		// Get the ExpandableListView
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        
        // Retrieve object "Category" from Parse.com database.
        for (ParseObject Dish : ob) {
        	if( Dish != null) {
        		if (listDataHeader.indexOf((String) Dish.get("Category")) < 0) {
        			listDataHeader.add((String) Dish.get("Category"));
        		}
        	}
        }
        
        // Set the dishes to the right category.
        for (int i = 0; i < listDataHeader.size(); ++i) {
        	List<String> dishes = new ArrayList<String>();
        	for (ParseObject Dish : ob) {
        		if (((String) Dish.get("Category")).equals(listDataHeader.get(i))) {
        			dishes.add((String) Dish.get("DishName"));
        		}
        	}
        	listDataChild.put(listDataHeader.get(i), dishes);
        }
  
        listAdapter = new MenuExpandableListAdapter(MenuPageActivity.this, listDataHeader, listDataChild);
 
        // Setting list adapter
        expListView.setAdapter(listAdapter);
        
        // Expandablelistview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {
 
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
            		int groupPosition, long id) {
                return false;
            }
        });
 
        // ExpandableListview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
 
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });
 
        // ExpandableListview Group collapsed listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
 
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });
        
        // ExpandableListview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {
 
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                
                String dishId = new String();
                
                // Get the selected dish id to send to DishPageActivity.
                for (ParseObject Dish : ob) {
                	if(Dish.get("DishName") == listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)) {
                		dishId = Dish.getObjectId();
                		System.out.println(dishId);
                	}
                }
                
               
                Intent i = new Intent(MenuPageActivity.this, DishPageActivity.class);
        		i.putExtra("dishId", dishId);
        		com.Cse110.view.DishPageActivity.gdishName = dishId;
        		startActivity(i);
                
                return false;
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


}
