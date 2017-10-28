package com.ammyt.guedr.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ammyt.guedr.R;
import com.ammyt.guedr.fragment.CityPagerFragment;

public class CityPagerActivity extends AppCompatActivity {

    public static final String EXTRA_CITY_INDEX = "extra_city_index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_pager);

        // Vamos a decirle a la actividad que use nuestra toolbar personalizada
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        // Recibimos el índice de la ciudad que queremos mostrar
        int cityIndex = getIntent().getIntExtra(EXTRA_CITY_INDEX, 0);

        // Añadimos el CityPagerFragment a la jerarquía
        FragmentManager fm = getFragmentManager();

        if (fm.findFragmentById(R.id.view_pager_fragment) == null) {
            CityPagerFragment fragment = CityPagerFragment.newInstance(cityIndex);

            fm.beginTransaction()
                    .add(R.id.view_pager_fragment, fragment)
                    .commit();
        }
    }
}


