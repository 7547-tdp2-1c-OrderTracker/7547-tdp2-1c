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

import ar.fi.uba.trackerman.domains.Product;

/**
 * Created by glaghi on 16/4/16.
 */
public class GetProductTask extends AsyncTask<String,Void,Product> {

    private static final String SERVER_HOST="https://powerful-hollows-15939.herokuapp.com";
    private WeakReference<ProductReciver> weekReciverReference;

    public GetProductTask(ProductReciver reciver) {
        weekReciverReference = new WeakReference<ProductReciver>(reciver);
    }

    @Override
    protected Product doInBackground(String... params) {

        String productId= params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String productJsonStr;
        Product product=null;
        try {
            URL url = new URL(SERVER_HOST+"/v1/products/"+productId);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return product;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                return product;
            }
            productJsonStr = buffer.toString();
            try {
                JSONObject row = new JSONObject(productJsonStr);
                    product = new Product(row.getLong("id"));
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.e(GetProductTask.class.getCanonicalName(), "Error ", e);
            return product;
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
        return  product;
    }

    @Override
    protected void onPostExecute(Product product) {

        super.onPostExecute(product);
        ProductReciver reciver= weekReciverReference.get();
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
