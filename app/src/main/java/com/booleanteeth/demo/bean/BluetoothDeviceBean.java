package com.booleanteeth.demo.bean;

import android.bluetooth.BluetoothClass;
import android.os.ParcelUuid;

/**
 * Created by LuHao on 2016/9/26.
 */
public class BluetoothDeviceBean {
    private String address;//地址
    private String name;//名称
    private int type;//类型
    private int bondState;//设备状态，状态值：BOND_NONE},BOND_BONDING},BOND_BONDED}.
    private ParcelUuid[] uuids;//返回所支持的特性(uuid)的远程设备。
    private BluetoothClass bluetoothClass;//远程设备的蓝牙类

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBondState() {
        return bondState;
    }

    public void setBondState(int bondState) {
        this.bondState = bondState;
    }

    public ParcelUuid[] getUuids() {
        return uuids;
    }

    public void setUuids(ParcelUuid[] uuids) {
        this.uuids = uuids;
    }

    public BluetoothClass getBluetoothClass() {
        return bluetoothClass;
    }

    public void setBluetoothClass(BluetoothClass bluetoothClass) {
        this.bluetoothClass = bluetoothClass;
    }
}
