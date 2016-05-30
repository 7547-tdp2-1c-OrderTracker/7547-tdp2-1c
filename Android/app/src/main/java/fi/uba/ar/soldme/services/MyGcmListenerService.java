package fi.uba.ar.soldme.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ar.fi.uba.trackerman.activities.ClientActivity;
import ar.fi.uba.trackerman.activities.MainActivity;
import ar.fi.uba.trackerman.activities.ProductActivity;
import fi.uba.ar.soldme.R;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    private static final String NEW_CLIENT = "NEW_CLIENT";
    private static final String CLIENT_UPDATED = "CLIENT_UPDATED";
    private static final String PRODUCT_STOCKED = "PRODUCT_STOCKED";
    private static final String NEW_PROMOTION= "NEW_PROMOTION";


    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String identifier = data.getString("identifier");
        String type= data.getString("type");
        String message= data.getString("message");
        String picture= data.getString("picture");
        Log.d(TAG, "From: " + from + " , Tipo: " + type + " , Id: " + identifier + " , Pic:" + picture);
        if(NEW_CLIENT.equals(type)){
            showNewClientNotification(message,identifier,picture);
        }else if(CLIENT_UPDATED.equals(type)){
            showClientUpdatedNotification(message,identifier,picture);
        }else if(PRODUCT_STOCKED.equals(type)){
            showProductStockedNotification(message,identifier,picture);
        }else if(NEW_PROMOTION.equals(type)) {
            showNewPromotionNotification(message,picture,data);
        }
    }

    private void showNewPromotionNotification(String message, String picture, Bundle data) {
        String discount = data.getString("percent");
        String product = data.getString("product");
        String brand = data.getString("brand");
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,0);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String content = discount + "%" + ((product!=null)?" en "+product:(brand!=null)?" en "+brand:"");
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(message)
                .setContentText(content)
                .setAutoCancel(true)
                .setGroup("Promotions")
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if(picture!=null && !picture.isEmpty()){
            notificationBuilder.setLargeIcon(this.getBitmapFromURL(picture));
        }
        
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void showNewClientNotification(String message, String clientId, String picture) {
        Intent intent = new Intent(this, ClientActivity.class);
        intent.putExtra(Intent.EXTRA_UID,Long.parseLong(clientId));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 200+Integer.parseInt(clientId), intent, 0);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(getString(R.string.new_client))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if(picture!=null && !picture.isEmpty()){
            notificationBuilder.setLargeIcon(this.getBitmapFromURL(picture));
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(200+Integer.parseInt(clientId), notificationBuilder.build());
    }

    private void showClientUpdatedNotification(String message, String clientId, String picture) {
        Intent intent = new Intent(this, ClientActivity.class);
        intent.putExtra(Intent.EXTRA_UID,Long.parseLong(clientId));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, Integer.parseInt(clientId), intent,0);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(getString(R.string.client_updated))
                .setContentText(message)
                .setAutoCancel(true)
                .setGroup("ClientsUpdate")
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if(picture!=null && !picture.isEmpty()){
            notificationBuilder.setLargeIcon(this.getBitmapFromURL(picture));
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(Integer.parseInt(clientId), notificationBuilder.build());
    }

    private void showProductStockedNotification(String message, String productId, String picture) {
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra(Intent.EXTRA_UID,Long.parseLong(productId));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 500+Integer.parseInt(productId), intent,0);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(getString(R.string.product_stocked))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setGroup("stock")
                .setContentIntent(pendingIntent);

        if(picture!=null && !picture.isEmpty()){
            notificationBuilder.setLargeIcon(this.getBitmapFromURL(picture));
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(500+Integer.parseInt(productId), notificationBuilder.build());
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}