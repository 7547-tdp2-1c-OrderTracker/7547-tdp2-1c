package ar.fi.uba.trackerman.tasks.order;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ar.fi.uba.trackerman.activities.OrderActivity;
import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.domains.OrderWrapper;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.tasks.AbstractTask;

/**
 * Created by plucadei on 31/3/16.
 */
public class CancellOrderTask extends AbstractTask<String,Void,Order,OrderActivity> {

    public CancellOrderTask(OrderActivity activity) {
        super(activity);
    }

    @Override
    protected Order doInBackground(String... params) {
        String orderId= params[0];
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        String url = "/v1/orders/"+orderId;
        String body = "{\"status\": \"cancelled\"}";
        Order order = null;
        try {
            order = (Order) restClient.put(url,body,headers);
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
        if(order != null){
            weakReference.get().afterOrderCancelled(order);
        }else{
            weakReference.get().showSnackbarSimpleMessage("No se pudo cancelar el pedido.");
        }
    }

    public interface OrderCanceller{
        public void afterOrderCancelled(Order order);
    }
}
