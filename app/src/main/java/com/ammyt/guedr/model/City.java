package com.ammyt.guedr.model;


import java.io.Serializable;
import java.util.LinkedList;

public class City implements Serializable {

    private String name;
    private LinkedList<Forecast> forecast;

    public City(String name, LinkedList<Forecast> forecast) {
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

    public LinkedList<Forecast> getForecast() {
        return forecast;
    }

    public void setForecast(LinkedList<Forecast> forecast) {
        this.forecast = forecast;
    }

    // Implementamos este método pues es el que llama el adapter para pintar la celda en la lista
    @Override
    public String toString() {
        return getName();
    }
}
