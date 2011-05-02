package com.cube.jabbr.listView;
import org.json.JSONArray;
import org.json.JSONException;

public class Place {
	public String name;
	public String id;
	public String categories;
	public double lat;
	public double lon;
	
	public Place(String name, String id, double lat, double lon, String categories){
		this.name = name;
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.categories = categories;
		
	}
}
