package vickypatel.ca.androidtestexample;

import android.app.Application;
import android.content.Context;

/**
 * Created by VickyPatel on 2016-02-24.
 */
public class MyApplication extends Application {

    private static MyApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getsInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }





}