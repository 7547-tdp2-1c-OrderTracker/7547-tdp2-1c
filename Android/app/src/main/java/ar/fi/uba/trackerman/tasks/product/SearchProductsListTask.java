package ar.fi.uba.trackerman.tasks.product;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.adapters.ProductsListAdapter;
import ar.fi.uba.trackerman.domains.Product;
import ar.fi.uba.trackerman.domains.ProductsSearchResult;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.ShowMessage;

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
        if(brandFilter!=null) {
            urlString += "&brand_id=" + brandFilter;
        }

        ProductsSearchResult productsSearchResult = null;
        try{
            productsSearchResult = (ProductsSearchResult) restClient.get(urlString);
        } catch (BusinessException e) {
            ShowMessage.toastMessage(weakReference.get().getContext(),e.getMessage());
        }
        return productsSearchResult;
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject productSearchResultJson = new JSONObject(json);
        return ProductsSearchResult.fromJson(productSearchResultJson);
    }

    @Override
    protected void onPostExecute(ProductsSearchResult productsSearchResult) {
        weakReference.get().addProducts((productsSearchResult!=null)?productsSearchResult:new ProductsSearchResult());
    }
}
