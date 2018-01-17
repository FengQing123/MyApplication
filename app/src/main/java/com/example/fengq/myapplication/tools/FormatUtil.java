package com.example.fengq.myapplication.tools;

/**
 * Created by fengq on 2017/6/22.
 */

public class FormatUtil {

    /**
     * 十六进制字符串转byte数组
     *
     * @param hexString
     * @return
     */
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
            int h = hexDigits.indexOf(hexChars[pos]) << 4; //左移4相当于*2^4
            int l = hexDigits.indexOf(hexChars[pos + 1]); //
            if (h == -1 || l == -1) { // 非16进制字符
                return null;
            }
            bytes[i] = (byte) (h | l);//如果相对应位都是0，则结果为0，否则为1
        }
        return bytes;
    }

    /**
     * 生成16进制累加和校验码
     *
     * @param data
     * @return
     */
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
            total += Integer.parseInt(s, 16);//输出16进制数s 在十进制下的数
            num = num + 2;
        }
        /**
         * 用256求余最大是255，即16进制的FF
         */
        int mod = total % 256;
        String hex = Integer.toHexString(mod);//把十进制数mod 转为十六进制
        len = hex.length();
        //如果不够校验位的长度，补0,这里用的是两位校验
        if (len < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 十进制转十六进制字符串
     *
     * @param data
     * @return
     */
    public static String integer2Hex(byte data) {
        String hex = Integer.toHexString(data);
        int len = hex.length();
        if (len < 2) {
            hex = "0" + hex;
        }
        return hex;
    }
}
