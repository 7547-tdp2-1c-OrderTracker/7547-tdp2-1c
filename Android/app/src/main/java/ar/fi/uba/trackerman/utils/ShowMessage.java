package ar.fi.uba.trackerman.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

public class ShowMessage {

    public static void showSnackbarSimpleMessage(View view, String msg) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}
