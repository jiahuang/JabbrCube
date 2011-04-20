package com.cube.jabbr;

import java.util.ArrayList;

import com.cube.jabbr.listView.FoursquareWrapper;
import com.cube.jabbr.listView.Place;

import android.app.Activity;
import android.os.Bundle;

public class FoursquareDemo extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        FoursquareWrapper f = new FoursquareWrapper();
        ArrayList<Place> places = f.getVenues(42.2916247554439, -71.26439720392227);
        places = f.getVenues(42.36057345238455, -71.09390258789062);
    }
}