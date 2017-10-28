package com.ammyt.guedr.model;


import com.ammyt.guedr.R;

import java.util.LinkedList;
import java.util.List;

public class Cities {

    private LinkedList<City> cities;

    public Cities() {
        cities = new LinkedList<>();

        cities.add(new City("Madrid", new Forecast(25, 10, 35, "Soleado con alguna nube", R.drawable.semisunny)));
        cities.add(new City("Barcelona", new Forecast(38, 22, 48, "Soleado.", R.drawable.sunny)));
        cities.add(new City("Quito", new Forecast(30, 28, 17, "Nuboso", R.drawable.storm)));
    }

    public City getCity(int index) {
        return cities.get(index);
    }

    public LinkedList<City> getCities() {
        return cities;
    }

    public int getCount() {
        return cities.size();
    }
}
