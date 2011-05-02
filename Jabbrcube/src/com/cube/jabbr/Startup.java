package com.cube.jabbr;

import java.util.List;

import com.cube.jabbr.listView.Place;
import com.cube.jabbr.listView.Thumbnail;
import com.cube.jabbr.listView.ThumbnailAdapter;
import com.cube.jabbr.listView.ThumbnailObtainer;
import com.cube.jabbr.utils.NoBackgroundDialog;
import com.cube.jabbr.utils.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Startup extends Activity {
    /** Called when the activity is first created. */
	LocationManager mlocManager = null;
	ProgressDialog mDialog = null;
	NoBackgroundDialog mNoBgDialog = null;
	float lon = 0;
	float lat = 0;
	boolean dialog = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
        
        TextView tv_location = (TextView) findViewById(R.id.location);
        
        SharedPreferences sharedPreferences = getSharedPreferences(Utils.PREF, MODE_PRIVATE);
        lon = sharedPreferences.getFloat(Utils.PREF_LONGITUDE, 0);
        lat = sharedPreferences.getFloat(Utils.PREF_LATITUDE, 0);
        String placeId = sharedPreferences.getString(Utils.PREF_PLACEID, "");
        tv_location.setText(sharedPreferences.getString(Utils.PREF_PLACENAME, "Could not acquire location"));
        if (lon == 0 && lat == 0 && placeId == "" ){ 
        	dialog = true;
        	System.out.println("displaying... id:"+placeId + " lon:"+lon + " lat:"+lat);
	        mDialog = new ProgressDialog(Startup.this);
	        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        mDialog.setMessage("Aquiring Location. Please wait...");
	        mDialog.show();
        }

        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,onLocationChange);
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,onLocationChange);
        
        new Thread(new Runnable() {
			public void run(){
				try{
				ThumbnailObtainer tbo = new ThumbnailObtainer();
				Message msg =  Message.obtain();
    			Bundle bundle = new Bundle();
    			bundle.putStringArray("words", tbo.listOfWords);
    			bundle.putStringArray("foreign", tbo.listOfForeign);
    			bundle.putStringArray("images", tbo.listOfImageUrls);
    			bundle.putStringArray("thumbnails", tbo.listOfThumbnailUrls);
    			bundle.putInt("numOfCards", tbo.numOfCards);
    			msg.setData(bundle);
    			thumbnailHandler.sendMessage(msg);
				}
				catch(Exception e){
					System.out.println(e.toString());
				}
			}
        }).start();
	}
    
    Handler thumbnailHandler=new Handler() {
    	@Override
    	public void handleMessage(Message msg) {
    		Bundle bundle = msg.getData();
    		final int numOfCards = bundle.getInt("numOfCards");
            final String[] listOfWords = bundle.getStringArray("words");
            final String[] listOfForeign = bundle.getStringArray("foreign");
            final String[] listOfThumbnailUrls = bundle.getStringArray("thumbnails");
            final String[] listOfImageUrls = bundle.getStringArray("images");

            final GridView gridview = (GridView) findViewById(R.id.currentCards);
            if (numOfCards >0){
            ThumbnailAdapter gridAdapter = new ThumbnailAdapter(Startup.this.getApplicationContext(),
            		listOfThumbnailUrls, listOfWords, listOfForeign, numOfCards);
            gridview.setAdapter(gridAdapter);
            
            gridview.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Toast.makeText(Startup.this, "" + position, Toast.LENGTH_SHORT).show();
                    // pass bundle
                    Intent myIntent = new Intent(Startup.this, ViewCard.class);
                    myIntent.putExtra("pos_start", position);
                    myIntent.putExtra("num", numOfCards);
                    for(int i=0; i<numOfCards; i++){
                    	myIntent.putExtra("words"+i, listOfWords[i]);
                    	myIntent.putExtra("foreign"+i, listOfForeign[i]);
                    	myIntent.putExtra("image_urls"+i, listOfImageUrls[i]);
                    }
                    startActivityForResult(myIntent, 0);
                }
            });
            }
    	}
    };
    
    LocationListener onLocationChange=new LocationListener() {
        public void onProviderDisabled(String provider) {
        // required for interface, not used
        }
         
        public void onProviderEnabled(String provider) {
        // required for interface, not used
        }
         
        public void onStatusChanged(String provider, int status,
        Bundle extras) {
        // required for interface, not used
        }

		@Override
		public void onLocationChanged(Location location) {
			SharedPreferences sharedPreferences = getSharedPreferences(Utils.PREF, MODE_PRIVATE);
	    	SharedPreferences.Editor editor = sharedPreferences.edit();
	    	editor.putFloat(Utils.PREF_LATITUDE, (float)location.getLatitude());
	        editor.putFloat(Utils.PREF_LONGITUDE, (float)location.getLongitude());
	        editor.commit();
	        lat = sharedPreferences.getFloat(Utils.PREF_LATITUDE, (float) 0.0);
	        lon = sharedPreferences.getFloat(Utils.PREF_LONGITUDE,(float) 0.0);
	        System.out.println("location changed to:"+ location.getLatitude()+ " "+location.getLongitude());
	        if (dialog) {
	        	mDialog.dismiss();
	        	changeLoc();
	        }
		}
    };

    @Override
    public void onPause() {
        super.onPause();
        mlocManager.removeUpdates(onLocationChange);
    }
    
    //reactivates listener when app is resumed
    @Override
    public void onResume() {
        super.onResume();
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,onLocationChange);
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,onLocationChange);
    }

    public void game(View view){
    	Intent intent = new Intent().setClass(this, Game.class);
    	startActivity(intent);
    }
    
    public void newCard(View view){
    	Intent intent = new Intent().setClass(this, NewCard.class);
    	startActivity(intent);
    }
    
    public void profile(View view){
    	Intent intent = new Intent().setClass(this, Personal.class);
    	startActivity(intent);
    }
    
    public void changeLoc(){
    	Intent intent = new Intent().setClass(this, ChangeLoc.class);
    	startActivityForResult(intent, 0);
    }
    
    public void changeLoc(View view){
    	changeLoc();
    	System.out.println("change location popup");
    }

}