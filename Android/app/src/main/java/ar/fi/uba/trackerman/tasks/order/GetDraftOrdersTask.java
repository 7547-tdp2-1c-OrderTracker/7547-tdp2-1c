package ar.fi.uba.trackerman.tasks.order;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.exceptions.OrderTrackerException;
import ar.fi.uba.trackerman.tasks.AbstractTask;


public class GetDraftOrdersTask extends AbstractTask<String,Void,List<Order>,GetDraftOrdersTask.DraftOrdersValidation> {

    public GetDraftOrdersTask(DraftOrdersValidation validation) {
        super(validation);
    }

    public List<Order> getDraftOrders(String vendorId) {
        return (List<Order>) restClient.get("/v1/orders?status=draft&vendor_id="+vendorId);
    }

    public List<Order> getDraftOrders(String vendorId, String clientId) {
        return (List<Order>) restClient.get("/v1/orders?status=draft&vendor_id="+vendorId+"&client_id="+clientId);
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject ordersList = null;
        List<Order> list = new ArrayList<Order>();
        try {
            ordersList = new JSONObject(json);
            JSONArray resultsJson = ordersList.getJSONArray("results");
            for (int i = 0; i < resultsJson.length(); i++) {
                list.add(Order.fromJson(resultsJson.getJSONObject(i)));
            }
        } catch (OrderTrackerException e) {
            Log.e("business_error","lalalaalal",e);
        }
        return list;
    }

    @Override
    protected List<Order> doInBackground(String... strings) {
        if (strings.length == 1) {
            return this.getDraftOrders(strings[0]);
        } else {
            return this.getDraftOrders(strings[0], strings[1]);
        }

    }

    @Override
    protected void onPostExecute(List<Order> orders) {
        super.onPostExecute(orders);
        weakReference.get().setDraftOrders(orders);
    }

    public interface DraftOrdersValidation {
        public void setDraftOrders(List<Order> orders);
    }
}
