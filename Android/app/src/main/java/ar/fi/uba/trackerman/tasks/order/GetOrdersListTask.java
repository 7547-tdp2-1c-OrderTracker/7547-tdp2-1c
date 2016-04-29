package ar.fi.uba.trackerman.tasks.order;

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
import ar.fi.uba.trackerman.utils.ShowMessage;


public class GetOrdersListTask extends AbstractTask<Long,Void,OrdersSearchResult,OrdersListAdapter> {


    public GetOrdersListTask(OrdersListAdapter adapter) {
        super(adapter);
    }

    @Override
    protected OrdersSearchResult doInBackground(Long... params) {
        String urlString = "/v1/orders?limit=1000";
        Long offset = params[0];
        if(offset != null){
            urlString += "&offset="+offset.toString();
        }

        OrdersSearchResult ordersSearchResult = null;
        try {
            ordersSearchResult = (OrdersSearchResult) restClient.get(urlString);
        } catch (BusinessException e) {
            ShowMessage.toastMessage(weakReference.get().getContext(),e.getMessage());
            ordersSearchResult = new OrdersSearchResult();
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
        OrdersListAdapter ordersListAdapter= weakReference.get();
        if(ordersListAdapter!=null){
            ordersListAdapter.addOrders(ordersSearchResult);
        }else{
            Log.w(this.getClass().getCanonicalName(),"Adapter no longer available!");
        }
    }
}
