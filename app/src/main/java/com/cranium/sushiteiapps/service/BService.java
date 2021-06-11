package com.cranium.sushiteiapps.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BService extends Service {
    public class LocalBinder extends Binder {
        BService getService() {
            return BService.this;
        }
    }
    @Override
    public void onCreate() {
        for(int x = 0; x < 100; x++){
            Toast.makeText(this, "Wow", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    private final IBinder mBinder = new LocalBinder();
}