package zeev.fraiman.alarmnotification;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private TextView tvWhere;
    private Button bWhere, bSetAlarm, bAddTimeAlarm, bNow, bDateFragment;
    private EditText etTimeAfter;
    private Calendar calendar;

    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        calendar = Calendar.getInstance();

        initElements();
        checkPermissions();

        bAddTimeAlarm.setOnClickListener(view -> scheduleAlarmAfterSeconds());

        bWhere.setOnClickListener(view -> choiceDate());

        bSetAlarm.setOnClickListener(view -> scheduleSelectedAlarm());

        bNow.setOnClickListener(view -> triggerImmediateNotification());

        bDateFragment.setOnClickListener(view -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });
    }

    private void initElements() {
        tvWhere = findViewById(R.id.tvWhere);
        bWhere = findViewById(R.id.bWhere);
        bSetAlarm = findViewById(R.id.bSetAlarm);
        bAddTimeAlarm = findViewById(R.id.bAddTimeAlarm);
        bNow = findViewById(R.id.bNow);
        bDateFragment = findViewById(R.id.bDateFragment);
        etTimeAfter = findViewById(R.id.etTimeAfter);
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE
                );
            }
        }
    }

    private void scheduleAlarmAfterSeconds() {
        String inputText = etTimeAfter.getText().toString().trim();
        if (inputText.isEmpty()) {
            Toast.makeText(context, R.string.enter_valid_seconds, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int seconds = Integer.parseInt(inputText);
            if (seconds <= 0) {
                Toast.makeText(context, R.string.enter_valid_seconds, Toast.LENGTH_SHORT).show();
                return;
            }

            long triggerAtMillis = System.currentTimeMillis() + (seconds * 1000L);
            Intent intent = new Intent(context, ReceiverAfterTime.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    2,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
            );

            setExactAlarm(triggerAtMillis, pendingIntent);
            Toast.makeText(context, getString(R.string.alarm_is_on), Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(context, R.string.enter_valid_seconds, Toast.LENGTH_SHORT).show();
        }
    }

    private void scheduleSelectedAlarm() {
        Intent intent = new Intent(context, ReceiverAfterTime.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                (int) calendar.getTimeInMillis(),
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        setExactAlarm(calendar.getTimeInMillis(), pendingIntent);

        String timeStr = String.format(Locale.getDefault(), "%02d:%02d",
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        Toast.makeText(context, getString(R.string.alarm_set_for, timeStr), Toast.LENGTH_LONG).show();
    }

    private void triggerImmediateNotification() {
        Intent intent = new Intent(context, ReceiverAfterTime.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                17,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        setExactAlarm(System.currentTimeMillis(), pendingIntent);
        Toast.makeText(context, getString(R.string.alarm_is_on), Toast.LENGTH_SHORT).show();
    }

    private void setExactAlarm(long triggerAtMillis, PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            } else {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }
    }

    private void choiceDate() {
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    choiceTime();
                },
                currentYear, currentMonth, currentDay
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        Calendar maxCalendar = (Calendar) calendar.clone();
        maxCalendar.add(Calendar.YEAR, 1);
        datePickerDialog.getDatePicker().setMaxDate(maxCalendar.getTimeInMillis());

        datePickerDialog.setOnCancelListener(dialog ->
                Toast.makeText(this, R.string.date_selection_cancelled, Toast.LENGTH_SHORT).show()
        );

        datePickerDialog.show();
    }

    private void choiceTime() {
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                context,
                (timePicker, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);

                    String selectedDateTime = String.format(
                            Locale.getDefault(),
                            "%02d.%02d.%d %02d:%02d",
                            calendar.get(Calendar.DAY_OF_MONTH),
                            calendar.get(Calendar.MONTH) + 1,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE)
                    );
                    tvWhere.setText(selectedDateTime);
                },
                currentHour, currentMinute, true
        );

        timePickerDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
