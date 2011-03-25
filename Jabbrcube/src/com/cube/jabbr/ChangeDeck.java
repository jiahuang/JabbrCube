package com.cube.jabbr;

import java.util.ArrayList;
import java.util.List;

import com.cube.jabbr.listView.Deck;
import com.cube.jabbr.listView.DeckAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChangeDeck extends Activity {
	private int contextId;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changedeck);
        
        Deck d1 = new Deck("Resturant", 1, 10);
        Deck d2 = new Deck("Herp Derp", 1, 10);
        Deck d3 = new Deck("Stuff I fail at", 1, 100);
        
        final ListView listview = (ListView)findViewById(R.id.list);
        listview.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
        		/*TODO: set preferences to change  */
        		

        		// show toast
        		Deck item = (Deck)listview.getItemAtPosition(position);
        		Context context = getApplicationContext();
        		contextId = item.id;
	        	CharSequence text = "Your flash card deck has been changed to "+item.context;
	        	int duration = Toast.LENGTH_SHORT;
	        	Toast toast = Toast.makeText(context, text, duration);
	        	toast.show();
	        	
	        	// move to startup page
	        	launchActivity();
            }
          });
        
        
        List<Deck> decks = new ArrayList();
        decks.add(d1);
        decks.add(d2);
        decks.add(d3);
        listview.setAdapter(new DeckAdapter(this, decks));
        
    }
    
    public void launchActivity()
    {
		Intent i = new Intent(this, Startup.class);
		i.putExtra("deckId", contextId);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
    }
}