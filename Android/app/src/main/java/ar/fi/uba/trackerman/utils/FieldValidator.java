package ar.fi.uba.trackerman.utils;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by smpiano on 4/24/16.
 */
public class FieldValidator {

    private static DecimalFormat THREE_DECIMALS = new DecimalFormat("####0.000");
    private static DecimalFormat THREE_DECIMALS_LOCALIZED = new DecimalFormat("####0.000");
    private static DecimalFormat TWO_DECIMALS = new DecimalFormat("####0.00");
    private static DecimalFormat ONE_DECIMALS = new DecimalFormat("####0.0");
    private static DecimalFormat NO_DECIMALS = new DecimalFormat("####0");
    public static boolean isValid(String content) {
        return content != null && !"null".equalsIgnoreCase(content);
    }

    public static String isContentValid(String content) {
        return (isValid(content)) ? content : "";
    }

    public static boolean isValidMail(CharSequence expectedMail) {
        String trimmedMail = expectedMail.toString().trim();
        return android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedMail).matches();
    }

    public static boolean isValidPhone(CharSequence expectedPhone) {
        String trimmedPhone = expectedPhone.toString().trim();
        return PhoneNumberUtils.isGlobalPhoneNumber(trimmedPhone);
    }

    public static boolean isValidQuantity(String txt) {
        if (txt.isEmpty()) return false;
        if (isValidNumber(txt) && Integer.parseInt(txt) == 0) return false;
        return isValidNumber(txt);
    }

    public static boolean isValidNumber(String txt) {
        try {
            Integer.parseInt(txt.toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String showCoolDistance(Context ctx, double dist) {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(getAndroidLocale(ctx));
        dfs.setDecimalSeparator(',');
        THREE_DECIMALS_LOCALIZED.setDecimalFormatSymbols(dfs);
        String expected = THREE_DECIMALS.format(dist);

        if (expected=="0.000") return "";

        String unit = "km";
        if (!expected.isEmpty() && (Double.valueOf(expected).compareTo(1D) < 0)) {
            expected = Integer.valueOf(expected.substring(expected.indexOf(".")+1)).toString();
            unit = "m";
        } else if (!expected.isEmpty() && (Double.valueOf(expected).compareTo(100D) >= 0)) {
            expected = NO_DECIMALS.format(dist);
        } else if (!expected.isEmpty()) {
            expected = ONE_DECIMALS.format(dist);
        }
        return expected+" "+unit;
    }

    public static Locale getAndroidLocale(Context appContext) {
        return appContext.getResources().getConfiguration().locale;
    }

    public static String showPrice(double price) {
        return TWO_DECIMALS.format(price);
    }
}
