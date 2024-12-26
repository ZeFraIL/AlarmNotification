package zeev.fraiman.alarmnotification;

import android.app.Application;
import android.app.NotificationManager;

public class NotificationChannel extends Application {

    public static final String CHANNEL_1_ID = "Lens_app_channel_1";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        android.app.NotificationChannel channel1 = new android.app.NotificationChannel(
                CHANNEL_1_ID,
                "Channel 1",
                NotificationManager.IMPORTANCE_HIGH
        );
        channel1.setDescription("This is Channel for CursAnd teachers");
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel1);
    }
}
