package ar.fi.uba.trackerman.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import ar.fi.uba.trackerman.adapters.BrandsListAdapter;
import ar.fi.uba.trackerman.domains.Brand;
import fi.uba.ar.soldme.R;

/**
 * Created by plucadei on 10/4/16.
 */
public class BrandsListFragment extends Fragment implements AdapterView.OnItemClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView= inflater.inflate(R.layout.fragment_brands_list, container, false);
        ListView brandList= (ListView)fragmentView.findViewById(R.id.brands_list_view);

        BrandsListAdapter brandsAdapter = new BrandsListAdapter( getContext(), R.layout.brand_list_item, new ArrayList<Brand>());
        brandList.setAdapter(brandsAdapter);
        brandList.setOnItemClickListener(this);

        ProgressBar bar= new ProgressBar(getContext());
        bar.setIndeterminate(true);
        brandList.setEmptyView(bar);
        brandsAdapter.refresh();
        return fragmentView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
