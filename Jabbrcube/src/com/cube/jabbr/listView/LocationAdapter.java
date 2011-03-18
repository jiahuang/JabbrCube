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


 public class LocationAdapter extends BaseAdapter
    {

    	private List<LocationView> locations;
    	private Context c;
    	
    	public LocationAdapter(Context context, List<LocationView> locations) {
			this.locations = locations;
			this.c = context;
		}
		

        public Object getItem(int position) {

            return locations.get(position);
        }

        public long getItemId(int id) {

            return id;
        }

		
		public View getView(int position, View convertView, ViewGroup parent)
	    {

	        LinearLayout rowLayout;
	        LocationView t = locations.get(position);
	        TextView tv;
	        TextView tv2;

	        if (convertView == null)
	        {
	            rowLayout = (LinearLayout)LayoutInflater.from(this.c).inflate
	                      (R.layout.location_row, parent, false);
	           tv = (TextView)rowLayout.findViewById(R.id.context);
	           tv.setText(t.context);
	           
	           //rowLayout = (LinearLayout)LayoutInflater.from(c).inflate
               //(R.layout.user_row, parent, false);
	           tv2 = (TextView)rowLayout.findViewById(R.id.address);
	           tv2.setText(t.address);
	           
	        } else {
	            rowLayout = (LinearLayout)convertView;
	        }
	        return rowLayout;
	    }


		@Override
		public int getCount() {
			return locations.size();
		}
	}