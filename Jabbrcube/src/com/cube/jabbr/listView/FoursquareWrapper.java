package com.cube.jabbr.listView;

import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;


import android.util.Log;

public class FoursquareWrapper {
	private String mClient_id = "QYWMXCGIARFO2OLAPK5BGURG4A0FWSHINJZGP5KBBZ23PL41";
	private String mClient_secret = "WXRJUD1B0PHTYGEEOJVUHIXINGP0JHJCC4YREUDNTJSW2ZQV";
	private HttpClient client = new DefaultHttpClient();

	public ArrayList<Place> getVenues(double lat, double lon) {
		ArrayList<Place> places = new ArrayList<Place>();
		String f = "https://api.foursquare.com/v2/venues/search?ll=%f,%f&client_id=%s&client_secret=%s";
		String query = String.format(f, lat, lon, mClient_id, mClient_secret);
		HttpGet get = new HttpGet(query);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		try {
			String responseBody = client.execute(get, responseHandler);
			JSONObject jObject = new JSONObject(responseBody);
			JSONArray groups = jObject.getJSONObject("response").getJSONArray("groups");
			JSONArray items = ((JSONObject) groups.get(0)).getJSONArray("items");
			for (int i = 0; i < items.length(); i++)
			{
				JSONObject item = items.getJSONObject(i);
				String name = item.getString("name");
				String id = item.getString("id");
				double item_lat = item.getJSONObject("location").getDouble("lat");
				double item_lon = item.getJSONObject("location").getDouble("lng");
				Log.d("lat", Double.toString(item_lat));
				JSONArray categories = item.getJSONArray("categories");
				//String[] categoryIds = new String[categories.length()];
				String categoryIds = "";
				for (int a = 0; a<categories.length(); a++){
					JSONObject cat = categories.getJSONObject(a);
					categoryIds = categoryIds + cat.getString("id") + ",";
					//System.out.println("category id "+a+": "+ categoryIds[a]);
				}
				places.add(new Place(name, id, item_lat, item_lon, categoryIds));
			}
		}

		catch (Throwable t) {
			Log.e("MashUp", "Exception in getStuff()", t);
		}
		return places;
	}

}
