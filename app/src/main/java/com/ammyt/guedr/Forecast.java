package com.ammyt.guedr;

/**
 * Created by ammyt on 26/10/17.
 */

public class Forecast {
    private float maxTemp;
    private float minTemp;
    private float humidity;
    private String description;
    private int icon;   // Referencia al R.id

    public Forecast(float maxTemp, float minTemp, float humidity, String description, int icon) {
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.humidity = humidity;
        this.description = description;
        this.icon = icon;
    }

    public float getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(float maxTemp) {
        this.maxTemp = maxTemp;
    }

    public float getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(float minTemp) {
        this.minTemp = minTemp;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
