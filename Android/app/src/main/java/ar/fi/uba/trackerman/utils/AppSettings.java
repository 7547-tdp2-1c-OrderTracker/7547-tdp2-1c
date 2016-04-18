package ar.fi.uba.trackerman.utils;

public class AppSettings {

    private static final String SERVER_HOST = "https://powerful-hollows-15939.herokuapp.com";

    private static final long VENDOR_ID = 1;

    public static String getServerHost(){
        return SERVER_HOST;
    }

    public static long getVendorId() {
        return VENDOR_ID;
    }
}
