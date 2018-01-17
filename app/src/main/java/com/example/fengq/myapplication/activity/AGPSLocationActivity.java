package com.example.fengq.myapplication.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import com.example.fengq.myapplication.R;
import com.example.fengq.myapplication.base.BaseActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by fengq on 2017/9/29.
 */

public class AGPSLocationActivity extends BaseActivity {

    private TextView myLocationText;
    private LocationManager locationManager;
    private String provider;
    private Location location;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agps_location);
        getLocation();
    }

    private void getLocation() {
        myLocationText = (TextView) findViewById(R.id.myLocationText);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // String provider = LocationManager.GPS_PROVIDER;

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
            updateWithNewLocation(location);
        }
    }


    private void updateWithNewLocation(Location location) {
        String latLongString;
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            float spe = location.getSpeed();// 速度
            float acc = location.getAccuracy();// 精度
            double alt = location.getAltitude();// 海拔
            float bea = location.getBearing();// 轴承
            long tim = location.getTime();// 返回UTC时间1970年1月1毫秒
            latLongString = "纬度:" + lat + "\n经度:" + lng + "\n精度：" + acc
                    + "\n速度：" + spe + "\n海拔：" + alt + "\n轴承：" + bea + "\n时间："
                    + sdf.format(tim);

            try {
                List<Address> addList = null;
                Geocoder ge = new Geocoder(this);
                addList = ge.getFromLocation(lat, lng, 1);
                if (addList != null && addList.size() > 0) {
                    for (int i = 0; i < addList.size(); i++) {
                        Address ad = addList.get(i);
                        latLongString += "\n";
                        latLongString += ad.getCountryName() + "-" + ad.getLocality();
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(locationListener);
                locationManager = null;
                if (locationListener != null) {
                    locationListener = null;
                }
            }
        } else {
            latLongString = "无法获取位置信息";
        }
        myLocationText.setText("您当前的位置是:\n" + latLongString);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void next(View view) {

    }

    @Override
    public void pre(View view) {

    }
}
