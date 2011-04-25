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

import android.graphics.drawable.Drawable;
import android.util.Log;

public class ThumbnailObtainer {
	List<Thumbnail> thumbnails = new ArrayList<Thumbnail>();
	public List<Thumbnail> getThumbnails(){
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
			//JSONArray items = ((JSONObject) groups.get(0)).getJSONArray("items");
			for (int i = 0; i < items.length(); i++)
			{
				JSONObject item = items.getJSONObject(i);
				String image_url = item.getString("image_url");
				String word = item.getString("word");
				String answer = item.getString("answer");
				String flashcard_id = item.getString("flashcard_id");
				//thumbnails.add(new Thumbnail(name, id, item_lat, item_lon, categories));
				Drawable drawable;
				try {
					drawable= loadDrawable("http://www.nullamatix.com/images/I-dunno-lol.jpg");// TODO: replace with image_url
					Log.i("jabbr", "got flashcard drawable by loading:"+image_url);
				} catch (Exception e) {
					//drawable = getResources().getDrawable(R.drawable.no_image);
					drawable = loadDrawable("http://catsinsinks.com/images/cats/rotator.php");
					Log.i("jabbr", "CATS!!!! IN F*CKING SINKS Y'ALL.");
				}
				Thumbnail t = new Thumbnail(drawable, word, answer, flashcard_id);
				thumbnails.add(t);
			}
		}

		catch (Throwable t) {
			Log.e("MashUp", "Exception in getThumbnails()", t);
		}
		
		return thumbnails;
	}
	
	public Drawable loadDrawable(String image_url) {


		URL url = null;
		InputStream inputStream = null;
		try {
			url = new URL(image_url);
			inputStream = (InputStream) url.getContent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Drawable drawable = Drawable.createFromStream(inputStream, "src");
		//Toast.makeText(this.getApplicationContext(), "url:"+url.toString(), Toast.LENGTH_SHORT).show();
		//tableLayout.setBackgroundDrawable(drawable);
		return drawable;
	}
	
}
