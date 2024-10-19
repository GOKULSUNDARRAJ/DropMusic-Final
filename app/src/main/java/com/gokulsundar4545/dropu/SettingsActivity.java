package com.gokulsundar4545.dropu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity implements AudioManager.OnAudioFocusChangeListener {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREVIOUS_VOLUME_KEY = "previousVolume";
    private static final String KEY_SPEAKER_STATE = "speaker_state";

    private AudioManager audioManager;
    private SharedPreferences sharedPreferences;
    private Switch switch1, mSpeakerSwitch;

    LinearLayout edit12, edit3, edit4, edit5, edit6, edit7, edit8,edit44,edit75,edit45;
    ImageView imageView6;
    TextView title23;


    private static final int REQUEST_ENABLE_BT = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch bluetoothSwitch = findViewById(R.id.switch185);


        bluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Switch is ON, allow Bluetooth
                    enableBluetooth();
                } else {
                    // Switch is OFF, disable Bluetooth
                    disableBluetooth();
                }
            }
        });


        imageView6=findViewById(R.id.imageView6);
        title23=findViewById(R.id.title23);
        title23.startAnimation(AnimationUtils.loadAnimation(SettingsActivity.this, R.anim.recycler2));
        imageView6.startAnimation(AnimationUtils.loadAnimation(SettingsActivity.this, R.anim.recycler4));

        edit12 = findViewById(R.id.edit12);
        edit3 = findViewById(R.id.edit3);

        edit4 = findViewById(R.id.edit4);
        edit5 = findViewById(R.id.edit5);

        edit6 = findViewById(R.id.edit6);
        edit7 = findViewById(R.id.edit7);

        edit8 = findViewById(R.id.edit8);
        edit44=findViewById(R.id.edit44);
        edit75=findViewById(R.id.edit75);
        edit45=findViewById(R.id.edit45);

        edit12.startAnimation(AnimationUtils.loadAnimation(SettingsActivity.this, R.anim.recycler2));
        edit3.startAnimation(AnimationUtils.loadAnimation(SettingsActivity.this, R.anim.recycler2));
        edit4.startAnimation(AnimationUtils.loadAnimation(SettingsActivity.this, R.anim.recycler2));
        edit5.startAnimation(AnimationUtils.loadAnimation(SettingsActivity.this, R.anim.recycler2));
        edit6.startAnimation(AnimationUtils.loadAnimation(SettingsActivity.this, R.anim.recycler2));
        edit7.startAnimation(AnimationUtils.loadAnimation(SettingsActivity.this, R.anim.recycler2));
        edit8.startAnimation(AnimationUtils.loadAnimation(SettingsActivity.this, R.anim.recycler2));
        edit44.startAnimation(AnimationUtils.loadAnimation(SettingsActivity.this, R.anim.recycler2));
        edit75.startAnimation(AnimationUtils.loadAnimation(SettingsActivity.this, R.anim.recycler2));
        edit45.startAnimation(AnimationUtils.loadAnimation(SettingsActivity.this, R.anim.recycler2));


        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Initialize the speaker switch and set its initial state
        mSpeakerSwitch = findViewById(R.id.switch18);
        mSpeakerSwitch.setChecked(sharedPreferences.getBoolean(KEY_SPEAKER_STATE, true));

        // Listen for speaker switch state changes
        mSpeakerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save switch state to SharedPreferences
                saveSwitchState(KEY_SPEAKER_STATE, isChecked);

                // Adjust audio output based on the state of the switch
                if (isChecked) {
                    audioManager.setMode(AudioManager.MODE_NORMAL);
                    audioManager.setSpeakerphoneOn(true);
                } else {
                    audioManager.setMode(AudioManager.MODE_IN_CALL);
                    audioManager.setSpeakerphoneOn(false);
                }
            }
        });

        // Initialize the volume switch and set its initial state
        switch1 = findViewById(R.id.switch1);
        switch1.setChecked(sharedPreferences.getBoolean("switchState", false));

        // Listen for volume switch state changes
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Save the previous volume level
                    int previousVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    sharedPreferences.edit().putInt(PREVIOUS_VOLUME_KEY, previousVolume).apply();
                    // Reduce device sound to 70%
                    int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    audioManager.setStreamVolume(
                            AudioManager.STREAM_MUSIC,
                            (int) (maxVolume * 0.3),
                            0
                    );
                    // Disable volume controls
                    setVolumeControlStream(AudioManager.STREAM_MUSIC);
                } else {
                    // Restore device sound to the previous level
                    int previousVolume = sharedPreferences.getInt(PREVIOUS_VOLUME_KEY, 0);
                    audioManager.setStreamVolume(
                            AudioManager.STREAM_MUSIC,
                            previousVolume,
                            0
                    );
                    // Enable volume controls
                    setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
                }
                sharedPreferences.edit().putBoolean("switchState", isChecked).apply();
            }
        });

        // Request audio focus and register the activity as an audio focus change listener
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        // If the volume is adjusted by the user, release the volume switch
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
            switch1.setChecked(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release audio focus and unregister audio focus change listener
        audioManager.abandonAudioFocus(this);
    }

    public void gotoabout(View view) {
        startActivity(new Intent(SettingsActivity.this, AboutActivity.class));
    }

    public void gotoaccount(View view) {
        startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
    }

    public void logoutall(View view) {
        CustomDialogClassfoeadd cdd = new CustomDialogClassfoeadd(SettingsActivity.this);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }

    public void gotoEdit(View view) {
        startActivity(new Intent(SettingsActivity.this, SoundAdjustActivity.class));
    }

    public void gotoprivacy(View view) {
        startActivity(new Intent(SettingsActivity.this, QrActivity.class));
    }

    private void saveSwitchState(String key, boolean state) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, state);
        editor.apply();
    }

    public void gotomain(View view) {
        Intent intent = new Intent(SettingsActivity.this, MainActivity2.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    public void gotoaboutedit44(View view) {
        Intent intent = new Intent(SettingsActivity.this,webActivity.class);
        startActivity(intent);

    }

    private void enableBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                // Bluetooth is not enabled, request to enable it
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }


    private void disableBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
        }
    }


    public void gotostorage(View view) {
        Intent intent = new Intent(SettingsActivity.this,StorageActivity.class);
        startActivity(intent);
    }
}
