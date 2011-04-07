package com.cube.jabbr;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

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
import android.text.Editable;
import android.text.TextWatcher;
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
	boolean textChanged = false;
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newcard);
        tv_Translation = (TextView) findViewById(R.id.translation);
        et_Original = (EditText) findViewById(R.id.translate);
        
        et_Original.addTextChangedListener(new TextWatcher() {
            
            @Override
			public void afterTextChanged(Editable s) {
            	textChanged = true;
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
        });
        
        et_Original.setOnFocusChangeListener(new View.OnFocusChangeListener()
    	{ 
    		@Override
    	   public void onFocusChange(View v, boolean gainedfocus)
    	   {
    			if (gainedfocus == false && textChanged == true){
    				textChanged = false;
    			new Thread(new Runnable() {
    				public void run(){
    		    		try{
    		    			//textUI.start();
    		    			Message msg =  Message.obtain();
    		    			Bundle bundle = new Bundle();

    		    			bundle.putString("Text", "Translating...");
    		    			msg.setData(bundle);
    		    			translationHandler.sendMessage(msg);
    		    			
    		    			DefaultHttpClient client = new DefaultHttpClient();
    				    	
    			        	HttpPost post=new HttpPost("http://jabbrcube.heroku.com/api/words");
    			        	post.addHeader(BasicScheme.authenticate(
    			        			 new UsernamePasswordCredentials("jialiya", "password"),
    			        			 "UTF-8", false));

    			        	List<NameValuePair> form=new ArrayList<NameValuePair>();
    			        	form.add(new BasicNameValuePair("word[word]", et_Original.getText().toString()));
    			        	
    			        	post.setEntity(new UrlEncodedFormEntity(form, HTTP.UTF_8));
    			        	ResponseHandler<String> responseHandler=new BasicResponseHandler();
    			        	
    			        	String responseBody=client.execute(post, responseHandler);
    			        	JSONObject word = new JSONObject(responseBody);
    			        	String translated_word = word.getString("translation");
    			        	
    			        	Message msg_translated =  Message.obtain();
    			        	bundle.putString("Text", translated_word);
    			        	msg_translated.setData(bundle);
    		    			translationHandler.sendMessage(msg_translated);
    		    			
    		    		}
    		    		catch(Exception t){
    		    			System.out.println(t.toString());
    		    		}
    		    	}
    			  }).start();
    			}
    	   }
    	});
    }
    
    Handler translationHandler=new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
    		Bundle bundle = msg.getData();

    		tv_Translation.setText(bundle.getString("Text"));
    	}
    };
    
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
    
    Handler submissionHandler=new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
    		tv_Translation.setText("");
        	
        	CharSequence text = "Your Flashcard has been sucessfully added!";
        	Toast toast = Toast.makeText(getApplicationContext(), text,  Toast.LENGTH_SHORT);
        	toast.show();
    	}
    };
    
    public void addCard(View view){
    	// set everything up and send to web
    	original = ((EditText) findViewById(R.id.translate)).getText().toString();
    	translation = ((TextView) findViewById(R.id.translation)).getText().toString();
    	
    	Toast toast = Toast.makeText(getApplicationContext(), "added card",  Toast.LENGTH_SHORT);
    	toast.show();
    	
    	// new thread for posting
    	new Thread(new Runnable() {
    		public void run() {
    			try{
    				
    				HttpClient httpClient = new DefaultHttpClient();
    			    HttpContext localContext = new BasicHttpContext();
    			    HttpPost httpPost = new HttpPost("http://jabbrcube.heroku.com/api/flashcards");
    			    httpPost.addHeader(BasicScheme.authenticate(
    	        			 new UsernamePasswordCredentials("jialiya", "password"),
    	        			 "UTF-8", false));
    			    
    				// Add your data
    				MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    				
    				ByteArrayOutputStream bos = new ByteArrayOutputStream();
    				pic.compress(CompressFormat.JPEG, 100, bos);
    				byte[] data = bos.toByteArray();
    				entity.addPart("flashcard[photo]", new ByteArrayBody(data, original+".jpg"));
    				entity.addPart("flashcard[word_str]", new StringBody(original));
    				entity.addPart("flashcard[lat]", new StringBody(Double.toString(latitude)));
    				entity.addPart("flashcard[long]", new StringBody(Double.toString(longitude)));
    				entity.addPart("flashcard[category_id]", new StringBody("1"));
    				
    				httpPost.setEntity(entity);
    	
    		        HttpResponse response = httpClient.execute(httpPost, localContext);
    		        ResponseHandler<String> responseHandler=new BasicResponseHandler();
    				String s_response = httpClient.execute(httpPost, responseHandler);
    				submissionHandler.sendMessage(Message.obtain());
    				System.out.println(s_response);
    			}
    			catch(Throwable t){
    				System.out.println(t.toString());
    			}
    			
    		}
    	});

    }
}