package com.squattingsasquatches.lucidity;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class CheckInManager {
	
	private Location currentLocation = null;
	private boolean checkedIn = false;
	private LocationManager locationManager;
	private String bestProvider;
	private Context ctx;
	
	public CheckInManager(Context ctx) {
		this.ctx = ctx;
	}
	
	public void startGPS() {
		Criteria criteria = new Criteria();

		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(true);

		locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		bestProvider = locationManager.getBestProvider(criteria, true);
		
		locationManager.requestLocationUpdates(bestProvider, 0, 0, gpsListener);
	}
	
	public Location getLocation() {
		return currentLocation;
	}
	
	public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
	    double earthRadius = 6370990.56;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;

	    return dist;
	}
	
	public boolean isCheckedIn() {
		return checkedIn;
	}

	public void setCheckedIn(boolean checkedIn) {
		this.checkedIn = checkedIn;
	}
	
	private void saveLocation(Location loc) {
		LocalDBAdapter localDB = new LocalDBAdapter(ctx).open();
		localDB.saveLocation(loc);
		localDB.close();
	}

	private LocationListener gpsListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location loc) {
			currentLocation = loc;
			saveLocation(currentLocation);
			locationManager.removeUpdates(this);
			ctx.sendBroadcast(new Intent("com.squattingsasquatches.lucidity.LOCATION_FOUND"));
		}

		@Override
		public void onProviderDisabled(String provider) {}

		@Override
		public void onProviderEnabled(String provider) {}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
		
	};

}
