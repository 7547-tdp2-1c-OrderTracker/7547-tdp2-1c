package ar.fi.uba.trackerman.exceptions;

import android.util.Log;

/**
 * Created by smpiano on 4/23/16.
 */
public class BusinessException extends RuntimeException {

    private BusinessException(String msg, Boolean logError, Throwable e) {
        super(msg, e);
        if (logError) Log.e("business_error",msg,this);
    }

    public BusinessException(String msg) {
        super(msg);
        Log.e("business_error",msg,this);
    }

    public BusinessException(String msg, Throwable e) {
        this(msg, true, e);
    }
}
