package model;

import org.osmdroid.util.GeoPoint;

public class MyLocation {
    private GeoPoint location;
    private String name;

    public MyLocation(GeoPoint location, String name) {
        this.location = location;
        this.name = name;
    }

    public GeoPoint getGeoPoint() {
        return location;
    }

    public String getName() {
        return name;
    }
}