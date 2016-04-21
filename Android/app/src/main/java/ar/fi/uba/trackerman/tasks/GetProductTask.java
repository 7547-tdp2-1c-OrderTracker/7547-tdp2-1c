package ar.fi.uba.trackerman.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
        Product product = new Product(row.getLong("id"));
        product.setName(row.getString("name"));
        product.setBrand(row.getString("brand"));
        product.setPicture(row.getString("picture"));
        product.setStock(row.getInt("stock"));
        product.setDescription(row.getString("description"));
        product.setCurrency(row.getString("currency"));
        // @TODO: deberiamos volar en algun momento el price. verificar tambien la clase Product y que todo cierre.
        product.setPrice(row.getDouble("retailPrice"));
        product.setRetailPrice(row.getDouble("retailPrice"));
        product.setWholesalePrice(row.getDouble("wholesalePrice"));
        return product;
    }

    @Override
    protected void onPostExecute(Product product) {

        super.onPostExecute(product);
        ProductReciver reciver= weakReference.get();
        if(reciver!=null){
            ((ProductReciver)reciver).updateProductInformation(product);
        }else{
            Log.w(this.getClass().getCanonicalName(),"Adapter no longer available!");
        }
    }

    public interface ProductReciver{
        public void updateProductInformation(Product product);
    }

}
