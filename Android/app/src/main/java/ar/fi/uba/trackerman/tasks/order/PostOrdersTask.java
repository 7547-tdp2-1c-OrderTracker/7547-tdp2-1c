package ar.fi.uba.trackerman.tasks.order;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ar.fi.uba.trackerman.activities.ClientActivity;
import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.ShowMessage;

public class PostOrdersTask extends AbstractTask<String,Void,Order,ClientActivity> {

    public PostOrdersTask(ClientActivity activity) {
        super(activity);
    }

    @Override
    protected Order doInBackground(String... params) {
        Context ctx = weakReference.get().getApplicationContext();
        String vendorId = params[0];
        String clientId = params[1];
        String body = "{\"client_id\": "+clientId+",\"seller_id\":"+vendorId+"}";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        Order order = null;
        try {
            order = (Order) restClient.post("/v1/orders", body, withAuth(ctx, headers));
        } catch (BusinessException e) {
            weakReference.get().showSnackbarSimpleMessage(e.getMessage());
        } catch (Exception e) {
            ShowMessage.toastMessage(ctx, e.getMessage());
        }
        return order;
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject orderJson = new JSONObject(json);
        return Order.fromJson(orderJson);
    }

    @Override
    protected void onPostExecute(Order order) {
        super.onPostExecute(order);
        if (order == null){
            weakReference.get().showSnackbarSimpleMessage("Cáspitas! Tenemos un problema!");
        } else {
            weakReference.get().afterCreatingOrder(order);
        }
    }
}
