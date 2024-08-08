package com.gokulsundar4545.dropu;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SoundAdjustActivity extends AppCompatActivity {

    private Equalizer mEqualizer;
    private SeekBar mBassSeekBar;
    private SeekBar mTrebleSeekBar;
    private SeekBar mMidrangeSeekBar;

    private static final int PERMISSION_REQUEST_CODE = 123;
    private static final String PREF_NAME = "SoundAdjustPrefs";
    private static final String KEY_BASS_PROGRESS = "bass_progress";
    private static final String KEY_TREBLE_PROGRESS = "treble_progress";
    private static final String KEY_MIDRANGE_PROGRESS = "midrange_progress";

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_adjust);

        mSharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if (checkPermissions()) {
            initializeEqualizer();
            setupEqualizerControls();
            loadSeekBarProgress();
        } else {
            requestPermissions();
        }
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.MODIFY_AUDIO_SETTINGS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeEqualizer();
                setupEqualizerControls();
                loadSeekBarProgress();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                // Handle permission denied
            }
        }
    }

    private void initializeEqualizer() {
        try {
            mEqualizer = new Equalizer(0, 0);
            mEqualizer.setEnabled(true);
        } catch (RuntimeException e) {
            e.printStackTrace();
            Toast.makeText(this, "Equalizer initialization failed!", Toast.LENGTH_SHORT).show();
            // Handle equalizer initialization error
        }
    }

    private void setupEqualizerControls() {
        if (mEqualizer != null) {
            mBassSeekBar = findViewById(R.id.bassSeekBar);
            mTrebleSeekBar = findViewById(R.id.trebleSeekBar);
            mMidrangeSeekBar = findViewById(R.id.midrangeSeekBar);

            mBassSeekBar.setMax(100);
            mTrebleSeekBar.setMax(100);
            mMidrangeSeekBar.setMax(100);

            mBassSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mEqualizer.setBandLevel((short) 0, (short) (progress - 50));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    saveSeekBarProgress(KEY_BASS_PROGRESS, seekBar.getProgress());
                }
            });

            mTrebleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mEqualizer.setBandLevel((short) 1, (short) (progress - 50));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    saveSeekBarProgress(KEY_TREBLE_PROGRESS, seekBar.getProgress());
                }
            });

            mMidrangeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mEqualizer.setBandLevel((short) 2, (short) (progress - 50));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    saveSeekBarProgress(KEY_MIDRANGE_PROGRESS, seekBar.getProgress());
                }
            });
        }
    }

    private void saveSeekBarProgress(String key, int progress) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, progress);
        editor.apply();
    }

    private void loadSeekBarProgress() {
        mBassSeekBar.setProgress(mSharedPreferences.getInt(KEY_BASS_PROGRESS, 50));
        mTrebleSeekBar.setProgress(mSharedPreferences.getInt(KEY_TREBLE_PROGRESS, 50));
        mMidrangeSeekBar.setProgress(mSharedPreferences.getInt(KEY_MIDRANGE_PROGRESS, 50));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mEqualizer != null) {
            mEqualizer.release();
        }
    }
}
