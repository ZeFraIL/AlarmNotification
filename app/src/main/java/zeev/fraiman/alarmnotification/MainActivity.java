package zeev.fraiman.alarmnotification;

import static zeev.fraiman.alarmnotification.NotificationChannel.CHANNEL_1_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Context context;
    TextView tvWhere;
    Button bWhere, bSetAlarm, bAddTimeAlarm, bNow;
    EditText etTimeAfter;
    Calendar calendar, cNoti;
    int notiYear, notiMonth, notiDay, notiHour, notiMinute,
            cyear, cmonth, cday, cminute, chour;
    String stWhere = "";
    NotificationManagerCompat nmc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 100);

        initElements();

        bAddTimeAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String st=etTimeAfter.getText().toString();
                int t=Integer.parseInt(st);
                long afterT=System.currentTimeMillis()+1000*t;
                Intent after=new Intent(context,ReceiverAfterTime.class);
                PendingIntent afterIntent=PendingIntent.getBroadcast(context,
                        2,after,PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP,afterT,afterIntent);
            }
        });

        bWhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                cyear = calendar.get(Calendar.YEAR);
                cmonth = calendar.get(Calendar.MONTH);
                cday = calendar.get(Calendar.DAY_OF_MONTH);
                chour = calendar.get(Calendar.HOUR);
                cminute = calendar.get(Calendar.MINUTE);
                choiceDate();
            }
        });

        bSetAlarm=findViewById(R.id.bSetAlarm);
        bSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent after=new Intent(context,ReceiverAfterTime.class);
                PendingIntent afterIntent=PendingIntent.getBroadcast(context,
                        0,after,PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alma = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alma.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(), afterIntent);
                Toast.makeText(context, "Alarm is ON in "+
                        calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE), Toast.LENGTH_LONG).show();
            }
        });

        bNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent after=new Intent(context,ReceiverAfterTime.class);
                PendingIntent afterIntent=PendingIntent.getBroadcast(context,
                        17,after,PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alma = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alma.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), afterIntent);
                Toast.makeText(context, "Alarm is ON", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void choiceDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        notiYear = year;
                        notiMonth = month;
                        notiDay = day;
                        calendar.set(Calendar.YEAR,notiYear);
                        calendar.set(Calendar.MONTH,notiMonth);
                        calendar.set(Calendar.DAY_OF_MONTH,notiDay);
                        stWhere = notiDay + "." + (notiMonth+1) + "." + notiYear + " in ";
                        choiceTime();
                    }
                }, cyear, cmonth, cday);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void choiceTime() {
        TimePickerDialog tpd = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        notiHour = i;
                        notiMinute = i1;
                        calendar.set(Calendar.HOUR,notiHour);
                        calendar.set(Calendar.MINUTE,notiMinute);
                        stWhere += notiHour + ":" + notiMinute;
                        tvWhere.setText(stWhere);
                    }
                }, chour, cminute, true);
        tpd.show();
    }

    private void initElements() {
        bAddTimeAlarm=findViewById(R.id.bAddTimeAlarm);
        context = MainActivity.this;
        tvWhere = findViewById(R.id.tvWhere);
        bWhere = findViewById(R.id.bWhere);
        bNow = findViewById(R.id.bNow);
        etTimeAfter=findViewById(R.id.etTimeAfter);
    }

}