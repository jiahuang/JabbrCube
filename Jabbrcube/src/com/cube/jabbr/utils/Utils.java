package com.cube.jabbr.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

public class Utils {
	// shared preferences
	public static String PREF = "jabbrPrefs";
	public static String PREF_VALIDLOGIN = "validlogin";
	public static String PREF_LONGITUDE = "longitude";
	public static String PREF_LATITUDE = "latitude";
	public static String PREF_PLACENAME = "placeName";
	public static String PREF_PLACEID = "placeId";
	public static String PREF_NAME = "username";
	public static String PREF_PASSWORD = "password";
	public static String PREF_CATEGORIES = "categories";
	
	public static Drawable loadDrawable(String image_url) {
		URL url = null;
		InputStream inputStream = null;
		try {
			url = new URL(image_url);
			inputStream = (InputStream) url.getContent();
		} catch (Exception e) {
			System.out.println("Malformed url:"+image_url);
			e.printStackTrace();
		}
		Drawable drawable = Drawable.createFromStream(inputStream, "src");
		return drawable;
	}
	
	public static Bitmap getBitmapFromURL(String src) {
	    try {
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}

}
