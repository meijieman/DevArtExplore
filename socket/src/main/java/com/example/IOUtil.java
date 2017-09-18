package com.example;

import java.io.Closeable;
import java.io.IOException;

/**
 * @desc: TODO
 * @author: Major
 * @since: 2017/9/18 21:15
 */
public class IOUtil{

    public static void close(Closeable... args){
        for(Closeable arg : args){
            if(arg != null){
                try{
                    arg.close();
                    arg = null;
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
