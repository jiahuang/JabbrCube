package com.cube.jabbr;

import java.util.ArrayList;
import java.util.List;

import com.cube.jabbr.listView.Deck;
import com.cube.jabbr.listView.DeckAdapter;
import com.cube.jabbr.listView.LocationAdapter;
import com.cube.jabbr.listView.LocationView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChangeLoc extends Activity {
	private int contextId;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeloc);
        
        LocationView d1 = new LocationView("Olin", 1, 29.913131, 19.1934134, "1000 Olin Way");
        LocationView d2 = new LocationView("Babson", 1, 29.913131, 19.1934134, "1000 Babson Way");
        LocationView d3 = new LocationView("Nowhere", 1, 29.913131, 19.1934134, "USA");
        
        final ListView listview = (ListView)findViewById(R.id.list);
        listview.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
        		/*TODO: set preferences to change  */
        		

        		// show toast
        		LocationView item = (LocationView)listview.getItemAtPosition(position);
        		Context context = getApplicationContext();
        		contextId = item.id;
	        	CharSequence text = "Your location has been changed to "+item.context;
	        	int duration = Toast.LENGTH_SHORT;
	        	Toast toast = Toast.makeText(context, text, duration);
	        	toast.show();
	        	
	        	// move to startup page
	        	launchActivity();
            }
          });
        
        
        List<LocationView> locations = new ArrayList();
        locations.add(d1);
        locations.add(d2);
        locations.add(d3);
        listview.setAdapter(new LocationAdapter(this, locations));
    }
    
    public void launchActivity()
    {
		Intent i = new Intent(this, Startup.class);
		i.putExtra("locationId", contextId);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
    }
}