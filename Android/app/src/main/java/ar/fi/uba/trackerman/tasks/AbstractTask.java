package ar.fi.uba.trackerman.tasks;

import android.os.AsyncTask;

import ar.fi.uba.trackerman.utils.AppSettings;
import java.lang.ref.WeakReference;

/**
 * Created by smpiano on 17/04/16.
 */
public abstract class AbstractTask<Params,Progress,Return,Reference> extends AsyncTask<Params,Process,Return> {

    protected static final String SERVER_HOST = AppSettings.getServerHost();

    protected WeakReference<Reference> weakReference;

    public AbstractTask(Reference ref) {
        weakReference = new WeakReference<Reference>(ref);
    }
}
