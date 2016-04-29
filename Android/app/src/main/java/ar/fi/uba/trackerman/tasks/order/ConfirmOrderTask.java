package ar.fi.uba.trackerman.tasks.order;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ar.fi.uba.trackerman.activities.OrderActivity;
import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.tasks.AbstractTask;

/**
 * Created by plucadei on 31/3/16.
 */
public class ConfirmOrderTask extends AbstractTask<String,Void,Order,OrderActivity> {

    public ConfirmOrderTask(OrderActivity activity) {
        super(activity);
    }

    @Override
    protected Order doInBackground(String... params) {
        String orderId= params[0];
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        String url = "/v1/orders/"+orderId;
        String body = "{\"status\": \"confirmed\"}";
        return (Order) restClient.put(url,body,headers);
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject orderJson = new JSONObject(json);
        return Order.fromJson(orderJson);
    }

    @Override
    protected void onPostExecute(Order order) {
        OrderConfirmer reciver= weakReference.get();
        if(reciver!=null){
            reciver.afterOrderConfirmed(order);
        }else{
            Log.w(this.getClass().getCanonicalName(),"Adapter no longer available!");
        }
    }

    public interface OrderConfirmer {
        public void afterOrderConfirmed(Order order);
    }
}
