package com.cube.jabbr;

import java.io.InputStream;
import java.net.URL;

import com.cube.jabbr.utils.AnimationUtils;
import com.cube.jabbr.utils.Utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class ViewCard extends Activity {

    private static final int SWIPE_MIN_DISTANCE = 100;
    private static final int SWIPE_THRESHOLD_VELOCITY = 150;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    TextView tv_wordZero;
    TextView tv_foreignZero;
    ImageView iv_imageZero;
    TextView tv_wordOne;
    TextView tv_foreignOne;
    ImageView iv_imageOne;
    TextView tv_wordTwo;
    TextView tv_foreignTwo;
    ImageView iv_imageTwo;
    ViewFlipper vf_flipper;

    String[] listOfWords;
    String[] listOfForeign;
    String[] listOfImageUrls;
    int pos = 0;
    int num = 0;
    int currentView = 0; 
    
    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcard);
        Bundle extras = getIntent().getExtras();
        pos = (Integer) extras.get("pos_start");
        num = (Integer) extras.get("num");
        
        listOfWords = new String[num+1];
        listOfForeign = new String[num+1];
        listOfImageUrls = new String[num+1];
        
        // grab rest of bundle
        for(int i =0; i<num; i++){
        	listOfWords[i] = extras.getString("words"+i);
        	listOfForeign[i] = extras.getString("foreign"+i);
        	listOfImageUrls[i] = extras.getString("image_urls"+i);
        }
        String foreign = listOfForeign[pos];
        String word = listOfWords[pos];
        Toast.makeText(ViewCard.this, "pos:"+((Integer) pos).toString()+" num:"+((Integer) num).toString(), Toast.LENGTH_SHORT).show();
        
        tv_wordZero = (TextView) findViewById(R.id.wordZero);
        tv_foreignZero = (TextView) findViewById(R.id.foreignZero);
        iv_imageZero = (ImageView) findViewById(R.id.imageZero);
        tv_wordOne = (TextView) findViewById(R.id.wordOne);
        tv_foreignOne = (TextView) findViewById(R.id.foreignOne);
        iv_imageOne = (ImageView) findViewById(R.id.imageOne);
        tv_wordTwo = (TextView) findViewById(R.id.wordTwo);
        tv_foreignTwo = (TextView) findViewById(R.id.foreignTwo);
        iv_imageTwo = (ImageView) findViewById(R.id.imageTwo);
        vf_flipper = (ViewFlipper) findViewById(R.id.viewflipper);
        
        tv_wordZero.setText(word);
        tv_foreignZero.setText(foreign);
        //Drawable pic = Utils.loadDrawable(listOfImageUrls[pos]);
        
        Bitmap pic = Utils.getBitmapFromURL(listOfImageUrls[pos]);
        Bitmap resizedBitmap = resize(pic);
        iv_imageZero.setImageBitmap(resizedBitmap);
       
        gestureDetector = new GestureDetector(new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };
        
        iv_imageZero.setOnTouchListener(gestureListener);
        iv_imageOne.setOnTouchListener(gestureListener);
        iv_imageTwo.setOnTouchListener(gestureListener);

    }
    

    private Bitmap resize(Bitmap pic){
    	Bitmap resizedBitmap = null;
        // resize picture so it doesnt get out of hand
       // Bitmap resizedBitmap = Bitmap.createScaledBitmap(((BitmapDrawable) pic).getBitmap(), 200, 85, true);
        int srcWidth = pic.getWidth();
        int srcHeight = pic.getHeight();
        int desiredWidth = 350;
        int desiredHeight = 450;
			// do whatever you want with the bitmap (Resize, Rename, Add To Gallery, etc)
        if (srcWidth > srcHeight && srcWidth > desiredWidth){
        	float scaleRatio = (float)srcWidth/(float)desiredWidth;
            float newHeight = (float)srcHeight / scaleRatio;
            resizedBitmap = Bitmap.createScaledBitmap(pic, desiredWidth, (int)newHeight, true);
            pic.recycle();
        }
        else if (srcHeight > desiredHeight){
        	float scaleRatio = (float)srcHeight/(float)desiredWidth;
            float newWidth = (float)srcWidth/scaleRatio;
            resizedBitmap = Bitmap.createScaledBitmap(pic, (int)newWidth, desiredHeight, true);
            pic.recycle();
        }
        return resizedBitmap;
    }
    
    private class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                // right to left swipe
                //System.out.println("e1 to e2:"+((Float)(e1.getX() - e2.getX())).toString());
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	
                    //Toast.makeText(ViewCard.this, ((Integer) pos).toString(), Toast.LENGTH_SHORT).show();

                    vf_flipper.setInAnimation(AnimationUtils.inFromRightAnimation());
                    vf_flipper.setOutAnimation(AnimationUtils.outToLeftAnimation());
                    if (pos == (num-1))
                    	pos = 0;
					else 
						pos++;
                    //Drawable pic = Utils.loadDrawable(listOfImageUrls[pos]);
                    Bitmap pic = Utils.getBitmapFromURL(listOfImageUrls[pos]);
                    Bitmap resizedBitmap = resize(pic);
                    if (currentView == 0) {
						currentView = 1;
						tv_wordOne.setText(listOfWords[pos]);
				        tv_foreignOne.setText(listOfForeign[pos]);
				        iv_imageOne.setImageBitmap(resizedBitmap);
				    } else if (currentView == 1) {
						currentView = 2;
						tv_wordTwo.setText(listOfWords[pos]);
				        tv_foreignTwo.setText(listOfForeign[pos]);
				        iv_imageTwo.setImageBitmap(resizedBitmap);
					} else {
						currentView = 0;
						tv_wordZero.setText(listOfWords[pos]);
				        tv_foreignZero.setText(listOfForeign[pos]);
				        iv_imageZero.setImageBitmap(resizedBitmap);
					}
                    
                    vf_flipper.showNext();
                    
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	//System.out.println("left");
                    //Toast.makeText(ViewCard.this, ((Integer) pos).toString(), Toast.LENGTH_SHORT).show();
                	// show prev
                    vf_flipper.setInAnimation(AnimationUtils.inFromLeftAnimation());
                    vf_flipper.setOutAnimation(AnimationUtils.outToRightAnimation());

                    if (pos == 0)
                    	pos = (num-1);
					else 
						pos--;
                    //Drawable pic = Utils.loadDrawable(listOfImageUrls[pos]);
                    Bitmap pic = Utils.getBitmapFromURL(listOfImageUrls[pos]);
                    Bitmap resizedBitmap = resize(pic);
                    if (currentView == 0) {
						currentView = 2;
						tv_wordTwo.setText(listOfWords[pos]);
				        tv_foreignTwo.setText(listOfForeign[pos]);
				        iv_imageTwo.setImageBitmap(resizedBitmap);
				    } else if (currentView == 2) {
						currentView = 1;
						tv_wordOne.setText(listOfWords[pos]);
				        tv_foreignOne.setText(listOfForeign[pos]);
				        iv_imageOne.setImageBitmap(resizedBitmap);
					} else {
						currentView = 0;
						tv_wordZero.setText(listOfWords[pos]);
				        tv_foreignZero.setText(listOfForeign[pos]);
				        iv_imageZero.setImageBitmap(resizedBitmap);
					}
                    
                    vf_flipper.showPrevious();
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
        	Log.d("ondown",e.toString());
           return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent ev) {
          Log.d("onSingleTapUp",ev.toString());
          return true;
        }
        @Override
        public void onShowPress(MotionEvent ev) {
          Log.d("onShowPress",ev.toString());
        }
        @Override
        public void onLongPress(MotionEvent ev) {
          Log.d("onLongPress",ev.toString());
        }
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
          Log.d("onScroll",e1.toString());
          return true;
        }

    }
    
}