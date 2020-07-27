package com.vatsal.kesarwani.fcmexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.vatsal.kesarwani.fcmexample.App.CHANNEL_1_ID;
import static com.vatsal.kesarwani.fcmexample.App.CHANNEL_2_ID;

public class MainActivity extends AppCompatActivity {

    private EditText mssg,tit;
    private String message,title;
    private Button channel1,channel2;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        channel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title=tit.getText().toString();
                message=mssg.getText().toString();

                Notification notification =new NotificationCompat.Builder(MainActivity.this,CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.ic_one)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .build();

                notificationManager.notify(1,notification);
            }
        });

        channel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title=tit.getText().toString();
                message=mssg.getText().toString();

                Notification notification =new NotificationCompat.Builder(MainActivity.this,CHANNEL_2_ID)
                        .setSmallIcon(R.drawable.ic_two)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .build();

                notificationManager.notify(2,notification);
            }
        });

    }

    private void init(){
        mssg=findViewById(R.id.message_descrption);
        tit=findViewById(R.id.message_title);
        channel1=findViewById(R.id.channel1);
        channel2=findViewById(R.id.channel2);
        notificationManager= NotificationManagerCompat.from(MainActivity.this);
    }
}