package com.major.dev;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.major.dev.socket.TCPClient;
import com.major.dev.socket.TCPServerService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int MESSAGE_RECEIVE_NEW_MSG = 1;
    public static final int MESSAGE_SOCKET_CONNECTED = 2;

    private EditText mMessageEditText;
    private TextView mMessageTextView;
    private View mSendButton;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_RECEIVE_NEW_MSG:
                    mMessageTextView.setText(mMessageTextView.getText() + (String) msg.obj);
                    break;
                case MESSAGE_SOCKET_CONNECTED:
                    mSendButton.setEnabled(true);
                    break;
            }
        }
    };
    private TCPClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);

        mSendButton = findViewById(R.id.btn_3);
        mSendButton.setOnClickListener(this);

        mMessageEditText = (EditText) findViewById(R.id.et_1);
        mMessageTextView = (TextView) findViewById(R.id.tv_1);

        mClient = new TCPClient(mHandler);

    }

    @Override
    protected void onDestroy() {
        mClient.destroy();
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                // 启动服务端
                startService(new Intent(this, TCPServerService.class));
                break;
            case R.id.btn_2:
                stopService(new Intent(this, TCPServerService.class));
                break;
            case R.id.btn_3:
                // send msg
                String msg = mMessageEditText.getText().toString();
                mClient.sendMsg(msg);
                if (!TextUtils.isEmpty(msg)) {
                    mMessageEditText.setText("");
                    String time = MyUtils.formatDateTime(System.currentTimeMillis());
                    String showedMsg = "self " + time + ": " + msg + "\n";
                    mMessageTextView.setText(mMessageTextView.getText() + showedMsg);
                }
                break;
            case R.id.btn_4:
                // 启动 client
                new Thread() {
                    @Override
                    public void run() {
                        // 连接服务
                        mClient.connectTCPServer();
                    }
                }.start();
                break;
            case R.id.btn_5:
                //　关闭　client

                break;
        }
    }

}
