package com.example.user.tourassistant.google_place;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by navneet on 17/7/16.
 */
public interface RetrofitMaps {

    /*
     * Retrofit get annotation with our URL
     * And our method that will return us details of student.
     *  https://maps.googleapis.com/maps/
     *  api/place/nearbysearch/json?
     *  location=-33.8670522,151.1957362
     *  &&radius=100
     *  &sensor=true
     *  &types=food
     *  &key=YOUR_API_KEY
     */
    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyDNeai9oJaTgOWuvohReDXGkSkDvKcBWsc")
    Call<Example> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

}
