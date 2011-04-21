package com.cube.jabbr;

import java.util.List;

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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Startup extends Activity {
    /** Called when the activity is first created. */
	LocationManager mlocManager = null;
	ProgressDialog mDialog = null;
	float lon = 0;
	float lat = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
        
        mDialog = new ProgressDialog(Startup.this);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage("Aquiring Location. Please wait...");
        mDialog.show();

        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,onLocationChange);
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,onLocationChange);
        
        GridView gridview = (GridView) findViewById(R.id.currentCards);
        List<Thumbnail> thumbnails = (new ThumbnailObtainer()).getThumbnails();
        gridview.setAdapter(new ThumbnailAdapter(this, thumbnails));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(Startup.this, "" + position, Toast.LENGTH_SHORT).show();
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
		        mDialog.dismiss();
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
    
    public void changeLoc(View view){
    	Intent intent = new Intent().setClass(this, ChangeLoc.class);
    	startActivityForResult(intent, 0);
    	
    }
}