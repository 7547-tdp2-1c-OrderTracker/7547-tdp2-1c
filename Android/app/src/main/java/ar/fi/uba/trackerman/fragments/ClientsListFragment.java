package ar.fi.uba.trackerman.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fi.uba.ar.soldme.R;

/**
 * Created by plucadei on 29/3/16.
 */
public class ClientsListFragment extends Fragment {

    public ClientsListFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_clients_list, container, false);
    }
}
