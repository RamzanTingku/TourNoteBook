package com.example.user.tourassistant.weather.WeekUpdate;

/**
 * Created by Ramzan Ullah on 10/23/2017.
 */

public class WeeklyUpdate {

    private String  day, maxtemp, mintemp, tempUnit, conditionIcon;

    public WeeklyUpdate(String day, String maxtemp, String mintemp, String tempUnit, String conditionIcon) {

        this.day = day;
        this.maxtemp = maxtemp;
        this.mintemp = mintemp;
        this.tempUnit = tempUnit;
        this.conditionIcon = conditionIcon;
    }


    public String getDay() {
        return day;
    }

    public String getMaxtemp() {
        return maxtemp;
    }

    public String getMintemp() {
        return mintemp;
    }

    public String getTempUnit() {
        return tempUnit;
    }

    public String getConditionIcon() {
        return conditionIcon;
    }
}
