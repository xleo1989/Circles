package com.x.leo.circles;

import android.content.Context;

/**
 * @作者:My
 * @创建日期: 2017/3/27 14:00
 * @描述:${TODO}
 * @更新者:${Author}$
 * @更新时间:${Date}$
 * @更新描述:${TODO}
 */

public class DensityUtils {
    public static int dp2px(Context context,int size){

        float density = context.getResources().getDisplayMetrics().density;
        return (int) (size * density + 0.5f);
    }

    public static int px2dp(Context context,int size){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (size / density + 0.5f);
    }

    public static int px2sp(Context context, float textSize) {
        float density = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (textSize / density + 0.5f);
    }

    public static int sp2px(Context context,int size){
        float density = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (size * density + 0.5f);
    }
}
