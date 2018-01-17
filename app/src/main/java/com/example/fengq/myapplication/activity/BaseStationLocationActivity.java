package com.example.fengq.myapplication.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.fengq.myapplication.R;
import com.example.fengq.myapplication.base.BaseActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by fengq on 2017/9/29.
 */

public class BaseStationLocationActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_station_location);
        onBtnClick();
    }


    /**
     * 基站信息结构体
     */
    public class SCell {
        public int MCC;
        public int MNC;
        public int LAC;
        public int CID;
    }

    /**
     * 经纬度信息结构体
     */
    public class SItude {
        public String latitude;
        public String longitude;
    }

    /**
     * 按钮点击回调函数
     */
    private void onBtnClick() {
        /** 弹出一个等待状态的框 */
        ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在获取中...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();

        try {
            /** 获取基站数据 */
            SCell cell = getCellInfo();

            /** 根据基站数据获取经纬度 */
            SItude itude = getItude(cell);

            /** 获取地理位置 */
            String location = getLocation(itude);

            /** 显示结果 */
            showResult(cell, location);

            /** 关闭对话框 */
            mProgressDialog.dismiss();
        } catch (Exception e) {
            /** 关闭对话框 */
            mProgressDialog.dismiss();
            /** 显示错误 */
            TextView cellText = (TextView) findViewById(R.id.cellText);
            cellText.setText(e.getMessage());
            Log.e("Error", e.getMessage());
        }
    }

    /**
     * 获取基站信息
     *
     * @throws Exception
     */
    private SCell getCellInfo() throws Exception {
        SCell cell = new SCell();

        /** 调用API获取基站信息 */
        TelephonyManager mTelNet = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        GsmCellLocation location = (GsmCellLocation) mTelNet.getCellLocation();
        if (location == null)
            throw new Exception("获取基站信息失败");

        String operator = mTelNet.getNetworkOperator();
        int mcc = Integer.parseInt(operator.substring(0, 3));
        int mnc = Integer.parseInt(operator.substring(3));
        int cid = location.getCid();
        int lac = location.getLac();

        /** 将获得的数据放到结构体中 */
        cell.MCC = mcc;
        cell.MNC = mnc;
        cell.LAC = lac;
        cell.CID = cid;

        return cell;
    }

    /**
     * 获取经纬度
     *
     * @throws Exception
     */
    private SItude getItude(SCell cell) throws Exception {
        SItude itude = new SItude();
        String url = "http://www.google.com/loc/json";
        try {
            /** 构造POST的JSON数据 */
            JSONObject holder = new JSONObject();
            holder.put("version", "1.1.0");
            holder.put("host", "maps.google.com");
            holder.put("address_language", "zh_CN");
            holder.put("request_address", true);
            holder.put("radio_type", "gsm");
            holder.put("carrier", "HTC");

            JSONObject tower = new JSONObject();
            tower.put("mobile_country_code", cell.MCC);
            tower.put("mobile_network_code", cell.MNC);
            tower.put("cell_id", cell.CID);
            tower.put("location_area_code", cell.LAC);

            JSONArray towerarray = new JSONArray();
            towerarray.put(tower);
            holder.put("cell_towers", towerarray);

            String result = postDownloadJson(url, holder.toString());

            /** 解析返回的JSON数据获得经纬度 */
            JSONObject json = new JSONObject(result);
            JSONObject subjosn = new JSONObject(json.getString("location"));

            itude.latitude = subjosn.getString("latitude");
            itude.longitude = subjosn.getString("longitude");

            Log.i("Itude", itude.latitude + itude.longitude);

        } catch (Exception e) {
            Log.e(e.getMessage(), e.toString());
            throw new Exception("获取经纬度出现错误:" + e.getMessage());
        }
        return itude;
    }

    /**
     * 获取地理位置
     *
     * @throws Exception
     */
    private String getLocation(SItude itude) throws Exception {
        String resultString = "";
        try {
            /** 这里采用get方法，直接将参数加到URL上 */
            String urlString = String.format("http://maps.google.cn/maps/geo?key=abcdefg&q=%s,%s", itude.latitude, itude.longitude);
            Log.i("URL", urlString);

            resultString = getJsonByInternet(urlString);

            /** 解析JSON数据，获得物理地址 */
            if (resultString != null && resultString.length() > 0) {
                JSONObject jsonobject = new JSONObject(resultString);
                JSONArray jsonArray = new JSONArray(jsonobject.get("Placemark").toString());
                resultString = "";
                for (int i = 0; i < jsonArray.length(); i++) {
                    resultString = jsonArray.getJSONObject(i).getString("address");
                }
            }
        } catch (Exception e) {
            throw new Exception("获取物理位置出现错误:" + e.getMessage());
        }

        return resultString;
    }

    /**
     * 显示结果
     */
    private void showResult(SCell cell, String location) {
        TextView cellText = (TextView) findViewById(R.id.cellText);
        cellText.setText(String.format("基站信息：mcc:%d, mnc:%d, lac:%d, cid:%d",
                cell.MCC, cell.MNC, cell.LAC, cell.CID));

        TextView locationText = (TextView) findViewById(R.id.lacationText);
        locationText.setText("物理位置：" + location);
    }


    public static String getJsonByInternet(String path) {
        try {
            URL url = new URL(path.trim());
            //打开连接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if (200 == urlConnection.getResponseCode()) {
                //得到输入流
                InputStream is = urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while (-1 != (len = is.read(buffer))) {
                    baos.write(buffer, 0, len);
                    baos.flush();
                }
                return baos.toString("utf-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String postDownloadJson(String path, String post) {
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
            // conn.setReadTimeout(2000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // 发送请求参数
            printWriter.write(post);//post的参数 xx=xx&yy=yy
            // flush输出流的缓冲
            printWriter.flush();
            //开始获取数据
            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            byte[] arr = new byte[1024];
            while ((len = bis.read(arr)) != -1) {
                bos.write(arr, 0, len);
                bos.flush();
            }
            bos.close();
            return bos.toString("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void next(View view) {

    }

    @Override
    public void pre(View view) {

    }
}
