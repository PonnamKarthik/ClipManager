package net.karthikponnam.clipmanager;

import android.app.IntentService;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import net.karthikponnam.clipmanager.Database.DataQuery;

/**
 * Created by ponna on 17-05-2017.
 */

public class ClipService extends Service {

    private String TAG = ClipService.class.getName();

    public ClipService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final DataQuery dataQuery = new DataQuery(getApplicationContext());
        final ClipboardManager clipManage = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        dataQuery.addClip(clipManage.getText().toString());
        clipManage.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                Log.d(TAG, "onPrimaryClipChanged: " + clipManage.getText().toString());
                dataQuery.addClip(clipManage.getText().toString());
            }
        });
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        startService(new Intent(getApplicationContext(), ClipService.class));
        //super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "onTaskRemoved: ");
        startService(new Intent(getApplicationContext(), ClipService.class));
        //super.onTaskRemoved(rootIntent);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return START_STICKY;
    }

}
