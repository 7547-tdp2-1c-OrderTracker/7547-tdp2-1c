package ar.fi.uba.trackerman.tasks;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
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

import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.fragments.ClientDetailFragment;

/**
 * Created by plucadei on 31/3/16.
 */
public class GetClientTask extends AsyncTask<String,Void,Client> {
    private static final String SERVER_HOST="http://192.168.1.43:8090";
    private WeakReference<Fragment> weekFragmentReference;

    public GetClientTask(Fragment fragment) {
        weekFragmentReference = new WeakReference<Fragment>(fragment);
    }
    @Override
    protected Client doInBackground(String... params) {

        String clientId= params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String clientJsonStr;
        Client client=null;
        try {
            URL url = new URL(SERVER_HOST+"/v1/clients/"+clientId);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return client;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                return client;
            }
            clientJsonStr = buffer.toString();
            try {
                JSONObject clientJson = new JSONObject(clientJsonStr);
                    client= new Client(clientJson.getLong("id"));
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.e(GetClientTask.class.getCanonicalName(), "Error ", e);
            return client;
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
        return  client;
    }

    @Override
    protected void onPostExecute(Client client) {

        super.onPostExecute(client);
        Fragment fragment= weekFragmentReference.get();
        if(fragment!=null){
            ((ClientDetailFragment)fragment).updateClientInformation(client);
        }else{
            Log.w(this.getClass().getCanonicalName(),"Adapter no longer available!");
        }
    }

}
