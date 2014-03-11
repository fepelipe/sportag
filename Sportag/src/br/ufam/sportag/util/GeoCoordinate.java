package br.ufam.sportag.util;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public abstract class GeoCoordinate implements LocationListener
{
	private static final int TIMEOUT = 1000 * 3;
	
	public static Location currentLocation;
	private Context context;
	private LocationManager locationManager;
	
	public GeoCoordinate(Context context)
	{
		this.context = context;
	}
	
	public abstract void onGPSSuccess(Location location);
	
	public abstract void onFail(String title, String message);
	
	public void startService()
	{
		// onGPSStart();
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		
		// String provider = locationManager.getBestProvider(criteria, true);
		
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		
		// if (provider != null)
		// locationManager.requestLocationUpdates(provider, 0, 0, this);
		// else
		// onFail(context.getResources().getString(R.string.tituloServicosLocalizacao),
		// context.getResources().getString(R.string.needsActivateLocationService));
	}
	
	@Override
	public void onLocationChanged(Location location)
	{
		if (isBetterLocation(location, currentLocation)
				&& locationManager != null)
		{
			onGPSSuccess(location);
			locationManager.removeUpdates(this);
			locationManager = null;
		}
		currentLocation = location;
		// onGPSFinish();
	}
	
	@Override
	public void onProviderDisabled(String provider)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onProviderEnabled(String provider)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix
	 * 
	 * @param location
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 */
	protected boolean isBetterLocation(Location location,
			Location currentBestLocation)
	{
		if (currentBestLocation == null)
		{
			// A new location is always better than no location
			return true;
		}
		
		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TIMEOUT;
		boolean isSignificantlyOlder = timeDelta < -TIMEOUT;
		boolean isNewer = timeDelta > 0;
		
		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer)
		{
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder)
		{
			return false;
		}
		
		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;
		
		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());
		
		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate)
		{
			return true;
		} else if (isNewer && !isLessAccurate)
		{
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider)
		{
			return true;
		}
		return false;
	}
	
	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2)
	{
		if (provider1 == null)
		{
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}
	
}
