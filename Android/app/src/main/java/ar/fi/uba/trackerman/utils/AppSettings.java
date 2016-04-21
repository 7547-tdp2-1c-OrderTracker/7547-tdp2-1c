package ar.fi.uba.trackerman.utils;

public class AppSettings {

    private static final String SERVER_HOST = "https://trackerman-api.herokuapp.com";

    private static final long VENDOR_ID = 1;

    private static final int SERVER_TIMEOUT = 15000; //15seg

    public static String getServerHost(){
        return SERVER_HOST;
    }

    public static long getVendorId() {
        return VENDOR_ID;
    }

    public static int getServerTimeout() { return SERVER_TIMEOUT; }
}
