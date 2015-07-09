package com.Cse110.view;

import java.util.ArrayList;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.Cse110.R;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ReviewAdapter extends ArrayAdapter<ParseObject> {
	private ArrayList<ParseObject> items;
	private Context context;

	private String userID;
	private String dishID;


	public ReviewAdapter(Context context, int textViewResourceId,
			ArrayList<ParseObject> items, String uid, String did) {
		super(context, textViewResourceId, items);
		this.items = items;
		this.context = context;
		//this.resource = textViewResourceId;
		this.userID = uid;
		this.dishID = did;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.review, null);
		}
		ParseObject o = items.get(position);
		TextView name = (TextView) v.findViewById(R.id.reviewName);
		name.setText(o.getString("Name"));

		TextView description = (TextView) v
				.findViewById(R.id.reviewDescription);
		description.setText(o.getString("ReviewDescription"));

		TextView edit = (TextView) v.findViewById(R.id.editReview);
		
		edit.setTag(position);
		
		TextView delete = (TextView) v.findViewById(R.id.deleteReview);

		if (o.getString("userID").equals(userID)) {
			edit.setVisibility(View.VISIBLE);
			delete.setVisibility(View.VISIBLE);
		
			final int p = position;

			edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					
					int num = (Integer) arg0.getTag();
					
					Intent intent = new Intent(context, AddReviewActivity.class);
					intent.putExtra("dish", dishID); // Send
					intent.putExtra("user", userID);
					
					System.out.println("The reviewID is = " + items.get(num).getObjectId());
					
					intent.putExtra("review", items.get(num).getObjectId());
					context.startActivity(intent);
				}
			});
			delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					AlertDialog d = createDialog(p);
					d.show();
				}
			});
		} else {
			edit.setVisibility(View.INVISIBLE);
			delete.setVisibility(View.INVISIBLE);
		}

		return v;
	}

	private AlertDialog createDialog(final int positionToRemove) {
		// See if user wants to keep picture
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Are you sure you want to delete your review?");
		// Add the buttons
		builder.setNegativeButton("No", null);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				ParseQuery<ParseObject> itemQuery = new ParseQuery<ParseObject>("Reviews");
				itemQuery.whereEqualTo("objectId", items.get(positionToRemove)
						.getObjectId());
				final ParseObject toDelete;
				try {
					toDelete = (ParseObject) itemQuery.find().get(0);
					toDelete.deleteInBackground(new DeleteCallback() {

		                @Override
		                public void done(ParseException e) {
		                    try {
		                    	toDelete.delete();
		                    } catch (ParseException e1) {
		                        e1.printStackTrace();
		                    }
		                }
		            });
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				items.remove(positionToRemove);
				ReviewAdapter.this.notifyDataSetChanged();
				CharSequence text = "Review Deleted";
				int duration = Toast.LENGTH_SHORT;
				Toast t = Toast.makeText(context, text, duration);
				t.show();
			}
		});
		// Create the AlertDialog
		AlertDialog dialog = builder.create();
		return dialog;
	}
	
}