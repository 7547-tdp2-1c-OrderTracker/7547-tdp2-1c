package ar.fi.uba.trackerman.utils;

import android.content.Context;

import com.google.gson.Gson;

import ar.fi.uba.trackerman.domains.Seller;
import fi.uba.ar.soldme.R;

/**
 * Created by smpiano on 5/23/16.
 */
public class MyPreferenceHelper {

    private final Context context;

    public MyPreferenceHelper(Context context) {
        this.context = context;
    }

    public void saveSeller(Seller seller) {
        MyPreferences pref = new MyPreferences(context);
        pref.save(context.getString(R.string.shared_pref_current_seller), "");
        Gson gson = new Gson();
        String json = gson.toJson(seller);
        pref.save(context.getString(R.string.shared_pref_current_seller), json);
    }

    public Seller getSeller() {
        MyPreferences pref = new MyPreferences(context);
        Gson gson = new Gson();
        String json = pref.get(context.getString(R.string.shared_pref_current_seller), "");
        Seller seller = gson.fromJson(json, Seller.class);
        return seller;
    }
}
