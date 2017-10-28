package com.ammyt.guedr.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.ammyt.guedr.R;
import com.ammyt.guedr.model.Forecast;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_FORECAST = "EXTRA_FORECAST";
    public static final String EXTRA_SHOWCELSIUS = "EXTRA_SHOWCELSIUS";

    private ImageView mForecastImage;
    private TextView mMaxTempText;
    private TextView mMinTempText;
    private TextView mHumidity;
    private TextView mForecastDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Accedemos a los datos de entrada de la actividad anterior
        Forecast forecast = (Forecast) getIntent().getSerializableExtra(EXTRA_FORECAST);
        boolean showCelsius = getIntent().getBooleanExtra(EXTRA_SHOWCELSIUS, true);

        // Accedemos a las vistas de la interfaz
        mForecastImage = findViewById(R.id.forecast_image);
        mMaxTempText = findViewById(R.id.max_temp);
        mMinTempText = findViewById(R.id.min_temp);
        mHumidity = findViewById(R.id.humidity);
        mForecastDescription = findViewById(R.id.forecast_description);

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
        mForecastImage.setImageResource(forecast.getIcon());
        mMaxTempText.setText(getString(R.string.max_temp_format, maxTemp, unitsToShow));
        mMinTempText.setText(getString(R.string.min_temp_format, minTemp, unitsToShow));
        mHumidity.setText(getString(R.string.humidity_format, forecast.getHumidity()));
        mForecastDescription.setText(forecast.getDescription());
    }
}
