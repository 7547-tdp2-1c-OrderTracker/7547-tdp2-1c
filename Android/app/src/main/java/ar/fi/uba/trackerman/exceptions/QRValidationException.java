package ar.fi.uba.trackerman.exceptions;

/**
 * Created by smpiano on 4/26/16.
 */
public class QRValidationException extends BusinessException {

    public QRValidationException(String msg, Throwable e) {
        super("Error código QR", e);
    }

    public QRValidationException(String msg, Integer status) {
        super("Error al verificar código QR [status:"+status+"] - " + msg);
    }
}
