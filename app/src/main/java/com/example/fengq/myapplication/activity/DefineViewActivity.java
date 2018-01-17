package com.example.fengq.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.example.fengq.myapplication.R;
import com.example.fengq.myapplication.base.BaseActivity;
import com.example.fengq.myapplication.tools.UIHelper;
import com.example.fengq.myapplication.widget.LineGraphicView;
import com.example.fengq.myapplication.widget.MyImageView;
import com.example.fengq.myapplication.widget.ProgressDefineView;

import java.util.ArrayList;

/**
 * Created by fengq on 2017/7/4.
 */

public class DefineViewActivity extends BaseActivity {

    private static final String TAG = "DefineViewActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_define_view);

        ProgressDefineView progressDefineView = (ProgressDefineView) findViewById(R.id.progress_define_view);
        progressDefineView.setScoreAndMaxScore(String.valueOf(5), 100f);

        ArrayList<Double> yList = new ArrayList<>();
        yList.add(2.103);
        yList.add(4.05);
        yList.add(6.60);
        yList.add(3.08);
        yList.add(4.32);
        yList.add(2.0);
        yList.add(5.0);

        ArrayList<String> xRawDatas = new ArrayList<>();
        xRawDatas.add("05-19");
        xRawDatas.add("05-20");
        xRawDatas.add("05-21");
        xRawDatas.add("05-22");
        xRawDatas.add("05-23");
        xRawDatas.add("05-24");
        xRawDatas.add("05-25");
        xRawDatas.add("05-26");

        LineGraphicView lineGraphicView = (LineGraphicView) findViewById(R.id.lineGraphicView);
        lineGraphicView.setData(yList, xRawDatas, 8, 2);

        MyImageView imageView = (MyImageView) findViewById(R.id.image_view);
        imageView.setImageResource(R.drawable.checkbox_select);
    }

    @Override
    public void next(View view) {
        Log.e(TAG, "next: ");
        UIHelper.showActivity(getActivity(),PermissionActivity.class);
    }

    @Override
    public void pre(View view) {
        Log.e(TAG, "pre: ");
        UIHelper.showActivityLeftIn(getActivity(),PermissionActivity.class);

    }


}
