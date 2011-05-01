package com.cube.jabbr;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
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
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cube.jabbr.MIME.HttpMultipartMode;
import com.cube.jabbr.MIME.MultipartEntity;
import com.cube.jabbr.content.ByteArrayBody;
import com.cube.jabbr.content.StringBody;

public class NewCard extends Activity {
	final int CAMERA_PIC_REQUEST = 1;
	Bitmap pic;
	double longitude = 0;
	double latitude = 0;
	String original = "";
	String translation = "";
	TextView tv_Translation;
	TextView tv_TakeAPic;
	EditText et_Original;
	ImageButton ib_Camera;
	ImageView iv_Image;
	Bitmap resizedBitmap;
	Button b_AddCard;
    boolean dialog = false;
    boolean tookPic = false;
	
    ProgressDialog mDialog = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newcard);
        tv_Translation = (TextView) findViewById(R.id.translation);
        et_Original = (EditText) findViewById(R.id.translate);
        ib_Camera = (ImageButton) findViewById(R.id.Camera);
        iv_Image = (ImageView) findViewById(R.id.image);
        b_AddCard = (Button) findViewById(R.id.addCard);
        tv_TakeAPic = (TextView) findViewById(R.id.takePicText);
        mDialog =  new ProgressDialog(NewCard.this);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        
        // hide everything
        tv_Translation.setVisibility(View.INVISIBLE);
        ib_Camera.setVisibility(View.INVISIBLE);
        iv_Image.setVisibility(View.INVISIBLE);
        b_AddCard.setVisibility(View.INVISIBLE);
        tv_TakeAPic.setVisibility(View.INVISIBLE);
    }
    
    public void translateWord(View view){
    	if (!dialog){
    		dialog = true;
    		mDialog.setMessage("Translating... Please wait...");
	        mDialog.show();
    	}
    	new Thread(new Runnable() {
			public void run(){
	    		try{
	    			int flag = 0;
	    			original = et_Original.getText().toString();
		        	if (original.endsWith(" ")){
		        		original = original.substring(0, original.length() - 1);
		        	}
		        	if (original.contains(" ")){ // can't translate more than one word
		        		Message msg =  Message.obtain();
		    			Bundle bundle = new Bundle();
		    			bundle.putString("Text", "Sorry, we can make flashcards for only individual words, not phrases or sentences");
		    			bundle.putInt("Error", 1);
		    			msg.setData(bundle);
		    			translationHandler.sendMessage(msg);
		    			flag = 1;
		        	}
		        	if(original.compareTo("") == 0){
		        		Message msg =  Message.obtain();
		    			Bundle bundle = new Bundle();
		    			bundle.putString("Text", "Please type in something to translate");
		    			bundle.putInt("Error", 1);
		    			msg.setData(bundle);
		    			translationHandler.sendMessage(msg);
		    			flag = 1;
		        	}
		        	if (flag == 0) {
		    			Message msg =  Message.obtain();
		    			Bundle bundle = new Bundle();
		    			bundle.putString("Text", "Translating...");
		    			bundle.putInt("Error", 0);
		    			msg.setData(bundle);
		    			translationHandler.sendMessage(msg);
		    			
		    			DefaultHttpClient client = new DefaultHttpClient();
				    	
			        	HttpPost post=new HttpPost("http://jabbrcube.heroku.com/api/words");
			        	post.addHeader(BasicScheme.authenticate(
			        			 new UsernamePasswordCredentials("poorva", "password"),
			        			 "UTF-8", false));
	
			        	List<NameValuePair> form=new ArrayList<NameValuePair>();
			        	
			        	form.add(new BasicNameValuePair("word[word]", original));
			        	
			        	post.setEntity(new UrlEncodedFormEntity(form, HTTP.UTF_8));
			        	ResponseHandler<String> responseHandler=new BasicResponseHandler();
			        	
			        	String responseBody=client.execute(post, responseHandler);
			        	JSONObject word = new JSONObject(responseBody);
			        	String translated_word = word.getString("translation");
			        	
			        	Message msg_translated =  Message.obtain();
			        	bundle.putString("Text", translated_word);
			        	bundle.putInt("Error", 0);
			        	msg_translated.setData(bundle);
		    			translationHandler.sendMessage(msg_translated);
		        	}
	    			
	    		}
	    		catch(Exception t){
	    			System.out.println(t.toString());
	    		}
	    	}
		  }).start();
    }
    
    Handler translationHandler=new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
    		Bundle bundle = msg.getData();

    		tv_Translation.setText(bundle.getString("Text"));
    		tv_Translation.setVisibility(View.VISIBLE);
    		if (bundle.getInt("Error") != 1){
    		// show everything
	    		if (!tookPic){
	                ib_Camera.setVisibility(View.VISIBLE);
	                tv_TakeAPic.setVisibility(View.VISIBLE);
	    		}
	    	}
    		else{
    			tv_TakeAPic.setVisibility(View.INVISIBLE);
    			ib_Camera.setVisibility(View.INVISIBLE);
    	        iv_Image.setVisibility(View.INVISIBLE);
    	        b_AddCard.setVisibility(View.INVISIBLE);
    		}
    		if (dialog) {
    			dialog = false;
    			mDialog.dismiss();
    		}
    	}
    };
    
    public void camera(View view){
    	Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    	cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(this)) ); 
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	if (requestCode == CAMERA_PIC_REQUEST){
            
    		final File file = getTempFile(this);
            try {
            	pic = Media.getBitmap(getContentResolver(), Uri.fromFile(file) );
        		
            	int srcWidth = pic.getWidth();
                int srcHeight = pic.getHeight();
                int desiredWidth = 350;
                int desiredHeight = 350;
      			// do whatever you want with the bitmap (Resize, Rename, Add To Gallery, etc)
                if (srcWidth > srcHeight && srcWidth > desiredWidth){
                	float scaleRatio = (float)srcWidth/(float)desiredWidth;
                    float newHeight = (float)srcHeight / scaleRatio;
                    resizedBitmap = Bitmap.createScaledBitmap(pic, desiredWidth, (int)newHeight, true);
                    pic.recycle();
                }
                else if (srcHeight > desiredHeight){
                	float scaleRatio = (float)srcHeight/(float)desiredWidth;
                    float newWidth = (float)srcWidth/scaleRatio;
                    resizedBitmap = Bitmap.createScaledBitmap(pic, (int)newWidth, desiredHeight, true);
                    pic.recycle();
                }
                
                tookPic = true;
                et_Original.setText(original);
      			iv_Image.setImageBitmap(resizedBitmap);
      			iv_Image.setVisibility(View.VISIBLE);
                b_AddCard.setVisibility(View.VISIBLE);
      			// remove take picture button
      			ib_Camera.setVisibility(View.GONE);
      			tv_TakeAPic.setVisibility(View.GONE);
            } catch (FileNotFoundException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            }
    		
    	}
    }
    
    private File getTempFile(Context context){
    	  //it will return /sdcard/image.tmp
    	  final File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName() );
    	  if(!path.exists()){
    	    path.mkdir();
    	  }
    	  return new File(path, "image.tmp");
    	}
    
    Handler submissionHandler=new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
        	
        	CharSequence text = "Your Flashcard has been sucessfully added!";
        	Toast toast = Toast.makeText(getApplicationContext(), text,  Toast.LENGTH_SHORT);
        	toast.show();
        	
        	if (dialog) {
    			dialog = false;
    			mDialog.dismiss();
    		}
			// reset text translation, camera, and picture
        	tookPic = false;
			iv_Image.setImageDrawable(null);
    		tv_Translation.setText("");
			if (resizedBitmap != null)
        		resizedBitmap.recycle();
			iv_Image.setVisibility(View.INVISIBLE);
			b_AddCard.setVisibility(View.INVISIBLE);
  			//ib_Camera.setVisibility(View.VISIBLE);
    	}
    };
    
    public void addCard(View view){
    	// set everything up and send to web
    	//original = ((EditText) findViewById(R.id.translate)).getText().toString();
    	translation = tv_Translation.getText().toString();
    	if (!dialog){
    		dialog = true;
    		mDialog.setMessage("Adding your flashcard... Please wait...");
	        mDialog.show();
    	}
    	// new thread for posting
    	new Thread(new Runnable() {
    		public void run() {
    			try{
    				System.out.println("uploading new flashcard");
    				HttpClient httpClient = new DefaultHttpClient();
    			    HttpContext localContext = new BasicHttpContext();
    			    HttpPost httpPost = new HttpPost("http://jabbrcube.heroku.com/api/flashcards");
    			    httpPost.addHeader(BasicScheme.authenticate(
    	        			 new UsernamePasswordCredentials("poorva", "password"),
    	        			 "UTF-8", false));
    			    
    				// Add your data
    				MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    				
    				ByteArrayOutputStream bos = new ByteArrayOutputStream();
    				resizedBitmap.compress(CompressFormat.JPEG, 100, bos);
    				byte[] data = bos.toByteArray();
    				entity.addPart("flashcard[photo]", new ByteArrayBody(data, original+".jpg"));
    				entity.addPart("flashcard[word_str]", new StringBody(original));
    				entity.addPart("flashcard[place_id]", new StringBody("1"));
    				
    				httpPost.setEntity(entity);
    	
    		        HttpResponse response = httpClient.execute(httpPost, localContext);
    		        ResponseHandler<String> responseHandler=new BasicResponseHandler();
    				String s_response = httpClient.execute(httpPost, responseHandler);
    				submissionHandler.sendMessage(Message.obtain());
    				System.out.println(s_response);
    			}
    			catch(Throwable t){
    				System.out.println("Exception: "+ t.toString());
    			}
    			
    		}
    	}).start();

    }
}