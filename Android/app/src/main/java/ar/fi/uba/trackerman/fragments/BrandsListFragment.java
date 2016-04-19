package ar.fi.uba.trackerman.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ar.fi.uba.trackerman.adapters.BrandsListAdapter;
import ar.fi.uba.trackerman.domains.Brand;
import fi.uba.ar.soldme.R;

/**
 * Created by plucadei on 10/4/16.
 */
public class BrandsListFragment extends Fragment{

    private BrandsListAdapter brandsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View fragmentView= inflater.inflate(R.layout.fragment_brands_list, container, false);
        ListView brandList= (ListView)fragmentView.findViewById(R.id.brands_list_view);

        brandsAdapter = new BrandsListAdapter( getContext(), R.layout.brand_list_item, new ArrayList<Brand>());
        brandList.setAdapter(brandsAdapter);
        brandList.setOnItemClickListener(brandsAdapter);

        ProgressBar bar= new ProgressBar(getContext());
        bar.setIndeterminate(true);
        brandList.setEmptyView(bar);
        brandsAdapter.refresh();
        return fragmentView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.apply_filter:
                Intent returnIntent = new Intent();
                returnIntent.putExtra("EXTRA_BRANDS", getSelectedBrands());
                getActivity().setResult(Activity.RESULT_OK, returnIntent);
                getActivity().finish();
                return true;
        }

        return false;
    }

    private String getSelectedBrands(){
        List<Long> selected= brandsAdapter.getSelected();
        if(selected.isEmpty()){
            return null;
        }
        StringBuilder sb= new StringBuilder();
        Iterator<Long> it= selected.iterator();
        sb.append(it.next());
        while(it.hasNext()){
            sb.append(',').append(it.next());
        }
        return sb.toString();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.menu_brands, menu);
    }
}
