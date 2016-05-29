package ar.fi.uba.trackerman.tasks.order;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ar.fi.uba.trackerman.adapters.OrdersListAdapter;
import ar.fi.uba.trackerman.domains.OrderWrapper;
import ar.fi.uba.trackerman.domains.OrdersSearchResult;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.AppSettings;
import ar.fi.uba.trackerman.utils.MyPreferenceHelper;
import ar.fi.uba.trackerman.utils.MyPreferences;
import ar.fi.uba.trackerman.utils.ShowMessage;
import fi.uba.ar.soldme.R;


public class GetOrdersListTask extends AbstractTask<Long,Void,OrdersSearchResult,OrdersListAdapter> {

    private MyPreferenceHelper helper;
    private String key;

    public GetOrdersListTask(OrdersListAdapter adapter) {
        super(adapter);
        this.key = adapter.getContext().getString(R.string.shared_pref_current_seller);
        helper = new MyPreferenceHelper(adapter.getContext());
    }

    @Override
    protected OrdersSearchResult doInBackground(Long... params) {
        Context ctx = weakReference.get().getContext();
        String urlString = "/v1/orders?limit=1000";
        Long offset = params[0];
        if(offset != null){
            urlString += "&offset="+offset.toString();
        }
        urlString+="&seller_id="+ helper.getSeller().getId();

        OrdersSearchResult ordersSearchResult = null;
        try {
            ordersSearchResult = (OrdersSearchResult) restClient.get(urlString, withAuth(ctx));
        } catch (Exception e) {
            ShowMessage.toastMessage(ctx,e.getMessage());
        }
        return ordersSearchResult;
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject ordersList = new JSONObject(json);
        JSONObject pagingJSON = ordersList.getJSONObject("paging");
        OrdersSearchResult ordersSearchResult= new OrdersSearchResult();
        ordersSearchResult.setTotal(pagingJSON.getLong("total"));
        ordersSearchResult.setOffset(pagingJSON.getLong("offset"));
        JSONArray resultJSON = (JSONArray) ordersList.get("results");
        OrderWrapper orderWrapper;
        for (int i = 0; i < resultJSON.length(); i++) {
            orderWrapper = OrderWrapper.fromJson(resultJSON.getJSONObject(i));
            ordersSearchResult.addOrder(orderWrapper);
        }
        return ordersSearchResult;
    }

    @Override
    protected void onPostExecute(OrdersSearchResult ordersSearchResult) {
        weakReference.get().addOrders((ordersSearchResult != null) ? ordersSearchResult : new OrdersSearchResult());
    }
}
