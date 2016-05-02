package ar.fi.uba.trackerman.utils;

import android.content.Context;
import android.location.Location;
import android.widget.TextView;

import ar.fi.uba.trackerman.server.LocationService;
import ar.fi.uba.trackerman.server.RestClient;
import fi.uba.ar.soldme.R;

/**
 * Created by smpiano on 5/2/16.
 */
public class LocationHelper {

    public static void updatePosition(final Context context) {
        LocationService ls = new LocationService(context);
        ls.config(new LocationService.MyLocation() {
            @Override
            public void processLocation(Location loc) {
                MyPreferences pref = new MyPreferences(context);
                pref.save(context.getString(R.string.shared_pref_current_location_lat), String.valueOf(loc.getLatitude()));
                pref.save(context.getString(R.string.shared_pref_current_location_lon), String.valueOf(loc.getLongitude()));
            }
        });
    }
}
