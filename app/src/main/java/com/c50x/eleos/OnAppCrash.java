package com.c50x.eleos;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.c50x.eleos.activities.LoadingActivity;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

public class OnAppCrash implements UncaughtExceptionHandler {
    private Activity activity;
    private UncaughtExceptionHandler defaultHandler;

    public OnAppCrash(Activity a) {
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        activity = a;
    }
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Intent intent = new Intent(activity, LoadingActivity.class);
        intent.putExtra("crash", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyApplication.getInstance().getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) MyApplication.getInstance().getBaseContext().getSystemService(Context.ALARM_SERVICE);
        //mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, pendingIntent);

        final Writer stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        String stacktrace = stringWriter.toString();
        printWriter.close();

        Log.e("CRASH", stacktrace);
        Log.e("CRASH", ex.toString());

        activity.finish();
        System.exit(2);
        defaultHandler.uncaughtException(thread, ex);

    }
}
