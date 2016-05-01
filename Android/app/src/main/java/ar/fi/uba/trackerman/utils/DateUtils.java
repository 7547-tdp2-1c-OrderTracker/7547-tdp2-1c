package ar.fi.uba.trackerman.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fi.uba.ar.soldme.R;

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

    public static String dayOfWeekToText(int day_of_week) {
        switch (day_of_week) {
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
            case 7:
                return "Sunday";
        }
        return "";
    }

}
