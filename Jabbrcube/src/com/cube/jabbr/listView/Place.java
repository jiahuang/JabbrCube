package com.cube.jabbr.listView;
import org.json.JSONArray;
import org.json.JSONException;

public class Place {
	public String name;
	public String id;
	public JSONArray categories;
	public double lat;
	public double lon;
	
	public Place(String name, String id, double lat, double lon, JSONArray categories){
		this.name = name;
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.categories = categories;
		for(int i =0; i<categories.length(); i++){
			try {
				System.out.println(this.name + " cat:"+categories.get(i).toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
