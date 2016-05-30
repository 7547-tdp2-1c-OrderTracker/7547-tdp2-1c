package ar.fi.uba.trackerman.tasks.order;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ar.fi.uba.trackerman.domains.OrderItem;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.exceptions.NoStockException;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.ShowMessage;

public class PostOrderItemsTask extends AbstractTask<String,Void,OrderItem,PostOrderItemsTask.OrderItemCreator> {

    public PostOrderItemsTask(OrderItemCreator orderItemCreator) {
        super(orderItemCreator);
    }

    public OrderItem createOrderItem(String orderId, String productId, String quantity) {
        String body = "{\"product_id\": "+productId+",\"quantity\":"+quantity+"}";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        String url = "/v1/orders/"+orderId+"/order_items";
        Context ctx = weakReference.get().getApplicationContext();
        return (OrderItem) restClient.post(url, body, withAuth(ctx, headers));
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject orderItemJson = new JSONObject(json);
        return OrderItem.fromJson(orderItemJson);
    }

    @Override
    protected OrderItem doInBackground(String... params) {
        try {
            return this.createOrderItem(params[0], params[1], params[2]);
        } catch (NoStockException e) {
            ShowMessage.showSnackbarSimpleMessage(weakReference.get().getCurrentView(), "Nos quedamos sin stock!");
        } catch (BusinessException e) {
            ShowMessage.showSnackbarSimpleMessage(weakReference.get().getCurrentView(),e.getMessage());
        } catch (final Exception e) {
            weakReference.get().getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    ShowMessage.showSnackbarSimpleMessage(weakReference.get().getActivity().getCurrentFocus(), e.getMessage());
                }
            });
        }
        return null;
    }

    @Override
    protected void onPostExecute(OrderItem orderItem) {
        super.onPostExecute(orderItem);
        if (orderItem != null) {
            weakReference.get().afterCreatingOrderItem(orderItem);
        }
    }

    public interface OrderItemCreator {
        public void afterCreatingOrderItem(OrderItem orderItemCreated);
        public View getCurrentView();
        public Context getApplicationContext();
        public Activity getActivity();
    }
}
