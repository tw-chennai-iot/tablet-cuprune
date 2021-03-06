package com.thoughtworks.iot.buybuddy;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FMS extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        String nfcTag = remoteMessage.getNotification().getBody();
        Log.d(TAG, "Notification Message Body: " + nfcTag);
        Intent intent = new Intent("ACTION_REFRESH");
        // You can also include some extra data.
        intent.putExtra("nfctag", nfcTag);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
