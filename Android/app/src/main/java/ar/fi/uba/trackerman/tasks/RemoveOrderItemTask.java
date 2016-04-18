package ar.fi.uba.trackerman.tasks;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.utils.AppSettings;

public class RemoveOrderItemTask extends AbstractTask<String,Void,String> {

    private WeakReference<OrderItemRemover> weekModifierReference;

    public RemoveOrderItemTask(OrderItemRemover modifier) {
        weekModifierReference = new WeakReference<OrderItemRemover>(modifier);

    }

    @Override
    protected String doInBackground(String... params) {
        String orderId= params[0];
        String itemId= params[1];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String orderJsonStr;
        Order order=null;
        try {
            URL url = new URL(AppSettings.getServerHost()+"/v1/orders/"+orderId+"/order_items/"+itemId);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("DELETE");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return "OK";
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                return "OK";
            }
            orderJsonStr = buffer.toString();
            return "OK";

        } catch (IOException e) {
            Log.e(ConfirmOrderTask.class.getCanonicalName(), "Error ", e);
            return "FAIL";
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(this.getClass().getCanonicalName(), "Error closing stream", e);
                }
            }
        }
    }


    @Override
    protected void onPostExecute(String result) {
        Log.e(this.getClass().getCanonicalName(),"el resultado "+ result);
        OrderItemRemover modifier= weekModifierReference.get();
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
