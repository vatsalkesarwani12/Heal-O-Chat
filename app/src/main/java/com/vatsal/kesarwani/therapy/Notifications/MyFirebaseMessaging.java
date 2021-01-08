package com.vatsal.kesarwani.therapy.Notifications;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vatsal.kesarwani.therapy.Activity.ChatActivity;

public class MyFirebaseMessaging extends FirebaseMessagingService {



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String sented = remoteMessage.getData().get("sent");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null && sented.equals(firebaseUser.getUid())){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sendOreoNotification(remoteMessage);

            }else{

            sendNotification(remoteMessage);}

        }


    }

    private void sendOreoNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));

        Intent intent = new Intent(this, ChatActivity.class);
        Bundle bundle= new Bundle();
        bundle.putString("uid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotifications oreoNotifications=new OreoNotifications(this);
        Notification.Builder builder = oreoNotifications.getOreoNotification(title,body,pendingIntent,soundURI,icon);

        int i =0;
        if (j>0){
            i=j;
        }
        oreoNotifications.getManager().notify(i, builder.build());

    }




    private void sendNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));

        Intent intent = new Intent(this, ChatActivity.class);
        Bundle bundle= new Bundle();
        bundle.putString("userID",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "ChatNotification")
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundURI)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int i =0;
        if (j>0){
            i=j;
        }
        notificationManager.notify(i, builder.build());
    }
}
