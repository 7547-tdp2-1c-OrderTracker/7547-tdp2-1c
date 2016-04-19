package ar.fi.uba.trackerman.utils;


import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

public class ShowMessage {

    public static void showSnackbarSimpleMessage(CoordinatorLayout layout, String msg) {
        CoordinatorLayout coordinatorLayout = layout;
        Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}
