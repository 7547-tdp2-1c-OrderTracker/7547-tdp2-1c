package ar.fi.uba.trackerman.tasks.client;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.ShowMessage;

/**
 * Created by guido on 15/05/16.
 */

public class GetClientDirectTask extends AbstractTask<String,Void,Client,AppCompatActivity> {

    public GetClientDirectTask(AppCompatActivity validation) {
        super(validation);
    }

    public Client getClient(String clientId) {
        Context ctx = weakReference.get().getApplicationContext();
        Client client = null;
        try {
            client = (Client) restClient.get("/v1/clients/"+clientId, withAuth(ctx));
        } catch (Exception e) {
            ShowMessage.toastMessage(ctx, e.getMessage());
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
        if(client != null){
            ((ClientDirectReceiver) weakReference.get()).updateClientDirect(client);
        }
    }

    public interface ClientDirectReceiver {
        public void updateClientDirect(Client client);
    }
}
