package com.example.fengq.myapplication.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.example.fengq.myapplication.R;

/**
 * Created by fengq on 2017/4/13.
 */

public class UIHelper {
    public static void showActivity(Context context, Class clz) {
        Intent intent = new Intent(context, clz);
        context.startActivity(intent);
    }

    public static void ToastMessage(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showActivityLeftIn(Activity context, Class clz) {
        Intent intent = new Intent(context, clz);
        context.startActivity(intent);
        LeftInAnimation(context);
    }

    /**
     * Activity 左边进 右边出
     *
     * @param activity
     */
    public static void LeftInAnimation(Activity activity) {
        int version = Build.VERSION.SDK_INT;
        if (version >= 5) {
            activity.overridePendingTransition(R.anim.left_in, android.R.anim.slide_out_right);
        }
    }

    /**
     * Activity 右边进 左边出
     *
     * @param activity
     */
    public static void RightInAnimation(Activity activity) {
        int version = Build.VERSION.SDK_INT;
        if (version >= 5) {
            activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }
}
