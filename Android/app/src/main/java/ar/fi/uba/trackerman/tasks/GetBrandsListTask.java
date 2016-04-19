package ar.fi.uba.trackerman.tasks;

import android.os.AsyncTask;
import android.util.Log;

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

import ar.fi.uba.trackerman.adapters.BrandsListAdapter;
import ar.fi.uba.trackerman.domains.Brand;

public class GetBrandsListTask extends AbstractTask<Long,Void,List<Brand>,BrandsListAdapter> {

    public GetBrandsListTask(BrandsListAdapter adapter) {
        super(adapter);
    }

    @Override
    protected List<Brand> doInBackground(Long... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String brandsJsonStr;
        List<Brand> brands=null;

        try {
            String urlString= SERVER_HOST+"/v1/brands?limit=999";
            Log.d(this.getClass().getCanonicalName(),"About to get :"+urlString);
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return brands;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                return brands;
            }
            brandsJsonStr = buffer.toString();
            try {
                return parseJsonToBrand(brandsJsonStr);
            } catch (JSONException e) {

            }
        } catch (IOException e) {
            Log.e(this.getClass().getCanonicalName(), "Error fetching brands.", e);
            return brands;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(GetBrandsListTask.class.getCanonicalName(), "Error closing stream", e);
                }
            }
        }
        return brands;
    }

    private List<Brand> parseJsonToBrand(String jsonString) throws JSONException{
        JSONObject json = new JSONObject(jsonString);
        JSONArray resultJSON = (JSONArray) json.get("results");
        List<Brand> brands= new ArrayList<Brand>();
        Brand brand;
        for (int i = 0; i < resultJSON.length(); i++) {
            JSONObject row = resultJSON.getJSONObject(i);
            brand= new Brand(row.getLong("id"),row.getString("name"),row.getString("picture"));
            brands.add(brand);
        }
        return brands;
    }

    @Override
    protected void onPostExecute(List<Brand> brands) {
        BrandsListAdapter brandsListAdapter= weakReference.get();
        if(brandsListAdapter!=null){
            brandsListAdapter.addBrands(brands);
        }else{
            Log.w(this.getClass().getCanonicalName(),"Adapter no longer available!");
        }
    }
}
