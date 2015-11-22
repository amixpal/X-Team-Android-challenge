package com.xteam.sample;

import android.app.Application;
import android.content.Context;

import com.xteam.sample.dagger.AppConfComponent;
import com.xteam.sample.dagger.AppConfModule;
import com.xteam.sample.dagger.DaggerAppConfComponent;


/**
 * Created by Amit on 10/24/15.
 */

public class AppDelegate extends Application {
    public AppConfComponent component;

    // Dagger Configuration
    public static AppConfComponent getComponent(Context context) {
        return ((AppDelegate) context.getApplicationContext()).component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppConfComponent.builder().appConfModule(new AppConfModule()).build();
    }
}
