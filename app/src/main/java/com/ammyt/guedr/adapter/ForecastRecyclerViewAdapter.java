package com.ammyt.guedr.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ammyt.guedr.R;
import com.ammyt.guedr.model.Forecast;

import java.util.LinkedList;


public class ForecastRecyclerViewAdapter extends RecyclerView.Adapter<ForecastRecyclerViewAdapter.ForecastViewHolder> {

    private LinkedList<Forecast> mForecast;
    private boolean mShowCelsius;
    private View.OnClickListener mOnClickListener;

    public ForecastRecyclerViewAdapter(LinkedList<Forecast> forecast, boolean showCelsius) {
        super();
        mForecast = forecast;
        mShowCelsius = showCelsius;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_forecast, parent,false);

        view.setOnClickListener(mOnClickListener);

        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        holder.bindForecast(mForecast.get(position), mShowCelsius);
    }

    @Override
    public int getItemCount() {
        return mForecast.size();
    }

    //
    // Este es quien actualiza la celda
    //
    protected class ForecastViewHolder extends RecyclerView.ViewHolder {

        private View mRoot;
        private final ImageView mForecastImage;
        private final TextView mMaxTempText;
        private final TextView mMinTempText;
        private final TextView mHumidity;
        private final TextView mForecastDescription;

        public ForecastViewHolder(View itemView) {
            super(itemView);

            mRoot = itemView;

            // Accedemos a las vistas de la interfaz
            mForecastImage = itemView.findViewById(R.id.forecast_image);
            mMaxTempText = itemView.findViewById(R.id.max_temp);
            mMinTempText = itemView.findViewById(R.id.min_temp);
            mHumidity = itemView.findViewById(R.id.humidity);
            mForecastDescription = itemView.findViewById(R.id.forecast_description);
        }

        // Llamarán a este método cuando haya datos para rellenar la celda
        public void bindForecast(Forecast forecast, boolean showCelsius) {
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

            // Accedemos al contexto
            Context context = mRoot.getContext();

            // Actualizamos la vista con el modelo
            mForecastImage.setImageResource(forecast.getIcon());
            mMaxTempText.setText(context.getString(R.string.max_temp_format, maxTemp, unitsToShow));
            mMinTempText.setText(context.getString(R.string.min_temp_format, minTemp, unitsToShow));
            mHumidity.setText(context.getString(R.string.humidity_format, forecast.getHumidity()));
            mForecastDescription.setText(forecast.getDescription());
        }
    }
}
