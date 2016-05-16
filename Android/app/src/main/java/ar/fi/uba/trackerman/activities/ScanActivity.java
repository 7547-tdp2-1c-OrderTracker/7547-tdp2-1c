package ar.fi.uba.trackerman.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import ar.fi.uba.trackerman.domains.Client;
import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.domains.OrderWrapper;
import ar.fi.uba.trackerman.domains.QRValidationWrapper;
import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.tasks.client.GetClientDirectTask;
import ar.fi.uba.trackerman.tasks.client.GetClientTask;
import ar.fi.uba.trackerman.tasks.order.GetDraftOrdersTask;
import ar.fi.uba.trackerman.tasks.qr.GetQRValidationTask;
import ar.fi.uba.trackerman.utils.AppSettings;
import ar.fi.uba.trackerman.utils.MyPreferences;
import ar.fi.uba.trackerman.utils.ShowMessage;
import fi.uba.ar.soldme.R;

public class ScanActivity extends AppCompatActivity implements GetQRValidationTask.QRValidationResponse {

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        try {

            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
            startActivityForResult(intent, 0);

        } catch (Exception e) {

            if (isEmulator()) {
                Toast.makeText(getApplicationContext(), "Desde emulador no se accede al scanner", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
                startActivity(marketIntent);
            }

        }

    }

    // solo tomo como contenido de QR valido, numeros enteros positivos
    private boolean validQRContent(String str) {
        return str.matches("^\\d*$");
    }

    private void openMyClientActivity(long idClient) {
        Intent intent = new Intent(this, ClientActivity.class);
        intent.putExtra(Intent.EXTRA_UID, idClient);
        startActivity(intent);
    }

    private void openClientOrderActivity(long idOrder) {
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(Intent.EXTRA_UID, idOrder);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                if (validQRContent(contents)) {

                    //if (RestClient.isOnline(this)) new GetClientDirectTask(this).execute( contents );

                    String sellerId = String.valueOf(AppSettings.getSellerId());

                    MyPreferences pref = new MyPreferences(this);
                    String lat = pref.get(this.getString(R.string.shared_pref_current_location_lat), "");
                    String lon = pref.get(this.getString(R.string.shared_pref_current_location_lon), "");

                    if (RestClient.isOnline(this)) new GetQRValidationTask(this).execute( contents, sellerId, lat, lon);

                } else {
                    Toast.makeText(getApplicationContext(), "Formato código inválido", Toast.LENGTH_SHORT).show();
                }
            }
            if(resultCode == RESULT_CANCELED){
                //Toast.makeText(getApplicationContext(), "Scan Cancelado", Toast.LENGTH_SHORT).show();
            }
        }

        finishActivity(requestCode);
    }

    public void showSnackbarSimpleMessage(String msg){
        ShowMessage.showSnackbarSimpleMessage(this.getCurrentFocus(), msg);
    }

    public void updateClientDirect(Client client) {
    }


    @Override
    public void afterQRValidationResponse(QRValidationWrapper qrValidationWrapper) {
        Order order = qrValidationWrapper.getOrder();
        Client client = qrValidationWrapper.getClient();
        Toast.makeText(getApplicationContext(), "Orden identificada: "+ client.getFullName(), Toast.LENGTH_SHORT).show();

        openClientOrderActivity(order.getId());
    }
}
