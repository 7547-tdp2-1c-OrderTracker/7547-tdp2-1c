package ar.fi.uba.trackerman.tasks.product;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.activities.ProductActivity;
import ar.fi.uba.trackerman.domains.Product;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.ShowMessage;

/**
 * Created by glaghi on 16/4/16.
 */
public class GetProductTask extends AbstractTask<String,Void,Product,ProductActivity> {

    public GetProductTask(ProductActivity activity) {
        super(activity);
    }

    @Override
    protected Product doInBackground(String... params) {
        Context ctx = weakReference.get().getApplicationContext();
        String productId= params[0];
        Product product = null;
        try{
            product = (Product) restClient.get("/v1/products/"+productId, withAuth(ctx));
        } catch (BusinessException e) {
            weakReference.get().showSnackbarSimpleMessage(e.getMessage());
        } catch (Exception e) {
            ShowMessage.toastMessage(ctx, e.getMessage());
        }
        return product;
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
