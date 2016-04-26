package ar.fi.uba.trackerman.server;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;

import ar.fi.uba.trackerman.exceptions.BusinessException;
import ar.fi.uba.trackerman.exceptions.NoStockException;

/**
 * Created by smpiano on 4/26/16.
 */
public enum ErrorMatcher {
    NO_STOCK("no_stock", NoStockException.class);

    private String errorKey;
    private Class<? extends BusinessException> throwable;

    private ErrorMatcher(String key, Class<? extends BusinessException> throwable) {
        this.errorKey = key;
        this.throwable = throwable;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public BusinessException getThrowable(String msg) {
        try {
            return throwable.getDeclaredConstructor(String.class).newInstance(msg);
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
