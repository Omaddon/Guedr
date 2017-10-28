package com.ammyt.guedr.model;

import com.ammyt.guedr.R;

import java.util.LinkedList;

public class Cities {

    private static Cities mInstance;

    private LinkedList<City> cities;

    public static Cities getInstance() {
        if (mInstance == null) {
            mInstance = new Cities();
        }

        return mInstance;
    }

    public Cities() {
        cities = new LinkedList<>();

        cities.add(new City("Madrid"));
        cities.add(new City("Barcelona"));
        cities.add(new City("Quito"));
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
