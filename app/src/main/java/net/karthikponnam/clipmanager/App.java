package net.karthikponnam.clipmanager;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by ponna on 17-05-2017.
 */

public class App extends Application {

    String TAG = getClass().getName();

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/ArvoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        String model = Build.BRAND;
        Log.d(TAG, "onCreate: " + model);

    }

}
