package com.gokulsundar4545.dropu;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class ShareSongActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // UUID for SPP (Serial Port Profile)

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice selectedDevice;
    private BluetoothSocket bluetoothSocket;

    Button button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_song);

        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize BluetoothAdapter
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                // Check if device supports Bluetooth
                if (bluetoothAdapter == null) {
                    // Device does not support Bluetooth
                    Toast.makeText(ShareSongActivity.this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                // Check if Bluetooth is enabled, if not, request to enable it
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    // Bluetooth is already enabled, proceed with sharing the song
                    discoverDevicesAndShareSong();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                // Bluetooth is enabled, proceed with sharing the song
                discoverDevicesAndShareSong();
            } else {
                // User declined to enable Bluetooth, handle this case
                Toast.makeText(this, "Bluetooth is required to share the song", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void discoverDevicesAndShareSong() {
        // Discover nearby Bluetooth devices
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        // For simplicity, let's assume you select the first paired device in the set
        if (!pairedDevices.isEmpty()) {
            for (BluetoothDevice device : pairedDevices) {
                selectedDevice = device;
                break; // Select the first paired device
            }

            if (selectedDevice != null) {
                // Establish a Bluetooth connection with the selected device
                connectToDeviceAndShareSong();
            } else {
                Toast.makeText(this, "No paired Bluetooth devices found", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "No paired Bluetooth devices found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void connectToDeviceAndShareSong() {
        // Establish a Bluetooth connection with the selected device
        try {
            bluetoothSocket = selectedDevice.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();

            // Once connected, send the song data to the connected device
            sendSongDataOverBluetooth();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to connect to the device123", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void sendSongDataOverBluetooth() {
        try {
            // Get the song data (e.g., file path or song details) to share
            String songData = MediaPlayerManager.getCurrentSong().getUrl();

            // Send the song data over Bluetooth
            OutputStream outputStream = bluetoothSocket.getOutputStream();
            outputStream.write(songData.getBytes());

            // Notify user that song data has been shared successfully
            Toast.makeText(this, "Song shared successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to share the song", Toast.LENGTH_SHORT).show();
        } finally {
            // Close the Bluetooth socket after sharing the song data
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finish(); // Close the activity after sharing the song data
        }
    }
}
