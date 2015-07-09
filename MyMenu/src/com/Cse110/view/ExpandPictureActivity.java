package com.Cse110.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;

import com.Cse110.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ExpandPictureActivity extends ActionBarActivity {
	

	private String picId;
	ParseImageView parsePic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.err.println("Reached PictureViewActivity");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_view);
			
		Bundle extras = getIntent().getExtras();

		picId = (String) extras.get("pic");
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("UserPicture");
		query.whereEqualTo("objectId", picId);
		
		try {
			ParseObject o = query.find().get(0);
			parsePic = (ParseImageView) findViewById(R.id.picView1);
			parsePic.setVisibility(View.VISIBLE);
			ParseFile photoFile = o.getParseFile("UserPic");
			if (photoFile != null) {
				parsePic.setParseFile(photoFile);
				parsePic.loadInBackground(new GetDataCallback() {
					@Override
					public void done(byte[] arg0, com.parse.ParseException arg1) {
						
					}
				});
			}
			
		} catch (ParseException e) {
			
		}
		
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
