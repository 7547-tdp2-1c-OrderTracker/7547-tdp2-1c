package ar.fi.uba.trackerman.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import ar.fi.uba.trackerman.domains.ScheduleWeekView;
import ar.fi.uba.trackerman.domains.Semaphore;
import ar.fi.uba.trackerman.server.RestClient;
import ar.fi.uba.trackerman.tasks.schedule.GetScheduleWeekTask;
import ar.fi.uba.trackerman.utils.AppSettings;
import ar.fi.uba.trackerman.utils.DateUtils;
import ar.fi.uba.trackerman.utils.DayOfWeek;
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
        highlightToday();

        if (RestClient.isOnline(this)) new GetScheduleWeekTask(this).execute(DateUtils.formatShortDate(Calendar.getInstance().getTime()), String.valueOf(AppSettings.getSellerId()));

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

    public void setSemaphore(int dayOfWeek,String color, int ammount) {
        // identifier sample: semaphore_green_monday_text
        String identifier = "";

        identifier = "semaphore_" + color +"_"+ DayOfWeek.byReference(dayOfWeek).toEng().toLowerCase() +"_text";
        int resID = getResources().getIdentifier(identifier, "id", getPackageName());
        ((TextView)findViewById(resID)).setText(isContentValid(String.valueOf(ammount)));
    }

    public void setSemaphoreFR(String color, int ammount) {
        // identifier sample: semaphore_green_monday_text
        String identifier = "";

        identifier = "semaphore_" + color +"_outofroute_text";
        int resID = getResources().getIdentifier(identifier, "id", getPackageName());
        ((TextView)findViewById(resID)).setText(isContentValid(String.valueOf(ammount)));
    }

    public void clearAllSemaphore() {
        for(int i = 0; i<5; i++) {
            setSemaphore(i,"red",0);
            setSemaphore(i,"green",0);
            setSemaphore(i,"yellow",0);
        }
        setSemaphoreFR("red",0);
        setSemaphoreFR("green",0);
        setSemaphoreFR("yellow",0);
    }

    public void highlightToday() {
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        //dia 1 para el Calendar.DAY_OF_WEEK es domingo, por eso el -1
        if (dayOfWeek == 1) dayOfWeek = 6; //nuestro domingo
        else dayOfWeek -= 2; //de lunes a sabado
        if (DayOfWeek.isWorkingDay(dayOfWeek)) {
            String identifier = "";
            identifier = "agenda_week_" + DayOfWeek.byReference(dayOfWeek).toEng().toLowerCase() +"_card";
            int resID = getResources().getIdentifier(identifier, "id", getPackageName());
            ((CardView) findViewById(resID)).setCardBackgroundColor(Color.CYAN);
        }
    }

    public void fillWeek(ScheduleWeekView week) {

        int totalRed = 0;
        int totalYellow = 0;
        int totalGreen = 0;

        for(Semaphore semaphore : week.getSemaphores()) {
            if(!DayOfWeek.isWorkingDay(semaphore.getDayOfWeek())) break;

            int redDay = semaphore.getRed();
            int yellowDay = semaphore.getYellow();
            int greenDay = semaphore.getGreen();
            totalRed += redDay;
            totalYellow += yellowDay;
            totalGreen += greenDay;
            setSemaphore(semaphore.getDayOfWeek(),"red",redDay);
            setSemaphore(semaphore.getDayOfWeek(),"yellow",yellowDay);
            setSemaphore(semaphore.getDayOfWeek(),"green",greenDay);
        }

        setSemaphoreFR("red",totalRed);
        setSemaphoreFR("yellow",totalYellow);
        setSemaphoreFR("green",totalGreen);

    }

    public void openMyClientsActivity(View view) {
        Intent intent = new Intent(this, MyClientsActivity.class);
        startActivity(intent);
    }

    public void clickCardFR(View v) {
        openMyClientsActivity(v);
    }

}
