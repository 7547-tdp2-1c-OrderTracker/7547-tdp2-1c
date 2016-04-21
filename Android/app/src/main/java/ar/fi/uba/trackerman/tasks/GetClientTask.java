package ar.fi.uba.trackerman.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import ar.fi.uba.trackerman.activities.ClientActivity;
import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.server.RestClient;

/**
 * Created by plucadei on 31/3/16.
 */
public class GetClientTask extends AbstractTask<String,Void,Client,ClientActivity> implements RestClient.ResponseParse {

    public GetClientTask(ClientActivity activity) {
        super(activity);
    }

    @Override
    protected Client doInBackground(String... params) {
        String clientId = params[0];
        return (Client) restClient.get("/v1/clients/"+clientId,null);
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject clientJson = new JSONObject(json);
        Client client= new Client(clientJson.getLong("id"));
        client.setName(clientJson.getString("name"));
        client.setLastName(clientJson.getString("lastname"));
        client.setAddress(clientJson.getString("address"));
        client.setThumbnail(clientJson.getString("thumbnail"));
        client.setCuil(clientJson.getString("cuil"));
        client.setLat(clientJson.getDouble("lat"));
        client.setLon(clientJson.getDouble("lon"));
        client.setEmail(clientJson.getString("email"));
        client.setAvatar(clientJson.getString("avatar"));
        client.setPhoneNumber(clientJson.getString("phone_number"));
        return client;
    }

    @Override
    protected void onPostExecute(Client client) {

        super.onPostExecute(client);
        ClientReciver reciver= weakReference.get();
        if(reciver!=null){
            ((ClientReciver)reciver).updateClientInformation(client);
        }else{
            Log.w(this.getClass().getCanonicalName(),"Adapter no longer available!");
        }
    }

    public interface ClientReciver{
        public void updateClientInformation(Client client);
    }

}
