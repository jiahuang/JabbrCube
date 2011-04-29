package com.cube.jabbr;

import com.cube.jabbr.R;
import com.cube.jabbr.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Jabbrcube extends Activity {
	
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
    	SharedPreferences sharedPreferences = getSharedPreferences(Utils.PREF, MODE_PRIVATE);
    	SharedPreferences.Editor editor = sharedPreferences.edit();
    	Boolean loginValid = sharedPreferences.getBoolean(Utils.PREF_VALIDLOGIN, false);
    	/*if (!loginValid){
    		// auth stuff
    		
    		editor.putBoolean("loginValid", true);
    	}
    	else {
	    	Intent intent = new Intent().setClass(this, Startup.class);
	    	startActivity(intent);
    	}*/

		editor.putString(Utils.PREF_NAME, et_Username.getText().toString());
        editor.putString(Utils.PREF_PASSWORD, et_Password.getText().toString());
        editor.commit();
    	Intent intent = new Intent().setClass(this, Startup.class);
    	startActivity(intent);
    	
    }
    
    public void signup(View view){
    	
    	Intent intent = new Intent().setClass(this, SignUp.class);
    	startActivity(intent);
    	
    }
}