package com.booleanteeth.demo.manager;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;

/**
 * Created by LuHao on 2016/9/26.
 * 蓝牙对象管理器
 */
public class BltManager {
    /**
     * 设置成单例模式
     */
    private BltManager(Context context) {
        initBltManager(context);
    }

    private static BltManager bltManager;

    public static synchronized BltManager getIntaces(Context context) {
        if (bltManager != null) {
            synchronized (BltManager.class) {
                if (bltManager != null) {
                    bltManager = new BltManager(context);
                }
            }
        }
    }

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothAdapter.LeScanCallback leScanCallback;

    public BluetoothManager getBluetoothManager() {
        return bluetoothManager;
    }

    /**
     * api 18以上
     *
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initBltManager(Context context) {
        //首先获取BluetoothManager
        bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);

        //获取BluetoothAdapter
        bluetoothAdapter = bluetoothManager.getAdapter();

        //创建BluetoothAdapter.LeScanCallback

    }

    /**
     * api 18 以上
     *
     * @param mLeScanCallback
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void bltLeScanCallback(final BluetoothAdapter.LeScanCallback mLeScanCallback) {
        leScanCallback = new BluetoothAdapter.LeScanCallback() {
            /**
             * @param device 蓝牙设备
             * @param rssi 信号强度指示器
             * @param scanRecord 扫描记录
             */
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                mLeScanCallback.onLeScan(device, rssi, scanRecord);
                try {
                    String struuid = NumberUtils.bytes2HexString(NumberUtils.reverseBytes(scanRecord)).replace("-", "").toLowerCase();
                    if (device!=null && struuid.contains(DEVICE_UUID_PREFIX.toLowerCase())) {
                        mBluetoothDevices.add(device);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void startBltDevice(){
        //开始搜索设备
        bluetoothAdapter.stopLeScan(leScanCallback);
    }

}
