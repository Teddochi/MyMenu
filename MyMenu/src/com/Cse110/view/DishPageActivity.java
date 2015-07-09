package com.Cse110.view;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.Cse110.R;
import com.parse.*;

public class DishPageActivity extends Activity {

	private ParseObject selectedDish;
	// Strings to update with dish info
	private String dishName = "O6L8z0KKos";
	private String userName = "795082640504779";
	private String dishPrice = "11.99";

	public static String gdishName;

	// Views to place strings in.
	private TextView descriptionView;
	private TextView dishTitle;
	private TextView price;
	private TextView nutrition;
	private ParseImageView ownerPicView;
	private RatingBar ratingBar;
	private static final int SELECT_PHOTO = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dish);

		// Removed bundle in favor of global variable
		dishName = gdishName;
		userName = com.Cse110.view.LoginActivity.user.getString("UserName");

		getUser();
		fillUI();
		getReviews();
		getPictures();
		getRating();

	}

	private void fillUI() {
		// Get selected dish from particular restaurant
		ParseQuery<ParseObject> dishQuery = ParseQuery.getQuery("Dish");
		dishQuery.whereEqualTo("objectId", dishName);

		// Run Query
		dishQuery.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> dishInfo,
					com.parse.ParseException e) {
				if (e == null && dishInfo.size() != 0) {
					selectedDish = dishInfo.get(0);
					dishPrice = "$" + selectedDish.getNumber("Price");

					// get owner uploaded picture
					ownerPicView = (ParseImageView) findViewById(R.id.ownerPic);
					ownerPicView.setVisibility(View.VISIBLE);
					ParseFile photoFile = selectedDish.getParseFile("Picture");
					if (photoFile != null) {
						ownerPicView.setParseFile(photoFile);
						ownerPicView.loadInBackground(new GetDataCallback() {

							@Override
							public void done(byte[] arg0,
									com.parse.ParseException arg1) {
								// TODO Auto-generated method stub

							}

						});
					}

					// Set Title
					dishTitle = (TextView) findViewById(R.id.dish);
					if (dishTitle != null)
						dishTitle.setText(selectedDish.getString("DishName"));

					// Set Price
					price = (TextView) findViewById(R.id.price);
					if (price != null)
						price.setText(dishPrice);

					// Set description
					descriptionView = (TextView) findViewById(R.id.description);
					if (descriptionView != null)
						descriptionView.setText(selectedDish
								.getString("Description"));

					// Set nutrition info
					nutrition = (TextView) findViewById(R.id.nutrition);
					if (nutrition != null)
						nutrition.setText(selectedDish.getNumber("Calories")
								+ " Calories\n" + selectedDish.getNumber("Fat")
								+ "g Fat\n" + selectedDish.getNumber("Carbs")
								+ "g Carbs\n"
								+ selectedDish.getNumber("Protein")
								+ "g Protein");

				}
			}
		});
	}

	private void getUser() {
		ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("UserAccount");
		userQuery.whereEqualTo("UserName", userName);
		try {
			userQuery.find().get(0);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void getPictures() {
		ParseQuery<ParseObject> picQuery = ParseQuery.getQuery("UserPicture");
		picQuery.whereContains("DishID", dishName);

		try {
			ArrayList<ParseObject> images = (ArrayList<ParseObject>) picQuery
					.find();
			Gallery g = (Gallery) findViewById(R.id.gallery);
			g.setSpacing(20);
			g.setAnimationDuration(1500);
			g.setAdapter(new ImageAdapter(this, R.layout.user_picture, images,
					true));

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getReviews() {

		ParseQuery<ParseObject> reviewQuery = ParseQuery.getQuery("Reviews");
		reviewQuery.whereContains("dishID", dishName);
		try {
			ArrayList<ParseObject> reviews = (ArrayList<ParseObject>) reviewQuery
					.find();
			ListView listview = (ListView) findViewById(R.id.reviewsList);

			if (reviews.size() == 0) {
				ParseQuery<ParseObject> rQuery = ParseQuery.getQuery("Reviews");
				
				//Fills in reviews with the pre-made blank review message
				rQuery.whereEqualTo("objectId", "iDQdemaXBR");

				ParseObject blankReview = rQuery.find().get(0);

				reviews.add(blankReview);
			}

			ReviewAdapter itemsAdapter = new ReviewAdapter(this,
					R.layout.review, reviews, userName, dishName);
			listview.setAdapter(itemsAdapter);

		} catch (ParseException e1) {
			e1.printStackTrace();
		}

	}

	public void SubmitRating(View v) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Rating");
		query.whereEqualTo("dishId", dishName).whereEqualTo("userId", userName);
		try {
			ArrayList<ParseObject> userRatings = (ArrayList<ParseObject>) query.find();
			if(userRatings.size() == 0)
			{
				ParseObject newRating = new ParseObject("Rating");
				newRating.put("userId", userName);
				newRating.put("dishId", dishName);
				newRating.put("Rating", ratingBar.getRating());
				newRating.saveInBackground();
			}
			else
			{
				userRatings.get(0).put("Rating", ratingBar.getRating());
				userRatings.get(0).saveInBackground();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Context context = getApplicationContext();
		CharSequence text = "Rating Submitted";
		int duration = Toast.LENGTH_SHORT;

		Toast t = Toast.makeText(context, text, duration);
		t.show();
		getRating();
	}

	public void getRating() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Rating");
		query.whereEqualTo("dishId", dishName);

		try {
			ArrayList<ParseObject> ratings = (ArrayList<ParseObject>) query
					.find();
			
			query.whereEqualTo("dishId", dishName).whereEqualTo("userId", userName);
			ArrayList<ParseObject> user = (ArrayList<ParseObject>) query.find();
			ratingBar = (RatingBar) findViewById(R.id.ratingBar1);
			TextView userRating = (TextView) findViewById(R.id.userRating);

			if (ratings.size() == 0)
			{
				ratingBar.setRating(0);
				if(user.size() == 0 ) userRating.setText("No Rating Found");
				
			}
			else {
				double total = 0;
				for (ParseObject o : ratings) {
					total += o.getDouble("Rating");
				}
				ratingBar.setRating((float) total / ratings.size());
				
				if(user.size() == 0 ) userRating.setText("No Rating Found");
				else userRating.setText("Your Rating: " + user.get(0).getDouble("Rating"));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addReview(View v) {
		Intent intent = new Intent(this, AddReviewActivity.class);
		intent.putExtra("dish", dishName); // Send
		intent.putExtra("user", userName);
		intent.putExtra("review", "");
		startActivity(intent);
	}

	public void addPicture(View v) {
		Intent intent = new Intent(this, AddPictureActivity.class);
		intent.putExtra("dish", dishName); // Send
		intent.putExtra("user", userName);
		startActivity(intent);
	}

	public void viewPictures(View v) {
		Intent intent = new Intent(this, ViewPicturesActivity.class);
		intent.putExtra("dish", dishName); // Send
		intent.putExtra("user", userName);
		startActivity(intent);
	}
	
	public void UploadPicture(View v) {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, SELECT_PHOTO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		switch (requestCode) {
		case SELECT_PHOTO:
			if (resultCode == RESULT_OK) {
				Uri selectedImage = imageReturnedIntent.getData();
				InputStream imageStream;
				try {
					ContentResolver resolver = getContentResolver();
					imageStream = resolver.openInputStream(selectedImage);
					addPicture(BitmapFactory.decodeStream(imageStream));
					goBack();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void goBack() {
		Intent intent = new Intent(this, DishPageActivity.class);
		intent.putExtra("dish", dishName);
		intent.putExtra("user", userName);
		startActivity(intent);
	}

	public void addPicture(Bitmap image) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, stream);
		ParseFile file = new ParseFile(stream.toByteArray());

		ParseObject userPic = new ParseObject("UserPicture");
		userPic.put("UserID", userName);
		userPic.put("DishID", dishName);
		userPic.put("UserPic", file);
		userPic.saveInBackground();
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