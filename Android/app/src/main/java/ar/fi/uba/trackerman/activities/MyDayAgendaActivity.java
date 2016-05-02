package ar.fi.uba.trackerman.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.Calendar;
import java.util.concurrent.RecursiveTask;

import ar.fi.uba.trackerman.fragments.DailyRouteFragment;
import ar.fi.uba.trackerman.utils.DayOfWeek;
import fi.uba.ar.soldme.R;

public class MyDayAgendaActivity extends AppCompatActivity {

    private DailyViewPagerAdapter pagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_day_agenda);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pagerAdapter = new DailyViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setCurrentItem(getCurrentDay());
        mViewPager.setAdapter(pagerAdapter);
    }

    private int getCurrentDay(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK)-2;
        if (day<0){
            day=0;
        }
        return day;
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

    public static class DailyViewPagerAdapter extends FragmentPagerAdapter {
        public DailyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            DailyRouteFragment fragment = new DailyRouteFragment();
            Bundle args = new Bundle();
            args.putString(DailyRouteFragment.DAY_ARG, DayOfWeek.byReference(i).toEsp());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Section " + (position + 1);
        }
    }

}
