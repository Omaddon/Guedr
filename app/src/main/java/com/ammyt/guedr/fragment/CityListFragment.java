package com.ammyt.guedr.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ammyt.guedr.R;
import com.ammyt.guedr.model.City;

import java.util.LinkedList;


public class CityListFragment extends Fragment {

    private final static String ARG_CITIES = "cities";

    protected LinkedList<City> mCities;
    protected OnCitySelectedListener mOnCitySelectedListener;

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

        // Obtener la lista de ciudades
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

        // Asignamos un listener a la lista para detectar cuando se ha pulsado una celda
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // No deberíamos llamar aquí a la activity. Queremos desacoplar
                // Por ello, lo mejor es avisar a la actividad (por interfaz)
                if (mOnCitySelectedListener != null) {
                    City selectedCity = mCities.get(position);
                    mOnCitySelectedListener.onCitySelected(selectedCity, position);
                }
            }
        });

        return root;
    }

    // Método para avisar al activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() instanceof OnCitySelectedListener) {
            mOnCitySelectedListener = (OnCitySelectedListener) getActivity();
        }
    }

    // Método para avisar al activity
    @Override
    public void onDetach() {
        super.onDetach();

        mOnCitySelectedListener = null;
    }

    // Nos creamos esta custom interface para poder comunicarnos con la actividad desde el fragment
    public interface OnCitySelectedListener {
        void onCitySelected(City city, int position);
    }

}
