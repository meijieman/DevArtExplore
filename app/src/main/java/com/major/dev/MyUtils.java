package com.major.dev;

import java.io.Closeable;
import java.io.IOException;

/**
 * @desc: TODO
 * @author: Major
 * @since: 2017/9/17 9:19
 */
public class MyUtils{

    public static void close(Closeable closeable){
        try{
            closeable.close();
            closeable = null;
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
