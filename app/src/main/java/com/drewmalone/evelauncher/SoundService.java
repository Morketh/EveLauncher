package com.drewmalone.evelauncher;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;

public class SoundService extends Service {

    private MediaPlayer mp;
    private MediaSessionCompat mediaSession;
    private AudioManager audioManager;
    private AudioFocusRequest focusRequest;

    @Override
    public void onCreate() {
        super.onCreate();
        initMediaSession();
        initAudioManager();
        playAudio();
    }

    private void initMediaSession() {
        mediaSession = new MediaSessionCompat(this, "EVE_Launcher_Session");
        mediaSession.setActive(true);
    }

    private void initAudioManager() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    private void playAudio() {
        mp = MediaPlayer.create(this, R.raw.eve_welcome);

        if (mp == null) {
            cleanup();
            return;
        }

        // AudioAttributes optimized for AA / car audio
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        mp.setAudioAttributes(audioAttributes);

        // Request audio focus to duck other media (Spotify, etc.)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                    .setAudioAttributes(audioAttributes)
                    .setOnAudioFocusChangeListener(focusChange -> {})
                    .build();

            int result = audioManager.requestAudioFocus(focusRequest);
            if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                cleanup();
                return;
            }
        } else {
            int result = audioManager.requestAudioFocus(
                    null,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            );
            if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                cleanup();
                return;
            }
        }

        // Completion listener cleans up everything
        mp.setOnCompletionListener(mediaPlayer -> cleanup());

        // Start playback
        mp.start();
    }

    private void cleanup() {
        if (mp != null) {
            mp.release();
            mp = null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && focusRequest != null) {
            audioManager.abandonAudioFocusRequest(focusRequest);
        } else {
            audioManager.abandonAudioFocus(null);
        }

        if (mediaSession != null) {
            mediaSession.setActive(false);
            mediaSession.release();
            mediaSession = null;
        }

        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
