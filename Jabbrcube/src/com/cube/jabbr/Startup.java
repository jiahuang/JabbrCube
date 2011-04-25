package com.cube.jabbr;

import java.util.List;

import com.cube.jabbr.listView.Place;
import com.cube.jabbr.listView.Thumbnail;
import com.cube.jabbr.listView.ThumbnailAdapter;
import com.cube.jabbr.listView.ThumbnailObtainer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Startup extends Activity {
    /** Called when the activity is first created. */
	LocationManager mlocManager = null;
	ProgressDialog mDialog = null;
	float lon = 0;
	float lat = 0;
	boolean dialog = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
        
        TextView tv_location = (TextView) findViewById(R.id.location);
        
        SharedPreferences sharedPreferences = getSharedPreferences("jabbr_prefs", MODE_PRIVATE);
        lon = sharedPreferences.getFloat("latitude", 0);
        lat = sharedPreferences.getFloat("longitude", 0);
        String placeId = sharedPreferences.getString("placeId", "");
        tv_location.setText(sharedPreferences.getString("placeName", "Could not acquire location"));
        if (lon == 0 && lat == 0 && placeId == ""){
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
        
        final GridView gridview = (GridView) findViewById(R.id.currentCards);
        List<Thumbnail> thumbnails = (new ThumbnailObtainer()).getThumbnails();
        gridview.setAdapter(new ThumbnailAdapter(this, thumbnails));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(Startup.this, "" + position, Toast.LENGTH_SHORT).show();
                // pass bundle
                Thumbnail item = (Thumbnail)gridview.getItemAtPosition(position);
                Intent myIntent = new Intent(Startup.this, ViewCard.class);
                // TODO: swap this out with real image url
        		myIntent.putExtra("image_url", "http://imagemacros.files.wordpress.com/2009/06/dunnololdog.jpg");
        		myIntent.putExtra("word", item.word);
        		myIntent.putExtra("foreign", item.foreign);
                startActivityForResult(myIntent, 0);
            }
        });
		
		
	}
    
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
			SharedPreferences sharedPreferences = getSharedPreferences("jabbr_prefs", MODE_PRIVATE);
	    	SharedPreferences.Editor editor = sharedPreferences.edit();
	    	editor.putFloat("latitude", (float)location.getLatitude());
	        editor.putFloat("longitude", (float)location.getLongitude());
	        editor.commit();
	        lat = sharedPreferences.getFloat("latitude", (float) 0.0);
	        lon = sharedPreferences.getFloat("longitude",(float) 0.0);
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