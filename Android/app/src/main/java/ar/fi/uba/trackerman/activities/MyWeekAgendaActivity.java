package ar.fi.uba.trackerman.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import ar.fi.uba.trackerman.utils.DateUtils;
import fi.uba.ar.soldme.R;

import static ar.fi.uba.trackerman.utils.FieldValidator.isContentValid;

public class MyWeekAgendaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_week_agenda);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        clearAllSemaphore();

        setSemaphore(1,"red",0);
        setSemaphore(1,"green",32);
        setSemaphore(1,"yellow",18);

        setSemaphore(2,"red",15);
        setSemaphore(2,"green",3);
        setSemaphore(2,"yellow",0);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setSemaphore(int day_of_week,String color, int ammount) {

        // identifier sample: semaphore_green_monday_text
        String identifier = "";
        String datOfWeekStr = DateUtils.dayOfWeekToText(day_of_week).toLowerCase();
        identifier = "semaphore_" + color +"_"+ datOfWeekStr +"_text";
        int resID = getResources().getIdentifier(identifier, "id", getPackageName());

        ((TextView)findViewById(resID)).setText(isContentValid(Integer.toString(ammount)));

    }

    public void clearAllSemaphore() {
        setSemaphore(1,"red",0);
        setSemaphore(1,"green",0);
        setSemaphore(1,"yellow",0);
        setSemaphore(2,"red",0);
        setSemaphore(2,"green",0);
        setSemaphore(2,"yellow",0);
    }

}
