package com.c50x.eleos;

import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;

import com.c50x.eleos.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class,true);

    @Test public void stuff(){
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(30));
    }
}
