package ar.fi.uba.trackerman.tasks;

import android.os.AsyncTask;

import ar.fi.uba.trackerman.utils.AppSettings;

/**
 * Created by guido on 17/04/16.
 */
public abstract class AbstractTask<A,B,E> extends AsyncTask<A,B,E> {

    protected static final String SERVER_HOST = AppSettings.getServerHost();

}
