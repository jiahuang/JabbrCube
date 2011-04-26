package com.cube.jabbr;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
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

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
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
        String image_url = listOfImageUrls[pos];
        String foreign = listOfForeign[pos];
        String word = listOfWords[pos];
        
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
        Drawable pic = loadDrawable(image_url);
        
        iv_imageZero.setBackgroundDrawable(pic);
       
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
    
    
    private class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                System.out.println("on fling");
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	System.out.println("left");
                    Toast.makeText(ViewCard.this, "Left Swipe", Toast.LENGTH_SHORT).show();
                    // show prev
                    // TODO: view flipper animation
                    //vf_flipper.setInAnimation(slideRightIn);
                    //vf_flipper.setOutAnimation(slideRightOut);
                    if (pos == 0)
                    	pos = num;
					else 
						pos--;
                    if (currentView == 0) {
						currentView = 2;
						tv_wordTwo.setText(listOfWords[pos]);
				        tv_foreignTwo.setText(listOfForeign[pos]);
				        Drawable pic = loadDrawable(listOfImageUrls[pos]);
				        iv_imageTwo.setBackgroundDrawable(pic);
				    } else if (currentView == 2) {
						currentView = 1;
						tv_wordOne.setText(listOfWords[pos]);
				        tv_foreignOne.setText(listOfForeign[pos]);
				        Drawable pic = loadDrawable(listOfImageUrls[pos]);
				        iv_imageOne.setBackgroundDrawable(pic);
					} else {
						currentView = 0;
						tv_wordZero.setText(listOfWords[pos]);
				        tv_foreignZero.setText(listOfForeign[pos]);
				        Drawable pic = loadDrawable(listOfImageUrls[pos]);
				        iv_imageZero.setBackgroundDrawable(pic);
					}
                    
                    vf_flipper.showPrevious();
                    
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	System.out.println("right");
                    Toast.makeText(ViewCard.this, "Right Swipe", Toast.LENGTH_SHORT).show();
                    //vf_flipper.setInAnimation(slideLeftIn);
                    //vf_flipper.setOutAnimation(slideLeftOut);
                    if (pos == num)
                    	pos = 0;
					else 
						pos++;
                    if (currentView == 0) {
						currentView = 1;
						tv_wordOne.setText(listOfWords[pos]);
				        tv_foreignOne.setText(listOfForeign[pos]);
				        Drawable pic = loadDrawable(listOfImageUrls[pos]);
				        iv_imageOne.setBackgroundDrawable(pic);
				    } else if (currentView == 1) {
						currentView = 2;
						tv_wordTwo.setText(listOfWords[pos]);
				        tv_foreignTwo.setText(listOfForeign[pos]);
				        Drawable pic = loadDrawable(listOfImageUrls[pos]);
				        iv_imageTwo.setBackgroundDrawable(pic);
					} else {
						currentView = 0;
						tv_wordZero.setText(listOfWords[pos]);
				        tv_foreignZero.setText(listOfForeign[pos]);
				        Drawable pic = loadDrawable(listOfImageUrls[pos]);
				        iv_imageZero.setBackgroundDrawable(pic);
					}
                    vf_flipper.showNext();
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

    // TODO: move this shit out into a utils
    public Drawable loadDrawable(String image_url) {


		URL url = null;
		InputStream inputStream = null;
		try {
			url = new URL(image_url);
			inputStream = (InputStream) url.getContent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Drawable drawable = Drawable.createFromStream(inputStream, "src");
		return drawable;
	}
}