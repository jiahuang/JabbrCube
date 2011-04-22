package com.cube.jabbr;

import java.util.List;

import com.cube.jabbr.listView.FoursquareWrapper;
import com.cube.jabbr.listView.LocationAdapter;
import com.cube.jabbr.listView.Place;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChangeLoc extends Activity {
	private String contextId;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeloc);
        
        final ListView listview = (ListView)findViewById(R.id.list);
        
        FoursquareWrapper f = new FoursquareWrapper();
        
        SharedPreferences sharedPreferences = getSharedPreferences("jabbr_prefs", MODE_PRIVATE);
        float lat = sharedPreferences.getFloat("latitude", (float) 0.0);
        float lon = sharedPreferences.getFloat("longitude",(float) 0.0);
        Toast.makeText(this.getApplicationContext(), "lat "+lat+" lon:"+lon, Toast.LENGTH_SHORT).show();
        List<Place> locations =  f.getVenues(lat, lon);
        
        listview.setAdapter(new LocationAdapter(this, locations));
        
        listview.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
        		/*TODO: set preferences to change  */
        		

        		// show toast
        		Place item = (Place)listview.getItemAtPosition(position);
        		Context context = getApplicationContext();
        		contextId = item.id;
	        	CharSequence text = "Your location has been changed to "+item.name;
	        	int duration = Toast.LENGTH_SHORT;
	        	Toast toast = Toast.makeText(context, text, duration);

	    		// persistant location store
	    		SharedPreferences sharedPreferences = getSharedPreferences("jabbr_prefs", MODE_PRIVATE);
	    		
	    		SharedPreferences.Editor editor = sharedPreferences.edit();
	            editor.putString("placeId", contextId);
	            editor.putString("placeName", item.name);
	            editor.putFloat("latitude", (float) item.lat);
	            editor.putFloat("longitude", (float) item.lon);
	            editor.commit();
	        	toast.show();
	        	
	        	// move to startup page
	        	launchActivity();
            }
          });
        
        
    }
    
    public void launchActivity()
    {
		Intent i = new Intent(this, Startup.class);
		i.putExtra("locationId", contextId);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
    }
}