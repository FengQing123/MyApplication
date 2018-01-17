package com.example.fengq.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.example.fengq.myapplication.R;
import com.example.fengq.myapplication.tools.PhoneUtil;


/**
 * Created by fengq on 2017/5/16.
 */

public class AnimationActivity extends Activity {

    private ImageView image_ic_launcher;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        image_ic_launcher = (ImageView) findViewById(R.id.image_ic_launcher);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_translate:
                if (i >= 4) {
                    i = 0;
                }
                TranslateAnimation translateAnimation = new TranslateAnimation(0, i * PhoneUtil.getScreenWidth() / 4, 0, 0);
                translateAnimation.setDuration(2000);
                translateAnimation.setFillAfter(true);// 动画终止时停留在最后一帧，不然会回到没有执行前的状态
                image_ic_launcher.startAnimation(translateAnimation);
                i++;
                break;
        }
    }
}
