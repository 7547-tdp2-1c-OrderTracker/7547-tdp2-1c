package ar.fi.uba.trackerman.tasks;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ar.fi.uba.trackerman.adapters.BrandsListAdapter;
import ar.fi.uba.trackerman.domains.Brand;

public class GetBrandsListTask extends AbstractTask<Long,Void,List<Brand>,GetBrandsListTask.BrandsListAggregator> {

    private List<Brand> brands;

    public GetBrandsListTask(BrandsListAggregator adapter) {
        super(adapter);
    }

    public GetBrandsListTask(List<Brand> brands) {
        super(null);
        this.brands = brands;
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
        BrandsListAggregator aggregator = weakReference.get();
        if(aggregator != null){
            aggregator.addBrands(brands);
        }else{
            Log.w(this.getClass().getCanonicalName(),"Adapter no longer available!");
        }
    }

    public interface BrandsListAggregator {
        public void addBrands(List<Brand> brands);
    }
}
