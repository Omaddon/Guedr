package com.ammyt.guedr.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.ammyt.guedr.R;
import com.ammyt.guedr.fragment.CityListFragment;
import com.ammyt.guedr.fragment.CityPagerFragment;
import com.ammyt.guedr.model.Cities;
import com.ammyt.guedr.model.City;


public class ForecastActivity extends AppCompatActivity implements CityListFragment.OnCitySelectedListener {
    // protected static String TAG = ForecastActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -------------------- METRICS ------------------------------------
        // TRUCO: para saber los detalles del dispositivo real que está ejecutando la app
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        int dpWidth = (int) (width / metrics.density);
        int dpHeight = (int) (height / metrics.density);
        String model = Build.MODEL;
        int dpi = metrics.densityDpi;
        // -----------------------------------------------------------------

        setContentView(R.layout.activity_forecast);

        // Forma más habitual de crear un fragment. En lugar de hacerlo en el layout con la etiqueta
        // Cargamos a mano el fragment
        FragmentManager fm = getFragmentManager();

        // Averiguamos qué interfaz hemos cargado (en función de tamaño y orientación de pantalla)
        if (findViewById(R.id.city_list_fragment) != null) {
            // Interfaz con hueco para CityListFragment
            // Comprobamos que no tenemos añadido ya el fragment a la jerarquía
            if (fm.findFragmentById(R.id.city_list_fragment) == null) {
                // No está añadido. Lo añadimos con una transacción de fragments
                // Creamos la instancia del nuevo fragment
                Cities cities = Cities.getInstance();
                CityListFragment fragment = CityListFragment.newInstance(cities.getCities());

                // Iniciamos la transacción, la cual dependerá del tamaño y orientación de pantalla
                fm.beginTransaction()
                        .add(R.id.city_list_fragment, fragment)
                        .commit();
            }
        }

        if (findViewById(R.id.view_pager_fragment) != null) {
            // Interfaz con hueco para CityPagerFragment
            // Comprobamos que no tenemos ya añadido el fragment
            if (fm.findFragmentById(R.id.view_pager_fragment) == null) {
                fm.beginTransaction()
                        .add(R.id.view_pager_fragment, CityPagerFragment.newInstance(0))
                        .commit();
            }
        }
    }

    @Override
    public void onCitySelected(City city, int position) {
        // Debemos saber qué fragmens tenemos cargados en la interfaz
        FragmentManager fm = getFragmentManager();
        CityPagerFragment cityPagerFragment = (CityPagerFragment) fm.findFragmentById(R.id.view_pager_fragment);
        if (cityPagerFragment != null) {
            // Tenemos un pager, por lo que le indicamos que debe moverse a dicha city
            cityPagerFragment.moveToCity(position);

        } else {
            // No tenemos pager, por lo que simplemente lanzamos la nueva actividad
            Intent intent = new Intent(this, CityPagerActivity.class);
            intent.putExtra(CityPagerActivity.EXTRA_CITY_INDEX, position);
            startActivity(intent);
        }

    }
}