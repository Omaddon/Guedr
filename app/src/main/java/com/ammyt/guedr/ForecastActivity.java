package com.ammyt.guedr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ForecastActivity extends AppCompatActivity {

    // protected static String TAG = ForecastActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        // Creamos el mock model
        Forecast forecast = new Forecast(
                25,
                10,
                35,
                "Soleado con alguna nube",
                R.drawable.sunny);

        setForecast(forecast);
    }

    private void setForecast(Forecast forecast) {
        // Accedemos a las vistas de la interfaz
        ImageView forecastImage = findViewById(R.id.forecast_image);
        TextView maxTemp = findViewById(R.id.max_temp);
        TextView minTemp = findViewById(R.id.min_temp);
        TextView humidity = findViewById(R.id.humidity);
        TextView forecastDescription = findViewById(R.id.forecast_description);

        // Actualizamos la vista con el modelo
        forecastImage.setImageResource(forecast.getIcon());
        maxTemp.setText(getString(R.string.max_temp_format, forecast.getMaxTemp()));
        minTemp.setText(getString(R.string.min_temp_format, forecast.getMinTemp()));
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
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return superReturn;
    }
}