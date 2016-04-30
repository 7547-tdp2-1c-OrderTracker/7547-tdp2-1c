package ar.fi.uba.trackerman.tasks.order;

import android.util.Log;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import ar.fi.uba.trackerman.activities.OrderActivity;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.exceptions.NoStockException;
import ar.fi.uba.trackerman.tasks.AbstractTask;

public class UpdateOrderItemTask extends AbstractTask<String,Void,String,OrderActivity> {

    public UpdateOrderItemTask(OrderActivity activity) {
        super(activity);
    }

    @Override
    protected String doInBackground(String... params) {
        String orderId = params[0];
        String itemId = params[1];
        String quantity = params[2];

        String url = "/v1/orders/" + orderId + "/order_items/" + itemId;
        String body = "{\"quantity\": " + quantity + "}";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        String resp = "FAIL";
        try {
            resp = (String) restClient.put(url,body,headers);
        } catch (NoStockException e) {
            weakReference.get().showSnackbarSimpleMessage("No tenemos stock del producto!");
        } catch (BusinessException e) {
            weakReference.get().showSnackbarSimpleMessage(e.getMessage());
        }
        return resp;
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        return "OK";
    }

    @Override
    protected void onPostExecute(String result) {
        weakReference.get().afterUpdateOrderItem(result);
    }

    public interface OrderItemModifier {
        public void afterUpdateOrderItem(String Item);
    }

}
