package ar.fi.uba.trackerman.utils;

import android.telephony.PhoneNumberUtils;

/**
 * Created by smpiano on 4/24/16.
 */
public class FieldValidator {

    public static boolean isValid(String content) {
        return content != null && !"null".equalsIgnoreCase(content);
    }

    public static String isContentValid(String content) {
        return (isValid(content))? content : "";
    }

    public static boolean isValidMail(CharSequence expectedMail) {
        String trimmedMail = expectedMail.toString().trim();
        return android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedMail).matches();
    }

    public static boolean isValidPhone(CharSequence expectedPhone) {
        String trimmedPhone = expectedPhone.toString().trim();
        return PhoneNumberUtils.isGlobalPhoneNumber(trimmedPhone);
    }
}
