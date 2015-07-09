package com.Cse110.view;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.Cse110.R;
import com.parse.ParseFile;
import com.parse.ParseObject;

public class AddPictureActivity extends Activity {

	private String userID;
	private String dishID;
	private static final int SELECT_PHOTO = 100;
	private File photoFile = null;
	private ImageView preview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_picture);
		preview = new ImageView(this);
		
		Bundle extras = getIntent().getExtras();
		dishID = (String) extras.get("dish");
		userID = (String) extras.get("user");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_picture, menu);
		return true;
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

	@SuppressLint("NewApi")
	private void Snapshot() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go
			try {
				String imageFileName = new SimpleDateFormat("yyyyMMdd")
						.format(new Date()) + "_" + userID;
				File storageDir = Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				photoFile = File.createTempFile(imageFileName, ".jpg",
						storageDir);

			} catch (IOException ex) {
				// Error occurred while creating the File
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, 1);

				// Generate Preview
				preview.setImageBitmap(BitmapFactory.decodeFile("file:"
						+ photoFile.getAbsolutePath()));
				preview.setVisibility(View.VISIBLE);

				AlertDialog dialog = createDialog();
				dialog.show();
			}
		}
	}

	private AlertDialog createDialog() {
		// See if user wants to keep picture
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Keep Picture?");
		// Add the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				addPicture(BitmapFactory.decodeFile("file:"
						+ photoFile.getAbsolutePath()));
				preview.setVisibility(View.INVISIBLE);
				goBack();
			}
		});
		builder.setNeutralButton("Retake",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						preview.setVisibility(View.INVISIBLE);
						Snapshot();
					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						preview.setVisibility(View.INVISIBLE);
						goBack();
					}
				});

		// Create the AlertDialog
		AlertDialog dialog = builder.create();
		return dialog;
	}

	public void cancel(View v) {
		goBack();
	}

	private void goBack() {
		Intent intent = new Intent(this, DishPageActivity.class);
		intent.putExtra("dish", dishID);
		intent.putExtra("user", userID);
		startActivity(intent);
	}

	public void addPicture(Bitmap image) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, stream);
		ParseFile file = new ParseFile(stream.toByteArray());

		ParseObject userPic = new ParseObject("UserPicture");
		userPic.put("UserID", userID);
		userPic.put("DishID", dishID);
		userPic.put("UserPic", file);
		userPic.saveInBackground();
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
