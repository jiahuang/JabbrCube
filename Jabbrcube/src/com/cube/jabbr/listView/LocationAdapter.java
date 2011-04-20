package com.cube.jabbr.listView;

import java.util.List;

import com.cube.jabbr.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


 public class LocationAdapter extends BaseAdapter
    {

    	private List<Place> locations;
    	private Context c;
    	
    	public LocationAdapter(Context context, List<Place> locations) {
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
			ViewHolder holder;

	        LinearLayout rowLayout;
	        Place t = locations.get(position);
	        TextView tv;
	        TextView tv2;

	        if (convertView == null)
	        {
	           convertView = LayoutInflater.from(this.c).inflate(R.layout.location_row, parent, false);

               // Creates a ViewHolder and store references to the two children views
               // we want to bind data to.
               holder = new ViewHolder();
               holder.text = (TextView)convertView.findViewById(R.id.address);
               //holder.icon = (ImageView) convertView.findViewById(R.id.icon);
               convertView.setTag(holder);
	           
	        } else {
	        	holder = (ViewHolder) convertView.getTag();
	        }
	        holder.text.setText(locations.get(position).name);
	        return convertView;
	    }

		static class ViewHolder {
            TextView text;
            //ImageView icon;
        }

		@Override
		public int getCount() {
			return locations.size();
		}
	}