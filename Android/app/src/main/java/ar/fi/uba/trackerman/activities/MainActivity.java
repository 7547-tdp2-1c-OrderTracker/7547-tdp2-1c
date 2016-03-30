package ar.fi.uba.trackerman.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import fi.uba.ar.soldme.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void openMyClientsActivity(View view) {
        Log.i(getClass().getName(), "openMyClientsActivity");
        Intent intent = new Intent(this, MyClientsActivity.class);
        startActivity(intent);
    }

    public void openProductsActivity(View view) {
        Log.i(getClass().getName(), "openProductsActivity");
        Snackbar.make(view, "Not Yet Implemented!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
