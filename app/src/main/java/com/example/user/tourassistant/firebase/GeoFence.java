package com.example.user.tourassistant.firebase;

/**
 * Created by user on 23/10/2017.
 */

public class GeoFence {
    private String placeName;
    private double latitude;
    private double longitude;
    private int radius;

    public GeoFence(String placeName, double latitude, double longitude, int radius) {
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }


    public String getPlaceName() {
        return placeName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getRadius() {
        return radius;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public GeoFence() {
    }
}
