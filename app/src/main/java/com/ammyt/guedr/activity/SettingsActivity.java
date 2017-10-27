package com.ammyt.guedr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;

import com.ammyt.guedr.R;


public class SettingsActivity extends AppCompatActivity {

    // Key para el diccionario de extras (parámetros del intent)
    public static final String EXTRA_UNITS = "units";
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        findViewById(R.id.accept_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptSettings();
            }
        });

        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelSettings();
            }
        });

        radioGroup = findViewById(R.id.units_rg);

        // Seleccionamos las unidades que me hayan pasado (por defecto 'farenheit')
        int radioSelected = getIntent().getIntExtra(SettingsActivity.EXTRA_UNITS, R.id.farenheit_rb);
        radioGroup.check(radioSelected);
    }

    private void cancelSettings() {
        // RESULT_CANCELED es un id por defecto de Android
        setResult(RESULT_CANCELED);

        // Ahora debemos indicar que esta actividad ha terminado y que vuelva a la anterior
        finish();
    }

    private void acceptSettings() {
        // Creamos intent con los datos de salida
        Intent returnIntent = new Intent();
        returnIntent.putExtra(SettingsActivity.EXTRA_UNITS, radioGroup.getCheckedRadioButtonId());

        // Indicamos OK como resultado y devolvemos los datos dentro del intent recien creado
        setResult(RESULT_OK, returnIntent);

        finish();
    }
}
