package ar.fi.uba.trackerman.exceptions;

import android.util.Log;

import java.net.URL;

import ar.fi.uba.trackerman.utils.AppSettings;

/**
 * Created by smpiano on 4/19/16.
 */
public class ServerErrorException extends RuntimeException {

    private ServerErrorException(String msg, Boolean logError) {
        super(msg);
        if (logError) Log.e("server_io_error", msg, this);
    }

    public ServerErrorException(URL url) {
        this("Error en servidor [" + AppSettings.getServerHost()+url.getPath() + "]", true);
    }

    public ServerErrorException(URL url, Integer status) {
        this("Error en servidor [" + AppSettings.getServerHost()+url.getPath() + "] status [" + status + "]", true);
    }

    public ServerErrorException(String msg) {
        this(msg, true);
    }

    public ServerErrorException(String msg, Throwable exception) {
        super(msg, exception);
    }
}
