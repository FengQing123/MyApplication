package com.example.fengq.myapplication.observer;

/**
 * Created by fengq on 2017/7/29.
 */

public abstract class Observer {
    protected Subject subject;
    public abstract void update();
}
