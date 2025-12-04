package com.drewmalone.evelauncher;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start the SoundService
        Intent serviceIntent = new Intent(this, SoundService.class);
        startService(serviceIntent);

        // Immediately finish activity so no UI flash
        finish();
    }
}
