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

public class UpdateOrderItemTask extends AbstractTask<String,Void,String> {

    private WeakReference<OrderItemModifier> weekModifierReference;

    public UpdateOrderItemTask(OrderItemModifier modifier) {
        weekModifierReference = new WeakReference<OrderItemModifier>(modifier);

    }

    @Override
    protected String doInBackground(String... params) {

        String orderId= params[0];
        String itemId= params[1];
        String quantity= params[2];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String orderJsonStr;
        Order order=null;
        try {
            URL url = new URL(AppSettings.getServerHost()+"/v1/orders/"+orderId+"/order_items/"+itemId);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            String str =  "{\"quantity\": "+quantity+"}";
            byte[] outputInBytes = str.getBytes("UTF-8");
            OutputStream os = urlConnection.getOutputStream();
            os.write(outputInBytes);
            os.close();
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return "FAIL";
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                return "FAIL";
            }
            orderJsonStr = buffer.toString();
            return "OK";
                // return parseOrderJson(orderJsonStr);

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
        OrderItemModifier modifier= weekModifierReference.get();
        if(modifier!=null){
            modifier.updateOrderItem(result);
        }else{
            Log.w(this.getClass().getCanonicalName(), "Adapter no longer available!");
        }
    }

    public interface OrderItemModifier {
        public void updateOrderItem(String Item);
    }

}
