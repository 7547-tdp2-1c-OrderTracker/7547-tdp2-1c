package ar.fi.uba.trackerman.server;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;

import ar.fi.uba.trackerman.exceptions.ApiCallException;
import ar.fi.uba.trackerman.exceptions.ServerErrorException;
import ar.fi.uba.trackerman.utils.AppSettings;

/**
 * Created by smpiano on 4/19/16.
 */
public class RestClient {

    private final ResponseParse parser;

    public RestClient(ResponseParse parser) {
        this.parser = parser;
    }

    public Object get(String endpoint) throws RuntimeException{
        return connect("GET", endpoint, null, null);
    }

    public Object get(String endpoint, Map<String, String> headers) throws RuntimeException{
        return connect("GET", endpoint, null, headers);
    }

    public Object post(String endpoint, String body, Map<String, String> headers) throws RuntimeException{
        return connect("POST", endpoint, body, headers);
    }

    public Object put(String endpoint, String body, Map<String, String> headers) throws RuntimeException{
        return connect("PUT", endpoint, body, headers);
    }

    public Object delete(String endpoint, String body, Map<String, String> headers) throws RuntimeException{
        return connect("DELETE", endpoint, body, headers);
    }

    private Object connect(String method, String endpoint,String body, Map<String,String> headers) throws RuntimeException{
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String json;
        Object expectedReturn = null;
        URL url = null;
        int responseStatus = 0;
        InputStream inputStream = null;
        String errorMsg = null;
        Boolean isError = false;
        try {
            url = new URL(AppSettings.getServerHost() + endpoint);
            urlConnection = (HttpURLConnection) url.openConnection();
            if (headers != null) {
                for(String key : headers.keySet()) {
                    urlConnection.setRequestProperty(key, headers.get(key));
                }
            }
            urlConnection.setConnectTimeout(AppSettings.getServerTimeout());
            //writing the request
            if (method == "POST" || method == "PUT" || method=="DELETE") {
                urlConnection.setRequestMethod(method);
                if (body != null) {
                    byte[] outputInBytes = body.getBytes("UTF-8");
                    OutputStream os = urlConnection.getOutputStream();
                    os.write(outputInBytes);
                    os.close();
                }
            } else if (method == "GET") {
                urlConnection.setRequestMethod(method);
            } else {
                throw new ApiCallException(url.getPath());
            }

            //makes the connection
            urlConnection.connect();

            //reading the response
            responseStatus = urlConnection.getResponseCode();
            errorMsg = urlConnection.getResponseMessage();
            isError = responseStatus >= HttpURLConnection.HTTP_INTERNAL_ERROR;
            if (isError) {
                inputStream = urlConnection.getErrorStream();
            } else {
                inputStream = urlConnection.getInputStream();
            }

            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return expectedReturn;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                return expectedReturn;
            }
            json = buffer.toString();
            try {
                if (isError) {
                    readErrorResponse(json);
                } else {
                    expectedReturn = parser.readResponse(json);
                }
            } catch (JSONException e) {
                Log.e("parse_response_error", "Error al leer el response de " + url.getPath(), e);
            }
        } catch (SocketTimeoutException e) {
            throw new ServerErrorException("El server no pudo responder antes del timeout ["+AppSettings.getServerTimeout()+"]");
        } catch (IOException e) {
            throw new ServerErrorException(method,url,body,headers,responseStatus,errorMsg);
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("server_stream_error", "Error closing stream", e);
                }
            }
        }
        return expectedReturn;
    }

    private void readErrorResponse(String json) throws ServerErrorException {
        String errorKey = "";
        try {
            JSONObject error = new JSONObject(json);
            errorKey = error.getString("error");

            //tiro exception de regla de negocio
            //FIXME smpiano cambiar lalalala por value
            throw ErrorMatcher.valueOf(errorKey).getThrowable("lalalala");
        } catch (JSONException e) {
            throw new ServerErrorException("Error parseando server errorJson",e);
        } catch (IllegalArgumentException e) {
            throw new ServerErrorException("Falta agregar "+errorKey+" en clase ErrorMatcher",e);
        }
    }

    public interface ResponseParse {
        public Object readResponse(String json) throws JSONException;
    }
}
