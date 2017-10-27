package com.ammyt.guedr.model;


import java.io.Serializable;

public class City implements Serializable {

    private String name;
    private Forecast forecast;

    public City(String name, Forecast forecast) {
        this.name = name;
        this.forecast = forecast;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }
}
