package com.cube.jabbr.listView;

import android.graphics.drawable.Drawable;

public class Thumbnail {
	public Drawable image;
	public String word;
	public String foreign;
	public String id;
	public String real_image_url;
	
	public Thumbnail(Drawable image, String word, String foreign, String id){
		this.image = image;
		this.word = word;
		this.foreign = foreign;
		this.id = id;
	}
}
