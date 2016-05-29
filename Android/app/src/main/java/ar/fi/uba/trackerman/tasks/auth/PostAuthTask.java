package ar.fi.uba.trackerman.tasks.auth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ar.fi.uba.trackerman.activities.LoginActivity;
import ar.fi.uba.trackerman.domains.Token;
import ar.fi.uba.trackerman.tasks.AbstractTask;
import ar.fi.uba.trackerman.utils.ShowMessage;

public class PostAuthTask extends AbstractTask<String,Void,Token,LoginActivity> {

    public PostAuthTask(LoginActivity activity) {
        super(activity);
    }

    @Override
    protected Token doInBackground(String... params) {
        String email = params[0];
        String pass = params[1];
        String body = "{\"email\": \""+email+"\",\"password\":\""+pass+"\"}";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        Token token = null;
        try {
            token = (Token) restClient.post("/v1/auth/login", body, headers);
        } catch (Exception e) {
            ShowMessage.showSnackbarSimpleMessage(weakReference.get().getCurrentFocus(), e.getMessage());
        }
        return token;
    }

    @Override
    public Object readResponse(String json) throws JSONException {
        JSONObject tokenJson = new JSONObject(json);
        return Token.fromJson(tokenJson);
    }

    @Override
    protected void onPostExecute(Token token) {
        super.onPostExecute(token);
        weakReference.get().onLoginSuccess(token);
    }

    public interface ResultLogin {
        public void onLoginSuccess(Token token);
    }
}
