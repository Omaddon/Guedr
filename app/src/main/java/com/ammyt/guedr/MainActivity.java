package com.ammyt.guedr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    protected static String TAG = MainActivity.class.getCanonicalName();

    protected Button changeToStone;
    protected Button changeToDonkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView offlineImage = findViewById(R.id.offline_weather_image);

        changeToStone = findViewById(R.id.change_stone_system);
        changeToStone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "PIEDRA");
                offlineImage.setImageResource(R.drawable.offline_weather);
            }
        });

        changeToDonkey = findViewById(R.id.change_donkey_system);
        changeToDonkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "BURRO");
                offlineImage.setImageResource(R.drawable.tardis);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}