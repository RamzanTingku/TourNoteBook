package com.example.user.tourassistant.weather;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by user on 23/9/2017.
 */

public interface ApiTool {

    String BASE_URL = "http://api.apixu.com/v1/";
    @GET("forecast.json?key=28094519d2bf40e3b81161108172309&days=7")
    Call<Example> getAWeather(@Query("q") List<Double> loc);

}
