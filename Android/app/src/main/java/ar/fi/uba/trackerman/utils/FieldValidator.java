package ar.fi.uba.trackerman.utils;

import android.telephony.PhoneNumberUtils;

import java.text.DecimalFormat;

/**
 * Created by smpiano on 4/24/16.
 */
public class FieldValidator {

    private static DecimalFormat TWO_DECIMALS = new DecimalFormat("####0.00");
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

    public static String showCoolDistance(double dist) {
        return TWO_DECIMALS.format(dist);
    }
}
