package zeev.fraiman.alarmnotification;

import static zeev.fraiman.alarmnotification.NotificationChannel.CHANNEL_1_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReceiverAfterTime extends BroadcastReceiver {

    private NotificationManagerCompat nmc;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show();

        build_and_view_Notification(context, intent);
    }

    private void build_and_view_Notification(Context context, Intent intent) {
        String title = "IMPORTANT!", message = "Something work";
        Intent goInfo = new Intent(context, MainActivity.class);
        //MediaPlayer mp=MediaPlayer.create(context,R.raw.gudok);
        //mp.start();
        PendingIntent go = PendingIntent.getActivity(context,
                100, goInfo,PendingIntent.FLAG_IMMUTABLE);
        //nmc = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.my_notifi_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(go)
                //.addAction(R.drawable.go_icon, "Go to info part", go)
                .setColor(Color.RED)
                .build();
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1,notification);
        //nmc.notify(1, notification);
    }

}