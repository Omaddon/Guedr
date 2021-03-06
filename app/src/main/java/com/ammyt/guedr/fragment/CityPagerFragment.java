package com.ammyt.guedr.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ammyt.guedr.R;
import com.ammyt.guedr.model.Cities;
import com.ammyt.guedr.model.City;


public class CityPagerFragment extends Fragment {

    private static final String ARG_INITIAL_CITY_INDEX = "initialCityIndex";

    private ViewPager mPager;
    private Cities mCities;
    private int mInitialCityIndex = 0;

    private View root;

    public static CityPagerFragment newInstance(int cityIndex) {
        CityPagerFragment fragment = new CityPagerFragment();

        Bundle arguments = new Bundle();
        arguments.putInt(ARG_INITIAL_CITY_INDEX, cityIndex);

        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       setHasOptionsMenu(true);

       if (getArguments() != null) {
           mInitialCityIndex = getArguments().getInt(ARG_INITIAL_CITY_INDEX, 0);
       }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        root = inflater.inflate(R.layout.fragment_city_list, container, false);

        mPager = root.findViewById(R.id.view_pager);
        mCities = Cities.getInstance();
        CityPagerAdapter adapter = new CityPagerAdapter(getFragmentManager(), mCities);

        // Le damos al viewPager su adaptar para que muestre tantos fragments como diga el modelo
        mPager.setAdapter(adapter);

        // Vamos a captar cuándo cambia de página el ViewPager
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateCityInfo(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Actualizamos el nombre de la toolbar en el inicio. Le pasamos la pos 0, pues es la primera
        // vez que se le llama y no habrá actualizado aún y nos encontraremos en la primera city.
        moveToCity(mInitialCityIndex);
        updateCityInfo(mInitialCityIndex);

        return root;
    }

    public void moveToCity(int cityIndex) {
        mPager.setCurrentItem(cityIndex);
    }

    private void updateCityInfo(int position) {
        String cityName = mCities.getCity(position).getName();

        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
            ActionBar toolbar = parentActivity.getSupportActionBar();
            if (toolbar != null) {
                toolbar.setTitle(cityName);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_pager, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean superReturn =  super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.previous) {
            // Movemos el pager hacia atrás
            moveToCity(mPager.getCurrentItem() - 1);

            return true;
        } else if (item.getItemId() == R.id.next) {
            // Movemos el pager hacia adelante
            moveToCity(mPager.getCurrentItem() + 1);

            return true;
        }

        return superReturn;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem menuPrev = menu.findItem(R.id.previous);
        MenuItem menuNext = menu.findItem(R.id.next);

        menuPrev.setEnabled(mPager.getCurrentItem() > 0);
        menuNext.setEnabled(mPager.getCurrentItem() < mCities.getCount() - 1);
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

