package com.lancewu.graceviewpager.util;

import android.util.Log;

/**
 * Created by wrs on 2018/8/7.
 * 日志输出工具
 */
public class LogUtil {
    private static final String TAG = "GraceViewPager";
    private static boolean mEnable = false;

    public static void setEnable(boolean enable) {
        mEnable = enable;
    }

    public static void d(String msg) {
        if (!mEnable) {
            return;
        }
        Log.d(TAG, msg);
    }
}
