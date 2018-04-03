package com.example.user.tourassistant.page_fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.tourassistant.R;
import com.example.user.tourassistant.activities.Log;
import com.example.user.tourassistant.weather.ApiTool;
import com.example.user.tourassistant.weather.Example;
import com.example.user.tourassistant.weather.HourUpdate.HourAdapter;
import com.example.user.tourassistant.weather.HourUpdate.HourUpdate;
import com.example.user.tourassistant.weather.WeekUpdate.WeeklyAdapter;
import com.example.user.tourassistant.weather.WeekUpdate.WeeklyUpdate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    String[] hour = new String[25];
    String[] hourTempC = new String[25];
    String[] hourTempF = new String[25];
    String[] hourCondIcon = new String[25];



    String[] date = new String[7];
    String[] dayMaxTempC = new String[7];
    String[] dayMaxTempF = new String[7];
    String[] dayMinTempC = new String[7];
    String[] dayMinTempF = new String[7];
    String[] dayCondIcon = new String[7];
    Date[] mDate = new Date[7];


    private RecyclerView hourRecyclerview;
    private ArrayList<HourUpdate>hourUpdates;
    private HourAdapter hourAdapter;

    private RecyclerView weekRecyclerview;
    private ArrayList<WeeklyUpdate>weeklyUpdates;
    private WeeklyAdapter weeklyAdapter;
    private SharedPreferences sharedPref;

    double latitude,longitude;
    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Weather forcast");

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String placeName=sharedPref.getString("placeName","Dhaka");
        latitude=sharedPref.getFloat("latitude",23);
        longitude=sharedPref.getFloat("longitude",90);




        showWeather(latitude,longitude);
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }


    public void showWeather(double lat,double lon){


        List<Double> loc=new ArrayList<>();
        loc.add(lat);
        loc.add(lon);
        String url = "http://api.apixu.com/v1/";


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiTool service = retrofit.create(ApiTool.class);

        Call<Example> call = service.getAWeather(loc);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {


                String TempUnit = "C";
                String crntTempC = response.body().getCurrent().getTempC().toString();
                String crntTempF = response.body().getCurrent().getTempF().toString();
                String crntmaxTempC = response.body().getForecast().getForecastday().get(0).getDay().getMaxtempC().toString();
                String crntmaxTempF = response.body().getForecast().getForecastday().get(0).getDay().getMaxtempF().toString();
                String crntmiTempC = response.body().getForecast().getForecastday().get(0).getDay().getMintempC().toString();
                String crntminTempF = response.body().getForecast().getForecastday().get(0).getDay().getMintempF().toString();
                String humidity = response.body().getCurrent().getHumidity().toString();
                String pressureIn = response.body().getCurrent().getPressureIn().toString();
                String pressureMb = response.body().getCurrent().getPressureMb().toString();
                String windKph = response.body().getCurrent().getWindKph().toString();
                String windMph = response.body().getCurrent().getWindMph().toString();
                String windDir = response.body().getCurrent().getWindDir().toString();
                String visivilityKm = response.body().getCurrent().getVisKm().toString();
                String visivilityM = response.body().getCurrent().getVisMiles().toString();
                String sunrise = response.body().getForecast().getForecastday().get(0).getAstro().getSunrise().toString();
                String sunset = response.body().getForecast().getForecastday().get(0).getAstro().getSunset().toString();
                String location = response.body().getLocation().toString();
                String currentCondition = response.body().getCurrent().getCondition().getText().toString();
                String currentConditionIcon = response.body().getCurrent().getCondition().getIcon().toString();
                Log.e("url",currentConditionIcon);
                String feelC = response.body().getCurrent().getFeelslikeC().toString();
                String feelF = response.body().getCurrent().getFeelslikeC().toString();
                String lsatUpdate = response.body().getCurrent().getLastUpdated();


                TextView avgTempt=(TextView)getView().findViewById(R.id.current_temp);
                avgTempt.setText(crntTempC);
                ImageView currentImageView = (ImageView)getView().findViewById(R.id.current_image);
                Glide.with(getContext()).load("http:"+currentConditionIcon).into(currentImageView);
                TextView crntTempUnitT = (TextView) getView().findViewById(R.id.current_temp_unit);
                crntTempUnitT.setText(TempUnit);
                TextView maxTempt=(TextView)getView().findViewById(R.id.current_high_temp);
                maxTempt.setText(crntmaxTempC);
                TextView minTempt=(TextView)getView().findViewById(R.id.current_low_temp);
                minTempt.setText(crntmiTempC);
                TextView crntTempRangeUnitT = (TextView)getView().findViewById(R.id.current_range_temp_unit);
                crntTempRangeUnitT.setText(TempUnit);
                TextView humidityt=(TextView)getView().findViewById(R.id.current_humidity);
                humidityt.setText(humidity);
                TextView pressuret=(TextView)getView().findViewById(R.id.pressure);
                pressuret.setText(pressureIn);
                TextView visivilityt=(TextView)getView().findViewById(R.id.visibility);
                visivilityt.setText(visivilityKm);
                TextView windt=(TextView)getView().findViewById(R.id.speed);
                windt.setText(windKph);
                TextView windDirt=(TextView)getView().findViewById(R.id.wind_direction);
                windDirt.setText(windDir);
                TextView sunriset=(TextView)getView().findViewById(R.id.sunrise);
                sunriset.setText(sunrise);
                TextView sunsett=(TextView)getView().findViewById(R.id.sunset);
                sunsett.setText(sunset);
                //locationt=(TextView)getView().findViewById(R.id.);
                //locationt.setText(location);
                TextView currentConditiontt=(TextView)getView().findViewById(R.id.current_condition);
                currentConditiontt.setText(currentCondition);
                TextView lastUpdateT = (TextView) getView().findViewById(R.id.last_update_time);
                lastUpdateT.setText(lsatUpdate);
                TextView feelsT = (TextView)getView().findViewById(R.id.feels);
                feelsT.setText(feelC);
                TextView feelsUnitT = (TextView)getView().findViewById(R.id.feels_unit);
                feelsUnitT.setText(TempUnit);




//////////////////////////////////// HOURLY UPDATES \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

                for (int i = 0; i < 24; i++) {

                    hour[i] = response.body().getForecast().getForecastday().get(0).getHour().get(i).getTime().substring(11,16).toString();
                    hourTempC[i] = response.body().getForecast().getForecastday().get(0).getHour().get(i).getTempC().toString();
                    hourTempF[i] = response.body().getForecast().getForecastday().get(0).getHour().get(i).getTempF().toString();
                    hourCondIcon[i] = response.body().getForecast().getForecastday().get(0).getHour().get(i).getCondition().getIcon().toString();
                }


                hourUpdates = new ArrayList<>();

                for (int i = 0; i <24 ; i++) {
                    if(i == 0){
                        hourUpdates.add(new HourUpdate(12+"am",hourTempC[i],"http:"+hourCondIcon[i]));
                    } else if(i > 0 && i< 12){
                        hourUpdates.add(new HourUpdate(hour[i]+"am",hourTempC[i],"http:"+hourCondIcon[i]));
                    } else if (i == 12){
                        hourUpdates.add(new HourUpdate(12+"pm",hourTempC[i],"http:"+hourCondIcon[i]));
                    }else if (i > 12 && i < 24){
                        hourUpdates.add(new HourUpdate(hour[i%12]+"pm",hourTempC[i],"http:"+hourCondIcon[i]));
                    }
                }
                hourRecyclerview = (RecyclerView) getView().findViewById(R.id.hour_recyclerview);
                hourAdapter = new HourAdapter(getActivity(),hourUpdates);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                hourRecyclerview.setLayoutManager(mLayoutManager);
                hourRecyclerview.setItemAnimator(new DefaultItemAnimator());
                hourRecyclerview.setAdapter(hourAdapter);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




///////////////////////////////WEEKLY UPDAATE\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\


                SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");


                for (int j = 0; j <7; j++) {

                    date[j] = response.body().getForecast().getForecastday().get(j).getDate().toString();
                    try {
                        mDate[j] = inFormat.parse( date[j]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dayMaxTempC[j] = response.body().getForecast().getForecastday().get(j).getDay().getMaxtempC().toString();
                    dayMaxTempF[j] = response.body().getForecast().getForecastday().get(j).getDay().getMaxtempF().toString();
                    dayMinTempC[j] = response.body().getForecast().getForecastday().get(j).getDay().getMintempC().toString();
                    dayMinTempF[j] = response.body().getForecast().getForecastday().get(j).getDay().getMintempF().toString();
                    dayCondIcon[j] = response.body().getForecast().getForecastday().get(j).getDay().getCondition().getIcon().toString();

                }

                weeklyUpdates = new ArrayList<>();

                for (int j = 0; j <7 ; j++) {
                    if (j == 0){
                        weeklyUpdates.add(new WeeklyUpdate("Today",dayMaxTempC[j],dayMinTempC[j],TempUnit,"http:"+dayCondIcon[j]));
                    }else{
                        weeklyUpdates.add(new WeeklyUpdate(android.text.format.DateFormat.format("EEEE",mDate[j]).toString(),dayMaxTempC[j],dayMinTempC[j],TempUnit,"http:"+dayCondIcon[j]));
                    }
                }



                weekRecyclerview = (RecyclerView) getView().findViewById(R.id.week_recyclerview);
                weeklyAdapter = new WeeklyAdapter(getActivity(),weeklyUpdates);
                RecyclerView.LayoutManager wLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                weekRecyclerview.setLayoutManager(wLayoutManager);
                weekRecyclerview.setItemAnimator(new DefaultItemAnimator());
                weekRecyclerview.setAdapter(weeklyAdapter);

                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Toast.makeText(getActivity(),"Please connect to internet",Toast.LENGTH_LONG).show();


            }
        });



    }

}
