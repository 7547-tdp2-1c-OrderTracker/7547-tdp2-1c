package ar.fi.uba.trackerman.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;

import fi.uba.ar.soldme.R;

/**
 * Created by smpiano on 4/25/16.
 */
public class MyPreferences {

    private final Context context;

    public MyPreferences(Context current) {
        this.context = current;
    }

    private SharedPreferences getSharedPreferences() {
        if (context == null) {
            Log.e("pref_error", "Error seteando el contexto");
        }
        return context.getSharedPreferences(context.getString(R.string.shared_pref_file_key), Context.MODE_PRIVATE);
    }

    public void save(String key, Long value) {
        SharedPreferences.Editor editor = this.getSharedPreferences().edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void save(String key, String value) {
        SharedPreferences.Editor editor = this.getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void save(String key, Integer value) {
        SharedPreferences.Editor editor = this.getSharedPreferences().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void load() {
        this.getSharedPreferences().getLong(context.getString(R.string.shared_pref_current_vendor_id), (long) 1);
        this.getSharedPreferences().getLong(context.getString(R.string.shared_pref_current_client_id), (long) -1);
        this.getSharedPreferences().getLong(context.getString(R.string.shared_pref_current_order_id), (long) -1);
    }

    public Long get(String key, Long defValue) {
        return this.getSharedPreferences().getLong(key, defValue);
    }

    public String get(String key, String defValue) {
        return this.getSharedPreferences().getString(key, defValue);
    }

    public Integer get(String key, Integer defValue) {
        return this.getSharedPreferences().getInt(key, defValue);
    }

    public Double get(String key, Double defValue) {
        return Double.parseDouble(this.getSharedPreferences().getString(key, defValue.toString()));
    }

    public void clear() {
        SharedPreferences.Editor editor = this.getSharedPreferences().edit();
        editor.clear();
        editor.commit();
    }
}
