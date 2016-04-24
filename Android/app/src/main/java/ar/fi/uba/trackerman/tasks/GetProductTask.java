package ar.fi.uba.trackerman.tasks;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.activities.ProductActivity;
import ar.fi.uba.trackerman.domains.Product;

/**
 * Created by glaghi on 16/4/16.
 */
public class GetProductTask extends AbstractTask<String,Void,Product,ProductActivity> {

    public GetProductTask(ProductActivity activity) {
        super(activity);
    }

    @Override
    protected Product doInBackground(String... params) {

        String productId= params[0];
        return (Product) restClient.get("/v1/products/"+productId);
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject row = new JSONObject(json);
        return Product.fromJson(row);
    }

    @Override
    protected void onPostExecute(Product product) {
        super.onPostExecute(product);
        if(product != null){
            ((ProductReceiver) weakReference.get()).updateProductInformation(product);
        }
    }

    public interface ProductReceiver {
        public void updateProductInformation(Product product);
    }

}
