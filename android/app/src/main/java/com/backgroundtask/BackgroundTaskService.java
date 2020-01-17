package com.backgroundtask;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.HeadlessJsTaskService;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.jstasks.HeadlessJsTaskConfig;

public class BackgroundTaskService extends HeadlessJsTaskService {
    @Override
    protected HeadlessJsTaskConfig getTaskConfig(Intent intent) {
        Bundle extras = intent.getExtras();
        
        if (extras != null) {
            return new HeadlessJsTaskConfig("BackgroundTask", Arguments.fromBundle(extras), 25000, true);
        }
        return null;
    }
}