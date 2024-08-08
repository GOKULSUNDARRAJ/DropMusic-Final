package com.gokulsundar4545.dropu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;

public class StorageActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView storageTextView;

    private ProgressBar storageProgressBar;
    private TextView storageInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        // Initialize views
        progressBar = findViewById(R.id.progressBar);
        storageTextView = findViewById(R.id.notification1);
        storageProgressBar = findViewById(R.id.progressBar2);
        storageInfoTextView = findViewById(R.id.notification12);

        // Update app storage usage
        updateStorageUsage();
        updateStorageUsage2(); // Optionally, update other storage info
    }

    private void updateStorageUsage() {
        long totalAppStorage = getTotalAppStorage();
        long maxStorage = 500 * 1024 * 1024; // Example: 500 MB
        int usedPercentage = (int) (((double) totalAppStorage / maxStorage) * 100);
        int freePercentage = 100 - usedPercentage;

        // Update TextView with storage information
        String formattedUsedSize = formatSize(totalAppStorage);
        String formattedFreeSize = formatSize(maxStorage - totalAppStorage);
        storageTextView.setText("App storage used: " + formattedUsedSize + " / " + formattedFreeSize + " free");

        // Update ProgressBar
        progressBar.setProgress(usedPercentage);
        progressBar.setSecondaryProgress(100); // Set secondary progress to full (indicating free space)
    }

    private long getTotalAppStorage() {
        long appDirSize = getDirSize(getFilesDir());
        long cacheDirSize = getDirSize(getCacheDir());
        long externalCacheDirSize = getDirSize(getExternalCacheDir());
        return appDirSize + cacheDirSize + externalCacheDirSize;
    }

    private long getDirSize(File directory) {
        if (directory == null || !directory.exists()) {
            return 0;
        }

        long totalSize = 0;
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    totalSize += getDirSize(file);
                } else {
                    totalSize += file.length();
                }
            }
        }
        return totalSize;
    }

    private String formatSize(long size) {
        if (size <= 0) return "0";

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return String.format("%.2f %s", size / Math.pow(1024, digitGroups), units[digitGroups]);
    }

    private void updateStorageUsage2() {
        long totalInternalStorage = getTotalStorage(Environment.getDataDirectory());
        long totalExternalStorage = getTotalStorage(Environment.getExternalStorageDirectory());
        long totalDeviceStorage = totalInternalStorage + totalExternalStorage;

        int internalStoragePercentage = (int) ((totalInternalStorage * 100) / totalDeviceStorage);
        int externalStoragePercentage = (int) ((totalExternalStorage * 100) / totalDeviceStorage);

        storageProgressBar.setProgress(internalStoragePercentage);
        storageProgressBar.setSecondaryProgress(internalStoragePercentage + externalStoragePercentage);

        String storageInfoText = "Internal Storage: " + internalStoragePercentage + "%\n"+"\n" +
                "External Storage: " + externalStoragePercentage + "%";

        storageInfoTextView.setText(storageInfoText);
    }

    private long getTotalStorage(File directory) {
        StatFs stat = new StatFs(directory.getAbsolutePath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return totalBlocks * blockSize;
    }

    public void gotomain(View view) {
        startActivity(new Intent(StorageActivity.this,SettingsActivity.class));
        finish();
    }


}

