package ar.fi.uba.trackerman.tasks.seller;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.domains.Seller;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.exceptions.ServerErrorException;
import ar.fi.uba.trackerman.tasks.AbstractTask;

/**
 * Created by guido on 15/05/16.
 */

public class GetSellerDirectTask extends AbstractTask<String,Void,Seller,AppCompatActivity> {

    public GetSellerDirectTask(AppCompatActivity validation) {
        super(validation);
    }

    public Seller getSeller(String sellerId) {
        Seller seller = null;
        try {
            seller = (Seller) restClient.get("/v1/sellers/"+sellerId);
        } catch (BusinessException e) {
            Log.e("business_error", e.getMessage(), e);
        } catch (ServerErrorException e) {
            Log.e("server_error", e.getMessage(), e);
        }
        return seller;
    }


    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject jsonObj = new JSONObject(json);
        return Seller.fromJson(jsonObj);
    }

    @Override
    protected Seller doInBackground(String... strings) {
        if (strings.length == 1) {
            return this.getSeller(strings[0]); // paso el idSeller
        }
        return null;
    }

    @Override
    protected void onPostExecute(Seller seller) {
//        super.onPostExecute(client);
        if(seller != null){
            ((SellerDirectReceiver) weakReference.get()).updateSellerDirect(seller);
        } else {
            // weakReference.get().showSnackbarSimpleMessage("No se puede obtener info del cliente");
        }

    }

    public interface SellerDirectReceiver {
        public void updateSellerDirect(Seller seller);
    }
}
