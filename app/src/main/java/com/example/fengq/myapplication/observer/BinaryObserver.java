package com.example.fengq.myapplication.observer;

/**
 * Created by fengq on 2017/7/29.
 */

public class BinaryObserver extends Observer {
    @Override
    public void update() {
        System.out.println( "Binary String: "
                + Integer.toBinaryString( subject.getState() ) );
    }
}
