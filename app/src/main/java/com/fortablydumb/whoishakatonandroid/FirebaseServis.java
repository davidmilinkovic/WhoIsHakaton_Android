package com.fortablydumb.whoishakatonandroid;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FirebaseServis extends FirebaseMessagingService {


    public static final String CHANNEL_ID_POR = "Channel1";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("SERVIS", "From: " + remoteMessage.getFrom());

        //if (remoteMessage.getData().size() > 0) {
            Log.d("SERVIS", "Message data payload: " + remoteMessage.getData());

            Intent notificationIntent = new Intent(getApplicationContext(), GlavniActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(FirebaseServis.this, (int) System.currentTimeMillis(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(FirebaseServis.this, CHANNEL_ID_POR)
                    .setContentTitle("AAA")
                    .setContentText("BBB!")
                    .setContentIntent(contentIntent)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setAutoCancel(true)
                    .setColor(getApplicationContext().getColor(R.color.akcent))
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            Notification n = builder.build();

            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId(CHANNEL_ID_POR);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID_POR ,
                        "Ko.Je",
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                }
            }


            notificationManager.notify(2, n);

        //}

        if (remoteMessage.getNotification() != null) {
            Log.d("Servis", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }
}
