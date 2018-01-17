package com.example.fengq.myapplication.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.fengq.myapplication.R;
import com.example.fengq.myapplication.config.Constants;
import com.example.fengq.myapplication.listeners.BlueToothReceiver;
import com.example.fengq.myapplication.tools.FormatUtil;
import com.example.fengq.myapplication.tools.StringUtil;
import com.example.fengq.myapplication.tools.UIHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 蓝牙演示
 * <p>
 * Created by fengq on 2017/5/3.
 */

public class BlueToothActivity extends Activity {
    

    private String one_bracelet = "00:0B:57:1F:C0:4E";
    private String two_bracelet = "00:0B:57:5C:3F:B3";

    private byte BRACELET_DATA_HEAD = 0x68;//手环数据包头
    private byte BRACELET_DATA_END = 0x16;//手环数据包尾
    private byte BRACELET_POWER = (byte) 0x83;//获取手环电量
    private byte BRACELET_FUCTION_SWITCH = (byte) 0x85;//功能开关
    private byte BRACELET_HEARTRATE = (byte) 0x86;//手环心率(步数、里程、消耗能量、步速)
    private byte BRACELET_REALTIME_HEARTRATE = 0x00;//手环实时数据
    private byte BRACELET_OPEN_HEARTRATE = 0x01;//打开手环心率测试
    private byte BRACELET_CLOSE_HEARTRATE = 0x02;//关闭手环心率测试
    private byte BRACELET_RAISE_HAND_BRIGHT = 0x06;//手环抬手亮屏
    private byte BRACELET_TURN_WRIST = 0x07;//手环翻腕切屏
    private byte BRACELET_SEND_ALL_DATA = (byte) 0xa6;//手环上传全天数据
    private byte BRACELET_SET_PHOTO = (byte) 0x8d;//手环设置拍照应答
    private byte BRACELET_PHOTO = (byte) 0x8e;//手环启动手机快门
    private byte BRACELET_EVENT_TOTAL_DATA = (byte) 0xa2;//手环事件总数据

    private byte[] firstByteArray = new byte[20];

    private TextView tv_show;
    private List<BluetoothDevice> blueList = new ArrayList<>();
    private BluetoothDevice device;
    private boolean realTimeHeart = true;

    private String TAG = BlueToothActivity.class.getSimpleName();
    private BluetoothAdapter bluetoothAdapter;
    private BlueToothReceiver blueToothReceiver;

    private boolean mScanning;
    private Handler mHandler = new Handler();

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

//    private LeDeviceListAdapter mLeDeviceListAdapter;

    private BluetoothGatt mBluetoothGatt; // GATT profile的封装

    BluetoothGattCharacteristic write_characteristic = null;// 写特征
    BluetoothGattCharacteristic read_characteristic = null;// 读特征
    public static UUID READ_UUID = null;// 读特征UUID
    public static UUID WRITE_UUID = null;// 写特征UUID
    public static UUID SERVICE_UUID = null;// 服务UUID

    BluetoothGattCharacteristic heart_read_characteristic = null;// 心率写特征
    BluetoothGattCharacteristic heart_write_characteristic = null;// 心率读特征
    public static UUID HEART_READ_UUID = null;//心率读特征UUID
    public static UUID HEART_WRITE_UUID = null;//心率写特征UUID
    public static UUID HEART_SERVICE_UUID = null;//心率服务UUID


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        tv_show = (TextView) findViewById(R.id.tv_show);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        blueToothReceiver = new BlueToothReceiver(bluetoothAdapter);
        if (bluetoothAdapter == null) {
            Log.e(TAG, "Device does not support Bluetooth");
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                //蓝牙未开启，则启动蓝牙
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT);//REQUEST_ENABLE_BT必须大于0
            } else {
                scanLeDevice(true);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                scanLeDevice(true);
                break;
            case RESULT_CANCELED:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            close();
            unregisterReceiver(blueToothReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    bluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            bluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            bluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    Log.e(TAG, "deviceName=" + device.getName() + ",deviceAddress=" + device.getAddress());
                    blueList.add(device);
                }
            });
        }
    };

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.e(TAG, "Connected to GATT server.");
                Log.e(TAG, "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.e(TAG, "Disconnected from GATT server.");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // 获取服务UUID
                List<BluetoothGattService> BluetoothGattServices = mBluetoothGatt.getServices();

                BluetoothGattService OXFFFO_SERVICE = BluetoothGattServices.get(1);//服务：0xfff0
                BluetoothGattService OX180D_SERVICE = BluetoothGattServices.get(2);//服务：0X180D

                if (OXFFFO_SERVICE != null) {
                    read_characteristic = OXFFFO_SERVICE.getCharacteristics().get(0);//0xfff1
                    write_characteristic = OXFFFO_SERVICE.getCharacteristics().get(1);//0xfff2
                    if (read_characteristic != null) {
                        READ_UUID = read_characteristic.getUuid();//0000fff1-0000-1000-8000-00805f9b34fb
                    }
                    if (write_characteristic != null) {
                        WRITE_UUID = write_characteristic.getUuid();//0000fff2-0000-1000-8000-00805f9b34fb
                    }
                    SERVICE_UUID = OXFFFO_SERVICE.getUuid();//0000fff0-0000-1000-8000-00805f9b34fb
                }

                if (OX180D_SERVICE != null) {
                    heart_read_characteristic = OX180D_SERVICE.getCharacteristics().get(0);//0x2A37
                    heart_write_characteristic = OX180D_SERVICE.getCharacteristics().get(1);//0x2A38
                    if (heart_read_characteristic != null) {
                        HEART_READ_UUID = heart_read_characteristic.getUuid();//00002a37-0000-1000-8000-00805f9b34fb
                    }
                    if (heart_write_characteristic != null) {
                        HEART_WRITE_UUID = heart_write_characteristic.getUuid();//00002a38-0000-1000-8000-00805f9b34fb
                    }
                    HEART_SERVICE_UUID = OX180D_SERVICE.getUuid();//0000180d-0000-1000-8000-00805f9b34fb
                }

                // 监听读特征
                setCharacteristicNotification(read_characteristic, true);
                mBluetoothGatt.readCharacteristic(read_characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            byte[] aa = characteristic.getValue();
            StringBuffer origStr = new StringBuffer();
            StringBuffer str = new StringBuffer();
            for (int i = 0; i < aa.length; i++) {
                str.append(Integer.toHexString(aa[i]) + ",");
                origStr.append(aa[i] + ",");
            }
            Log.e(TAG, "onCharacteristicChanged: str=" + str + "--------origStr=" + origStr);
            if (aa.length == 20 && aa[0] == BRACELET_DATA_HEAD) {
                firstByteArray = aa;
            } else if (aa[0] != BRACELET_DATA_HEAD && aa[aa.length - 1] == BRACELET_DATA_END) {
                byte[] data = new byte[100];
                for (int i = 0; i < firstByteArray.length; i++) {
                    data[i] = firstByteArray[i];
                }
                for (int i = 0; i < aa.length; i++) {
                    data[firstByteArray.length + i] = aa[i];
                }
                processReadData(data);
            } else {
                processReadData(aa);
            }

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                byte[] data = characteristic.getValue();
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < data.length; i++) {
                    stringBuffer.append(data[i] + ",");
                }
                Log.e(TAG, "onCharacteristicRead: 返回值=" + stringBuffer);
            } else {
                Log.e(TAG, "onCharacteristicRead: gatt_failure");
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "onCharacteristicWrite: success");
            } else {
                Log.e(TAG, "onCharacteristicWrite: failure");
            }
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "onMtuChanged: success--mtu=" + mtu);
            } else {
                Log.e(TAG, "onMtuChanged: failure");
            }
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "onReadRemoteRssi: success---rssi=" + rssi);
            } else {
                Log.e(TAG, "onReadRemoteRssi: failure");
            }
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "onReliableWriteCompleted: success");
            } else {
                Log.e(TAG, "onReliableWriteCompleted: failure");
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "onDescriptorWrite: success");
            } else {
                Log.e(TAG, "onDescriptorWrite: failure");
            }
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "onDescriptorRead: success");
            } else {
                Log.e(TAG, "onDescriptorRead: failure");
            }
        }
    };

    /**
     * 设置特征监听eeee
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (bluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        BluetoothGattDescriptor descriptor = characteristic.getDescriptors().get(0);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(descriptor);

    }

    public void close() {
        if (bluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.disconnect();
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
                boolean flag = false;
                for (BluetoothDevice device : blueList) {
                    if (device.getAddress().equals(two_bracelet)) {
                        this.device = device;
                        mBluetoothGatt = device.connectGatt(BlueToothActivity.this, false, mGattCallback);
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    UIHelper.ToastMessage(BlueToothActivity.this, "没有设备");
                }
                break;
            case R.id.btn_disconnect:
                close();
                break;
            case R.id.btn_open_heartRate:
                byte[] data = new byte[]{0x68, 0x06, 0x01, 0x00, 0x01, 0x70, 0x16};//回复68,ffffff86,1,0,1,fffffff0,16,
                write(data);
                break;
            case R.id.btn_close_heartRate:
                byte[] data1 = new byte[]{0x68, 0x06, 0x01, 0x00, 0x02, 0x71, 0x16};//回复68,ffffff86,1,0,2,fffffff1,16,
                write(data1);
                break;
            case R.id.btn_get_heartRate:
                byte[] data2 = new byte[]{0x68, 0x06, 0x01, 0x00, 0x00, 0x6f, 0x16};//回复68,ffffff86,f,0,0,43,34,0,0,0,22,0,0,0,51,0,0,0,0,ffffffe7,再回复16,
                write(data2);
                break;
            case R.id.btn_get_real_time_heartRate:
                if (realTimeHeart) {
                    //获取实时心率数据，如果没有打开则返回0,0，打开的话返回0,4b,
                    setCharacteristicNotification(heart_read_characteristic, realTimeHeart);
                    realTimeHeart = false;
                } else {
                    setCharacteristicNotification(heart_read_characteristic, realTimeHeart);
                    realTimeHeart = true;
                }
                break;
            case R.id.btn_get_electricity:
                byte[] data3 = new byte[]{0x68, 0x03, 0x00, 0x00, 0x6b, 0x16};//回复68,ffffff83,1,0,64,50,16,
                write(data3);
                break;
            case R.id.btn_set_steps_param:
                //设置计步参数：身高(173)、体重(60)、性别(男-0，女-1)、年龄(23)顺序,这里的校验码不是固定的，需要计算
                byte[] data4 = new byte[]{0x68, 0x04, 0x04, 0x00, (byte) 0xAD, 0x3c, 0x00, 0x17, 0x70, 0x16};//回复
                write(data4);
                break;
            case R.id.btn_set_time:
//                byte[] data5 = new byte[]{0x68, 0x20, 0x04, 0x00, 0x28, 0x24, 0x4b, 0x59, 0x7c, 0x16};
//                byte[] data5 = new byte[]{0x68, 0x20, 0x04, 0x00, 0x08, 0x2e, 0x4b, 0x59, 0x66, 0x16};//能正确算出，但是差8小时
//                byte[] data5 = new byte[]{0x68, 0x20, 0x04, 0x00, 0x38, (byte) 0x99, 0x4d, 0x59, 0x03, 0x16};
//                byte[] data5 = new byte[]{0x68, 0x20, 0x04, 0x00, 0x59, 0x4b, (byte) 0xa6, 0x00, (byte) 0xd6, 0x16};
//                write(data5);
                String time = getStringTimeStampHex("2017", "08", "08", "18", "00");
                if (time != null) {
                    StringBuffer stringBuffer = new StringBuffer();
                    StringBuffer totalData = stringBuffer.append("68").append("20").append("04").append("00").append(time);
                    String checkCode = FormatUtil.makeChecksum(totalData.toString());
                    String datas = StringUtil.stringAppend(StringUtil.stringAppend(totalData.toString(), checkCode), "16");
                    byte[] data5 = FormatUtil.hexToBytes(datas);
                    write(data5);
                }
                break;
            case R.id.btn_get_turn_wrist_info:
                byte[] data6 = new byte[]{0x68, 0x05, 0x02, 0x00, 0x01, 0x07, 0x77, 0x16};
                write(data6);
                break;
            case R.id.btn_set_turn_wrist_info:
                //关0，开1
                byte[] data7 = new byte[]{0x68, 0x05, 0x03, 0x00, 0x00, 0x07, 0x01, 0x78, 0x16};//开启
//                byte[] data7 = new byte[]{0x68, 0x05, 0x03, 0x00, 0x00, 0x07, 0x00, 0x77, 0x16};//关闭
                write(data7);
                break;
            case R.id.btn_get_raise_hand_info:
                byte[] data8 = new byte[]{0x68, 0x05, 0x02, 0x00, 0x01, 0x06, 0x76, 0x16};
                write(data8);
                break;
            case R.id.btn_set_raise_hand_info:
                byte[] data9 = new byte[]{0x68, 0x05, 0x03, 0x00, 0x00, 0x06, 0x01, 0x77, 0x16};//开启
//                byte[] data9 = new byte[]{0x68, 0x05, 0x03, 0x00, 0x00, 0x06, 0x00, 0x76, 0x16};//关闭
                write(data9);
                break;
            case R.id.btn_set_open_photo:
                byte[] data10 = new byte[]{0x68, 0x0d, 0x01, 0x00, 0x00, 0x76, 0x16};
                write(data10);
                break;
            case R.id.btn_set_close_photo:
                byte[] data11 = new byte[]{0x68, 0x0d, 0x01, 0x00, 0x01, 0x77, 0x16};
                write(data11);
                break;
            case R.id.btn_send_date:
//                byte[] data12 = new byte[]{0x68, 0x26, 0x04, 0x00, 0x04, 0x07, 0x11, 0x00, -82, 0x16};
//                write(data12);

//                StringBuffer sb = new StringBuffer("68260400");
//                sb.append("030711");
//                sb.append("01");
//                String checkCodeNum = FormatUtil.makeChecksum(sb.toString());
//                sb.append(checkCodeNum).append("16");
//                byte[] bytes = FormatUtil.hexToBytes(sb.toString());
//                write(bytes);
                break;
            case R.id.btn_request_params:
                StringBuffer sb = new StringBuffer("68020800");
                sb.append("0000010203040506");
                String checkCodeNum = FormatUtil.makeChecksum(sb.toString());
                sb.append(checkCodeNum).append("16");
                byte[] bytes = FormatUtil.hexToBytes(sb.toString());
                write(bytes);
                break;
        }
    }

    private void write(byte[] bb) {
        Log.e(TAG, "write: -----------------");
        write_characteristic.setValue(bb);
        write_characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        mBluetoothGatt.writeCharacteristic(write_characteristic);
    }


    private void processReadData(byte[] datas) {
        if (datas.length <= 0) {
            return;
        }
        if (datas[0] == BRACELET_DATA_HEAD) {
            if (datas[1] == BRACELET_POWER) {
                Log.e(TAG, "processReadData: 手环电量=" + datas[4]);
            } else if (datas[1] == BRACELET_HEARTRATE) {
                if (datas[4] == BRACELET_REALTIME_HEARTRATE) {
                    int nowHeartRate = datas[5] & 0xff;
                    Log.e(TAG, "processReadData: 当前心率数据=" + nowHeartRate);//最大值220，超过则用220表示

                    int nowSteps = (datas[6] & 0xff) | ((datas[7] & 0xff) << 8) | ((datas[8] & 0xff) << 16) | ((datas[9] & 0xff) << 24);
                    Log.e(TAG, "processReadData: 当前步数=" + nowSteps + "步");

                    int nowDistance = (datas[10] & 0xff) | ((datas[11] & 0xff) << 8) | ((datas[12] & 0xff) << 16) | ((datas[13] & 0xff) << 24);
                    Log.e(TAG, "processReadData: 当前里程=" + nowDistance + "米");

                    int nowConsumeCal = (datas[14] & 0xff) | ((datas[15] & 0xff) << 8) | ((datas[16] & 0xff) << 16) | ((datas[17] & 0xff) << 24);
                    Log.e(TAG, "processReadData: 当前消耗热量=" + nowConsumeCal + "Kcal");

                    int nowPace = (datas[18] & 0xff);
                    Log.e(TAG, "processReadData: 当前步速=" + nowPace + "步/秒");

                } else if (datas[4] == BRACELET_OPEN_HEARTRATE) {
                    Log.e(TAG, "processReadData: 打开心率" + datas[4]);
                } else if (datas[4] == BRACELET_CLOSE_HEARTRATE) {
                    Log.e(TAG, "processReadData: 关闭心率" + datas[4]);
                }
            } else if (datas[1] == BRACELET_FUCTION_SWITCH) {
                if (datas[4] == 0x01) {//读取操作
                    if (datas[5] == BRACELET_TURN_WRIST) {
                        if (datas[6] == 0) {
                            Log.e(TAG, "processReadData: 翻腕切屏=关闭状态" + datas[6]);
                        } else if (datas[6] == 1) {
                            Log.e(TAG, "processReadData: 翻腕切屏=开启状态" + datas[6]);
                        }
                    } else if (datas[5] == BRACELET_RAISE_HAND_BRIGHT) {
                        if (datas[6] == 0) {
                            Log.e(TAG, "processReadData: 抬手亮屏=关闭状态" + datas[6]);
                        } else if (datas[6] == 1) {
                            Log.e(TAG, "processReadData: 抬手亮屏=开启状态" + datas[6]);
                        }
                    }
                } else if (datas[4] == 0x00) {//设置操作
                    if (datas[5] == BRACELET_TURN_WRIST) {
                        Log.e(TAG, "processReadData: 翻屏设置操作成功");
                    } else if (datas[5] == BRACELET_RAISE_HAND_BRIGHT) {
                        Log.e(TAG, "processReadData: 抬手亮屏操作成功");
                    }
                }
            } else if (datas[1] == BRACELET_SEND_ALL_DATA) {  //手环上传全天数据
                if (datas[7] == 0) {//全天总数据
                    int year = (datas[6] & 0xff);
                    int month = (datas[5] & 0xff);
                    int day = (datas[4] & 0xff);
                    Log.e(TAG, "processReadData: 日期=" + year + "/" + month + "/" + day);

                    int totalStep = (datas[8] & 0xff) | ((datas[9] & 0xff) << 8) | ((datas[10] & 0xff) << 16) | ((datas[11] & 0xff) << 24);
                    Log.e(TAG, "processReadData: 总步数=" + totalStep);

                    int totalCal = (datas[12] & 0xff) | ((datas[13] & 0xff) << 8) | ((datas[14] & 0xff) << 16) | ((datas[15] & 0xff) << 24);
                    Log.e(TAG, "processReadData: 总热量=" + totalCal + "Kcal");

                    int totalDistance = (datas[16] & 0xff) | ((datas[17] & 0xff) << 8) | ((datas[18] & 0xff) << 16) | ((datas[19] & 0xff) << 24);
                    Log.e(TAG, "processReadData: 总里程=" + totalDistance + "米");

                    int activityTime = (datas[20] & 0xff) | ((datas[21] & 0xff) << 8) | ((datas[22] & 0xff) << 16) | ((datas[23] & 0xff) << 24);
                    Log.e(TAG, "processReadData: 活动时间" + activityTime + "分钟");

                    int activityConsumeCal = (datas[24] & 0xff) | ((datas[25] & 0xff) << 8) | ((datas[26] & 0xff) << 16) | ((datas[27] & 0xff) << 24);
                    Log.e(TAG, "processReadData: 活动消耗热量=" + activityConsumeCal + "Kcal");

                    int meditationTime = (datas[28] & 0xff) | ((datas[29] & 0xff) << 8) | ((datas[30] & 0xff) << 16) | ((datas[31] & 0xff) << 24);
                    Log.e(TAG, "processReadData: 静坐时间=" + meditationTime + "分钟");

                    int meditationConsumeCal = (datas[32] & 0xff) | ((datas[33] & 0xff) << 8) | ((datas[34] & 0xff) << 16) | ((datas[35] & 0xff) << 24);
                    Log.e(TAG, "processReadData: 静坐消耗热量" + meditationConsumeCal + "Kcal");

                    StringBuffer sb = new StringBuffer("68260400");
                    String dayHex = FormatUtil.integer2Hex(datas[4]);
                    String monthHex = FormatUtil.integer2Hex(datas[5]);
                    String yearHex = FormatUtil.integer2Hex(datas[6]);
                    sb.append(dayHex).append(monthHex).append(yearHex).append("00");
                    String checkCodeNum = FormatUtil.makeChecksum(sb.toString());
                    sb.append(checkCodeNum).append("16");
                    byte[] bytes = FormatUtil.hexToBytes(sb.toString());
//                    write(bytes);
//                    byte[] bytes = new byte[]{0x68, 0x26, 0x02, 0x00, datas[4], datas[5], datas[6], 0x00,checkCodeNum,};
                }
            } else if (datas[1] == BRACELET_SET_PHOTO) {
                if (datas[4] == 0) {
                    Log.e(TAG, "processReadData: 手环拍照启动成功");
                } else if (datas[4] == 1) {
                    Log.e(TAG, "processReadData: 手环拍照关闭成功");
                }
            } else if (datas[1] == BRACELET_PHOTO) {
                Log.e(TAG, "processReadData: 手环启动手机快门");
//                byte[] bytes = new byte[]{0x68, 0x0e, 0x00, 0x00, 0x76, 0x16};
//                write(bytes);
            } else if (datas[1] == BRACELET_EVENT_TOTAL_DATA) {
                if (datas[12] == 1) {
                    Log.e(TAG, "processReadData: 事件总数据");
                    StringBuffer sb = new StringBuffer("68220400");
                    sb.append(FormatUtil.integer2Hex(datas[4]));
                    sb.append(FormatUtil.integer2Hex(datas[5]));
                    sb.append(FormatUtil.integer2Hex(datas[6]));
                    sb.append(FormatUtil.integer2Hex(datas[7]));
                    sb.append(FormatUtil.integer2Hex(datas[8]));
                    sb.append(FormatUtil.integer2Hex(datas[9]));
                    sb.append(FormatUtil.integer2Hex(datas[10]));
                    sb.append(FormatUtil.integer2Hex(datas[11]));
                    sb.append(FormatUtil.integer2Hex(datas[12]));
                    String checkCodeNum = FormatUtil.makeChecksum(sb.toString());
                    byte[] bytes = FormatUtil.hexToBytes((sb.append(checkCodeNum).append("16")).toString());
//                    write(bytes);
                } else if (datas[12] == 2) {
                    Log.e(TAG, "processReadData: 事件每分钟心率数据");
                    StringBuffer sb = new StringBuffer("68220400");
                    sb.append(FormatUtil.integer2Hex(datas[4]));
                    sb.append(FormatUtil.integer2Hex(datas[5]));
                    sb.append(FormatUtil.integer2Hex(datas[6]));
                    sb.append(FormatUtil.integer2Hex(datas[7]));
                    sb.append(FormatUtil.integer2Hex(datas[8]));
                    sb.append(FormatUtil.integer2Hex(datas[9]));
                    sb.append(FormatUtil.integer2Hex(datas[10]));
                    sb.append(FormatUtil.integer2Hex(datas[11]));
                    sb.append(FormatUtil.integer2Hex(datas[12]));
                    sb.append(FormatUtil.integer2Hex(datas[15]));
                    sb.append(FormatUtil.integer2Hex(datas[16]));
                    String checkCodeNum = FormatUtil.makeChecksum(sb.toString());
                    byte[] bytes = FormatUtil.hexToBytes((sb.append(checkCodeNum).append("16")).toString());
//                    write(bytes);
                } else if (datas[12] == 3) {
                    StringBuffer sb = new StringBuffer("68220400");
                    sb.append(FormatUtil.integer2Hex(datas[4]));
                    sb.append(FormatUtil.integer2Hex(datas[5]));
                    sb.append(FormatUtil.integer2Hex(datas[6]));
                    sb.append(FormatUtil.integer2Hex(datas[7]));
                    sb.append(FormatUtil.integer2Hex(datas[8]));
                    sb.append(FormatUtil.integer2Hex(datas[9]));
                    sb.append(FormatUtil.integer2Hex(datas[10]));
                    sb.append(FormatUtil.integer2Hex(datas[11]));
                    sb.append(FormatUtil.integer2Hex(datas[12]));
                    sb.append(FormatUtil.integer2Hex(datas[15]));
                    sb.append(FormatUtil.integer2Hex(datas[16]));
                    String checkCodeNum = FormatUtil.makeChecksum(sb.toString());
                    byte[] bytes = FormatUtil.hexToBytes((sb.append(checkCodeNum).append("16")).toString());
//                    write(bytes);
                    Log.e(TAG, "processReadData: 事件每分钟运动强度");
                } else if (datas[12] == 4) {
                    Log.e(TAG, "processReadData: 上传运动类型的离线运动数据");
                    StringBuffer sb = new StringBuffer("68220400");
                    sb.append(FormatUtil.integer2Hex(datas[4]));
                    sb.append(FormatUtil.integer2Hex(datas[5]));
                    sb.append(FormatUtil.integer2Hex(datas[6]));
                    sb.append(FormatUtil.integer2Hex(datas[7]));
                    sb.append(FormatUtil.integer2Hex(datas[8]));
                    sb.append(FormatUtil.integer2Hex(datas[9]));
                    sb.append(FormatUtil.integer2Hex(datas[10]));
                    sb.append(FormatUtil.integer2Hex(datas[11]));
                    sb.append(FormatUtil.integer2Hex(datas[12]));
                    String checkCodeNum = FormatUtil.makeChecksum(sb.toString());
                    byte[] bytes = FormatUtil.hexToBytes((sb.append(checkCodeNum).append("16")).toString());
//                    write(bytes);
                }
            }
        } else if (datas.length == 2 && datas[0] == BRACELET_REALTIME_HEARTRATE) {
            Log.e(TAG, "processReadData: 实时心率=" + datas[1]);
        }
    }

    public String getStringTimeStampHex(String year, String month, String day, String hour, String minute) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String times = year + "-" + month + "-" + day + " " + hour + ":" + minute;
            Date date = format.parse(times);
            long t = (date.getTime() + 8 * 60 * 60 * 1000) / 1000;
            System.out.println("时间戳=" + t + ",时间=" + times);
            String hex = Long.toHexString(t);//594ba600
            StringBuffer stringBuffer = new StringBuffer();
            int num = hex.length();
            while (num > 0) {
                String s = hex.substring(num - 2, num);
                System.out.println(s);
                stringBuffer.append(s);//这里需要颠倒位置
                num = num - 2;
            }
            return stringBuffer.toString();//00a64b59
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getTimeStampByte(String year, String month, String day, String hour, String minute) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String times = year + "-" + month + "-" + day + " " + hour + ":" + minute;
            Date date = format.parse(times);
            long t = (date.getTime() + 8 * 60 * 60 * 1000) / 1000;
            System.out.println("时间戳=" + t + ",时间=" + times);
            String hex = Long.toHexString(t);
            return FormatUtil.hexToBytes(hex);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String addStringHex(String head, String control, String data) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(head).append(control).append(data);
        return stringBuffer.toString();
    }

}
