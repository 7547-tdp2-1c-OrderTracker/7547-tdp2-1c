package ar.fi.uba.trackerman.tasks.qr;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ar.fi.uba.trackerman.domains.QRValidationWrapper;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.ShowMessage;

/**
 * Created by guido on 15/05/16.
 */

public class GetQRValidationTask extends AbstractTask<String,Void,QRValidationWrapper,AppCompatActivity> {

    public GetQRValidationTask(AppCompatActivity activity) {
        super(activity);
    }

    public QRValidationWrapper getQRValidation(String idSeller, String idClient, String lat, String lon) {
        Context ctx = weakReference.get().getApplicationContext();
        QRValidationWrapper qrValidationWrapper = null;
        String body = "{\"client_id\": "+ idClient +",\"seller_id\":"+ idSeller +",\"lat\": "+ lat +",\"lon\": "+ lon+"}";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        String url = "/v1/scanqr/validate";

        try {
            qrValidationWrapper = (QRValidationWrapper) restClient.post(url, body, withAuth(ctx,headers));
        } catch (BusinessException e) {
            ((QRValidationResponse) weakReference.get()).showSnackbarSimpleMessage(e.getMessage());
        } catch (Exception e) {
            ShowMessage.toastMessage(ctx, e.getMessage());
        }
        return qrValidationWrapper;
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        return QRValidationWrapper.fromJson(jsonObject);
    }

    @Override
    protected QRValidationWrapper doInBackground(String... params) {
        if (params.length == 4) {
            String idClient = params[0];
            String idSeller = params[1];
            String lat = params[2];
            String lon = params[3];
            return this.getQRValidation(idSeller, idClient, lat, lon); // paso el idClient
        }
        return null;
    }

    @Override
    protected void onPostExecute(QRValidationWrapper qrValidationWrapper) {
        if(qrValidationWrapper != null){
            ((QRValidationResponse) weakReference.get()).afterQRValidationResponse(qrValidationWrapper);
        }
    }

    public interface QRValidationResponse {
        public void afterQRValidationResponse(QRValidationWrapper qrValidationWrapper);
        public void showSnackbarSimpleMessage(String message);
    }
}
