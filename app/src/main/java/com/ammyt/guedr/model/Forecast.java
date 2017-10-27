package com.ammyt.guedr.model;


import java.io.Serializable;

public class Forecast implements Serializable {

    public static final int CELSIUS = 0;
    public static final int FARENHEIT = 1;

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

    protected float toFarenheit(float celsius) {
        return (celsius * 1.8f) + 32;
    }

    public float getMaxTemp(int units) {
        if (units == CELSIUS) {
            return maxTemp;
        } else {
            return toFarenheit(maxTemp);
        }
    }

    public void setMaxTemp(float maxTemp) {
        this.maxTemp = maxTemp;
    }

    public float getMinTemp(int units) {
        if (units == CELSIUS) {
            return minTemp;
        } else {
            return toFarenheit(minTemp);
        }
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
