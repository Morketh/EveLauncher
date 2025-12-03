package com.drewmalone.evelauncher;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Launch the SoundService to play the audio
        Intent serviceIntent = new Intent(this, SoundService.class);
        startService(serviceIntent);

        // Close the launcher activity immediately
        finish();
    }
}
