package com.videocompressor;

import com.facebook.react.bridge.Promise;
import com.videocompressor.VideoCompress;
import com.videocompressor.Util;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VideoCompressor extends ReactContextBaseJavaModule {
    private static ReactApplicationContext reactContext;

    private Promise globalPromise;
    private String globalOutpath;
    private Callback thisonEnd;
    private long startTime, endTime;

    void sendEvent(ReactContext reactContext, String eventName, WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    Locale getLocale() {
        Configuration config = getReactApplicationContext().getResources().getConfiguration();
        Locale sysLocale = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = getSystemLocale(config);
        } else {
            sysLocale = getSystemLocaleLegacy(config);
        }

        return sysLocale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config) {
        return config.getLocales().get(0);
    }

    @SuppressWarnings("deprecation")
    public static Locale getSystemLocaleLegacy(Configuration config) {
        return config.locale;
    }

    final VideoCompress.CompressListener listener = new VideoCompress.CompressListener() {
        @Override
        public void onStart() {
            // tv_indicator.setText("Compressing..." + "\n"
            // + "Start at: " + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new
            // Date()));
            // pb_compress.setVisibility(View.VISIBLE);
            startTime = System.currentTimeMillis();
            Log.d("COMPRESSOR", "onstart");
            Util.writeFile(getReactApplicationContext().getCurrentActivity(),
                    "Start at: " + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()) + "\n");
        }

        @Override
        public void onSuccess() {
            // String previous = tv_indicator.getText().toString();
            // tv_indicator.setText(previous + "\n"
            // + "Compress Success!" + "\n"
            // + "End at: " + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new
            // Date()));
            // pb_compress.setVisibility(View.INVISIBLE);
            endTime = System.currentTimeMillis();
            Log.d("COMPRESSOR", "onsuccess");
            Util.writeFile(getReactApplicationContext().getCurrentActivity(),
                    "End at: " + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()) + "\n");
            Util.writeFile(getReactApplicationContext().getCurrentActivity(),
                    "Total: " + ((endTime - startTime) / 1000) + "s" + "\n");
            Util.writeFile(getReactApplicationContext().getCurrentActivity());
            // thisonEnd.invoke("success");
            WritableMap map = Arguments.createMap();

            map.putString("outPath",globalOutpath);

            globalPromise.resolve(map);
            WritableMap params = Arguments.createMap();
            sendEvent(reactContext, "EventSuccessCompress", params);
        }

        @Override
        public void onFail() {
            // tv_indicator.setText("Compress Failed!");
            // pb_compress.setVisibility(View.INVISIBLE);
            endTime = System.currentTimeMillis();
            Log.d("COMPRESSOR", "onfail");
            Util.writeFile(getReactApplicationContext().getCurrentActivity(),
                    "Failed Compress!!!" + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()));
            globalPromise.reject("onFail","onfail");
        }

        @Override
        public void onProgress(float percent) {
            Log.d("COMPRESSOR", "Percent :" + String.valueOf(percent));
            WritableMap params = Arguments.createMap();
            params.putString("Progress", String.valueOf(percent));

            sendEvent(reactContext, "EventOnProgress", params);
        }
    };

    public VideoCompressor(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @Override
    public String getName() {
        return "VideoCompressor";
    }

    @ReactMethod
    public void compress(String inPath, String outPath, Integer qualityType, Promise promise) {
        globalPromise = promise;
        globalOutpath = outPath;
        try {
            switch (qualityType) {
            case 1:
                VideoCompress.compressVideoLow(inPath, outPath, listener);
                break;
            case 2:
                VideoCompress.compressVideoMedium(inPath, outPath, listener);
                break;
            case 3:
                VideoCompress.compressVideoHigh(inPath, outPath, listener);
                break;
            default:
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}