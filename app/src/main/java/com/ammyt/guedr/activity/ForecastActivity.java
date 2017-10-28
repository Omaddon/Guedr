package com.ammyt.guedr.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ammyt.guedr.R;
import com.ammyt.guedr.fragment.CityListFragment;
import com.ammyt.guedr.model.Cities;
import com.ammyt.guedr.model.City;


public class ForecastActivity extends AppCompatActivity implements CityListFragment.OnCitySelectedListener {
    // protected static String TAG = ForecastActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        // Forma más habitual de crear un fragment. En lugar de hacerlo en el layout con la etiqueta
        // Cargamos a mano el fragment
        FragmentManager fm = getFragmentManager();

        // Comprobamos que no tenemos añadido ya el fragment a la jerarquía
        if (fm.findFragmentById(R.id.city_list_fragment) == null) {
            // No está añadido. Lo añadimos con una transacción de fragments
            // Creamos la instancia del nuevo fragment
            CityListFragment fragment = CityListFragment.newInstance(new Cities().getCities());

            // Iniciamos la transacción
            fm.beginTransaction()
                    .add(R.id.city_list_fragment, fragment)
                    .commit();
        }
    }

    @Override
    public void onCitySelected(City city, int position) {
        Intent intent = new Intent(this, CityPagerActivity.class);
        intent.putExtra(CityPagerActivity.EXTRA_CITY_INDEX, position);
        startActivity(intent);
    }
}