package ar.fi.uba.trackerman.server;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.Timer;

import ar.fi.uba.trackerman.utils.ShowMessage;

/**
 * Created by smpiano on 4/30/16.
 */
public class LocationService {
    private final LocationManager lm;
    private final Context ctx;
    private double longitude;
    private double latitude;
    private boolean gps_enabled;
    private boolean network_enabled;
    private MyLocation mine;

    public LocationService(Context context) {
        this.ctx = context;
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

    }

    public boolean config(MyLocation mine) {
        this.mine = mine;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!gps_enabled && !network_enabled) return false;

            if (gps_enabled)
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
            if (network_enabled)
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
        } catch (SecurityException e) {
            ShowMessage.toastMessage(ctx,"No tenemos permisos para saber la ubicacion.");
        } catch(Exception ex) {
            ShowMessage.toastMessage(ctx,ex.getMessage());
        }
        return true;
    }


    private LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            mine.processLocation(location);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    private LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            mine.processLocation(location);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };


    public interface MyLocation {
        public void processLocation(Location loc);
    }
}
