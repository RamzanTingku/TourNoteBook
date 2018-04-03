package com.example.user.tourassistant;

/**
 * Created by Ramzan Ullah on 10/24/2017.
 */

public class TopPlace {

    private String placeName;
    private String placeImg;

    public TopPlace(String placeName, String placeImg) {
        this.placeName = placeName;
        this.placeImg = placeImg;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceImg() {
        return placeImg;
    }
}
