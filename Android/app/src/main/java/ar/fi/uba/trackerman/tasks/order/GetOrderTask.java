package ar.fi.uba.trackerman.tasks.order;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.activities.OrderActivity;
import ar.fi.uba.trackerman.domains.OrderWrapper;
import ar.fi.uba.trackerman.tasks.AbstractTask;

/**
 * Created by plucadei on 31/3/16.
 */
public class GetOrderTask extends AbstractTask<String,Void,OrderWrapper,OrderActivity> {

    public GetOrderTask(OrderActivity activity) {
        super(activity);
    }

    @Override
    protected OrderWrapper doInBackground(String... params) {
        String orderId= params[0];
        return (OrderWrapper) restClient.get("/v1/orders/"+orderId);
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject ordersList = new JSONObject(json);
        return OrderWrapper.fromJson(ordersList);
    }

    @Override
    protected void onPostExecute(OrderWrapper orderWrapper) {
        if(orderWrapper != null){
            weakReference.get().updateOrderInformation(orderWrapper);
        }else{
            weakReference.get().showSnackbarSimpleMessage("No se puede obtener el pedido!");
        }
    }

    public interface OrderReceiver {
        public void updateOrderInformation(OrderWrapper orderWrapper);
    }
}
