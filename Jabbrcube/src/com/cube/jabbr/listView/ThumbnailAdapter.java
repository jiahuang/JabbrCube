package com.cube.jabbr.listView;

import java.util.List;

import com.cube.jabbr.Startup;
import com.cube.jabbr.utils.GridviewThread;
import com.cube.jabbr.utils.GridviewThread.GridviewThreadListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class ThumbnailAdapter extends BaseAdapter implements GridviewThreadListener{
    private Context mContext;
    //private List<Thumbnail> mThumbnails;
    private String[] mUrls;
    private Handler mHandler;
    private GridviewThread mImageLoader = null;
    private String[] mWords;
    private String[] mForeign;
    private int numOfCards;
    
    public ThumbnailAdapter(Context c, String[] urls, String[] words, String[] foreign, int numOfCards){//List<Thumbnail> thumbnails) {
    	mWords= words;
    	mForeign = foreign;
    	this.numOfCards = numOfCards;
        mContext = c;
        mUrls = urls;
        mImageLoader = new GridviewThread(this);
		mImageLoader.start();
		mHandler = new Handler();
    }

    @Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();

		// stop the thread we started
		mImageLoader.stopThread();
	}
    
    public int getCount() {
        return mUrls.length;
    }

    public String getItem(int position) {
        return mUrls[position];
    }

    public long getItemId(int id) {
        return id;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
    	final ViewSwitcher lViewSwitcher;
		String url = mUrls[position];
		ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	lViewSwitcher = new ViewSwitcher(mContext);
			lViewSwitcher.setPadding(0, 0, 0, 0);
			ProgressBar lProgress = new ProgressBar(mContext);
			lProgress.setLayoutParams(new ViewSwitcher.LayoutParams(40, 40));
			lViewSwitcher.addView(lProgress);
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
            lViewSwitcher.addView(imageView);
            
            // attach the onclick listener
			//lViewSwitcher.setOnClickListener(new ClickHandler());
        } else {
        	lViewSwitcher = (ViewSwitcher) convertView;
        }
        
        ViewTagInformation lTagHolder = (ViewTagInformation) lViewSwitcher
		.getTag();
        if (lTagHolder == null || 
    			!lTagHolder.url.equals(url)) {
    			// The Tagholder is null meaning this is a first time load
    			// or this view is being recycled with a different image
    			
    			// Create a ViewTag to store information for later
    			ViewTagInformation lNewTag = new ViewTagInformation();
    			lNewTag.url = url;
    			lViewSwitcher.setTag(lNewTag);

    			// Grab the image view
    			// Have the progress bar display
    			// Then queue the image loading
    			ImageView lImageView = (ImageView) lViewSwitcher.getChildAt(1);
    			lViewSwitcher.setDisplayedChild(0);
    			mImageLoader.queueImageLoad(url, lImageView, lViewSwitcher);
    		}
        //imageView.setImageDrawable(mThumbnails.get(position).image);
        return lViewSwitcher;
	}
    
	@Override
	public void handleImageLoaded(final ViewSwitcher vs, final ImageView iv, final Drawable draw) {
		// The enqueue the following in the UI thread
		mHandler.post(new Runnable() {
			public void run() {
				
				// set the bitmap in the ImageView
				//aImageView.setImageBitmap(aBitmap);
				iv.setImageDrawable(draw);
				// explicitly tell the view switcher to show the second view
				vs.setDisplayedChild(1);
			}
		});
		
	}
}

class ViewTagInformation {
	String url;
}