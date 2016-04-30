package ar.fi.uba.trackerman.tasks.order;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.domains.OrderWrapper;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.tasks.AbstractTask;


public class GetDraftOrdersTask extends AbstractTask<String,Void,List<OrderWrapper>,GetDraftOrdersTask.DraftOrdersValidation> {

    public GetDraftOrdersTask(DraftOrdersValidation validation) {
        super(validation);
    }

    public List<OrderWrapper> getDraftOrders(String vendorId) {
        List<OrderWrapper> ordersWrapper = null;
        try {
            ordersWrapper = (List<OrderWrapper>) restClient.get("/v1/orders?status=draft&seller_id="+vendorId);
        } catch (BusinessException e) {
            Log.e("business_error", e.getMessage(), e);
        }
        return ordersWrapper;
    }

    public List<OrderWrapper> getDraftOrders(String vendorId, String clientId) {
        List<OrderWrapper> ordersWrapper = null;
        try {
            ordersWrapper = (List<OrderWrapper>) restClient.get("/v1/orders?status=draft&seller_id="+vendorId+"&client_id="+clientId);
        } catch (BusinessException e) {
            Log.e("business_error", e.getMessage(), e);
        }
        return ordersWrapper;
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        List<OrderWrapper> list = new ArrayList<OrderWrapper>();
        JSONObject ordersList = new JSONObject(json);
        JSONArray resultsJson = ordersList.getJSONArray("results");
        for (int i = 0; i < resultsJson.length(); i++) {
            list.add(OrderWrapper.fromJson(resultsJson.getJSONObject(i)));
        }
        return list;
    }

    @Override
    protected List<OrderWrapper> doInBackground(String... strings) {
        if (strings.length == 1) {
            return this.getDraftOrders(strings[0]);
        } else {
            return this.getDraftOrders(strings[0], strings[1]);
        }

    }

    @Override
    protected void onPostExecute(List<OrderWrapper> ordersWrapper) {
        super.onPostExecute(ordersWrapper);
        weakReference.get().setDraftOrders(ordersWrapper);
    }

    public interface DraftOrdersValidation {
        public void setDraftOrders(List<OrderWrapper> orders);
    }
}
