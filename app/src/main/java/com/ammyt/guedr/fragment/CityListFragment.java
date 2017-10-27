package com.ammyt.guedr.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ammyt.guedr.R;
import com.ammyt.guedr.model.City;

import java.util.LinkedList;


public class CityListFragment extends Fragment {

    private final static String ARG_CITIES = "cities";

    protected LinkedList<City> mCities;

    public static CityListFragment newInstance(LinkedList<City> cities) {
        CityListFragment fragment = new CityListFragment();

        Bundle arguments = new Bundle();
        arguments.putSerializable(ARG_CITIES, cities);

        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCities = (LinkedList<City>) getArguments().getSerializable(ARG_CITIES);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_city_list, container, false);

        // Accedemos a la vista
        ListView list = root.findViewById(R.id.city_list);

        // Creamos el adapter con la lista de ciudades
        ArrayAdapter<City> adapter = new ArrayAdapter<City>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                mCities);

        // Le pasamos el adapter al ListVIew para que rellene la vista
        list.setAdapter(adapter);

        return root;
    }

}
