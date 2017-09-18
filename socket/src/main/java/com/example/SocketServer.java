package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @desc: TODO
 * @author: Major
 * @since: 2017/9/18 21:08
 */
public class SocketServer{

    public static void main(String[] args){
        SocketServer socketServer = new SocketServer();
        socketServer.startServer();
    }

    private void startServer(){
        ServerSocket serverSocket;
        BufferedReader reader;
        try{
            serverSocket = new ServerSocket(9898);
            SL.i("server started.");
            Socket socket = serverSocket.accept(); // 等待客户端接入
            SL.i("client " + socket.hashCode() + " connected.");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String receivedMsg;
            while((receivedMsg = reader.readLine()) != null){
                SL.i("receivedMsg " + receivedMsg);
            }


        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
