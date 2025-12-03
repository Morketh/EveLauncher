package com.drewmalone.evelauncher;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class SoundService extends Service {

    private static final String CHANNEL_ID = "EVE_LAUNCHER_CHANNEL";
    private MediaPlayer mp;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Start foreground service to comply with Android 14+ requirements
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("EVE Launcher")
                .setContentText("Playing welcome sound…")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .build();

        startForeground(1, notification);

        // Create MediaPlayer
        mp = MediaPlayer.create(this, R.raw.eve_welcome);

        if (mp != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes attrs = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build();
                mp.setAudioAttributes(attrs);
            } else {
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }

            mp.setOnCompletionListener(mediaPlayer -> {
                mediaPlayer.release();
                stopSelf(); // Stop service when done
            });

            mp.start();
        } else {
            // File missing: stop service gracefully
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "EVE Launcher",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setSound(null, null); // No notification sound
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // Not a bound service
    }
}
