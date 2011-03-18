package com.cube.jabbr.listView;

import java.util.List;

import com.cube.jabbr.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


 public class DeckAdapter extends BaseAdapter
    {

    	private List<Deck> decks;
    	private Context c;
    	
    	public DeckAdapter(Context context, List<Deck> decks) {
			this.decks = decks;
			this.c = context;
		}
		

        public Object getItem(int position) {

            return decks.get(position);
        }

        public long getItemId(int id) {

            return id;
        }

		
		public View getView(int position, View convertView, ViewGroup parent)
	    {

	        LinearLayout rowLayout;
	        Deck t = decks.get(position);
	        TextView tv;
	        TextView tv2;

	        if (convertView == null)
	        {
	            rowLayout = (LinearLayout)LayoutInflater.from(this.c).inflate
	                      (R.layout.deck_row, parent, false);
	           tv = (TextView)rowLayout.findViewById(R.id.context);
	           tv.setText(t.context);
	           
	           //rowLayout = (LinearLayout)LayoutInflater.from(c).inflate
               //(R.layout.user_row, parent, false);
	           tv2 = (TextView)rowLayout.findViewById(R.id.numOfCards);
	           tv2.setText(Integer.toString(t.numOfCards));
	           
	        } else {
	            rowLayout = (LinearLayout)convertView;
	        }
	        return rowLayout;
	    }


		@Override
		public int getCount() {
			return decks.size();
		}
	}