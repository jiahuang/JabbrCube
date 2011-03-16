package com.cube.jabbr;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class NewCard extends Activity {
	final int CAMERA_PIC_REQUEST = 1;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newcard);
    }
    
    public void camera(View view){
    	Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	if (requestCode == CAMERA_PIC_REQUEST){
    		Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
    		ImageView image = (ImageView) findViewById(R.id.image);
    		image.setImageBitmap(thumbnail);
    	}
    }
    
    public void addCard(View view){
    	
    }
}