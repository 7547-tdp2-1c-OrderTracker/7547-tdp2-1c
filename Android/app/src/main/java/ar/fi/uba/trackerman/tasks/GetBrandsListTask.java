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
        return (List<Brand>) restClient.get("/v1/brands?limit=999",null);
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject brandList = new JSONObject(json);
        JSONArray resultJSON = (JSONArray) brandList.get("results");
        List<Brand> brands = new ArrayList<Brand>();
        for (int i = 0; i < resultJSON.length(); i++) {
            brands.add(Brand.fromJson(resultJSON.getJSONObject(i)));
        }
        return brands;
    }

    @Override
    protected void onPostExecute(List<Brand> brands) {
        BrandsListAdapter brandsListAdapter = weakReference.get();
        if(brandsListAdapter != null){
            brandsListAdapter.addBrands(brands);
        }else{
            Log.w(this.getClass().getCanonicalName(),"Adapter no longer available!");
        }
    }
}
