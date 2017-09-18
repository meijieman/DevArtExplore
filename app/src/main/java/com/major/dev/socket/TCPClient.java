package com.major.dev.socket;

import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.major.dev.MainActivity;
import com.major.dev.MyUtils;
import com.major.dev.SL;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * TODO
 * Created by MEI on 2017/9/18.
 */

public class TCPClient {

    private PrintWriter mPrintWriter;
    private Socket mClientSocket;
    private boolean isRunning = true;

    private Handler mHandler;

    public TCPClient(Handler handler) {
        mHandler = handler;
    }

    public void connectTCPServer() {
        getSocket();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
            while (isRunning) {
                String msg = br.readLine();
                SL.i("receive: " + msg);
                if (msg != null) {
                    String time = MyUtils.formatDateTime(System.currentTimeMillis());
                    String showedMsg = "server " + time + ": " + msg + "\n";
                    mHandler.obtainMessage(MainActivity.MESSAGE_RECEIVE_NEW_MSG, showedMsg).sendToTarget();
                }
            }

            SL.i("quit...");
            MyUtils.close(mPrintWriter);
            MyUtils.close(br);
            mClientSocket.close();
            mClientSocket = null;
        } catch (IOException e) {
            e.printStackTrace();

            SL.i("connectTCPServer tcp break.");
            // 尝试重连
            mClientSocket = null;
            getSocket();
        }
    }

    @NonNull
    private void getSocket() {
        // 重连机制
        while (mClientSocket == null) {
            try {
                mClientSocket = new Socket("localhost", 8688);
                mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mClientSocket.getOutputStream())), true);
                mHandler.sendEmptyMessage(MainActivity.MESSAGE_SOCKET_CONNECTED);
                SL.i("connectTCPServer server success.");
            } catch (IOException e) {
//                e.printStackTrace();
                SystemClock.sleep(1000);
                SL.i("connectTCPServer tcp server failed, retry ...");
            }
        }
    }

    public void sendMsg(String msg) {
        if (!TextUtils.isEmpty(msg) && mPrintWriter != null) {
            mPrintWriter.println(msg);
        }
    }

    public void destroy() {
        isRunning = false;
        if (mClientSocket != null) {
            try {
                if (mClientSocket.isConnected()) {
                    mClientSocket.shutdownInput();
                    mClientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
