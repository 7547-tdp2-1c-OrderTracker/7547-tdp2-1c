package ar.fi.uba.trackerman.tasks.order;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.activities.OrderActivity;
import ar.fi.uba.trackerman.domains.OrderWrapper;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.ShowMessage;

/**
 * Created by plucadei on 31/3/16.
 */
public class GetOrderTask extends AbstractTask<String,Void,OrderWrapper,OrderActivity> {

    public GetOrderTask(OrderActivity activity) {
        super(activity);
    }

    @Override
    protected OrderWrapper doInBackground(String... params) {
        Context ctx = weakReference.get().getApplicationContext();
        String orderId= params[0];
        OrderWrapper orderWrapper = null;
        try {
            orderWrapper = (OrderWrapper) restClient.get("/v1/orders/"+orderId, withAuth(ctx));
        } catch (BusinessException e) {
            weakReference.get().showSnackbarSimpleMessage(e.getMessage());
        } catch (Exception e) {
            ShowMessage.toastMessage(ctx,e.getMessage());
        }
        return orderWrapper;
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
