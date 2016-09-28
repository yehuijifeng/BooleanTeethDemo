package com.booleanteeth.demo.service;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.booleanteeth.demo.appliaction.BltAppliaction;
import com.booleanteeth.demo.contants.BltContant;
import com.booleanteeth.demo.manager.BltManager;

import java.io.IOException;

/**
 * Created by LuHao on 2016/9/26.
 * 蓝牙服务
 */
public class BltService {

    /**
     * 设置成单例模式
     */
    private BltService() {
        createBltService();
    }

    private static class BlueToothServices {
        private static BltService bltService = new BltService();
    }

    public static BltService getInstance() {
        return BlueToothServices.bltService;
    }

    private BluetoothServerSocket bluetoothServerSocket;
    private BluetoothSocket socket;

    public BluetoothSocket getSocket() {
        return socket;
    }

    public BluetoothServerSocket getBluetoothServerSocket() {
        return bluetoothServerSocket;
    }

    /**
     * 从蓝牙适配器中创建一个蓝牙服务作为服务端，在获得蓝牙适配器后创建服务器端
     */
    private void createBltService() {
        try {
            if (BltManager.getInstance().getmBluetoothAdapter() != null && BltManager.getInstance().getmBluetoothAdapter().isEnabled()) {
                bluetoothServerSocket = BltManager.getInstance().getmBluetoothAdapter().listenUsingRfcommWithServiceRecord("com.bluetooth.demo", BltContant.SPP_UUID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 旦连接被接受并获得BluetoothSocket后，
     * 应用立即将该BluetoothSocket发送至独立的线程并关闭BluetoothSocket，跳出循环。
     */
    public void run(Handler handler) {
        while (true) {
            try {
                //注意，当accept()返回BluetoothSocket时，socket已经连接了，因此不应该调用connect方法。
                //manageConnectedSocket()是一个虚构的方法，用来初始化数据传输的线程，将在后文介绍数据传输的部分。
                socket = getBluetoothServerSocket().accept();

                // If a connection was accepted
                if (socket != null) {
                    BltAppliaction.bluetoothSocket = socket;
                    Message message = new Message();
                    message.what = 3;
                    message.obj = socket.getRemoteDevice();
                    handler.sendMessage(message);
                    //应该开一个子线程
                    //manageConnectedSocket(socket);
                    getBluetoothServerSocket().close();
                    break;
                }
            } catch (IOException e) {
                break;
            }
        }
    }

    /**
     * Will cancel the listening socket, and cause the thread to finish
     */
    public void cancel() {
        try {
            getBluetoothServerSocket().close();
        } catch (IOException e) {
            Log.e("blueTooth", "关闭服务器socket失败");
        }
    }
}
