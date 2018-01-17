package com.example.fengq.myapplication.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import com.example.fengq.myapplication.R;
import com.example.fengq.myapplication.base.BaseApplication;
import com.example.fengq.myapplication.bean.PersonBean;
import com.example.fengq.myapplication.tools.UIHelper;
import com.example.fengq.myapplication.widget.LineView;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by fengq on 2017/7/27.
 */

public class DefineChartActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "DefineChartActivity";
    private static final int MSG_DATA_CHANGE = 0x11;
    private LineView mLineView;
    private Handler mHandler;
    private int mX = 0;
    private RefWatcher refWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_define_chart);
        mLineView = (LineView) this.findViewById(R.id.line);
        refWatcher = BaseApplication.getRefWatcher(this);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                switch (msg.what) {
                    case MSG_DATA_CHANGE:
                        mLineView.setLinePoint(msg.arg1, msg.arg2);
                        break;

                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };

        new Thread() {
            public void run() {
                for (int index = 0; index < 2000; index++) {
                    Message message = new Message();
                    message.what = MSG_DATA_CHANGE;
                    message.arg1 = mX;
                    message.arg2 = (int) (Math.random() * 200);
                    mHandler.sendMessage(message);
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mX += 30;
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        refWatcher.watch(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_check_leakcanary:
                UIHelper.ToastMessage(getApplicationContext(), "click");
                startAsyncTask();
                break;
        }
    }

    private void startAsyncTask() {
        // This async task is an anonymous class and therefore has a hidden reference to the outer
        // class MainActivity. If the activity gets destroyed before the task finishes (e.g. rotation),
        // the activity instance will leak.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                // Do some slow work in background
                Log.e(TAG, "doInBackground: hhhhhhh");
                new PersonBean();
                SystemClock.sleep(8000);
                new PersonBean();
                return null;
            }
        }.execute();
    }
}
