package com.vatsal.kesarwani.fcmexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.vatsal.kesarwani.fcmexample.App.CHANNEL_1_ID;
import static com.vatsal.kesarwani.fcmexample.App.CHANNEL_2_ID;

public class MainActivity extends AppCompatActivity {

    private EditText mssg,tit;
    private String message,title;
    private TextView tok;
    private Button channel1,channel2;
    private NotificationManagerCompat notificationManager;
    private MediaSession mediaSession;
    static List<Message> MESSAGE;
    private static final String TAG = "MainActivity";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        sharedPreferences.edit()
                                .putString(AppConfig.TOKEN,token)
                                .apply();
                        tok.setText(sharedPreferences.getString(AppConfig.TOKEN,""));
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        channel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationChannel1(MainActivity.this);
            }
        });

        channel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title=tit.getText().toString();
                message=mssg.getText().toString();
                notificationChannel2(MainActivity.this ,title ,message);
            }
        });

    }

    public static void notificationChannel2(Context context ,String title ,String message){

        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),R.drawable.wall);

        Notification notification =new NotificationCompat.Builder(context,CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_two)


                //hide for messenger notification
                .setContentTitle(title)
                .setContentText(message)
                /*.setLargeIcon(largeIcon)
                .addAction(R.drawable.ic_two,"Like",null)
                .addAction(R.drawable.ic_two,"Dislike",null)
                .addAction(R.drawable.ic_two,"Next",null)
                .addAction(R.drawable.ic_two,"Previous",null)
                .addAction(R.drawable.ic_two,"Pause",null)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(1,2,3)
                    .setMediaSession(MediaSessionCompat.Token.fromToken(mediaSession.getSessionToken())))
                .setSubText("Sub Text")*/

                //multi line notification 1
                /*.setStyle(new NotificationCompat.InboxStyle()
                        .setBigContentTitle("Big Content title")
                        .setSummaryText("Summary Text")
                        .addLine("This is Line 1")
                        .addLine("This is Line 2")
                        .addLine("This is Line 3")
                        .addLine("This is Line 4")
                        .addLine("This is Line 5")
                        .addLine("This is Line 6")
                        .addLine("This is Line 7")
                )*/
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(2,notification);
    }

    public static void notificationChannel1(Context context){
//        title=tit.getText().toString();
//        message=mssg.getText().toString();

        Intent activityIntent = new Intent(context , MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0,activityIntent,0);

//                Intent broadCastIntent = new Intent(MainActivity.this,NotificationReceiver.class);
//                broadCastIntent.putExtra("toastMessage",message);
//                PendingIntent actionIntent = PendingIntent.getBroadcast(MainActivity.this,
//                        0,broadCastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        androidx.core.app.RemoteInput remoteInput =new RemoteInput.Builder("key_text_reply")
                .setLabel("Your Answer...")
                .build();

        Intent replyIntent = new Intent(context ,NotificationReceiver.class);
        PendingIntent replyPendingIntent = PendingIntent.getBroadcast(context,
                0,replyIntent,0);

        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                R.drawable.ic_reply,
                "Reply",
                replyPendingIntent
        ).addRemoteInput(remoteInput).build();

        NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle("Me");
        messagingStyle.setConversationTitle("Group Chat");

        for(Message chatMessage : MESSAGE){
            NotificationCompat.MessagingStyle.Message notificationMessage = new NotificationCompat.MessagingStyle.Message(
                    chatMessage.getText(),
                    chatMessage.getTimeStamp(),
                    chatMessage.getSender()
            );
            messagingStyle.addMessage(notificationMessage);
        }

        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),R.drawable.wall);

        Notification notification =new NotificationCompat.Builder(context,CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
//                .setContentTitle(title)
//                .setContentText(message)
                .addAction(replyAction)
                //.setLargeIcon(largeIcon)
                .setStyle(messagingStyle)
                //big text message notification
                /*.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText("Lorem Ipsum Lorem Ipsum Lorem Ipsum Lorem Ipsum Lorem Ipsum Lorem Ipsum Lorem Ipsum  Lorem Ipsum Lorem Ipsum Lorem Ipsum Lorem Ipsum Lorem Ipsum Lorem Ipsum Lorem Ipsum")
                    .setBigContentTitle("Big Content title")
                    .setSummaryText("Summary Text"))*/

                //big picture
                /*.setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(largeIcon)
                    .bigLargeIcon(null))*/
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setColor(Color.BLUE)
                .setAutoCancel(true)
                //.addAction(R.mipmap.ic_launcher,"Toast",actionIntent)
                .setOnlyAlertOnce(true)
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1,notification);
    }

    private void init(){
        mssg=findViewById(R.id.message_descrption);
        tit=findViewById(R.id.message_title);
        channel1=findViewById(R.id.channel1);
        channel2=findViewById(R.id.channel2);
        notificationManager= NotificationManagerCompat.from(MainActivity.this);
        mediaSession=new MediaSession(this,"tag");
        MESSAGE=new ArrayList<>();
        MESSAGE.add(new Message("Good Morning" ,"Sam"));
        MESSAGE.add(new Message("Good Morning Sam" ,null));
        MESSAGE.add(new Message("How are you?" ,"Sam"));
        sharedPreferences=getSharedPreferences(AppConfig.SHARED_PREF,Context.MODE_PRIVATE);
        tok=findViewById(R.id.token);
    }
}