package com.Cse110.view;

import java.util.ArrayList;

import com.Cse110.R;
import com.parse.*;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class AddReviewActivity extends Activity {

	private String user;
	private String dish;
	private String reviewID;

	private EditText name;
	private EditText review;
	private CheckBox anonymous;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_review);

		

		Bundle extras = getIntent().getExtras();
		user = extras.getString("user");
		dish = extras.getString("dish");
		reviewID = extras.getString("review");

		name = (EditText) findViewById(R.id.reviewName);
		review = (EditText) findViewById(R.id.reviewText);
		anonymous = (CheckBox) findViewById(R.id.anonymousCheck);

		if (!reviewID.equals("")) {
			ParseQuery<ParseObject> revQuery = ParseQuery.getQuery("Reviews");
			revQuery.whereEqualTo("objectId", reviewID);

			try {
				ParseObject Review = revQuery.find().get(0);
				name.setText(Review.getString("Name"));
				review.setText(Review.getString("ReviewDescription"));
				if (Review.getString("Name").equals("Anonymous"))
					anonymous.setChecked(true);
				else
					anonymous.setChecked(false);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);		
		return true;
		
	}

	public void submitReview(View v) {
		if (name.getText().length() == 0 && anonymous.isChecked() == false) {
			System.err.println("METHOD CALLED");

			Context context = getApplicationContext();
			CharSequence text = "Please enter your name or check the anonymous box";
			int duration = Toast.LENGTH_SHORT;
			Toast t = Toast.makeText(context, text, duration);
			t.show();

		}

		else if (review.getText().length() == 0) {
			System.out.println("Pressed review");
			Context context = getApplicationContext();
			CharSequence text = "Please enter a review";
			int duration = Toast.LENGTH_SHORT;
			Toast t = Toast.makeText(context, text, duration);
			t.show();
		} else {
			// Submit
			if (reviewID.equals("")) {
				ParseObject Review = new ParseObject("Reviews");
				Review.put("ReviewDescription", review.getText().toString());
				Review.put("userID", user);
				if (anonymous.isChecked())
					Review.put("Name", "Anonymous");
				else
					Review.put("Name", name.getText().toString());
				Review.put("dishID", dish);
				Review.saveInBackground();
			} else {				
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Reviews");
				System.out.println("The reviewID is " + reviewID);
				query.whereEqualTo("objectId", reviewID);
				ArrayList<ParseObject> rev = null;
				try {
					rev = (ArrayList<ParseObject>) query.find();                       //Send query to database
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				System.out.println("The review is " + rev);
				if (rev != null) {
					rev.get(0).put("ReviewDescription", review.getText()
							.toString());
					if (anonymous.isChecked())
						rev.get(0).put("Name", "Anonymous");
					else
						rev.get(0).put("Name", name.getText().toString());
					rev.get(0).saveInBackground();
				}
			}
			goBack();
		}

	}

	public void cancel(View v) {
		goBack();
	}

	public void goBack() {
		Intent intent = new Intent(this, DishPageActivity.class);
		intent.putExtra("dish", dish);
		intent.putExtra("user", user);
		startActivity(intent);
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
