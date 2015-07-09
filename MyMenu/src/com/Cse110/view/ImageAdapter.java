package com.Cse110.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.Cse110.R;
import com.parse.ParseException;
import com.parse.ParseObject;

public class ImageAdapter extends BaseAdapter {
	private Context context;

	private boolean thumbnail;
	private ParseObject picToView;

	
	public ArrayList<ParseObject> images = new ArrayList<ParseObject>();

	public ImageAdapter(Context context, int textViewResourceId,
			ArrayList<ParseObject> items, boolean thumbnails) {
		super();
		this.images = items;
		this.context = context;

		this.thumbnail = thumbnails;

	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.user_picture, null);
		}
		ParseObject o = images.get(position);

		ImageView iv = (ImageView) v.findViewById(R.id.dish_pic);
		iv.setTag(position);
		byte[] bytes;
		try {
			bytes = o.getParseFile("UserPic").getData();
			final Bitmap image = BitmapFactory
					.decodeByteArray(bytes, 0, bytes.length);
			if (thumbnail) {
				final int size = 64;
				Bitmap res_image = ThumbnailUtils.extractThumbnail(image, size, size);
				iv.setImageBitmap(res_image);
			}
			else
			{
				final int size = 90;
				Bitmap res_image = ThumbnailUtils.extractThumbnail(image, size, size);
				iv.setImageBitmap(res_image);
				picToView = o;
				iv.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
					    //NEED TO IMPLEMENT PICTURE PREVIEW//
						int num = (Integer) arg0.getTag();
						Intent i = new Intent(context, ExpandPictureActivity.class);
						Bundle extras = new Bundle();
						extras.putString("dish", picToView.getString("DishID"));
						extras.putString("user", picToView.getString("UserID"));
						extras.putString("pic", images.get(num).getObjectId());
						i.putExtras(extras);
						context.startActivity(i);
					}
				});
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * Set what information goes in which UI component here
		 */
		return v;
	}

	@Override
	public int getCount() {
		return images.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return images.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
