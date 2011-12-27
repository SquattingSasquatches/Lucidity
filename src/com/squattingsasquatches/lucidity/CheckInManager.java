package com.squattingsasquatches.lucidity;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public final class CheckInManager {
	
	private static LocalDBAdapter localDB;
	private static Location currentLocation;
	private static boolean checkedIn;
	private static LocationManager locationManager;
	private static String bestProvider;
	private static Context ctx;
	
	private CheckInManager() {
		throw new AssertionError();
	}
	
	public static void startGPS(Context ctx, int sectionId) {
		CheckInManager.ctx = ctx;
		CheckInManager.localDB = new LocalDBAdapter(ctx).open();
		CheckInManager.checkedIn = localDB.isCheckedIn(sectionId);
		CheckInManager.localDB.close();
		
		Criteria criteria = new Criteria();

		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(true);

		locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		bestProvider = locationManager.getBestProvider(criteria, true);
		
		locationManager.requestLocationUpdates(bestProvider, 60 * 1000, 10, gpsListener);
	}
	
	public static double getLatitude() {
		return currentLocation.getLatitude();
	}
	
	public static double getLongitude() {
		return currentLocation.getLongitude();
	}
	
	public static boolean isCheckedIn() {
		return checkedIn;
	}

	public static void saveCheckedIn(boolean checkedIn, int sectionId) {
		CheckInManager.checkedIn = checkedIn;
		
		localDB = new LocalDBAdapter(ctx).open();
		localDB.saveCheckedIn(checkedIn, sectionId);
		localDB.close();
	}
	
	private static void saveLocation(Location loc) {
		localDB = new LocalDBAdapter(ctx).open();
		localDB.saveLocation(loc);
		localDB.close();
	}
	
	public static void stopUpdating() {
		locationManager.removeUpdates(gpsListener);
	}
	
	private static LocationListener gpsListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location loc) {
			currentLocation = loc;
			saveLocation(currentLocation);
			ctx.sendBroadcast(new Intent("com.squattingsasquatches.lucidity.LOCATION_UPDATED"));
		}

		@Override
		public void onProviderDisabled(String provider) {}

		@Override
		public void onProviderEnabled(String provider) {}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
		
	};

}
