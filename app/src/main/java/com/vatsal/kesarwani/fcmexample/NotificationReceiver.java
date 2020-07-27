package com.vatsal.kesarwani.fcmexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.RemoteInput;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*String mssg=intent.getStringExtra("toastMessage");
        Toast.makeText(context, mssg, Toast.LENGTH_LONG).show();*/

        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

        if (remoteInput != null){
            CharSequence replyText =remoteInput.getCharSequence("key_text_reply");
            Message answer = new Message(replyText,null);
            MainActivity.MESSAGE.add(answer);
        }

        MainActivity.notificationChannel1(context);
    }
}
