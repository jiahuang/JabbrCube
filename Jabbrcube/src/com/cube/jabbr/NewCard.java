package com.cube.jabbr;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.cube.jabbr.MIME.*;
import com.cube.jabbr.content.ByteArrayBody;
import com.cube.jabbr.content.StringBody;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NewCard extends Activity {
	final int CAMERA_PIC_REQUEST = 1;
	Bitmap pic;
	double longitude = 0;
	double latitude = 0;
	String original = "";
	String translation = "";
	TextView tv_Translation;
	EditText et_Original;
	boolean switchText = false;
	boolean stillTranslating = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newcard);
        tv_Translation = (TextView) findViewById(R.id.translation);
        et_Original = (EditText) findViewById(R.id.translate);
        
        et_Original.setOnFocusChangeListener(new View.OnFocusChangeListener()
    	{ 
    		@Override
    	   public void onFocusChange(View v, boolean lostfocus)
    	   {
    	       if (lostfocus == true)
    	       { 
    	    	   translateText.start();
    	       }
    	   }
    	});
    }
    
    public void camera(View view){
    	Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	if (requestCode == CAMERA_PIC_REQUEST){
    		pic = (Bitmap) data.getExtras().get("data");
    		ImageView image = (ImageView) findViewById(R.id.image);
    		image.setImageBitmap(pic);
    	}
    }
    
    ResponseHandler<String> handler = new ResponseHandler<String>() {
	    public String handleResponse(
	            HttpResponse response) throws ClientProtocolException, IOException {
	        HttpEntity entity = response.getEntity();
	        if (entity != null) {

	        	String res = EntityUtils.toString(entity);
	        	System.out.println(res);
	        	// blank out proper fields
	        	((EditText) findViewById(R.id.translate)).setText("");
	        	tv_Translation.setText("");
	        	
	        	Context context = getApplicationContext();
	        	CharSequence text = "Your Flashcard has been sucessfully added!";
	        	int duration = Toast.LENGTH_SHORT;
	        	Toast toast = Toast.makeText(context, text, duration);
	        	toast.show();
	        	return null;
	        } else {
	            return null;
	        }
	    }
	};
	
	Handler progress=new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
    		
    		if (switchText){
    			tv_Translation.setText("Translating...");
    			switchText = false;
    		}
    		else {
    			tv_Translation.setText("Translating......");
    			switchText = true;
    		}
    	}
    };
    
    Thread textUI = new Thread(new Runnable(){
    	public void run(){
    		try{
    			while(stillTranslating){
    				Thread.sleep(500);
    				progress.sendMessage(progress.obtainMessage());
    			}
    		}
    		catch(Exception t){
        		
        	}
    	}
    	
    });
    
    ResponseHandler<String> finishTranslation = new ResponseHandler<String>() {
	    public String handleResponse(
	            HttpResponse response) throws ClientProtocolException, IOException {
	        HttpEntity entity = response.getEntity();
	        if (entity != null) {
    			textUI.stop();

    			String res = EntityUtils.toString(entity);
	        	
	        	System.out.println(res);

	        	tv_Translation.setText(res);
	        	
	        	return null;
	        } else {
	            return null;
	        }
	    }
	};
    
    Thread translateText = new Thread(new Runnable(){
    	public void run(){
    		try{
    			textUI.start();
    			DefaultHttpClient client = new DefaultHttpClient();
		    	
	        	HttpPost post=new HttpPost("http://jigimojo.heroku.com/attendees");
	        	List<NameValuePair> form=new ArrayList<NameValuePair>();
	        	form.add(new BasicNameValuePair("attendee[user_id]", "1"));
	        	form.add(new BasicNameValuePair("attendee[venue_id]", "1"));
	        	
	        	post.setEntity(new UrlEncodedFormEntity(form, HTTP.UTF_8));

	        	String responseBody=client.execute(post, finishTranslation);
	        	System.out.println(responseBody);

    			
    		}
    		catch(Exception t){
    		
    		}
    	}
    });
    
    Thread submit=new Thread(new Runnable() {
		public void run() {
			try{
				
				
				HttpClient httpClient = new DefaultHttpClient();
			    HttpContext localContext = new BasicHttpContext();
			    HttpPost httpPost = new HttpPost("http://192.168.51.214:3000/images/");
				// Add your data
				MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
				
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				pic.compress(CompressFormat.JPEG, 100, bos);
				byte[] data = bos.toByteArray();
				entity.addPart("image[photo]", new ByteArrayBody(data,"myImage.jpg"));
				entity.addPart("image[caption]", new StringBody(original));
				entity.addPart("image[Latitude]", new StringBody(Double.toString(latitude)));
				entity.addPart("image[Longitude]", new StringBody(Double.toString(longitude)));
				
				httpPost.setEntity(entity);
	
		        HttpResponse response = httpClient.execute(httpPost, localContext);

				String s_response = httpClient.execute(httpPost, handler);
				System.out.println(s_response);
			}
			catch(Throwable t){
			}
			
		}
	});
    
    public void addCard(View view){
    	// set everything up and send to web
    	original = ((EditText) findViewById(R.id.translate)).getText().toString();
    	translation = ((TextView) findViewById(R.id.translation)).getText().toString();
    	
    	// new thread for posting
    	submit.start();

    }
}