package ar.fi.uba.trackerman.tasks.client;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ar.fi.uba.trackerman.adapters.ClientsListAdapter;
import ar.fi.uba.trackerman.domains.Brand;
import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.domains.ClientSearchResult;
import ar.fi.uba.trackerman.tasks.AbstractTask;


public class GetClientListTask extends AbstractTask<Long,Void,ClientSearchResult,GetClientListTask.ClientsListAggregator> {

    private List<Client> clients;

    public GetClientListTask(ClientsListAggregator adapter) {
        super(adapter);
    }

    public GetClientListTask(ClientsListAdapter adapter) {
        super(adapter);
    }

    @Override
    protected ClientSearchResult doInBackground(Long... params) {
        String urlString = "";
        if (params.length > 0) {
            urlString = "/v1/clients?limit=10";
            Long offset = params[0];
            if (offset != null) {
                urlString += "&offset=" + offset.toString();
            }
        } else {
            urlString = "/v1/clients?limit=1000";
        }

        ClientSearchResult clientSearchResult = (ClientSearchResult) restClient.get(urlString,null);
        //TODO testear caso particular, solo en la exception devuelve null
        if (clientSearchResult==null) clientSearchResult = new ClientSearchResult();
        return clientSearchResult;
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject clientsList = new JSONObject(json);
        return ClientSearchResult.fromJson(clientsList);
    }

    @Override
    protected void onPostExecute(ClientSearchResult clientSearchResult) {
        ClientsListAggregator clientListAggregator= weakReference.get();
        if(clientListAggregator!=null){
            clientListAggregator.addClients(clientSearchResult);
        }else{
            Log.w(this.getClass().getCanonicalName(),"Adapter no longer available!");
        }
    }

    public interface ClientsListAggregator {
        public void addClients(ClientSearchResult clientSearchResult);
    }

}
