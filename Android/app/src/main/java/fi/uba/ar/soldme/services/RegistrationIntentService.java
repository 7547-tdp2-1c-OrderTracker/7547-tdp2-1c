package fi.uba.ar.soldme.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import android.provider.Settings.Secure;

import org.json.JSONException;

import ar.fi.uba.trackerman.domains.Order;
import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.exceptions.ServerErrorException;
import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.utils.AppSettings;
import ar.fi.uba.trackerman.utils.MyPreferenceHelper;
import ar.fi.uba.trackerman.utils.MyPreferences;
import ar.fi.uba.trackerman.utils.ShowMessage;
import fi.uba.ar.soldme.R;

public class RegistrationIntentService extends IntentService {
    private RestClient restClient;

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"promotions","stock"};
    private static final String SENT_TOKEN_TO_SERVER = "SENT_TOKEN_TO_SERVER";
    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.restClient=new RestClient(new RestClient.ResponseParse() {

            @Override
            public Object readResponse(String json) throws JSONException {
                Log.i(TAG,"Response: "+json);
                return null;
            }
        });
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "GCM Registration Token: " + token);

            MyPreferenceHelper helper = new MyPreferenceHelper(this);
            sendRegistrationToServer(token, helper.getSeller().getId());

            subscribeTopics(token);
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
        } catch (Exception e) {
            ShowMessage.toastMessage(this, "Failed to complete token refresh");
            sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
        }
    }

    private void sendRegistrationToServer(String token, Long sellerId) {
        Log.i(this.getClass().getCanonicalName(),"Token: "+token);
        String androidId = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);

        String body = "{\"seller_id\": "+sellerId+", \"registration_id\":\""+token+"\"}";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        MyPreferences pref = new MyPreferences(this);
        headers.put("authorization", pref.get(getString(R.string.shared_pref_current_token),""));
        try {
            restClient.put("/v1/devices/"+androidId, body, headers);
        } catch (Exception e) {
            ShowMessage.toastMessage(this, e.getMessage());
        }
    }

    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}