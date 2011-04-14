package com.cube.jabbr;

import com.cube.jabbr.location.MyLocationListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

public class Startup extends Activity {
    /** Called when the activity is first created. */
	MyLocationListener mlocListener = new MyLocationListener();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup);
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
       
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				mlocListener);
		mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
				mlocListener);
		// persistant location store
		SharedPreferences sharedPreferences = getSharedPreferences("jabbr_prefs", MODE_PRIVATE);
		/*Boolean loginValid = sharedPreferences.getBoolean();
		SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("loginValid", true);
        editor.putString("username", username);
        editor.putString("password", password);
		*/

    }
    
    public void game(View view){
    	Intent intent = new Intent().setClass(this, Game.class);
    	startActivity(intent);
    }
    
    public void newCard(View view){
    	Intent intent = new Intent().setClass(this, NewCard.class);
    	startActivity(intent);
    }
    
    public void changeDeck(View view){
    	Intent intent = new Intent().setClass(this, ChangeDeck.class);
    	startActivityForResult(intent, 0);
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