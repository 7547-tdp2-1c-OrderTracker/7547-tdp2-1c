package ar.fi.uba.trackerman.tasks;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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

import ar.fi.uba.trackerman.adapters.ClientsListAdapter;
import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.domains.ClientSearchResult;


public class GetClientListTask extends AsyncTask<Long,Void,ClientSearchResult> {

    private static final String SERVER_HOST="http://192.168.1.43:8090";
    private WeakReference<ClientsListAdapter> weekAdapterReference;

    public GetClientListTask(ClientsListAdapter adapter) {
        weekAdapterReference = new WeakReference<ClientsListAdapter>(adapter);
    }

    @Override
    protected ClientSearchResult doInBackground(Long... params) {
        Long offset= params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String clientsJsonStr;
        ClientSearchResult clientSearchResult;

        try {
            String urlString= SERVER_HOST+"/v1/clients?limit=10";
            if(offset!=null){
                urlString+="&offset="+offset.toString();
            }
            Log.d(this.getClass().getCanonicalName(),"About to get :"+urlString);
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return new ClientSearchResult();
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                return new ClientSearchResult();
            }
            clientsJsonStr = buffer.toString();
            try {
                return parseJsonClientSearchResult(clientsJsonStr);
            } catch (JSONException e) {

            }
        } catch (IOException e) {
            Log.e(this.getClass().getCanonicalName(), "Error fetching clients.", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(GetClientListTask.class.getCanonicalName(), "Error closing stream", e);
                }
            }
        }
        return null;
    }

    private ClientSearchResult parseJsonClientSearchResult(String jsonString) throws JSONException{
        JSONObject json = new JSONObject(jsonString);
        JSONObject pagingJSON = json.getJSONObject("paging");
        ClientSearchResult clientSearchResult= new ClientSearchResult();
        clientSearchResult.setTotal(pagingJSON.getLong("total"));
        clientSearchResult.setOffset(pagingJSON.getLong("offset"));
        JSONArray resultJSON = (JSONArray) json.get("results");
        Client client;
        for (int i = 0; i < resultJSON.length(); i++) {
            JSONObject row = resultJSON.getJSONObject(i);
            client= new Client(row.getLong("id"));
            client.setName(row.getString("name"));
            client.setLastName(row.getString("lastname"));
            client.setAddress(row.getString("address"));
            client.setThumbnail(row.getString("thumbnail"));
            clientSearchResult.addClient(client);
        }
        return clientSearchResult;
    }

    @Override
    protected void onPostExecute(ClientSearchResult clientSearchResult) {
        ClientsListAdapter clientListAdapter= weekAdapterReference.get();
        if(clientListAdapter!=null){
            clientListAdapter.addClients(clientSearchResult);
        }else{
            Log.w(this.getClass().getCanonicalName(),"Adapter no longer available!");
        }
    }
}
