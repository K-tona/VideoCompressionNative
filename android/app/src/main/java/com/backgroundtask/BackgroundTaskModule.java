package com.backgroundtask;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;

import com.backgroundtask.BackgroundTaskService;


public class BackgroundTaskModule extends ReactContextBaseJavaModule {
    public static ReactApplicationContext reactContext;

    BackgroundTaskModule(ReactApplicationContext context) {
        super(context);
        Log.d("BackgroundTask","init");
        reactContext = context;
    }

    @Override
    public String getName() {
        return "BackgroundTask";
    }

    @ReactMethod
    public void compress(String inPath, String outPath) {
        Intent intent = new Intent(getReactApplicationContext().getCurrentActivity(),BackgroundTaskService.class);
        Bundle bundle = new Bundle();
        Log.d("COMPRESSOR",inPath);
        Log.d("COMPRESSOR",outPath);

        bundle.putString("inPath", inPath);
        bundle.putString("outPath", outPath);
        intent.putExtras(bundle);

        getReactApplicationContext().getCurrentActivity().startService(intent);
    }
}