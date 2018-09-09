package com.running.moonlight.lib_dialog.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by liuheng on 2018/5/16.
 */
public class ScreenUtils {

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }
}
