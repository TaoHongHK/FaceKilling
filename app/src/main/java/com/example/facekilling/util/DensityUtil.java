package com.example.facekilling.util;

import android.content.Context;

public class DensityUtil {

    public static int dp2dx(Context context,float dpValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static float dx2dp(Context context,int pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxValue / scale + 0.5f);
    }
}
