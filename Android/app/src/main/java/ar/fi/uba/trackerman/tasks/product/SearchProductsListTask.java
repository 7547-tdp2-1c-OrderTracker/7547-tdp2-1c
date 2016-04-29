package ar.fi.uba.trackerman.tasks.product;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.adapters.ProductsListAdapter;
import ar.fi.uba.trackerman.domains.ProductsSearchResult;
import ar.fi.uba.trackerman.tasks.AbstractTask;

public class SearchProductsListTask extends AbstractTask<Long,Void,ProductsSearchResult,ProductsListAdapter> {

    private String brandFilter;

    public SearchProductsListTask(ProductsListAdapter adapter, String brandsFilter) {
        super(adapter);
        this.brandFilter = brandsFilter;
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
        return ProductsSearchResult.fromJson(productSearchResultJson);
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
