package com.example.fengq.myapplication.tools;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.Integer.parseInt;

/**
 * Created by fengq on 2017/4/28.
 */

public class Test {
    private static final String TAG = "Test";

    @Nullable
    private static String data;

    public static void main(String[] arg) throws Exception {
        List<User> list = new ArrayList<>();
        Map<String, User> map = new HashMap<>();
        User user = new User();
        user.setAge(12);
        list.add(user);
        map.put("1", user);
        User user3 = new User();
        user3.setAge(13);
        list.add(user3);
        map.put("2", user3);
        for (User user1 : list) {
            System.out.println("list user age=" + user1.getAge());
        }


//        int a = 124;
//        String ss = Integer.toHexString(a);
//        if (ss.length() == 1) {
//            int c = Integer.parseInt(ss, 16);
//            System.out.println("c=" + c);
//        } else {
//            char a1 = ss.charAt(0);
//            char a2 = ss.charAt(1);
//
//            int aa1 = Integer.parseInt(String.valueOf(a1), 16);
//            int aa2 = Integer.parseInt(String.valueOf(a2), 16);
//
//            System.out.println("a1=" + a1 + ",a2=" + a2 + ",aa1=" + aa1 + "，aa2=" + aa2);
//
//        }
//        System.out.println("ss=" + ss);
    }

    @Nullable
    public static String getData() {
        return data;
    }


    public static void getVersion(int data) {
        String hexString = Integer.toHexString(data);
        if (hexString.length() == 1) {
            int c = Integer.parseInt(hexString, 16);

            System.out.println("c=" + c);
        } else {
            char a1 = hexString.charAt(0);
            char a2 = hexString.charAt(1);

            int aa1 = Integer.parseInt(String.valueOf(a1), 16);
            int aa2 = Integer.parseInt(String.valueOf(a2), 16);

            System.out.println("a1=" + a1 + ",a2=" + a2 + ",aa1=" + aa1 + "，aa2=" + aa2);

        }
        System.out.println("ss=" + hexString);
    }

    public static void JsonParse() {
        // 构建用户geust
        User guestUser = new User();
        guestUser.setName("guest");
        guestUser.setAge(28);

        // 构建用户root
        User rootUser = new User();
        rootUser.setName("root");
        guestUser.setAge(35);

        // 构建用户对象数组
        User[] users = new User[2];
        users[0] = guestUser;
        users[1] = rootUser;

        List list = new ArrayList();
        list.add(guestUser);
        list.add(rootUser);
        // 用户对象数组转JSON串
        String jsonString2 = JSON.toJSONString(list);
        List<User> json = JSON.parseArray(jsonString2, User.class);

        System.out.println("jsonString2:" + jsonString2);
        System.out.println("json=" + json);
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static int getMouth() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        return mMonth;
    }


    public static String getHHmm(int seconds) {
        if (seconds < 60) {
            return "";
        }
        String timeStr;
        int hour, minute;

        minute = seconds / 60;
        hour = minute / 60;

        if (hour > 99)
            return "99:59:59";
        minute = minute % 60;
        timeStr = unitFormat(hour) + ":" + unitFormat(minute);
        return timeStr;
    }

    public static String AdditionTime(String datetime, int endseconds) {
        long newlong = 0;
        long oldlong = 0;
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat df2 = new SimpleDateFormat("HH:mm:ss");
        Date d1;
        try {
            d1 = df1.parse(datetime);
            oldlong = d1.getTime();
            newlong = oldlong + endseconds * 1000;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return df2.format(new Date(newlong));

    }

    @SuppressLint("DefaultLocale")
    public static String secToHour(int time) {
        String timeStr = null;
        float hour = 0;

        hour = time / 3600.0f;
        timeStr = String.format("%.2f ", hour);

        return timeStr;
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;

        minute = time / 60;

        hour = minute / 60;
        if (hour > 99)
            return "99:59:59";
        minute = minute % 60;
        second = time - hour * 3600 - minute * 60;
        timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);

        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public static String dateToStr(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    public static String conYearMonthDay(int year, int month, int day) {
        StringBuffer years;
        years = new StringBuffer("20" + String.valueOf(year));
        return years + "-" + unitFormat(month) + "-" + unitFormat(day);

//        if (String.valueOf(month).length() == 1) {
//            months = new StringBuffer("0" + String.valueOf(month));
//        } else if (String.valueOf(month).length() == 2) {
//            months = new StringBuffer(String.valueOf(month));
//        }
//
//        if (String.valueOf(day).length() == 1) {
//            days = new StringBuffer("0" + String.valueOf(day));
//        } else if (String.valueOf(day).length() == 2) {
//            days = new StringBuffer(String.valueOf(day));
//        }
//        return (years + "-" + months + "-" + days).toString();
    }

    public static int readUInt(byte[] bytes, int index) {
        //// FIXME: 2017/7/12
        int temp = ((bytes[index] & 0xff) | (bytes[index + 1] & 0xff) << 8 | (bytes[index + 2] & 0xff) << 16 | (bytes[index + 3] & 0xff) << 24);
        return temp;
    }

    public static void add(byte[] b) {
        b[1] = 11;
    }

    public static int adds(int b) {
        b = b + 1;
        return b;
    }

    public static void testBytes() {
        byte a = (byte) 128;
        byte b = (byte) 255;
        byte d = 127;
        int c = (b & 0xff);
        double e = (double) b;
        System.out.println("a=" + a + ",b=" + b + ",d=" + d + ",e=" + e);
        if (b < 0) {
            System.out.println("b+256=" + (b + 256));
        }
        System.out.println("c=" + c);
        System.out.println("0xff=" + 0xff);

        byte aa = -1;
        byte bb = (byte) -129;
        System.out.println("aa=" + aa);
        System.out.println("=" + Integer.toBinaryString(aa));
        System.out.println("===" + Integer.toBinaryString(-3));
        System.out.println("bb=" + bb);
        System.out.println("====" + Integer.toBinaryString(bb));

        byte cc = -3;
        System.out.println("cc=" + cc);

    }

    public static void textMathRound() {
        float a = 12.65f;
        System.out.println("inta=" + (int) a);
        System.out.println("Math.round(a)=" + Math.round(a));
    }

//    public static void aaa() {
//        String[] s = new String[]{"59", "4b", "a6", "00"};
//        byte[] bytes = new byte[s.length];
//        int i = 0;
//        for (String str : s) {
//            bytes[i]=str;
//            i++;
//        }
//    }

    public static String addStringHex(String head, String control, String data, String end) {
        StringBuffer stringBuffer = new StringBuffer();
        if (StringUtil.isNoNullOrNoBlank(head)) {
            stringBuffer.append(head);
        }
        if (StringUtil.isNoNullOrNoBlank(control)) {
            stringBuffer.append(control);
        }
        if (StringUtil.isNoNullOrNoBlank(data)) {
            stringBuffer.append(data);
        }
        if (StringUtil.isNoNullOrNoBlank(end)) {
            stringBuffer.append(end);
        }

        return stringBuffer.toString();
    }

    //生成16进制累加和校验码
    public static String makeChecksum(String data) {
        if (data == null || data.equals("")) {
            return "";
        }
        int total = 0;
        int len = data.length();
        int num = 0;
        while (num < len) {
            String s = data.substring(num, num + 2);
            System.out.println(s);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        /**
         * 用256求余最大是255，即16进制的FF
         */
        int mod = total % 256;
        String hex = Integer.toHexString(mod);
        len = hex.length();
        //如果不够校验位的长度，补0,这里用的是两位校验
        if (len < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    public static void setTime() {
        getTimeStamp("2017", "06", "22", "11", "12");
    }

    public static void getTimeStamp(String year, String month, String day, String hour, String minute) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String times = year + "-" + month + "-" + day + " " + hour + ":" + minute;
            Date date = format.parse(times);
            System.out.println("times=" + times);
            System.out.println("Format To times:" + (date.getTime() + 8 * 60 * 60 * 1000) / 1000);
            long t = (date.getTime() + 8 * 60 * 60 * 1000) / 1000;
            String hex = Long.toHexString(t);
            StringBuffer stringBuffer = new StringBuffer();
            int num = hex.length();
            while (num > 0) {
                String s = hex.substring(num - 2, num);
                System.out.println(s);
                stringBuffer.append(s);
                num = num - 2;
            }
            System.out.println("hex=" + hex);
            System.out.println("stringBuffer=" + stringBuffer);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static byte[] hexToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }

        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] bytes = new byte[length];
        String hexDigits = "0123456789abcdef";
        for (int i = 0; i < length; i++) {
            int pos = i * 2; // 两个字符对应一个byte
            int h = hexDigits.indexOf(hexChars[pos]) << 4; // 注1
            int l = hexDigits.indexOf(hexChars[pos + 1]); // 注2
            if (h == -1 || l == -1) { // 非16进制字符
                return null;
            }
            bytes[i] = (byte) (h | l);
        }
        return bytes;
    }

    public static void dateToTime() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String time = "2017-6-22 11:12";
            Date date = format.parse(time);
            System.out.println("Format To times-----:" + (date.getTime() + 8 * 60 * 60 * 1000) / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void TimeToDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(1435671735);
        System.out.println("time To date=" + formatter.format(curDate));
    }

    public static String getTodayTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }


    public static void HexToInt() {
        //Hex==68,ffffff83,1,0,56,42,16,-------Int-origStr=104,-125,1,0,86,66,22
        byte[] data = new byte[]{68, 83, 1, 0, 56, 42, 16};
        for (byte b : data) {
            int a = parseInt((b & 0xff) + "", 16);
            System.out.println("a=" + a);
        }
    }


    public static void ddd() {
        byte x = (byte) 0x83;
        int xx = x;
        int xxx = x & 0xff;
        System.out.println(x);//-125
        System.out.println(xx);//-125
        System.out.println(xxx);//131
        System.out.println(Integer.toHexString(x));//ffffff83
        System.out.println(Integer.toHexString(xx));//ffffff83
        System.out.println(Integer.toHexString(xxx));//83


        //        String hex = Integer.toHexString(var);
        //        BigInteger bigInteger = new BigInteger(hex, 16);
        //        int a = bigInteger.intValue() & 0xff;
        //        System.out.println("x=" + x);
        //        if (a == x) {
        //            System.out.println("yes,,," + a);
        //        } else {
        //            System.out.println("no,," + a);
        //        }
    }


    public static void arraytest() {
        float ave[] = new float[0];
        ave[0] = 1;
        ave[1] = 2;
        System.out.println("aves=" + ave[0] + ",avei=" + ave[1]);
    }

    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static void floatAndDouble() {
        float a = 10.02323f;
        double b = a;
        double c = 12.32323;
        float d = (float) c;
        System.out.println("a=" + a + ",b=" + b + ",c=" + c + ",d=" + d);//a=10.02323,b=10.023229598999023,c=12.32323,d=12.32323
    }

    public static float subFloat(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(Float.toString(v1));
        BigDecimal b2 = new BigDecimal(Float.toString(v2));
        return b1.subtract(b2).floatValue();
    }

    public static float addFloat(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(Float.toString(v1));
        BigDecimal b2 = new BigDecimal(Float.toString(v2));
        return b1.add(b2).floatValue();
    }


    public static void testThread() throws Exception {
        Thread threadInterrupt = new Thread(new MyRunnable());
        threadInterrupt.start();
        System.out.println("在50秒之内按任意键中断线程!");
        System.in.read();
        threadInterrupt.interrupt();
        threadInterrupt.join();
        System.out.println("线程已经退出!");
    }

    static class MyRunnable implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    System.out.println("线程运行中--------");
                    Thread.sleep(3000);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void testByte() {
        int time = 258234344;
        System.out.println("time%256===" + (byte) (time % 256));
        System.out.println("time/256===" + (byte) (time / 256));
    }

    public void getExample(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().toString());
    }

    public void postExample(String url, String json) throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().toString());
    }


    public String bowlingJson(String player1, String player2) {
        return "{'winCondition':'HIGH_SCORE',"
                + "'name':'Bowling',"
                + "'round':4,"
                + "'lastSaved':1367702411696,"
                + "'dateStarted':1367702378785,"
                + "'players':["
                + "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                + "{'name':'" + player2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
                + "]}";
    }

}

class User {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "{\"n\":" + name + ",\"a\":" + age + "}";
    }
}

