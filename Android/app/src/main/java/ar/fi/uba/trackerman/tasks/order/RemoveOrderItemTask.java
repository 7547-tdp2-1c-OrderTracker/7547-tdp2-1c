package ar.fi.uba.trackerman.tasks.order;

import android.util.Log;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import ar.fi.uba.trackerman.activities.OrderActivity;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.exceptions.ServerErrorException;
import ar.fi.uba.trackerman.tasks.AbstractTask;

public class RemoveOrderItemTask extends AbstractTask<String,Void,String,OrderActivity> {

    public RemoveOrderItemTask(OrderActivity activity) {
        super(activity);
    }

    @Override
    protected String doInBackground(String... params) {
        String orderId = params[0];
        String itemId = params[1];
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        String resp = null;
        String url = "/v1/orders/" + orderId + "/order_items/" + itemId;
        try {
            resp = (String) restClient.delete(url, null, headers);
            if (resp == null) resp = "OK";
        } catch (BusinessException e) {
            weakReference.get().showSnackbarSimpleMessage(e.getMessage());
        } catch (ServerErrorException e) {
            resp = "FAIL";
        }
        return resp;
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        return "OK";
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(this.getClass().getCanonicalName(),"el resultado "+ result);
        OrderItemRemover modifier= weakReference.get();
        if(modifier!=null){
            modifier.updateOrderItem(result);
        }else{
            Log.e(this.getClass().getCanonicalName(), "Adapter no longer available!");
        }
    }

    public interface OrderItemRemover {
        public void updateOrderItem(String Item);
    }

}
