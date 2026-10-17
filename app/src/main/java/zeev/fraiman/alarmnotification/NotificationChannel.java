package zeev.fraiman.alarmnotification;

import android.app.Application;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationChannel extends Application {

    public static final String CHANNEL_1_ID = "alarm_notification_channel_1";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.app.NotificationChannel channel1 = new android.app.NotificationChannel(
                    CHANNEL_1_ID,
                    "Alarm Notification Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Channel for alarm notifications");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel1);
            }
        }
    }
}
