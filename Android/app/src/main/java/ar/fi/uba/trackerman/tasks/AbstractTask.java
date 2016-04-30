package ar.fi.uba.trackerman.tasks;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import ar.fi.uba.trackerman.server.RestClient;

/**
 * Created by smpiano on 17/04/16.
 */
public abstract class AbstractTask<Params,Process,Return,Reference> extends AsyncTask<Params,Process,Return>  implements RestClient.ResponseParse {

    protected final RestClient restClient;

    protected WeakReference<Reference> weakReference;

    public AbstractTask(Reference ref) {
        weakReference = new WeakReference<Reference>(ref);
        restClient = new RestClient(this);
    }

}
