package com.drewmalone.evelauncher;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;

public class SoundService extends Service {

    private MediaPlayer mp;
    private MediaSessionCompat mediaSession;

    @Override
    public void onCreate() {
        super.onCreate();
        initMediaSession();
        playAudio();
    }

    private void initMediaSession() {
        mediaSession = new MediaSessionCompat(this, "EVE_Launcher_Session");
        mediaSession.setActive(true);
    }

    private void playAudio() {
        mp = MediaPlayer.create(this, R.raw.eve_welcome);
        if (mp != null) {
            mp.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build());

            mp.setOnCompletionListener(mediaPlayer -> {
                mediaPlayer.release();
                abandonFocus();
                stopMediaSession();
                stopSelf();
            });

            mp.start();
        } else {
            abandonFocus();
            stopMediaSession();
            stopSelf();
        }
    }

    private void stopMediaSession() {
        if (mediaSession != null) {
            mediaSession.setActive(false);
            mediaSession.release();
            mediaSession = null;
        }
    }

    private void abandonFocus() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (am != null) {
            am.abandonAudioFocus(null);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // No need for foreground service unless you want persistent playback
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.release();
            mp = null;
        }
        stopMediaSession();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
