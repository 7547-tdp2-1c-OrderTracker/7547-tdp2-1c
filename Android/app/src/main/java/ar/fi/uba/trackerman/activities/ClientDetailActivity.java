package ar.fi.uba.trackerman.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import ar.fi.uba.trackerman.tasks.GetClientTask;

import ar.fi.uba.trackerman.tasks.GetClientListTask;
import fi.uba.ar.soldme.R;

public class ClientDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_detail);
        Intent intent= getIntent();
        long clientId= intent.getLongExtra(Intent.EXTRA_UID,0);

        Fragment fragment= getSupportFragmentManager().findFragmentById(R.id.fragment_client_detail);
        new GetClientTask(fragment).execute(Long.toString(clientId));
    }

}
