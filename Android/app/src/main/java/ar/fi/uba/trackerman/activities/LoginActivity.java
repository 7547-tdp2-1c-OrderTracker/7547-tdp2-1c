package ar.fi.uba.trackerman.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ar.fi.uba.trackerman.domains.Seller;
import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.tasks.seller.GetSellerDirectTask;
import ar.fi.uba.trackerman.utils.AppSettings;
import ar.fi.uba.trackerman.utils.MyPreferenceHelper;
import ar.fi.uba.trackerman.utils.MyPreferences;
import ar.fi.uba.trackerman.utils.ShowMessage;
import fi.uba.ar.soldme.R;

import static ar.fi.uba.trackerman.utils.FieldValidator.isValidQuantity;


public class LoginActivity extends AppCompatActivity implements GetSellerDirectTask.SellerDirectReceiver {

    private static final int REQUEST_SIGNUP = 0;

    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;
    private TextView signupLink;
    private MyPreferences pref = new MyPreferences(this);
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loginButton = (Button) findViewById(R.id.activity_login_btn_login);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        findViewById(R.id.activity_login_link_signup).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ShowMessage.showSnackbarSimpleMessage(v, "No implementado!");
                // Start the Signup activity

                //Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                //startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d("login_activity", "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_NoActionBar); //FIXME change for dialog
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Autenticando...");
        progressDialog.show();

        String email = ((TextView)findViewById(R.id.activity_login_input_email)).getText().toString();
        //String password = passwordText.getText().toString();

        Log.d("Login email = ", email);
        loginButton.setEnabled(false);
        // obtengo el seller, para guardarlo en la shared pref (ver metodo updateSellerDirect);
        if (RestClient.isOnline(this)) new GetSellerDirectTask(this).execute(email);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        //moveTaskToBack(true);
    }

    public void updateSellerDirect(Seller seller) {
        if (seller == null) {
            onLoginFailed();
        } else {
            MyPreferenceHelper helper = new MyPreferenceHelper(this);
            helper.saveSeller(seller);
            // On complete call either onLoginSuccess or onLoginFailed
            onLoginSuccess();
        }
        // onLoginFailed();
        progressDialog.dismiss();
    }

    public void onLoginSuccess() {
        findViewById(R.id.activity_login_btn_login).setEnabled(true);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Ingreso fallido", Toast.LENGTH_LONG).show();
        findViewById(R.id.activity_login_btn_login).setEnabled(true);
    }

    public boolean validate() {
        String seller = ((TextView)findViewById(R.id.activity_login_input_email)).getText().toString();
        return isValidQuantity(seller) && seller.length()<=1;
        //boolean valid = true;

        //String email = emailText.getText().toString();
        //String password = passwordText.getText().toString();

        //if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        //   emailText.setError("enter a valid email address");
        //    valid = false;
        //} else {
        //    emailText.setError(null);
        //}

        //if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
        //    passwordText.setError("between 4 and 10 alphanumeric characters");
        //    valid = false;
        //} else {
        //    passwordText.setError(null);
        //}

        //return valid;
    }

}
