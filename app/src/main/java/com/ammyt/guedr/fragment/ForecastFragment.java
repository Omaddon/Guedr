package com.ammyt.guedr.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ammyt.guedr.model.City;
import com.ammyt.guedr.model.Forecast;
import com.ammyt.guedr.R;
import com.ammyt.guedr.activity.SettingsActivity;


public class ForecastFragment extends Fragment {

    public static final String PREFERENCE_SHOW_CELSIUS = "showCelsius";

    private static final int REQUEST_UNITS = 1;
    private static final String ARG_CITY = "city";

    protected boolean showCelsius;
    private City mCity;
    private View root;

    public static ForecastFragment newInstance(City city) {
        ForecastFragment fragment = new ForecastFragment();

        Bundle arguments = new Bundle();
        arguments.putSerializable(ARG_CITY, city);

        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        // Recuperamos el modelo que nos pasan como argumento desde CityPagerActivity
        if (getArguments() != null) {
            mCity = (City) getArguments().getSerializable(ARG_CITY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        root = inflater.inflate(R.layout.fragment_forecast, container, false);

        // Recuperamos el valor persistido de showCelsius
        showCelsius = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getBoolean(PREFERENCE_SHOW_CELSIUS, true);

        updateForecast();

        return root;
    }

    private void updateForecast() {
        // Accedemos a las vistas de la interfaz
        TextView cityName = root.findViewById(R.id.city);
        ImageView forecastImage = root.findViewById(R.id.forecast_image);
        TextView maxTempText = root.findViewById(R.id.max_temp);
        TextView minTempText = root.findViewById(R.id.min_temp);
        TextView humidity = root.findViewById(R.id.humidity);
        TextView forecastDescription = root.findViewById(R.id.forecast_description);

        // Accedemos al modelo
        Forecast forecast = mCity.getForecast();

        // Calculamos las temperaturas en función de las unidades
        // Por defecto las pondremos en Celsius
        float maxTemp;
        float minTemp;
        String unitsToShow;

        if (showCelsius) {
            maxTemp = forecast.getMaxTemp(Forecast.CELSIUS);
            minTemp = forecast.getMinTemp(Forecast.CELSIUS);
            unitsToShow = "ºC";
        } else {
            maxTemp = forecast.getMaxTemp(Forecast.FARENHEIT);
            minTemp = forecast.getMinTemp(Forecast.FARENHEIT);
            unitsToShow = "ºF";
        }

        // Actualizamos la vista con el modelo
        cityName.setText(mCity.getName());
        forecastImage.setImageResource(forecast.getIcon());
        maxTempText.setText(getString(R.string.max_temp_format, maxTemp, unitsToShow));
        minTempText.setText(getString(R.string.min_temp_format, minTemp, unitsToShow));
        humidity.setText(getString(R.string.humidity_format, forecast.getHumidity()));
        forecastDescription.setText(forecast.getDescription());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_forecast_settings, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean superReturn = super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.menu_show_settings) {

            // Creamos intent explícito para abrir la pantalla de ajustes
            Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);

            // Pasamos datos (argumentos, nada "grande") a la siguiente actividad
            if (showCelsius) {
                settingsIntent.putExtra(SettingsActivity.EXTRA_UNITS, R.id.celsius_rb);
            } else {
                settingsIntent.putExtra(SettingsActivity.EXTRA_UNITS, R.id.farenheit_rb);
            }

            // Lanzamos la actividad (siempre y cuando no devuelva nada
            //startActivity(settingsIntent);

            // Como nos devuelven datos desde la actividad, debemos usar este otro método
            startActivityForResult(settingsIntent, REQUEST_UNITS);

            return true;
        }

        return superReturn;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_UNITS) {
            // Volvemos de la pantalla SettingsActivity
            if (resultCode == Activity.RESULT_OK) {

                // Para poder volver al estado anterior
                final boolean oldShowCelsius = showCelsius;
                String snackBaRText = null;

                // El usuario ha seleccionado algo y nos han devuelto dichos datos en 'data'
                // Nos pide un valor por defecto, aunque en este punto sabemos que nos han devuelto
                // algo. Usaremos 'farenheit' por defecto
                int optionSelected = data.getIntExtra(SettingsActivity.EXTRA_UNITS, R.id.farenheit_rb);
                if (optionSelected == R.id.farenheit_rb) {
                    // 'Toast' muestra un mensaje temporal en pantalla
                    // Toast.makeText(this, "Se ha seleccionado Farenheit", Toast.LENGTH_SHORT).show();
                    showCelsius = false;
                    snackBaRText =  "Se ha seleccionado Farenheit";

                } else if (optionSelected == R.id.celsius_rb) {
                    // Toast.makeText(this, "Se ha seleccionado Celsius", Toast.LENGTH_SHORT).show();
                    showCelsius = true;
                    snackBaRText =  "Se ha seleccionado Celsius";
                }

                if (getView() != null) {
                    Snackbar.make(getView(), snackBaRText, Snackbar.LENGTH_LONG)
                            .setAction("Deshacer", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Deshacemos los cambios
                                    showCelsius = oldShowCelsius;

                                    PreferenceManager.getDefaultSharedPreferences(getActivity())
                                            .edit()
                                            .putBoolean(PREFERENCE_SHOW_CELSIUS, showCelsius)
                                            .apply();

                                    updateForecast();
                                }
                            })
                            .show();
                }

                // Persistimos las preferencias del usuario respecto a las unidades
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putBoolean(PREFERENCE_SHOW_CELSIUS, showCelsius);
//                editor.apply();
                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .putBoolean(PREFERENCE_SHOW_CELSIUS, showCelsius)
                        .apply();

                updateForecast();

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // No hacemos nada. El usuario ha cancelado
            }
        }
    }
}
