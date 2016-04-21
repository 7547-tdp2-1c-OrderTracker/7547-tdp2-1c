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

public class SearchProductsListTask extends AbstractTask<Long,Void,ProductsSearchResult,ProductsListAdapter> {

    private String brandFilter;

    public SearchProductsListTask(ProductsListAdapter adapter, String brandsFilter) {
        super(adapter);
        this.brandFilter=brandsFilter;
    }

    @Override
    protected ProductsSearchResult doInBackground(Long... params) {
        String urlString = "/v1/products?limit=10";
        Long offset= params[0];
        if(offset != null){
            urlString += "&offset="+offset.toString();
        }
        if(brandFilter!=null){
            urlString+="&brand_id="+brandFilter;
        }
        ProductsSearchResult productsSearchResult = (ProductsSearchResult) restClient.get(urlString);
        if (productsSearchResult == null) productsSearchResult = new ProductsSearchResult();
        return productsSearchResult;
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject productSearchResultJson = new JSONObject(json);
        JSONObject pagingJSON = productSearchResultJson.getJSONObject("paging");
        ProductsSearchResult productsSearchResult= new ProductsSearchResult();
        productsSearchResult.setTotal(pagingJSON.getLong("total"));
        productsSearchResult.setOffset(pagingJSON.getLong("offset"));
        JSONArray resultJSON = (JSONArray) productSearchResultJson.get("results");
        Product product;
        for (int i = 0; i < resultJSON.length(); i++) {
            JSONObject row = resultJSON.getJSONObject(i);
            product= new Product(row.getLong("id"));
            product.setName(row.getString("name"));
            product.setBrand(row.getString("brand"));
            // @TODO: deberiamos volar en algun momento el price. verificar tambien la clase Product y que todo cierre.
            product.setPrice(row.getDouble("retailPrice"));
            product.setRetailPrice(row.getDouble("retailPrice"));
            product.setWholesalePrice(row.getDouble("wholesalePrice"));
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
        ProductsListAdapter productsListAdapter= weakReference.get();
        if(productsListAdapter!=null){
            productsListAdapter.addProducts(productsSearchResult);
        }else{
            Log.w(this.getClass().getCanonicalName(),"Adapter no longer available!");
        }
    }
}
