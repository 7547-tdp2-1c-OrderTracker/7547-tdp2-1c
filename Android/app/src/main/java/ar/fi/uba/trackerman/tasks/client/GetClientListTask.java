package ar.fi.uba.trackerman.tasks.client;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ar.fi.uba.trackerman.adapters.ClientsListAdapter;
import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.domains.ClientSearchResult;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.ShowMessage;


public class GetClientListTask extends AbstractTask<Long,Void,ClientSearchResult,ClientsListAdapter> {

    private List<Client> clients;

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

        ClientSearchResult clientSearchResult = null;
        try{
            clientSearchResult = (ClientSearchResult) restClient.get(urlString);
        } catch (BusinessException e) {
            ShowMessage.toastMessage(weakReference.get().getContext(),e.getMessage());
        }

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
