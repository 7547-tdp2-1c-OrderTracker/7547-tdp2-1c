package ar.fi.uba.trackerman.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.utils.AppSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by smpiano on 17/04/16.
 */
public abstract class AbstractTask<Params,Progress,Return,Reference> extends AsyncTask<Params,Process,Return>  implements RestClient.ResponseParse {

    protected static final String SERVER_HOST = AppSettings.getServerHost();
    protected final RestClient restClient;

    protected WeakReference<Reference> weakReference;

    public AbstractTask(Reference ref) {
        weakReference = new WeakReference<Reference>(ref);
        restClient = new RestClient(this);
    }

}
