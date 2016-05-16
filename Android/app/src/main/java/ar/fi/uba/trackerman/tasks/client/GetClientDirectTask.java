package ar.fi.uba.trackerman.tasks.client;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.exceptions.ServerErrorException;
import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.tasks.AbstractTask;

/**
 * Created by guido on 15/05/16.
 */

public class GetClientDirectTask extends AbstractTask<String,Void,Client,AppCompatActivity> {

    public GetClientDirectTask(AppCompatActivity validation) {
        super(validation);
    }

    public Client getClient(String clientId) {
        Client client = null;
        try {
            client = (Client) restClient.get("/v1/clients/"+clientId);
        } catch (BusinessException e) {
            Log.e("business_error", e.getMessage(), e);
        } catch (ServerErrorException e) {
            Log.e("server_error", e.getMessage(), e);
        }
        return client;
    }


    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject clientJson = new JSONObject(json);
        return Client.fromJson(clientJson);
    }

    @Override
    protected Client doInBackground(String... strings) {
        if (strings.length == 1) {
            return this.getClient(strings[0]); // paso el idClient
        }
        return null;
    }

    @Override
    protected void onPostExecute(Client client) {
//        super.onPostExecute(client);
        if(client != null){
            ((ClientDirectReceiver) weakReference.get()).updateClientDirect(client);
        } else {
            // weakReference.get().showSnackbarSimpleMessage("No se puede obtener info del cliente");
        }

    }

    public interface ClientDirectReceiver {
        public void updateClientDirect(Client client);
    }
}
