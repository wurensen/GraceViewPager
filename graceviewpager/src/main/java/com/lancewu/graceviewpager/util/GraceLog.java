package com.lancewu.graceviewpager.util;

import android.util.Log;

/**
 * Created by wrs on 2018/8/7.<br>
 * 日志输出工具
 */
public class GraceLog {
    private static final String TAG = "GraceViewPager";
    private static boolean mEnable = false;

    /**
     * 是否开启调试日志，默认关闭
     *
     * @param enable 是否开启
     */
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
