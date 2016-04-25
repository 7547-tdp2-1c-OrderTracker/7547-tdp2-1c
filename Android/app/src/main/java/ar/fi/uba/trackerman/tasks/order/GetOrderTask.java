package ar.fi.uba.trackerman.tasks.order;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.activities.OrderActivity;
import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.tasks.AbstractTask;

/**
 * Created by plucadei on 31/3/16.
 */
public class GetOrderTask extends AbstractTask<String,Void,Order,OrderActivity> {

    public GetOrderTask(OrderActivity activity) {
        super(activity);
    }

    @Override
    protected Order doInBackground(String... params) {
        String orderId= params[0];
        return (Order) restClient.get("/v1/orders/"+orderId);
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject ordersList = new JSONObject(json);
        return Order.fromJson(ordersList);
    }

    @Override
    protected void onPostExecute(Order order) {
        if(order != null){
            ((OrderReceiver) weakReference.get()).updateOrderInformation(order);
        }else{
            weakReference.get().showSnackbarSimpleMessage("No se puede obtener el pedido!");
        }
    }

    public interface OrderReceiver {
        public void updateOrderInformation(Order order);
    }
}
