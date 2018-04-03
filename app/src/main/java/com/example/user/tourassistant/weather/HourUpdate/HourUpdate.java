package com.example.user.tourassistant.weather.HourUpdate;

/**
 * Created by RamzanUllah on 25-Sep-17.
 */

    public class HourUpdate {
    private String time;
    private String temp;
    //private String conditionText;
    private String conditionIcon;

    public HourUpdate(String time, String temp, String conditionIcon) {
        this.time = time;
        this.temp = temp;
        this.conditionIcon = conditionIcon;
    }

    /* public HourUpdate(String time, String temp, String conditionText) {
        this.time = time;
        this.temp = temp;
        this.conditionText = conditionText;
    }*/

    public String getTime() {
        return time;
    }

    public String getTemp() {
        return temp;
    }



  /*  public String getConditionText() {
        return conditionText;
    }
*/

    public String getConditionIcon() {
        return conditionIcon;
    }
}
