package com.gokulsundar4545.dropu;

import android.content.Context;

import com.airbnb.lottie.BuildConfig;

public class UpdateChecker {

    // Method to compare current version with the latest version
    public static boolean isUpdateAvailable(Context context) {
        String currentVersion = BuildConfig.VERSION_NAME;
        String latestVersion = fetchLatestVersionFromServer(); // Fetch latest version from a server

        return latestVersion != null && !currentVersion.equals(latestVersion);
    }

    // Dummy method to simulate fetching latest version from a server
    private static String fetchLatestVersionFromServer() {
        // In a real app, you would implement logic to retrieve latest version from a server
        // For demonstration, returning a hardcoded version
        return "1.2.0";
    }
}
