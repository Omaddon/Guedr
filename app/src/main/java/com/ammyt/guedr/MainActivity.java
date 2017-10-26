package com.ammyt.guedr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected static String TAG = MainActivity.class.getCanonicalName();

    protected Button changeToStone;
    protected Button changeToDonkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeToStone = findViewById(R.id.change_stone_system);
        changeToStone.setOnClickListener(this);

        changeToDonkey = findViewById(R.id.change_donkey_system);
        changeToDonkey.setOnClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_stone_system:
                Log.v(TAG, "Han llamado a STONE");
            case R.id.change_donkey_system:
                Log.v(TAG, "Han llamado a DONKEY");
            default:
                Log.v(TAG, "Han llamado a ??");
        }
    }
}
