package ar.fi.uba.trackerman.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by smpiano on 4/18/16.
 */
public class DateUtils {

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public static Date parseDate(String dateStr) {
        Date d = null;
        try {
            if (dateStr != null) {
                d = FORMATTER.parse(dateStr);
            }
        } catch (ParseException pe) {
            Log.e("parse_date_error","Error tratando de parsear fecha "+dateStr,pe);
        }
        return d;
    }
}
