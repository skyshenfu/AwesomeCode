package com.qljy.myapp;

import android.app.Application;
import android.content.Context;

import com.qljy.myapp.hook.PackageManagerHook;

public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
       PackageManagerHook.hook(base);
    }
}
