package com.example.fengq.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.fengq.myapplication.R;
import com.example.fengq.myapplication.base.BaseActivity;
import com.example.fengq.myapplication.tools.UIHelper;

/**
 * Created by fengq on 2017/9/29.
 */

public class PhoneLocationActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_location);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_base_station_location:
                UIHelper.showActivity(getActivity(), BaseStationLocationActivity.class);
                break;
            case R.id.btn_AGPS_location:
                UIHelper.showActivity(getActivity(), AGPSLocationActivity.class);
                break;
        }
    }

    @Override
    public void next(View view) {

    }

    @Override
    public void pre(View view) {

    }
}
