package com.cube.jabbr.listView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.cube.jabbr.utils.Utils;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class ThumbnailObtainer {
	List<Thumbnail> thumbnails = new ArrayList<Thumbnail>();
	public String[] listOfWords;
	public String[] listOfForeign;
	public String[] listOfImageUrls;
	public int numOfCards = 0;
	
	public ThumbnailObtainer(){
		// TODO: call server, create thumbnail objects
		HttpGet get = new HttpGet("http://jabbrcube.heroku.com/api/getthumbnails/1");
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		HttpClient client = new DefaultHttpClient();
		get.addHeader(BasicScheme.authenticate(
   			 new UsernamePasswordCredentials("poorva", "password"),
			 "UTF-8", false));
		try {
			String responseBody = client.execute(get, responseHandler);
			System.out.println(responseBody);
			JSONObject jObject = new JSONObject(responseBody);
			JSONArray items = jObject.getJSONArray("flashcards");
			listOfWords = new String[items.length()+1];
			listOfForeign = new String[items.length()+1];
			listOfImageUrls = new String[items.length()+1];
			//JSONArray items = ((JSONObject) groups.get(0)).getJSONArray("items");
			for (int i = 0; i < items.length(); i++)
			{
				numOfCards++;
				JSONObject item = items.getJSONObject(i);
				String image_url = item.getString("image_url");
				String word = item.getString("word");
				String answer = item.getString("answer");
				String flashcard_id = item.getString("flashcard_id");
				listOfWords[i] = word;
				listOfForeign[i] = answer;
				listOfImageUrls[i] = "http://www.nullamatix.com/images/I-dunno-lol.jpg"; //TODO: real image urls
			}
		}

		catch (Throwable t) {
			Log.e("MashUp", "Exception in getThumbnails()", t);
		}
		
	}
	
}
