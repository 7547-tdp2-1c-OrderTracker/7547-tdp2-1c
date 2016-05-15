package ar.fi.uba.trackerman.tasks.order;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ar.fi.uba.trackerman.activities.ClientActivity;
import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.tasks.AbstractTask;

public class PostOrdersTask extends AbstractTask<String,Void,Order,ClientActivity> {

    public PostOrdersTask(ClientActivity activity) {
        super(activity);
    }

    @Override
    protected Order doInBackground(String... params) {
        String vendorId = params[0];
        String clientId = params[1];
        String body = "{\"client_id\": "+clientId+",\"seller_id\":"+vendorId+"}";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        Order order = null;
        try {
            order = (Order) restClient.post("/v1/orders", body, headers);
        } catch (BusinessException e) {
            weakReference.get().showSnackbarSimpleMessage(e.getMessage());
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
