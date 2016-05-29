package ar.fi.uba.trackerman.exceptions;

/**
 * Created by smpiano on 4/26/16.
 */
public class LoginException extends BusinessException {

    public LoginException(String msg, Throwable e) {
        super("Parece que quieres intentar colarte! Email o pass incorrectos.", e);
    }

    public LoginException(String msg, Integer status) {
        super("Parece que quieres intentar colarte! " + msg);
    }
}
