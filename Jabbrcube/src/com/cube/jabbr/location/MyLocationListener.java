package com.cube.jabbr.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyLocationListener implements LocationListener {
	public double longitude = 0.0;
	public double latitude = 0.0;

	@Override
	public void onLocationChanged(Location loc) {
		longitude = loc.getLongitude();
		latitude = loc.getLatitude();
	}

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
}
