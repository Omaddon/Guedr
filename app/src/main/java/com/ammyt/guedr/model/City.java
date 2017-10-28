package com.ammyt.guedr.model;


import java.io.Serializable;

public class City implements Serializable {

    private String name;
    private Forecast forecast;

    public City(String name, Forecast forecast) {
        this.name = name;
        this.forecast = forecast;
    }

    public City(String name) {
        this(name, null);
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

    // Implementamos este método pues es el que llama el adapter para pintar la celda en la lista
    @Override
    public String toString() {
        return getName();
    }
}
