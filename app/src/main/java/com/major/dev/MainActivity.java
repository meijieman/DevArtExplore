package com.major.dev;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.major.dev.socket.TCPServerService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int MESSAGE_RECEIVE_NEW_MSG = 1;
    private static final int MESSAGE_SOCKET_CONNECTED = 2;

    private PrintWriter mPrintWriter;
    private EditText mMessageEditText;
    private TextView mMessageTextView;
    private View mSendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSendButton = findViewById(R.id.btn_3);
        mSendButton.setOnClickListener(this);

        mMessageEditText = (EditText)findViewById(R.id.et_1);
        mMessageTextView = (TextView)findViewById(R.id.tv_1);

        // 启动服务端
        startService(new Intent(this, TCPServerService.class));


        new Thread(){
            @Override
            public void run(){
                // 连接服务
                connectTCPServer();
            }
        }.start();


    }

    @Override
    protected void onDestroy(){
        if(mClientSocket != null){
            try{
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    Socket mClientSocket;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch(msg.what) {
                case MESSAGE_RECEIVE_NEW_MSG:
                    mMessageTextView.setText(mMessageTextView.getText() + (String)msg.obj);
                    break;
                case MESSAGE_SOCKET_CONNECTED:
                    mSendButton.setEnabled(true);
                    break;
            }
        }
    };

    private void connectTCPServer(){
        Socket socket = null;
        while(socket == null){
            try{
                socket = new Socket("localhost", 8688);
                mClientSocket = socket;
                mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED);
                System.out.println("connectTCPServer server success.");
            } catch(IOException e){
//                e.printStackTrace();
                SystemClock.sleep(1000);
                System.out.println("connectTCPServer tcp server failed, retry ...");
            }
        }

        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while(!isFinishing()){
                String msg = br.readLine();
                System.out.println("receive: " + msg);
                if(msg != null){
                    String time = formatDateTime(System.currentTimeMillis());
                    String showedMsg = "server " + time + ": " + msg + "\n";
                    mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, showedMsg).sendToTarget();
                }
            }

            System.out.println("quit...");
            MyUtils.close(mPrintWriter);
            MyUtils.close(br);
            socket.close();
        } catch(IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v){
        switch(v.getId()) {
            case R.id.btn_3:
                // send msg
                String msg = mMessageEditText.getText().toString();
                if(!TextUtils.isEmpty(msg) && mPrintWriter != null){
                    mPrintWriter.println(msg);
                    mMessageEditText.setText("");
                    String time = formatDateTime(System.currentTimeMillis());
                    String showedMsg = "self " + time + ": " + msg + "\n";
                    mMessageTextView.setText(mMessageTextView.getText() + showedMsg);
                }
                break;
        }
    }

    private String formatDateTime(long time){
        return new SimpleDateFormat("(HH:mm:ss)").format(new Date(time));
    }
}
