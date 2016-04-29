package ar.fi.uba.trackerman.tasks.brand;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.activities.ProductActivity;
import ar.fi.uba.trackerman.domains.Brand;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.tasks.AbstractTask;

/**
 * Created by smpiano on 24/4/16.
 */
public class GetBrandTask extends AbstractTask<String,Void,Brand,ProductActivity> {

    public GetBrandTask(ProductActivity activity) {
        super(activity);
    }

    @Override
    protected Brand doInBackground(String... params) {
        String brandId= params[0];
        Brand brand = null;
        try {
            brand = (Brand) restClient.get("/v1/brands/"+brandId);
        } catch (BusinessException e) {
            weakReference.get().showSnackbarSimpleMessage(e.getMessage());
        }
        return brand;
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject row = new JSONObject(json);
        return Brand.fromJson(row);
    }

    @Override
    protected void onPostExecute(Brand brand) {
        super.onPostExecute(brand);
        if (brand != null) {
            weakReference.get().afterBrandResolve(brand);
        } else {
            weakReference.get().showSnackbarSimpleMessage("No se puede resolver la marca.");
        }
    }

}
