package com.cube.jabbr;

import com.cube.jabbr.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SignUp  extends Activity {
	Spinner s_fluent;
	Spinner s_learning;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        s_fluent = (Spinner) findViewById(R.id.fluentLanguages);
        s_learning = (Spinner) findViewById(R.id.learningLanguages);
        // TODO: populate with correct langauges
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, new String[] { "1", "2", "3" });
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s_fluent.setAdapter(adapter);
        s_learning.setAdapter(adapter);
    }
	
	public void create(View view){
		// TODO: make this sign user up
		/*SharedPreferences sharedPreferences = getSharedPreferences(Utils.PREF, MODE_PRIVATE);
    	SharedPreferences.Editor editor = sharedPreferences.edit();
    	
		editor.putString(Utils.PREF_NAME, et_Username.getText().toString());
        editor.putString(Utils.PREF_PASSWORD, et_Password.getText().toString());
        editor.commit();*/
    	Intent intent = new Intent().setClass(this, Startup.class);
    	startActivity(intent);
	}
}
