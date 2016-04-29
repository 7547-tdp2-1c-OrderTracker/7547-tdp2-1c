package ar.fi.uba.trackerman.exceptions;

/**
 * Created by smpiano on 4/28/16.
 */
public class AlreadyConfirmedException extends BusinessException {
    public AlreadyConfirmedException(String msg, Integer status) {
        super("Already confirmed order [status:"+status+"] - " + msg);
    }
}
