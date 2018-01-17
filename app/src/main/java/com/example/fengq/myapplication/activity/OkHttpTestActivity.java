package com.example.fengq.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.fengq.myapplication.R;
import com.example.fengq.myapplication.base.BaseCallback;
import com.example.fengq.myapplication.bean.PersonBean;
import com.example.fengq.myapplication.helper.OkHttpHelper;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by fengq on 2017/4/28.
 */

public class OkHttpTestActivity extends Activity {

    private String TAG = "__OkHttpTestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_begin_okHttp:
                postExample();
                break;
        }
    }

    public void postExample() {
        String url = "http://testapp.paokejia.com/paoke/api/user.php?method=login";
        Map<String, String> map = new HashMap<>();
        map.put("username", "18859658237");
        map.put("password", "18859658237");
        OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
        okHttpHelper.post(url, map, new BaseCallback<PersonBean>() {
            @Override
            public void onRequestBefore() {
                System.out.println("onRequestBefore");
            }

            @Override
            public void onFailure(Request request, Exception e) {
                System.out.println("onFailure" + request.toString());
            }

            @Override
            public void onSuccess(Response response, PersonBean personBean) {
                Log.e(TAG, "onSuccess" + ",response=" + response.toString() + ",nickname=" + personBean.getNickname() + ",uid=" + personBean.getUid());
                Log.e(TAG, "onSuccess" + ",personBean=" + personBean.toString());
            }

//            @Override
//            public void onSuccess(Response response, String s) {
//                Log.e(TAG, "onSuccess" + ",response=" + response.toString() + ",s=" + s);
//            }

            @Override
            public void onError(Response response, int errorCode, Exception e) {
                System.out.println("onError" + ",response=" + response.toString() + ",errorCode=" + errorCode);
            }
        });
    }

}
