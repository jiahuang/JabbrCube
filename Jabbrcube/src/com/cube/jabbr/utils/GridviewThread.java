package com.cube.jabbr.utils;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

public class GridviewThread extends Thread {
	public interface GridviewThreadListener {
		void handleImageLoaded(ViewSwitcher vs, ImageView iv, Drawable draw);
	}
	GridviewThreadListener mListener = null;
	private Handler mHandler;
	
	public GridviewThread(GridviewThreadListener lListener){
		mListener = lListener;
	}
	
	@Override
	public void run() {
		try {
			
			// preparing a looper on current thread			
			// the current thread is being detected implicitly
			Looper.prepare();
			
			// Looper gets attached to the current thread by default
			mHandler = new Handler();
			
			Looper.loop();
			// Thread will start
			
		} catch (Throwable t) {
			System.out.println(t.toString());
		} 
	}
	
	public synchronized void stopThread() {
		
		// Use the handler to schedule a quit on the looper
		mHandler.post(new Runnable() {
			
			public void run() {
				System.out.println("GridviewThread quitting");
				
				Looper.myLooper().quit();
			}
		});
	}
	
	public synchronized void queueImageLoad(
			final String url, 
			final ImageView iv, 
			final ViewSwitcher vs) {
		
		// Wrap DownloadTask into another Runnable to track the statistics
		mHandler.post(new Runnable() {
			public void run() {
				try {
					
					synchronized (iv){
						// make sure this thread is the only one performing activities on
						// this imageview
						//BitmapFactory.Options lOptions = new BitmapFactory.Options();
						//lOptions.inSampleSize = 1;
						//Bitmap lBitmap = BitmapFactory.decodeFile(aPath, lOptions);
						//aImage.setImageBitmap(lBitmap);
						Drawable image = Utils.loadDrawable(url);
						// Load the image here
						if(mListener != null){
							// we have an object that implements ImageLoadListener
							
							mListener.handleImageLoaded(vs, iv, image);
						}
					}
				} 
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
}
