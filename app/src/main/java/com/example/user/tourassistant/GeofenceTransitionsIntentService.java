package com.example.user.tourassistant;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;


public class GeofenceTransitionsIntentService extends IntentService {


    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent currentEvent = GeofencingEvent.fromIntent(intent);
        if(currentEvent.hasError()){
            Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
            return;
        }

        int geofenceTransition = currentEvent.getGeofenceTransition();
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){

            List<Geofence>trigerringGeofences = currentEvent.getTriggeringGeofences();

            String geofenceTransitionDetails = getGeofenceTransitionDetails(geofenceTransition,trigerringGeofences);

            sendNotification(geofenceTransitionDetails);
        }

    }

    private void sendNotification(String geofenceTransitionDetails) {
        Intent notificationIntent = new Intent(getApplicationContext(),HomeActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setColor(Color.RED)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(geofenceTransitionDetails)
                .setContentText("Click notification to return to app")
                .setContentIntent(notificationPendingIntent);

        builder.setAutoCancel(true);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());
    }

    private String getGeofenceTransitionDetails(int geofenceTransition, List<Geofence> trigerringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);
        ArrayList<String>triggeringGeofenceIdsList = new ArrayList<>();
        for(Geofence geofence : trigerringGeofences){
            triggeringGeofenceIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ",triggeringGeofenceIdsList);

        return geofenceTransitionString + ": "+triggeringGeofencesIdsString;
    }

    private String getTransitionString(int geofenceTransition) {
        switch (geofenceTransition){
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "Exited";
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "Entered";
        }
        return null;
    }

}
