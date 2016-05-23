package ar.fi.uba.trackerman.utils;

/**
 * Created by smpiano on 4/22/16.
 */
public class AppSettings {

    //private static final String SERVER_HOST = "https://trackerman-api.herokuapp.com";
    private static final String SERVER_HOST = "https://powerful-hollows-15939.herokuapp.com";

    private static final int SERVER_TIMEOUT = 15000; //15seg

    public static String getServerHost(){
        return SERVER_HOST;
    }

    public static int getServerTimeout() { return SERVER_TIMEOUT; }
}
