package com.example.fengq.myapplication.tools;

/**
 * Created by fengq on 2017/6/22.
 */

public class StringUtil {
    /**
     * 判断字符串非空或者非空格
     *
     * @param s
     * @return
     */
    public static boolean isNoNullOrNoBlank(String s) {
        return !((s == null) || (s.equals("")));
    }

    /**
     * 两个字符串相加
     *
     * @param a
     * @param b
     * @return
     */
    public static String stringAppend(String a, String b) {
        StringBuffer stringBuffer = new StringBuffer();
        if (isNoNullOrNoBlank(a)) {
            stringBuffer.append(a);
        }
        if (isNoNullOrNoBlank(b)) {
            stringBuffer.append(b);
        }
        return stringBuffer.toString();
    }
}
