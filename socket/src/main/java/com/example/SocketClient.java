package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @desc: TODO
 * @author: Major
 * @since: 2017/9/18 21:05
 */
public class SocketClient{

    public static void main(String[] args){
        // 创建多个 client
        for(int i = 0; i < 1; i++){
            new Thread(){
                @Override
                public void run(){
                    new SocketClient().start();

                }
            }.start();
        }
    }

    private void start(){
        BufferedReader inputReader = null;
        BufferedWriter writer = null;
        BufferedReader reader = null;
        Socket socket = null;
        try{
            socket = new Socket("127.0.0.1", 9898);
            SL.i("connecting..");
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            inputReader = new BufferedReader(new InputStreamReader(System.in));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            startServerReplyListener(reader);

            String inputContent;
            while(!(inputContent = inputReader.readLine()).equals("bye")){
                SL.i("inputContent " + inputContent);
                writer.write(inputContent + "\n");
                writer.flush();
            }
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            IOUtil.close(inputReader, writer, socket, reader);
        }
    }

    public void startServerReplyListener(final BufferedReader reader){
        new Thread(new Runnable(){
            @Override
            public void run(){
                String response;
                try{
                    while((response = reader.readLine()) != null){
                        SL.i(response);
                    }
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
