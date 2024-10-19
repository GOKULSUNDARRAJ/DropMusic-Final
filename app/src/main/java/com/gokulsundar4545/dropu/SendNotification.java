package com.gokulsundar4545.dropu;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendNotification {

    private static final String TAG = "SendNotification";

    String userfcmToken;
    String title;
    String body;
    Context context;
    String postUrl = "https://fcm.googleapis.com/v1/projects/drop-track-e203a/messages:send";

    public SendNotification(String userfcmToken, String title, String body, Context context) {
        this.userfcmToken = userfcmToken;
        this.title = title;
        this.body = body;
        this.context = context;
    }

    public void sendNotification() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject mainJson = new JSONObject();

        try {
            JSONObject notification = new JSONObject();
            JSONObject notificationBody = new JSONObject();

            // Prepare notification body with title and message content
            notificationBody.put("title", "New Message from " + title);
            notificationBody.put("body", body);

            // Add notification body to the main notification
            notification.put("token", userfcmToken);
            notification.put("notification", notificationBody);

            // Add custom data (like chatId) in the "data" section
            JSONObject data = new JSONObject();
            data.put("chatId", FirebaseAuth.getInstance().getCurrentUser().getUid()); // Replace with dynamic chat ID
            notification.put("data", data);

            // Build the entire message structure
            mainJson.put("message", notification);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainJson,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Handle success
                            Toast.makeText(context, "Notification sent successfully", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Notification response: " + response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String errorMessage;
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                // Log the response data
                                String responseBody = new String(error.networkResponse.data);
                                errorMessage = "Error sending notification: " + responseBody;
                            } else {
                                errorMessage = error.getMessage() != null ? error.getMessage() : "Unknown error";
                            }
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Notification error: " + errorMessage);
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    FirebaseAccessToken accessToken = new FirebaseAccessToken();
                    String accessKey = accessToken.getAccessToken();
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + accessKey);  // Added space after Bearer
                    return headers;
                }
            };

            requestQueue.add(request);

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

}
