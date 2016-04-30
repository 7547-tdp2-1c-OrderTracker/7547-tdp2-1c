package ar.fi.uba.trackerman.tasks.brand;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ar.fi.uba.trackerman.adapters.BrandsListAdapter;
import ar.fi.uba.trackerman.domains.Brand;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.ShowMessage;

public class GetBrandsListTask extends AbstractTask<Long,Void,List<Brand>,BrandsListAdapter> {

    private List<Brand> brands;

    public GetBrandsListTask(BrandsListAdapter adapter) {
        super(adapter);
    }

    public GetBrandsListTask(List<Brand> brands) {
        super(null);
        this.brands = brands;
    }

    @Override
    protected List<Brand> doInBackground(Long... params) {
        List<Brand> brands = null;
        try{
            brands = (List<Brand>) restClient.get("/v1/brands?limit=999");
        } catch (BusinessException e) {
            ShowMessage.toastMessage(weakReference.get().getContext(), e.getMessage());
        }
        return brands;
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
        if(brands != null){
            weakReference.get().addBrands(brands);
        }else{
            ShowMessage.toastMessage(weakReference.get().getContext(),"No se puede obtener Marcas.");
        }
    }

    public interface BrandsListAggregator {
        public void addBrands(List<Brand> brands);
    }
}
