package ar.fi.uba.trackerman.exceptions;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by smpiano on 4/26/16.
 */
public enum ErrorMatcher {
    ALREADY_CONFIRMED(AlreadyConfirmedException.class),
    DEFAULT_ERROR(BusinessException.class),
    NO_STOCK(NoStockException.class),
    UNKNOWN(UnknownException.class);

    private Class<? extends BusinessException> throwable;

    private ErrorMatcher(Class<? extends BusinessException> throwable) {
        this.throwable = throwable;
    }

    public BusinessException getThrowable(String msg, Integer status) {
        try {
            return throwable.getDeclaredConstructor(String.class, Integer.class).newInstance(msg, status);
        } catch (InstantiationException e) {
            Log.e("error_matcher","No se encuentra constructor por defecto para "+throwable.getName(),e);
        } catch (IllegalAccessException e) {
            Log.e("error_matcher", "No se puede instanciar " + throwable.getName(), e);
        } catch (NoSuchMethodException e) {
            Log.e("error_matcher", "No se encuentra el metodo con esos params " + throwable.getName(), e);
        } catch (InvocationTargetException e) {
            Log.e("error_matcher", "Mala invocacion " + throwable.getName(), e);
        }
        return null;
    }
}
