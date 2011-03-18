package com.cube.jabbr.listView;

public class LocationView {
	
	public String context;
	public String address;
	public int id;
	public double longitude;
	public double latitude;

	public LocationView(String context, int id, double longitude, double latitude, String address)
	{
		this.context = context;
		this.address = address;
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
}
