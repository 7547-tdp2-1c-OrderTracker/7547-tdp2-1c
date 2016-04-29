package ar.fi.uba.trackerman.exceptions;

import android.util.Log;

import java.net.URL;
import java.util.Map;

import ar.fi.uba.trackerman.utils.AppSettings;

/**
 * Created by smpiano on 4/19/16.
 */
public class ServerErrorException extends RuntimeException {

    private ServerErrorException(String msg, Boolean logError) {
        super(msg);
        if (logError) Log.e("server_io_error", msg, this);
    }

    private static String getHeaders(Map<String, String> headers) {
        String headersStr="";
        if (headers != null){
            headersStr = "-H '";
            for(String k : headers.keySet()){
                headersStr+=k+":"+headers.get(k);
            }
            headersStr += "' ";
        }
        return headersStr;
    }
    private static String getBody(String body){
        String bodyStr = "";
        if (body != null) {
            bodyStr = "-d '" + body + "' ";
        }
        return bodyStr;
    }

    public ServerErrorException(String method, URL url, String body, Map<String, String> headers, Integer status, String msg) {
        this("Error en servidor, request [curl -X" + method + " '" + AppSettings.getServerHost() + url.getPath() + "' " + getBody(body) + getHeaders(headers) + "] -> response ["+status+" - "+msg+"]", true);
    }

    public ServerErrorException(String method, URL url, String body, Map<String, String> headers) {
        this("Error en servidor [curl -X" + method + " '" + AppSettings.getServerHost() + url.getPath() + "' " + getBody(body) + getHeaders(headers) + "]", true);
    }

    public ServerErrorException(URL url, Integer status) {
        this("Error en servidor [" + AppSettings.getServerHost() + url.getPath() + "] status [" + status + "]", true);
    }

    public ServerErrorException(String msg) {
        this(msg, true);
    }

    public ServerErrorException(String msg, Throwable exception) {
        super(msg, exception);
    }
}
