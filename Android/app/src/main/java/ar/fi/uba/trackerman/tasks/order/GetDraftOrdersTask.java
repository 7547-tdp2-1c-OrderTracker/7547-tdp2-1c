package ar.fi.uba.trackerman.tasks.order;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.domains.OrderWrapper;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.exceptions.ServerErrorException;
import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.ShowMessage;


public class GetDraftOrdersTask extends AbstractTask<String,Void,List<OrderWrapper>,AppCompatActivity> {

    public GetDraftOrdersTask(AppCompatActivity validation) {
        super(validation);
    }

    public List<OrderWrapper> getDraftOrders(String vendorId) {
        Context ctx = weakReference.get().getApplicationContext();
        List<OrderWrapper> ordersWrapper = null;
        try {
            ordersWrapper = (List<OrderWrapper>) restClient.get("/v1/orders?status=draft&seller_id="+vendorId, withAuth(ctx));
        } catch (Exception e) {
            ShowMessage.toastMessage(ctx, e.getMessage());
        }
        return ordersWrapper;
    }

    public List<OrderWrapper> getDraftOrders(String vendorId, String clientId) {
        Context ctx = weakReference.get().getApplicationContext();
        List<OrderWrapper> ordersWrapper = null;
        try {
            ordersWrapper = (List<OrderWrapper>) restClient.get("/v1/orders?status=draft&seller_id="+vendorId+"&client_id="+clientId, withAuth(ctx));
        } catch (Exception e) {
            ShowMessage.toastMessage(ctx, e.getMessage());
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
        ((DraftOrdersValidation) weakReference.get()).setDraftOrders(ordersWrapper);
    }

    public interface DraftOrdersValidation {
        public void setDraftOrders(List<OrderWrapper> orders);
    }
}
