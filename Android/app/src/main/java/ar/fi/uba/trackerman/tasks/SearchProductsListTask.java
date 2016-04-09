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

import ar.fi.uba.trackerman.adapters.ProductsListAdapter;
import ar.fi.uba.trackerman.domains.Product;
import ar.fi.uba.trackerman.domains.ProductsSearchResult;

public class SearchProductsListTask extends AsyncTask<Long,Void,ProductsSearchResult> {

    private static final String SERVER_HOST="http://192.168.1.35:8090";
    private WeakReference<ProductsListAdapter> weekAdapterReference;

    public SearchProductsListTask(ProductsListAdapter adapter) {
        weekAdapterReference = new WeakReference<ProductsListAdapter>(adapter);
    }

    @Override
    protected ProductsSearchResult doInBackground(Long... params) {
        Long offset= params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String productsJsonStr;

        try {
            String urlString= SERVER_HOST+"/v1/products?limit=10";
            if(offset!=null){
                urlString+="&offset="+offset.toString();
            }
            Log.d(this.getClass().getCanonicalName(), "About to get :" + urlString);
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return new ProductsSearchResult();
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                return new ProductsSearchResult();
            }
            productsJsonStr = buffer.toString();
            try {
                return parseJsonResponse(productsJsonStr);
            } catch (JSONException e) {
                Log.e(this.getClass().getCanonicalName(), "Error parsing response.", e);
                return null;
            }
        } catch (IOException e) {
            Log.e(this.getClass().getCanonicalName(), "Error fetching clients.", e);
            return null;
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
    }

    private ProductsSearchResult parseJsonResponse(String jsonString) throws JSONException{
        JSONObject json = new JSONObject(jsonString);
        JSONObject pagingJSON = json.getJSONObject("paging");
        ProductsSearchResult productsSearchResult= new ProductsSearchResult();
        productsSearchResult.setTotal(pagingJSON.getLong("total"));
        productsSearchResult.setOffset(pagingJSON.getLong("offset"));
        JSONArray resultJSON = (JSONArray) json.get("results");
        Product product;
        for (int i = 0; i < resultJSON.length(); i++) {
            JSONObject row = resultJSON.getJSONObject(i);
            product= new Product(row.getLong("id"));
            product.setName(row.getString("name"));
            product.setBrand(row.getString("brand"));
            product.setPrice(row.getDouble("price"));
            product.setCurrency(row.getString("currency"));
            product.setStock(row.getInt("stock"));
            product.setPicture(row.getString("picture"));
            product.setThumbnail(row.getString("thumbnail"));
            productsSearchResult.addProduct(product);
        }
        return productsSearchResult;
    }

    @Override
    protected void onPostExecute(ProductsSearchResult productsSearchResult) {
        ProductsListAdapter productsListAdapter= weekAdapterReference.get();
        if(productsListAdapter!=null){
            productsListAdapter.addProducts(productsSearchResult);
        }else{
            Log.w(this.getClass().getCanonicalName(),"Adapter no longer available!");
        }
    }
}
