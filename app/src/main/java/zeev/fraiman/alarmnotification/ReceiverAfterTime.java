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
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

public class ReceiverAfterTime extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm triggered!", Toast.LENGTH_SHORT).show();

        buildAndShowNotification(context);
    }

    private void buildAndShowNotification(Context context) {
        String title = "IMPORTANT!";
        String message = "Alarm notification triggered!";

        Intent goInfo = new Intent(context, MainActivity.class);
        PendingIntent go = PendingIntent.getActivity(
                context,
                100,
                goInfo,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.my_notifi_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(go)
                .setAutoCancel(true)
                .addAction(R.drawable.go_icon, "Open App", go)
                .setColor(Color.RED)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(1, notification);
        }
    }
}
