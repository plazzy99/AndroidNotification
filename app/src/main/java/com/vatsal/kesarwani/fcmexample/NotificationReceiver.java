package com.vatsal.kesarwani.fcmexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String mssg=intent.getStringExtra("toastMessage");
        Toast.makeText(context, mssg, Toast.LENGTH_LONG).show();
    }
}
