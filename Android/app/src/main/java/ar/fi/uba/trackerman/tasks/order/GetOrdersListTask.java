package ar.fi.uba.trackerman.tasks.order;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import ar.fi.uba.trackerman.adapters.OrdersListAdapter;
import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.domains.OrdersSearchResult;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.DateUtils;


public class GetOrdersListTask extends AbstractTask<Long,Void,OrdersSearchResult,OrdersListAdapter> {


    public GetOrdersListTask(OrdersListAdapter adapter) {
        super(adapter);
    }

    @Override
    protected OrdersSearchResult doInBackground(Long... params) {
        String urlString = "/v1/orders?limit=10";
        Long offset = params[0];
        if(offset != null){
            urlString += "&offset="+offset.toString();
        }

        OrdersSearchResult ordersSearchResult = (OrdersSearchResult) restClient.get(urlString,null);
        //TODO testear caso particular, solo en la exception devuelve null
        if (ordersSearchResult==null) ordersSearchResult = new OrdersSearchResult();
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
        Order order;
        for (int i = 0; i < resultJSON.length(); i++) {
            JSONObject row = resultJSON.getJSONObject(i);

            String dateCreatedStr = row.getString("date_created");
            Date dateCreated = null;
            if (dateCreatedStr != null && !"null".equalsIgnoreCase(dateCreatedStr)) dateCreated = DateUtils.parseDate(dateCreatedStr);

            //order = new Order(row.getLong("id"));
            Double totalPrice = row.getDouble("total_price");
            order = new Order(row.getLong("id"), row.getLong("client_id"), row.getLong("vendor_id"), dateCreated, row.getString("status"), totalPrice, row.getString("currency"));
            order.setStatus(row.getString("status"));

            ordersSearchResult.addOrder(order);
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
