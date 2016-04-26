package ar.fi.uba.trackerman.exceptions;

/**
 * Created by smpiano on 4/26/16.
 */
public class NoStockException extends BusinessException {

    public NoStockException(String msg, Throwable e) {
        super("No hay stock", e);
    }

    public NoStockException(String msg) {
        super("No hay stock - " + msg);
    }
}
