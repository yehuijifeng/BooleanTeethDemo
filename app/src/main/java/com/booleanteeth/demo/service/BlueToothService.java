package com.booleanteeth.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.booleanteeth.demo.manager.BltManager;

/**
 * Created by LuHao on 2016/9/26.
 * 蓝牙服务
 */
public class BlueToothService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        BltManager.getIntaces(this);
        // 若蓝牙为打开，就会提示用户启动蓝牙服务
        BltManager.getIntaces().checkBleDevice(this);
        super.onCreate();
    }
}
