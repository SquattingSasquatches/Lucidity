package com.squattingsasquatches.lucidity;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.squattingsasquatches.lucidity.objects.Section;
import com.squattingsasquatches.lucidity.objects.User;

public final class CheckInManager {

	private static String bestProvider;
	private static boolean checkedIn;
	private static Context ctx;
	private static Location currentLocation;
	private static LocationListener gpsListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location loc) {
			currentLocation = loc;
			saveLocation(currentLocation);
			ctx.sendBroadcast(new Intent(
					"com.squattingsasquatches.lucidity.LOCATION_UPDATED"));
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

	};

	private static LocationManager locationManager;

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
		Section.setStoredCheckedIn(checkedIn, sectionId);
	}

	private static void saveLocation(Location loc) {
		User.setStoredLocation(loc);
	}

	public static void startGPS(Context ctx, int sectionId) {
		CheckInManager.ctx = ctx;

		final int c = Section.getStoredCheckedIn(sectionId);

		CheckInManager.checkedIn = true;

		if (c == 0)
			CheckInManager.checkedIn = false;

		final Criteria criteria = new Criteria();

		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(true);

		locationManager = (LocationManager) ctx
				.getSystemService(Context.LOCATION_SERVICE);
		bestProvider = locationManager.getBestProvider(criteria, true);

		locationManager.requestLocationUpdates(bestProvider, 60 * 1000, 10,
				gpsListener);
	}

	public static void stopUpdating() {
		locationManager.removeUpdates(gpsListener);
	}

	private CheckInManager() {
		throw new AssertionError();
	}

}
