package com.booleanteeth.demo.service;

import android.text.TextUtils;

import com.booleanteeth.demo.appliaction.BltAppliaction;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Luhao on 2016/9/28.
 * 发送消息的服务
 */
public class SendSocketService {

    public static void sendMessage(String message) {
        if (BltAppliaction.bluetoothSocket == null || TextUtils.isEmpty(message)) return;
        try {
            message+="\n";
            OutputStream outputStream = BltAppliaction.bluetoothSocket.getOutputStream();
            outputStream.write(message.getBytes("utf-8"));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
