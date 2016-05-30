package ar.fi.uba.trackerman.tasks.client;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ar.fi.uba.trackerman.adapters.ClientsListAdapter;
import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.domains.ClientSearchResult;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.MyPreferenceHelper;
import ar.fi.uba.trackerman.utils.ShowMessage;
import fi.uba.ar.soldme.R;


public class GetClientListTask extends AbstractTask<String,Void,ClientSearchResult,ClientsListAdapter> {

    private List<Client> clients;
    private MyPreferenceHelper helper;
    private String key;

    public GetClientListTask(ClientsListAdapter adapter) {
        super(adapter);
        this.key = adapter.getContext().getString(R.string.shared_pref_current_seller);
        helper = new MyPreferenceHelper(adapter.getContext());
    }

    @Override
    protected ClientSearchResult doInBackground(String... params) {
        Context ctx = weakReference.get().getContext();
        String urlString = "";
        if (params.length == 0) {
            urlString = "/v1/clients?limit=1000";
        } else if (params.length == 1) {
            urlString = "/v1/clients?limit=10&offset=" + params[0];
        } else {
            String offset = params[0];
            String lat = params[1];
            String lon = params[2];
            urlString = "/v1/clients?limit=10&offset="+offset+"&lat="+lat+"&lon="+lon+"&order=distance";
        }
        urlString+="&seller_id="+ helper.getSeller().getId();

        ClientSearchResult clientSearchResult = null;
        try{
            clientSearchResult = (ClientSearchResult) restClient.get(urlString, withAuth(ctx));
        } catch (final Exception e) {
            weakReference.get().getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    ShowMessage.showSnackbarSimpleMessage(weakReference.get().getActivity().getCurrentFocus(), e.getMessage());
                }
            });
        }
        return clientSearchResult;
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject clientsList = new JSONObject(json);
        return ClientSearchResult.fromJson(clientsList);
    }

    @Override
    protected void onPostExecute(ClientSearchResult clientSearchResult) {
        weakReference.get().addClients((clientSearchResult!=null)?clientSearchResult:new ClientSearchResult());
    }

    public interface ClientsListAggregator {
        public void addClients(ClientSearchResult clientSearchResult);
    }

}
