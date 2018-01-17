package com.example.fengq.myapplication.activity;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.fengq.myapplication.R;
import com.example.fengq.myapplication.jnis.Java2CJNI;
import com.example.fengq.myapplication.observer.BaseFr;
import com.example.fengq.myapplication.observer.OneF;
import com.example.fengq.myapplication.observer.ThreeF;
import com.example.fengq.myapplication.observer.TwoF;
import com.example.fengq.myapplication.tools.FileUtil;
import com.example.fengq.myapplication.tools.UIHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean isCommunication = false;
    private File mSdCardDir = Environment.getExternalStorageDirectory();
    private List<BaseFr> frList = new ArrayList<>();

    private AudioManager audioManager;
    private MyOnAudioFocusChangeListener listener;

    private LocationManager locationManager;
    /* GPS Constant Permission */
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;

    /* Position */
    private static final int MINIMUM_TIME = 5000;  // 5s
    private static final int MINIMUM_DISTANCE = 50; // 50m


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        isCommunication = true;
        Log.e("-----------", "onCreate");

        OneF oneF = new OneF();
        TwoF twoF = new TwoF();
        ThreeF threeF = new ThreeF();

        frList.add(oneF);
        frList.add(twoF);
        frList.add(threeF);


        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        listener = new MyOnAudioFocusChangeListener();
        int result = audioManager.requestAudioFocus(listener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_REQUEST_GRANTED);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.e(TAG, "onCreate: requestAudioFocus successfully");
        } else {
            Log.e(TAG, "onCreate: requestAudioFocus failed");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("-----------", "onResume");
//        getLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioManager.abandonAudioFocus(listener);

    }

//    public void getLocation() {
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        criteria.setAltitudeRequired(false);
//        criteria.setCostAllowed(true);
//        criteria.setPowerRequirement(Criteria.POWER_LOW);
//        String provider = locationManager.getBestProvider(criteria, true);
//
//        // API 23: we have to check if ACCESS_FINE_LOCATION and/or ACCESS_COARSE_LOCATION permission are granted
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//            // No one provider activated: prompt GPS
//            if (provider == null || provider.equals("")) {
//                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//            }
//
//            // At least one provider activated. Get the coordinates
//            switch (provider) {
//                case "passive":
//                    locationManager.requestLocationUpdates(provider, MINIMUM_TIME, MINIMUM_DISTANCE, locationListener);
//                    Location location = locationManager.getLastKnownLocation(provider);
//                    updateWithNewLocation("passive", location);
//                    break;
//                case "network":
//                    locationManager.requestLocationUpdates(provider, MINIMUM_TIME, MINIMUM_DISTANCE, locationListener);
//                    location = locationManager.getLastKnownLocation(provider);
//                    updateWithNewLocation("network", location);
//                    break;
//                case "gps":
//                    locationManager.requestLocationUpdates(provider, MINIMUM_TIME, MINIMUM_DISTANCE, locationListener);
//                    location = locationManager.getLastKnownLocation(provider);
//                    updateWithNewLocation("gps", location);
//                    break;
//            }
//
//            // One or both permissions are denied.
//        } else {
//
//            // The ACCESS_COARSE_LOCATION is denied, then I request it and manage the result in
//            // onRequestPermissionsResult() using the constant MY_PERMISSION_ACCESS_FINE_LOCATION
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_ACCESS_COARSE_LOCATION);
//            }
//            // The ACCESS_FINE_LOCATION is denied, then I request it and manage the result in
//            // onRequestPermissionsResult() using the constant MY_PERMISSION_ACCESS_FINE_LOCATION
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION);
//            }
//        }
//
//    }

//    private LocationListener locationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//            Log.e("Location", "onLocationChanged");
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            Log.e("Location", "onStatusChanged");
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//            Log.e("Location", "onProviderEnabled");
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//            Log.e("Location", "onProviderDisabled");
//        }
//    };

    private void updateWithNewLocation(String s, Location location) {
        if (location != null) {
            float speed = location.getSpeed();
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            UIHelper.ToastMessage(this, "provide==" + s + ",speed=" + speed + ",lat=" + lat + ",lng=" + lng);
        } else {
            UIHelper.ToastMessage(this, "location is null");
        }
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_screen_adaptation:
                Log.e(TAG, "onCreate: isMusicActive=" + audioManager.isMusicActive());
                UIHelper.ToastMessage(this, "isCommunition=" + isCommunication);
                UIHelper.showActivity(this, ScreenAdaptationActivity.class);
                break;
            case R.id.btn_okhttp:
                UIHelper.showActivity(this, OkHttpTestActivity.class);
                break;
            case R.id.btn_retrofit:
                UIHelper.showActivity(this, RetrofitTestActivity.class);
                break;
            case R.id.btn_animation:
                UIHelper.showActivity(this, AnimationActivity.class);
                break;
            case R.id.btn_permission:
                UIHelper.showActivity(this, PermissionActivity.class);
                break;
            case R.id.btn_compress:
                String filePath = mSdCardDir.getAbsolutePath() + "/HHHHH/qq.jpg";
                try {
                    Log.e(TAG, "onClick: filesize=" + FileUtil.getFileSizesWithUnit(filePath));
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                Bitmap bitmap1 = ImageUtil.FileToBitmap(mSdCardDir.getAbsolutePath() + "/HHHHH/qq.jpg");
//                Bitmap bitmap = ImageUtil.getBitmap(mSdCardDir.getAbsolutePath() + "/HHHHH/qq.jpg");
//                byte[] bytes = ImageUtil.compressBitmap(bitmap, 5);
//                ImageUtil.Bytes2File(bytes, mSdCardDir.getAbsolutePath() + "/HHHHH/ww3.jpg");
                break;
            case R.id.btn_ndk:
                String result = new Java2CJNI().java2C();
                UIHelper.ToastMessage(this, result);
                break;
            case R.id.btn_barrage:
                UIHelper.showActivity(this, BarrageActivity.class);
                break;
            case R.id.btn_MPAndroid_chart:
                UIHelper.showActivity(this, MPAndroidChartActivity.class);
                break;
            case R.id.btn_view:
                UIHelper.showActivity(this, RadioCheckBoxActivity.class);
                break;
            case R.id.btn_blueTooth:
                UIHelper.showActivity(this, BlueToothActivity.class);
                break;
            case R.id.btn_define_view:
                UIHelper.showActivity(this, DefineViewActivity.class);
                break;
            case R.id.btn_define_chart:
                UIHelper.showActivity(this, DefineChartActivity.class);
                break;
            case R.id.btn_update:
                notifyAlls();
                break;
            case R.id.btn_location:
                UIHelper.showActivity(this, PhoneLocationActivity.class);
                break;
            case R.id.btn_learn_dp:
                UIHelper.showActivity(this, ScreenLearnDPActivity.class);
                break;
        }
    }

    public void notifyAlls() {
        for (BaseFr f : frList) {
            f.update();
        }
    }


    public class MyOnAudioFocusChangeListener implements AudioManager.OnAudioFocusChangeListener {

        @Override
        public void onAudioFocusChange(int focusChange) {
            Log.e(TAG, "requestAudioFocus: focusChange=" + focusChange);
        }
    }

}
