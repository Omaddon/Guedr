package com.ammyt.guedr.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import com.ammyt.guedr.R;
import com.ammyt.guedr.fragment.ForecastFragment;
import com.ammyt.guedr.model.Cities;
import com.ammyt.guedr.model.City;

public class CityPagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_pager);

        // Vamos a decirle a la actividad que use nuestra toolbar personalizada
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        ViewPager pager = findViewById(R.id.view_pager);
        Cities cities = new Cities();
        CityPagerAdapter adapter = new CityPagerAdapter(getFragmentManager(), cities);

        // Le damos al viewPager su adaptar para que muestre tantos fragments como diga el modelo
        pager.setAdapter(adapter);
    }

}

// Librería v13 para los FragmentAdapter
class CityPagerAdapter extends FragmentPagerAdapter {

    private Cities mCities;

    public CityPagerAdapter(FragmentManager fm, Cities cities) {
        super(fm);
        mCities = cities;
    }

    // Cada pantalla debe mostrar una City con su Forecast
    @Override
    public Fragment getItem(int position) {
        ForecastFragment fragment = ForecastFragment.newInstance(mCities.getCity(position));

        return fragment;
    }

    @Override
    public int getCount() {
        return mCities.getCount();
    }

    // Solo lo usamos si activamos el PagerTitleStrip o el PagerTabStrip en el content_city_pager.xml
    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);

        City city = mCities.getCity(position);
        String cityName = city.getName();

        return cityName;
    }
}
