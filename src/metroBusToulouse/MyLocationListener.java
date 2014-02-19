package metroBusToulouse;

import metroBusToulouse.controleur.Controleur;
import android.app.ListActivity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;


/* Class My Location Listener */
public class MyLocationListener extends ListActivity implements LocationListener {
	Controleur controleur;
	
    private long mTimeOfLastLocationEvent;
    private long mTimeBetweenLocationEvents;
	private float mLastAccuracy;
    boolean mAccuracyOverride = true;
    boolean mOverrideLocation = false;

	private static final float INVALID_ACCURACY = 999999.0f;
	 
	public MyLocationListener(Controleur c){
		controleur = c;
		mLastAccuracy = INVALID_ACCURACY;
        mTimeOfLastLocationEvent = 0;
        mTimeBetweenLocationEvents = 60*1000;

	} 
	/*
	public Location getLocation(){
		MetroBusToulouse.mlocManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, this);		//listener sur nouvelle location
    	Location loc = MetroBusToulouse.mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);					//obtention derni�re location connue
    	return loc;
		return null;
	}*/
	
	
	@Override
public void onLocationChanged(Location loc)	{

     if (loc != null)
     {

		//if a more accurate coordinate is available within a set of events, then use it (if enabled by programmer)
         if (mAccuracyOverride  == true)
         {
             //whenever the expected time period is reached invalidate the last known accuracy
             // so that we don't just receive better and better accuracy and eventually risk receiving
             // only minimal locations
             if (loc.getTime() - mTimeOfLastLocationEvent >= mTimeBetweenLocationEvents)
             {
                 mLastAccuracy = INVALID_ACCURACY;
             }


             if (loc.hasAccuracy())
             {
                 final float fCurrentAccuracy = loc.getAccuracy();

                 //the '<' is important here to filter out equal accuracies !
                 if ((fCurrentAccuracy != 0.0f) && (fCurrentAccuracy < mLastAccuracy))
                 {
                     mOverrideLocation = true;
                     mLastAccuracy = fCurrentAccuracy;
                 }
             }
         }

         //ensure that we don't get a lot of events
         // or if enabled, only get more accurate events within mTimeBetweenLocationEvents
         if (  (loc.getTime() - mTimeOfLastLocationEvent >= mTimeBetweenLocationEvents)
             ||(mOverrideLocation == true) )
         {
        	 //be sure to store the time of receiving this event !
             mTimeOfLastLocationEvent = loc.getTime();             
             controleur.locationMaj(loc);
         }
     }

}

@Override
public void onProviderDisabled(String provider)
{
}

@Override
public void onProviderEnabled(String provider)
{
}


@Override
public void onStatusChanged(String provider, int status, Bundle extras)
{
}

}


