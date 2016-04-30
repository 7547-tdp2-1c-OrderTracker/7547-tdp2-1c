package ar.fi.uba.trackerman.tasks;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.utils.ShowMessage;

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

    protected final boolean isOnline(Context ctx) {
        boolean isOnline = RestClient.isOnline(ctx);
        if (!isOnline) {
            ShowMessage.toastMessage(ctx, "Estás desconectado!");
        }
        return isOnline;
    }

}
