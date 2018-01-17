package com.example.fengq.myapplication.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by fengq on 2017/5/12.
 */

public class ImageUtil {

    private static String TAG = ImageUtil.class.getSimpleName();

    /**
     * 字节数组保存为文件
     *
     * @param bytes
     * @param outputFile
     * @return
     */
    public static File Bytes2File(byte[] bytes, String outputFile) {
        File file = null;
        BufferedOutputStream stream = null;
        try {
            file = new File(outputFile);
            FileOutputStream outputStream = new FileOutputStream(file);
            stream = new BufferedOutputStream(outputStream);
            stream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 质量压缩
     *
     * @param bitmap
     * @param max    指定的大小
     * @return
     */
    public static byte[] compressBitmap(Bitmap bitmap, int max) {
        int quality = 100;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
        Log.e(TAG, "compressBitmap:压缩前：byteArrayOutputStream.toByteArray().length= " + byteArrayOutputStream.toByteArray().length);
        while (byteArrayOutputStream.toByteArray().length / 1024 > max && quality > 0) {
            byteArrayOutputStream.reset();
            quality = quality - 5;
            Log.e(TAG, "compressBitmap: quality=" + quality);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
            Log.e(TAG, "compressBitmap:压缩后：byteArrayOutputStream.toByteArray().length= " + byteArrayOutputStream.toByteArray().length);
        }
        return byteArrayOutputStream.toByteArray();
    }


    /**
     * 图片像素压缩
     *
     * @param srcPath
     * @return
     */
    public static Bitmap getBitmap(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options(); //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight; //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例 //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    /**
     * Bitmap转为byte[] (未压缩)
     *
     * @param bitmap
     * @return
     */
    public static byte[] Bitmap2byte(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        }
        return null;
    }

    /**
     * byte[]转Bitmap
     *
     * @param b
     * @return
     */

    public static Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }


    public static Bitmap Bytes2Bitmaps(byte[] b) {
        Bitmap bitmap;
        if (b.length != 0) {
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
        } else {
            return null;
        }
    }

    /**
     * 文件转为Bitmap
     *
     * @param file
     * @return
     */
    public static Bitmap FileToBitmap(String file) {
        return BitmapFactory.decodeFile(file);
    }

}
