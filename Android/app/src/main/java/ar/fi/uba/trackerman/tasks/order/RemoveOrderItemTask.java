package ar.fi.uba.trackerman.tasks.order;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import ar.fi.uba.trackerman.activities.OrderActivity;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.exceptions.ServerErrorException;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.ShowMessage;

public class RemoveOrderItemTask extends AbstractTask<String,Void,String,OrderActivity> {

    public RemoveOrderItemTask(OrderActivity activity) {
        super(activity);
    }

    @Override
    protected String doInBackground(String... params) {
        Context ctx = weakReference.get().getApplicationContext();
        String orderId = params[0];
        String itemId = params[1];
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        String resp = null;
        String url = "/v1/orders/" + orderId + "/order_items/" + itemId;
        try {
            resp = (String) restClient.delete(url, null, withAuth(ctx,headers));
            if (resp == null) resp = "OK";
        } catch (BusinessException e) {
            weakReference.get().showSnackbarSimpleMessage(e.getMessage());
        } catch (ServerErrorException e) {
            resp = "FAIL";
        } catch (Exception e) {
            ShowMessage.toastMessage(ctx, e.getMessage());
        }
        return resp;
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        return "OK";
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(this.getClass().getCanonicalName(), "el resultado " + result);
        if(result!=null){
            weakReference.get().updateOrderItem(result);
        }else{
            weakReference.get().showSnackbarSimpleMessage("No se pudo quitar el item del pedido!");
        }
    }

    public interface OrderItemRemover {
        public void updateOrderItem(String Item);
    }

}
