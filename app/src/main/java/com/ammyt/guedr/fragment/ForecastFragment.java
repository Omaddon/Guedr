package com.ammyt.guedr.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.ammyt.guedr.adapter.ForecastRecyclerViewAdapter;
import com.ammyt.guedr.model.City;
import com.ammyt.guedr.model.Forecast;
import com.ammyt.guedr.R;
import com.ammyt.guedr.activity.SettingsActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;


public class ForecastFragment extends Fragment {

    public static final String PREFERENCE_SHOW_CELSIUS = "showCelsius";

    private static final int REQUEST_UNITS = 1;
    private static final String ARG_CITY = "city";
    private static final int LOADING_VIEW_INDEX = 0;
    private static final int FORECAST_VIEW_INDEX = 1;

    protected boolean showCelsius;
    private City mCity;
    private View root;
    private RecyclerView mList;

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

        // RECYCLER VIEW
        // Accedemos al RecyclerView
        mList = root.findViewById(R.id.forecast_list);

        // Ahora el indicamos cómo debe visualizarse el RecyclerView (su Layout Manager)
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Ahora le indicamos cómo debe animarse el RecyclerView
        mList.setItemAnimator(new DefaultItemAnimator());

        // Por último le asignamos un adapter al RecyclerView
        // Esto lo haremos en updateForecast

        updateForecast();

        return root;
    }

    private void updateForecast() {

        // Accedemos al modelo
        LinkedList<Forecast> forecast = mCity.getForecast();

        // Accedemos al ViewSwitcher
        final ViewSwitcher viewSwitcher = root.findViewById(R.id.view_switcher);
        viewSwitcher.setInAnimation(getActivity(), android.R.anim.fade_in);
        viewSwitcher.setOutAnimation(getActivity(), android.R.anim.fade_out);

        if (forecast == null) {
            AsyncTask<City, Integer, LinkedList<Forecast>> weatherDownloader =
                    new AsyncTask<City, Integer, LinkedList<Forecast>>() {
                // Esto se ejecutará en el hilo principal ANTES de la task
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    // Mostramos el progressBar
                    viewSwitcher.setDisplayedChild(LOADING_VIEW_INDEX);
                }

                @Override
                protected LinkedList<Forecast> doInBackground(City... cities) {
                    // Métodos útiles que no usaremos
                    //publishProgress(50);
                    //isCancelled();
                    return downloadForecast(cities[0]);
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                    super.onProgressUpdate(values);
                }

                @Override
                protected void onCancelled(LinkedList<Forecast> forecast) {
                    super.onCancelled(forecast);

                    viewSwitcher.setDisplayedChild(FORECAST_VIEW_INDEX);
                }

                // Esto se ejecutará en el hilo principal tras terminar la task
                @Override
                protected void onPostExecute(LinkedList<Forecast> forecast) {
                    super.onPostExecute(forecast);

                    if (forecast != null) {
                        // No ha habido errores descargando de la API
                        mCity.setForecast(forecast);
                        updateForecast();

                        // Mostramos el forecast
                        viewSwitcher.setDisplayedChild(FORECAST_VIEW_INDEX);
                    } else {
                        // Ha habido un error
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                        alertDialog.setTitle(R.string.error);
                        alertDialog.setMessage(R.string.couldnt_download);
                        alertDialog.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateForecast();
                            }
                        });
                        alertDialog.show();
                    }
                }
            };

            weatherDownloader.execute(mCity);

            return;
        }

        // Asignamos el adapter al RecyclerView
        ForecastRecyclerViewAdapter adapter = new ForecastRecyclerViewAdapter(forecast, showCelsius);
        mList.setAdapter(adapter);

    }

    // Recordar activar los permisos en el Manifest
    private LinkedList<Forecast> downloadForecast(City city) {
        URL url = null;
        InputStream input = null;

        // Descargamos los datos de la API
        // Podríamos usar algún framework, pero lo haremos a mano
        try {
            url = new URL(String.format(
                    "https://api.openweathermap.org/data/2.5/forecast/daily?q=%s&lang=es&units=metric&appid=4cef94e2559e8f62a5f567ab654b0a70",
                    city.getName()));

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();

            byte data[] = new byte[1024];
            int downloadedBytes;
            input = con.getInputStream();

            StringBuilder sb = new StringBuilder();
            while ((downloadedBytes = input.read(data)) != -1) {
                sb.append(new String(data, 0, downloadedBytes));
            }

            // Analizamos los datos para convertilos de JSON a algo manejable por nuestra app
            JSONObject jsonRoot = new JSONObject(sb.toString());
            JSONArray list = jsonRoot.getJSONArray("list");

            // Nos descargamos TODOS los días de la precidicción
            LinkedList<Forecast> forecasts = new LinkedList<>();

            for (int i = 0; i < list.length(); i++) {
                JSONObject today = list.getJSONObject(i);
                float maxTemp = (float) today.getJSONObject("temp").getDouble("max");
                float minTemp = (float) today.getJSONObject("temp").getDouble("min");
                float humidity = (float) today.getDouble("humidity");
                String description = today.getJSONArray("weather").getJSONObject(0).getString("description");
                String iconString = today.getJSONArray("weather").getJSONObject(0).getString("icon");

                // Convertimos el texto del icono en drawable
                // (le quitamos el último carácter que tan solo indica si es de día o de noche)
                iconString = iconString.substring(0, iconString.length() - 1);
                int iconInt = Integer.parseInt(iconString);
                int iconResource = R.drawable.sunny;

                // Me faltan muchas imágenes, estas son solo unas cuantas de prueba
                switch (iconInt) {
                    case 1:
                        iconResource = R.drawable.sunny; break;
                    case 2:
                        iconResource = R.drawable.semisunny; break;
                    case 3:
                        iconResource = R.drawable.rainning; break;
                    case 4:
                        iconResource = R.drawable.semisunny; break;
                    case 5:
                        iconResource = R.drawable.snowy; break;
                    default:
                        iconResource = R.drawable.storm; break;
                }

                Forecast forecast = new Forecast(maxTemp, minTemp, humidity, description, iconResource);
                forecasts.add(forecast);
            }

            // Un sleep para poder ver nuestra progressBar, porque carga muy rápido y no se ve
            Thread.sleep(1500);
            return forecasts;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
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
