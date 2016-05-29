package ar.fi.uba.trackerman.utils;

/**
 * Created by smpiano on 4/22/16.
 */
public class AppSettings {

    private static final String SERVER_HOST = "https://trackerman-api.herokuapp.com";
    //private static final String SERVER_HOST = "https://powerful-hollows-15939.herokuapp.com";

    private static final int SERVER_TIMEOUT = 15000; //15seg
    private static final Double GPS_LAT = -34.563424;
    private static final Double GPS_LON = -58.463874;


    public static String getServerHost(){
        return SERVER_HOST;
    }

    public static int getServerTimeout() { return SERVER_TIMEOUT; }

    public static String getGpsLat() {
        return GPS_LAT.toString();
    }

    public static String getGpsLon() {
        return GPS_LON.toString();
    }
}
