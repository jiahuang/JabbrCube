package com.cube.jabbr;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewCard extends Activity {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    /** Called when the activity is first created. */
    TextView tv_word;
    TextView tv_foreign;
    RelativeLayout rl_background;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcard);
        Bundle extras = getIntent().getExtras();
        String image_url = (String) extras.get("image_url");
        String word = (String) extras.get("word");
        String foreign = (String) extras.get("foreign");
        
        tv_word = (TextView) findViewById(R.id.word);
        tv_foreign = (TextView) findViewById(R.id.foreign);
        rl_background = (RelativeLayout) findViewById(R.id.background);
        
        tv_word.setText(word);
        tv_foreign.setText(foreign);
        Drawable pic = loadDrawable(image_url);
        
        rl_background.setBackgroundDrawable(pic);
       // rl_background.setOnTouchListener(l)
        /*rl_background.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
            	GestureListener.onTouchEvent(event);
                return true;
            }
        });*/
        
        gestureDetector = new GestureDetector(new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        };
        rl_background.setOnClickListener(null);
        rl_background.setOnTouchListener(gestureListener);

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
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	System.out.println("right");
                    Toast.makeText(ViewCard.this, "Right Swipe", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            return false;
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