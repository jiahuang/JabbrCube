package com.cube.jabbr;

import com.cube.jabbr.R;
import com.cube.jabbr.R.layout;
import com.cube.jabbr.location.MyLocationListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Jabbrcube extends Activity {
	MyLocationListener mlocListener = new MyLocationListener();
	EditText et_Username; 
	EditText et_Password; 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        et_Username = (EditText) findViewById(R.id.username);
        et_Password = (EditText) findViewById(R.id.password);
    }
    
    public void login(View view){
    	SharedPreferences sharedPreferences = getSharedPreferences("jabbr_prefs", MODE_PRIVATE);
    	SharedPreferences.Editor editor = sharedPreferences.edit();
    	Boolean loginValid = sharedPreferences.getBoolean("loginValid", false);
    	/*if (!loginValid){
    		// auth stuff
    		
    		editor.putBoolean("loginValid", true);
    	}
    	else {
	    	Intent intent = new Intent().setClass(this, Startup.class);
	    	startActivity(intent);
    	}*/

		editor.putString("username", et_Username.getText().toString());
        editor.putString("password", et_Password.getText().toString());
    	LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
		mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
		editor.putLong("latitude", (long) mlocListener.latitude);
        editor.putLong("longitude", (long) mlocListener.longitude);
        Toast.makeText(this.getBaseContext(), "lat:"+mlocListener.latitude + " long:"+  mlocListener.longitude,
				Toast.LENGTH_SHORT).show();
    	Intent intent = new Intent().setClass(this, Startup.class);
    	startActivity(intent);
    	
    }
}