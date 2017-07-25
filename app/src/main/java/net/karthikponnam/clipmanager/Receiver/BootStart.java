package net.karthikponnam.clipmanager.Receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import net.karthikponnam.clipmanager.ClipService;

/**
 * Created by ponna on 17-05-2017.
 */

public class BootStart extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, ClipService.class);
        startWakefulService(context, startServiceIntent);
    }
}
