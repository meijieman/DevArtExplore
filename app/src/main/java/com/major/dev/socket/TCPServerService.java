package com.major.dev.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.major.dev.MyUtils;
import com.major.dev.SL;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class TCPServerService extends Service{

    private boolean mIsServiceDestroyed = false;
    private String[] mDefinedMessages = new String[]{
            "你好啊，哈哈",
            "请问你叫什么名字",
            "今天北京天气不错啊，shy"
    };

    @Override
    public void onCreate(){
        new Thread(new TcpServer()).start();
        super.onCreate();

    }

    @Override
    public void onDestroy() {

        mIsServiceDestroyed = true;
        SL.i("stop server");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent){
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class TcpServer implements Runnable{

        @Override
        public void run(){
            ServerSocket serverSocket;
            try{
                serverSocket = new ServerSocket(8688);
            } catch(IOException e){
                e.printStackTrace();
                SL.i("establish tcp server failed, port:8688");
                return;
            }

            while(!mIsServiceDestroyed){
                try{
                    final Socket client = serverSocket.accept();
                    SL.i("accept");
                    new Thread(){
                        @Override
                        public void run(){
                            try{
                                responseClient(client);
                            } catch(IOException e){
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void responseClient(Socket client) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        out.println("欢迎来到聊天室！");
        while(!mIsServiceDestroyed){
            String str = in.readLine();
            SL.i("msg from client: " + str);
            if(str == null){
                // 客户端断开连接
                break;
            }

            int i = new Random().nextInt(mDefinedMessages.length);
            String msg = mDefinedMessages[i];
            out.println(msg);
            SL.i("send: " + msg);

        }
        SL.i("client quit.");
        MyUtils.close(out);
        MyUtils.close(in);
        client.close();
    }
}
