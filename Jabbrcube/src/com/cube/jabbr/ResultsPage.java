package com.cube.jabbr;

import java.util.Arrays;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ResultsPage extends ListActivity {
	int pos_start = 0, num;
	String[] listOfWords, listOfForeign, listOfImageUrls, listOfResults;
	Integer[] listOfRight;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		final ListView listview = (ListView)findViewById(android.R.id.list);
		//Toast.makeText(this.getApplicationContext(), "results", Toast.LENGTH_SHORT).show();
		
		
		Bundle extras = getIntent().getExtras();
		num = (Integer) extras.get("num");
		listOfWords = new String[num];
        listOfForeign = new String[num];
        listOfImageUrls = new String[num];
        
        listOfResults = new String[num+1];
        listOfRight = new Integer[num+1];
        
        // grab rest of bundle
        for(int i =0; i<num; i++){
        	listOfWords[i] = extras.getString("words"+i);
        	listOfForeign[i] = extras.getString("foreign"+i);
        	listOfImageUrls[i] = extras.getString("image_urls"+i);
        	listOfResults[i] = listOfWords[i] + " = " + listOfForeign[i];
        	listOfRight[i] = extras.getInt("user_got_right"+i);
        	/*Log.i("jabbr","debundled:"+i);
        	Log.i("jabbr",listOfWords[i] );
        	Log.i("jabbr",listOfImageUrls[i] );*/
        }
        
        listOfResults[num] = "Exit";
        listOfRight[num] = 0;
		ResultsAdapter ra = new ResultsAdapter(this, Arrays.asList(listOfResults));
		ra.setUserGotRight(Arrays.asList(listOfRight));
		listview.setAdapter(ra);
		listview.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
        		pos_start = position;
        		
        		/*String item = (String)listview.getItemAtPosition(position);
        		Context context = getApplicationContext();
	        	CharSequence text = ""+item;
	        	int duration = Toast.LENGTH_SHORT;
	        	Toast toast = Toast.makeText(context, text, duration);
	        	toast.show();*/
	        	launchActivity();
        	}
		});
		
	}
	
	public void launchActivity()
    {
		if (pos_start >= num) {
			Intent i = new Intent(this, Startup.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			//Toast.makeText(getApplicationContext(), "EXIT", Toast.LENGTH_SHORT).show();
		} else {
		Intent i = new Intent(this, ViewCard.class);
		i.putExtra("pos_start", pos_start);
		i.putExtra("num", num);
		for(int it=0; it<num; it++){
			
			//Log.i("jabbr", "respagesend:"+listOfWords[it]);
        	i.putExtra("words"+it, listOfWords[it]);
        	i.putExtra("foreign"+it, listOfForeign[it]);
        	i.putExtra("image_urls"+it, listOfImageUrls[it]);
        	//Log.i("jabbr","send:"+i.getStringExtra("words"+it));
        }
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		}
    }
}
