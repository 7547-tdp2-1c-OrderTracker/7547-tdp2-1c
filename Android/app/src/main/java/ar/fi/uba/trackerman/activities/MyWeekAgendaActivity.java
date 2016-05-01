package ar.fi.uba.trackerman.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;

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
        fillRandomSemaphore();

        highlightToday();

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
        String dayOfWeekStr = DateUtils.dayOfWeekToText(day_of_week).toLowerCase();
        identifier = "semaphore_" + color +"_"+ dayOfWeekStr +"_text";
        int resID = getResources().getIdentifier(identifier, "id", getPackageName());

        ((TextView)findViewById(resID)).setText(isContentValid(Integer.toString(ammount)));

    }

    public void fillRandomSemaphore() {
        setSemaphore(1,"red",0);
        setSemaphore(1,"green",32);
        setSemaphore(1,"yellow",18);

        setSemaphore(2,"red",15);
        setSemaphore(2,"green",3);
        setSemaphore(2,"yellow",0);

        setSemaphore(3,"red",115);
        setSemaphore(3,"green",30);
        setSemaphore(3,"yellow",2);

        setSemaphore(4,"red",1);
        setSemaphore(4,"green",2);
        setSemaphore(4,"yellow",1);

        setSemaphore(5,"red",150);
        setSemaphore(5,"green",32);
        setSemaphore(5,"yellow",9);

    }

    public void clearAllSemaphore() {
        setSemaphore(1,"red",0);
        setSemaphore(1,"green",0);
        setSemaphore(1,"yellow",0);

        setSemaphore(2,"red",0);
        setSemaphore(2,"green",0);
        setSemaphore(2,"yellow",0);

        setSemaphore(3,"red",0);
        setSemaphore(3,"green",0);
        setSemaphore(3,"yellow",0);

        setSemaphore(4,"red",0);
        setSemaphore(4,"green",0);
        setSemaphore(4,"yellow",0);

        setSemaphore(5,"red",0);
        setSemaphore(5,"green",0);
        setSemaphore(5,"yellow",0);
    }

    public void highlightToday() {

        int day_of_week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        day_of_week = 4;
        if ((day_of_week >= 1) && (day_of_week <= 5)) {
            String identifier = "";
            String dayOfWeekStr = DateUtils.dayOfWeekToText(day_of_week).toLowerCase();
            identifier = "agenda_week_" + dayOfWeekStr +"_card";
            int resID = getResources().getIdentifier(identifier, "id", getPackageName());
            ((CardView) findViewById(resID)).setCardBackgroundColor(Color.CYAN);
        }

    }

}
