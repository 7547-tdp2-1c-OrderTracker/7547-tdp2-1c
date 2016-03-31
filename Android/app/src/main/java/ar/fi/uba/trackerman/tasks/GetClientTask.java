package ar.fi.uba.trackerman.tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ar.fi.uba.trackerman.domains.Client;


public class GetClientTask extends AsyncTask<String,Void,List<Client>> {

    private static final String SERVER_HOST="http://192.168.1.41:8090";

    private WeakReference<ArrayAdapter<Client>> weekAdapterReference;

    public GetClientTask(ArrayAdapter<Client> adapter) {
        weekAdapterReference = new WeakReference<ArrayAdapter<Client>>(adapter);
    }

    @Override
    protected List<Client> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String clientsJsonStr;
        List<Client> clientList = new ArrayList<>();

        try {
            URL url = new URL(SERVER_HOST+"/v1/clients");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return clientList;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                return clientList;
            }
            clientsJsonStr = buffer.toString();
            try {
                JSONObject json = new JSONObject(clientsJsonStr);
                JSONArray resultJSON = (JSONArray) json.get("results");
                Client client;
                for (int i = 0; i < resultJSON.length(); i++) {
                    JSONObject row = resultJSON.getJSONObject(i);
                    client= new Client();
                    client.setName(row.getString("name"));
                    client.setLastName(row.getString("lastname"));
                    client.setThumbnail(row.getString("thumbnail"));
                    clientList.add(client);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.e(GetClientTask.class.getCanonicalName(), "Error ", e);
            return clientList;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(GetClientTask.class.getCanonicalName(), "Error closing stream", e);
                }
            }
        }
        return clientList;
    }

    @Override
    protected void onPostExecute(List<Client> clients) {
        super.onPostExecute(clients);
        ArrayAdapter<Client> clientArrayAdapter= weekAdapterReference.get();
        if(clientArrayAdapter!=null){
            clientArrayAdapter.addAll(clients);
        }else{
            Log.w(this.getClass().getCanonicalName(),"Adapter no longer available!");
        }
    }
}
