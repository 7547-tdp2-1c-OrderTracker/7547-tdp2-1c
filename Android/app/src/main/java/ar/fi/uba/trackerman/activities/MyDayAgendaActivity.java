package ar.fi.uba.trackerman.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.concurrent.RecursiveTask;

import ar.fi.uba.trackerman.fragments.DailyRouteFragment;
import ar.fi.uba.trackerman.utils.DateUtils;
import ar.fi.uba.trackerman.utils.DayOfWeek;
import ar.fi.uba.trackerman.utils.MyPreferences;
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

        pagerAdapter = new DailyViewPagerAdapter(getSupportFragmentManager(), this.getApplicationContext(), getCurrentDay());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        //mViewPager.setCurrentItem(getCurrentDay());
        mViewPager.setAdapter(pagerAdapter);

        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(getCurrentDay());
            }
        });
    }

    private int getCurrentDay(){
        MyPreferences pref = new MyPreferences(this.getApplicationContext());
        String currentDate = pref.get(getString(R.string.shared_pref_current_schedule_date), "");
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.parseShortDate(currentDate));
        Integer position = cal.get(Calendar.DAY_OF_WEEK)-2;
        return position;
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
        private final Context context;
        private final int startPosition;
        public DailyViewPagerAdapter(FragmentManager fm, Context context, int startPosition) {
            super(fm);
            this.context = context;
            this.startPosition = startPosition;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public Fragment getItem(int i) {
            DailyRouteFragment fragment = new DailyRouteFragment();

            Bundle args = new Bundle();
            args.putInt(DailyRouteFragment.ITEM_POSITION, i);
            args.putInt(DailyRouteFragment.DIFF, i-startPosition);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

}
