package com.cube.jabbr;

import com.cube.jabbr.R;
import com.cube.jabbr.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Jabbrcube extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void login(View view){
    	// log in and swap activities
    	
    	Intent intent = new Intent().setClass(this, Tabs.class);
    	startActivityForResult(intent, 0);
    	// start acquiring location
    }
}