package ar.fi.uba.trackerman.tasks.order;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ar.fi.uba.trackerman.activities.OrderActivity;
import ar.fi.uba.trackerman.domains.OrderWrapper;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.ShowMessage;

/**
 * Created by plucadei on 31/3/16.
 */
public class EmptyOrderTask extends AbstractTask<String,Void,OrderWrapper,OrderActivity> {

    public EmptyOrderTask(OrderActivity activity) {
        super(activity);
    }

    @Override
    protected OrderWrapper doInBackground(String... params) {
        String orderId = params[0];
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        OrderWrapper orderWrapper = null;
        try{
            orderWrapper = (OrderWrapper) restClient.put("/v1/orders/"+orderId+"/empty",null,headers);
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
        if(orderWrapper != null){
            weakReference.get().updateOrderInformation(orderWrapper);
        }else{
            weakReference.get().showSnackbarSimpleMessage("Error obteniendo el pedido, luego de vaciarlo!");
        }
    }
}
