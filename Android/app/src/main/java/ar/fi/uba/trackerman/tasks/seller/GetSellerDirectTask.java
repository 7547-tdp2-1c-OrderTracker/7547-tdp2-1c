package ar.fi.uba.trackerman.tasks.seller;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import ar.fi.uba.trackerman.domains.Seller;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.ShowMessage;

/**
 * Created by guido on 15/05/16.
 */

public class GetSellerDirectTask extends AbstractTask<String,Void,Seller,AppCompatActivity> {

    private View view;
    public GetSellerDirectTask(AppCompatActivity activity) {
        super(activity);
        this.view = activity.getCurrentFocus();
    }

    public Seller getSeller(String sellerId) {
        Context ctx = weakReference.get().getApplicationContext();
        Seller seller = null;
        try {
            seller = (Seller) restClient.get("/v1/sellers/"+sellerId, withAuth(ctx));
        } catch (BusinessException e) {
            ShowMessage.showSnackbarSimpleMessage(view,e.getMessage());
        } catch (Exception e) {
            ShowMessage.toastMessage(ctx, e.getMessage());
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
        ((SellerDirectReceiver) weakReference.get()).updateSellerDirect(seller);
    }

    public interface SellerDirectReceiver {
        public void updateSellerDirect(Seller seller);
    }
}
