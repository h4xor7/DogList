package com.example.doglistjet.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.doglistjet.R;
import com.example.doglistjet.view.MainActivity;

public class NotificationsHelper {


    private static final String CHANNEL_ID = "Dogs Channel id";
    private static final int NOTIFICATION_ID = 777;

    private static NotificationsHelper instance;
    private Context context;

    private NotificationsHelper(Context context) {
        this.context = context;

    }

    public static NotificationsHelper getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationsHelper(context);
        }

        return instance;
    }

    public void createNotification() {
        createNotificationChennel();
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.dog_icon);
        Notification notification = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.dog)
                .setLargeIcon(icon)
                .setContentTitle("Dogs retrieved")
                .setContentText("This is a Notification to let you that the dog information has benn retrieved")
                .setStyle(
                        new NotificationCompat.BigPictureStyle()
                        .bigPicture(icon)
                        .bigLargeIcon(null)
                )
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();


        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID,notification);

    }

    private void createNotificationChennel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String name = CHANNEL_ID;
            String discription = "Dogs retrieved notification channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name,importance);
            channel.setDescription(discription);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

    }


}
