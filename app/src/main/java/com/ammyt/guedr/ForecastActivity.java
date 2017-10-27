package com.ammyt.guedr;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ForecastActivity extends AppCompatActivity {

    // protected static String TAG = ForecastActivity.class.getCanonicalName();
    public static final String PREFERENCE_SHOW_CELSIUS = "showCelsius";
    private static final int REQUEST_UNITS = 1;

    protected boolean showCelsius;
    private Forecast forecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        // Recuperamos el valor persistido de showCelsius
        showCelsius = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(PREFERENCE_SHOW_CELSIUS, true);

        // Creamos el mock model
        forecast = new Forecast(
                25,
                10,
                35,
                "Soleado con alguna nube",
                R.drawable.sunny);

        updateForecast();
    }

    private void updateForecast() {
        // Accedemos a las vistas de la interfaz
        ImageView forecastImage = findViewById(R.id.forecast_image);
        TextView maxTempText = findViewById(R.id.max_temp);
        TextView minTempText = findViewById(R.id.min_temp);
        TextView humidity = findViewById(R.id.humidity);
        TextView forecastDescription = findViewById(R.id.forecast_description);

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
        forecastImage.setImageResource(forecast.getIcon());
        maxTempText.setText(getString(R.string.max_temp_format, maxTemp, unitsToShow));
        minTempText.setText(getString(R.string.min_temp_format, minTemp, unitsToShow));
        humidity.setText(getString(R.string.humidity_format, forecast.getHumidity()));
        forecastDescription.setText(forecast.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_forecast_settings, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean superReturn = super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.menu_show_settings) {

            // Creamos intent explícito para abrir la pantalla de ajustes
            Intent settingsIntent = new Intent(this, SettingsActivity.class);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_UNITS) {
            // Volvemos de la pantalla SettingsActivity
            if (resultCode == RESULT_OK) {

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

                Snackbar.make(findViewById(android.R.id.content), snackBaRText, Snackbar.LENGTH_LONG)
                        .setAction("Deshacer", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Deshacemos los cambios
                                showCelsius = oldShowCelsius;

                                PreferenceManager.getDefaultSharedPreferences(ForecastActivity.this)
                                        .edit()
                                        .putBoolean(PREFERENCE_SHOW_CELSIUS, showCelsius)
                                        .apply();

                                updateForecast();
                            }
                        })
                        .show();

                // Persistimos las preferencias del usuario respecto a las unidades
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putBoolean(PREFERENCE_SHOW_CELSIUS, showCelsius);
//                editor.apply();
                PreferenceManager.getDefaultSharedPreferences(this)
                        .edit()
                        .putBoolean(PREFERENCE_SHOW_CELSIUS, showCelsius)
                        .apply();

                updateForecast();

            } else if (resultCode == RESULT_CANCELED) {
                // No hacemos nada. El usuario ha cancelado
            }
        }
    }

}