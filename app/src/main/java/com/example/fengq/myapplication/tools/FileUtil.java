package com.example.fengq.myapplication.tools;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

/**
 * Created by fengq on 2017/5/18.
 */

public class FileUtil {
    private static final String TAG = FileUtil.class.getSimpleName();

    /**
     * 获取文件大小带单位的
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String getFileSizesWithUnit(String filePath) throws Exception {
        File file = new File(filePath);
        long blockSize;
        if (file.isDirectory()) {
            blockSize = getFileSizes(file);
        } else {
            blockSize = getFileSize(file);
        }
        return formatFileSize(blockSize);
    }

    /**
     * 获取整个文件夹的大小
     *
     * @param files
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File files) throws Exception {
        long size = 0;
        File fileList[] = files.listFiles();
        for (File f : fileList) {
            if (f.isDirectory()) {
                size += getFileSizes(f);
            } else {
                size += getFileSize(f);
            }
        }
        return size;
    }


    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e(TAG, "getFileSize: 文件不存在");
        }
        return size;
    }


    /**
     * 转换大小
     *
     * @param fileS
     * @return
     */
    private static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
}
