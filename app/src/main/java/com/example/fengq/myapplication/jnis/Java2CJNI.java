package com.example.fengq.myapplication.jnis;

/**
 * Created by fengq on 2017/5/18.
 */

public class Java2CJNI {
    static {
        System.loadLibrary("Java2C");
    }

    public native String java2C();
}
