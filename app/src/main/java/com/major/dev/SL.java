package com.major.dev;

import android.util.Log;

/**
 * TODO
 * Created by MEI on 2017/9/18.
 */

public class SL {
    private static final String TAG = "tag_ele";


    public static void i(String msg) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stackTrace = new Thread().getStackTrace();
        if (stackTrace.length > 3) {
            sb.append(stackTrace[3]);
            sb.append(": ");
        }

        sb.append(msg);
        Log.i(TAG, sb.toString());
    }
}
