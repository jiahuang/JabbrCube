package com.cube.jabbr;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


 public class ResultsAdapter extends BaseAdapter
    {

    	private List<String> results;
    	private List<Integer> right;
    	private Context c;
    	
    	public ResultsAdapter(Context context, List<String> results) {
			this.results = results;
			this.c = context;
		}
		public void setUserGotRight(List<Integer> right) {
			this.right = right;
		}

        public Object getItem(int position) {

            return results.get(position);
        }

        public long getItemId(int id) {

            return id;
        }

		
		public View getView(int position, View convertView, ViewGroup parent)
	    {
			ViewHolder holder;

	        LinearLayout rowLayout;
	        String t = results.get(position);
	        TextView tv;
	        TextView tv2;

	        if (convertView == null)
	        {
	        	if (right.get(position).intValue() >0 ) {
	 	           convertView = LayoutInflater.from(this.c).inflate(R.layout.result_row_right, parent, false);

	        	} else {
	 	           convertView = LayoutInflater.from(this.c).inflate(R.layout.result_row, parent, false);

	        	}

               // Creates a ViewHolder and store references to the two children views
               // we want to bind data to.
               holder = new ViewHolder();
               holder.text = (TextView)convertView.findViewById(R.id.context);
               //holder.icon = (ImageView) convertView.findViewById(R.id.icon);
               convertView.setTag(holder);
	           
	        } else {
	        	holder = (ViewHolder) convertView.getTag();
	        }
	        holder.text.setText(results.get(position));
	        return convertView;
	    }

		static class ViewHolder {
            TextView text;
            //ImageView icon;
        }

		@Override
		public int getCount() {
			return results.size();
		}
	}