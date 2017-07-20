package com.dabang.mvvmdemo.Http;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Jane on 2017/7/20.
 */

public class CacheUtils {

    //SD卡是否存在
    public static boolean isSDCardExsit() {
        String state = Environment.getExternalStorageState();
        if (state == null) return false;
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (isSDCardExsit()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }


    //录音文件存放位置
    public static String getRecoderDiskFileDir(Context context) {
        String cachePath = null;
        if (isSDCardExsit()) {
            cachePath = context.getExternalFilesDir("recoder").getPath();
        } else {
            cachePath = context.getFilesDir().getPath();
        }
        return cachePath;
    }


    //马赛克图片文件存放位置
    public static String getMosickDiskFileDir(Context context) {
        String cachePath = null;
        if (isSDCardExsit()) {
            cachePath = context.getExternalFilesDir("mosaic").getPath();
        } else {
            cachePath = context.getFilesDir().getPath();
        }
        return cachePath;
    }

    public static File getOkHttpFile(Context context) {
        String path = getDiskCacheDir(context) + File.separator + "okhttp";
        File file = new File(path);
        return file;
    }
}