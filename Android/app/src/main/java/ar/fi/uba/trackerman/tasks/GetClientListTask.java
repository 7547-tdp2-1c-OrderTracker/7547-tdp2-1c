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


public class GetClientListTask extends AbstractTask<Long,Void,ClientSearchResult,ClientsListAdapter> {


    public GetClientListTask(ClientsListAdapter adapter) {
        super(adapter);
    }

    @Override
    protected ClientSearchResult doInBackground(Long... params) {
        String urlString = "/v1/clients?limit=10";
        Long offset = params[0];
        if(offset != null){
            urlString += "&offset="+offset.toString();
        }

        ClientSearchResult clientSearchResult = (ClientSearchResult) restClient.get(urlString,null);
        //TODO testear caso particular, solo en la exception devuelve null
        if (clientSearchResult==null) clientSearchResult = new ClientSearchResult();
        return clientSearchResult;
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject clientsList = new JSONObject(json);
        JSONObject pagingJSON = clientsList.getJSONObject("paging");
        ClientSearchResult clientSearchResult= new ClientSearchResult();
        clientSearchResult.setTotal(pagingJSON.getLong("total"));
        clientSearchResult.setOffset(pagingJSON.getLong("offset"));
        JSONArray resultJSON = (JSONArray) clientsList.get("results");
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
        ClientsListAdapter clientListAdapter= weakReference.get();
        if(clientListAdapter!=null){
            clientListAdapter.addClients(clientSearchResult);
        }else{
            Log.w(this.getClass().getCanonicalName(),"Adapter no longer available!");
        }
    }
}
