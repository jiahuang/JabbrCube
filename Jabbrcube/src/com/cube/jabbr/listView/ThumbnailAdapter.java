package com.cube.jabbr.listView;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ThumbnailAdapter extends BaseAdapter {
    private Context mContext;
    private List<Thumbnail> mThumbnails;

    public ThumbnailAdapter(Context c, List<Thumbnail> thumbnails) {
        mContext = c;
        mThumbnails = thumbnails;
    }

    public int getCount() {
        return mThumbnails.size();
    }

    public Object getItem(int position) {
        return mThumbnails.get(position);
    }

    public long getItemId(int id) {
        return id;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageDrawable(mThumbnails.get(position).image);
        return imageView;
	}
}