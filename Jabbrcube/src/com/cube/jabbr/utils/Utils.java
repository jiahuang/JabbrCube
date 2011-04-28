package com.cube.jabbr.utils;

import java.io.InputStream;
import java.net.URL;

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
	
	public static Drawable loadDrawable(String image_url) {
		URL url = null;
		InputStream inputStream = null;
		try {
			url = new URL(image_url);
			inputStream = (InputStream) url.getContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Drawable drawable = Drawable.createFromStream(inputStream, "src");
		return drawable;
	}
}
