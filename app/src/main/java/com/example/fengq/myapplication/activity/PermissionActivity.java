package com.example.fengq.myapplication.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.example.fengq.myapplication.R;
import com.example.fengq.myapplication.base.BaseActivity;
import com.example.fengq.myapplication.tools.UIHelper;

/**
 * Created by fengq on 2017/5/16.
 */

public class PermissionActivity extends BaseActivity {

    private int PERMISSION_REQUEST_CODE_CAMERA = 0x10001;
    private static final String TAG = PermissionActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
    }

    @Override
    public void next(View view) {
        Log.e(TAG, "next: ");
        UIHelper.showActivity(getActivity(), DefineViewActivity.class);
    }

    @Override
    public void pre(View view) {
        Log.e(TAG, "pre: ");
        UIHelper.showActivity(getActivity(), DefineViewActivity.class);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_camera_permission:
                requestCameraPermission(new PermissionCallback() {
                    @Override
                    public void onGranted() {
                        Log.e(TAG, "onGranted");
                        Intent intent = new Intent(); //调用照相机
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivity(intent);
                    }

                    @Override
                    public void onDenied() {
                        Log.e(TAG, "onDenied");
                    }
                });
                break;
        }
    }

//    @NeedsPermission(Manifest.permission.CAMERA)
//    void startCamera() {
//        Log.e(TAG, "startCamera: 允许");
//        Intent intent = new Intent(); //调用照相机
//        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivity(intent);
//    }
//
//    @OnPermissionDenied(Manifest.permission.CAMERA)
//    void CameraPermissionDenied() {
//        Log.e(TAG, "CameraPermissionDenied: 禁止");
//    }


    public void CheckCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                Log.e(TAG, "CheckCameraPermission: PERMISSION_DENIED");
                requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE_CAMERA);
            } else {
                Log.e(TAG, "CheckCameraPermission: PERMISSION_GRANTED");
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE_CAMERA) {
            int grantResult = grantResults[0];
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "onRequestPermissionsResult: PERMISSION_GRANTED");
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
            } else {
                Log.e(TAG, "onRequestPermissionsResult: PERMISSION_DENIED");
            }
        }
    }
}
