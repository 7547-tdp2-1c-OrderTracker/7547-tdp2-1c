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
public class CancellOrderTask extends AbstractTask<String,Void,OrderWrapper,OrderActivity> {

    public CancellOrderTask(OrderActivity activity) {
        super(activity);
    }

    @Override
    protected OrderWrapper doInBackground(String... params) {
        String orderId= params[0];
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        String url = "/v1/orders/"+orderId;
        String body = "{\"status\": \"cancelled\"}";
        OrderWrapper orderWrapper = null;
        try {
            orderWrapper = (OrderWrapper) restClient.put(url,body,headers);
        } catch (BusinessException e) {
            weakReference.get().showSnackbarSimpleMessage(e.getMessage());
        }
        return orderWrapper;
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject orderJson = new JSONObject(json);
        return OrderWrapper.fromJson(orderJson);
    }

    @Override
    protected void onPostExecute(OrderWrapper orderWrapper) {
        OrderCanceller reciver = weakReference.get();
        if(reciver != null){
            reciver.afterOrderCancelled(orderWrapper);
        }else{
            Log.w(this.getClass().getCanonicalName(), "Adapter no longer available!");
        }
    }

    public interface OrderCanceller{
        public void afterOrderCancelled(OrderWrapper orderWrapper);
    }
}
