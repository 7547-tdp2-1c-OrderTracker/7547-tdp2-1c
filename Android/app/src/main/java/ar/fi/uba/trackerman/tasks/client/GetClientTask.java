package ar.fi.uba.trackerman.tasks.client;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.activities.ClientActivity;
import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.tasks.AbstractTask;

/**
 * Created by plucadei on 31/3/16.
 */
public class GetClientTask extends AbstractTask<String,Void,Client,ClientActivity> {

    public GetClientTask(ClientActivity activity) {
        super(activity);
    }

    @Override
    protected Client doInBackground(String... params) {
        String clientId = params[0];
        Client client = null;
        try {
            client = (Client) restClient.get("/v1/clients/"+clientId);
        } catch (BusinessException e) {
            weakReference.get().showSnackbarSimpleMessage(e.getMessage());
        }
        return client;
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject clientJson = new JSONObject(json);
        return Client.fromJson(clientJson);
    }

    @Override
    protected void onPostExecute(Client client) {
        super.onPostExecute(client);
        if(client != null){
            ((ClientReceiver) weakReference.get()).updateClientInformation(client);
        } else{
            weakReference.get().showSnackbarSimpleMessage("No se puede obtener info del cliente");
        }
    }

    public interface ClientReceiver {
        public void updateClientInformation(Client client);
    }

}
