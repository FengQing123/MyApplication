package com.example.fengq.myapplication.base;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by fengq on 2017/5/16.
 */

@RuntimePermissions
public abstract class BaseActivity extends AppCompatActivity {

    private PermissionCallback permissionCallback;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGesture();
    }

    public BaseActivity getActivity() {
        return this;
    }


    public void initGesture() {
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //判断竖直方向移动的大小
                if (Math.abs(e1.getRawY() - e2.getRawY()) > 100) {
                    //Toast.makeText(getApplicationContext(), "动作不合法", 0).show();
                    return true;
                }
                if (Math.abs(velocityX) < 150) {
                    //Toast.makeText(getApplicationContext(), "移动的太慢", 0).show();
                    return true;
                }

                if ((e1.getRawX() - e2.getRawX()) > 200) {// 表示 向右滑动表示下一页
                    //显示下一页
                    next(null);
                    return true;
                }

                if ((e2.getRawX() - e1.getRawX()) > 200) {  //向左滑动 表示 上一页
                    //显示上一页
                    pre(null);
                    return true;//消费掉当前事件  不让当前事件继续向下传递
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    /**
     * 下一个页面
     *
     * @param view
     */
    public abstract void next(View view);

    /**
     * 上一个页面
     *
     * @param view
     */
    public abstract void pre(View view);

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaseActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 请求相机权限
     *
     * @param callback
     */
    protected void requestCameraPermission(PermissionCallback callback) {
        this.permissionCallback = callback;
        BaseActivityPermissionsDispatcher.HandleCameraPermissionWithCheck(this);
    }


    @NeedsPermission(Manifest.permission.CAMERA)
    void HandleCameraPermission() {
        if (permissionCallback != null)
            permissionCallback.onGranted();
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void DeniedCameraPermission() {
        if (permissionCallback != null)
            permissionCallback.onDenied();
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void OnCameraNeverAskAgain() {
        showDialog("[相机]");
    }


    public interface PermissionCallback {
        void onGranted();

        void onDenied();
    }

    public void showDialog(String permission) {
        new AlertDialog.Builder(this)
                .setTitle("权限申请")
                .setMessage("在设置-应用-MyApplication-权限中开启" + permission + "权限，以正常使用其功能")
                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (permissionCallback != null) {
                            permissionCallback.onDenied();
                        }
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

}
