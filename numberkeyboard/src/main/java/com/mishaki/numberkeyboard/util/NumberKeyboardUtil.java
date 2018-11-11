package com.mishaki.numberkeyboard.util;

import android.content.Context;
import android.util.TypedValue;

public class NumberKeyboardUtil {
    public static float px2dp(final Context context, final float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static float px2sp(final Context context, final float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static float dp2px(final Context context, final float px) {
        return px * (1f / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, context.getResources().getDisplayMetrics()));
    }

    public static float sp2px(final Context context, final float px) {
        return px * (1f / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 1f, context.getResources().getDisplayMetrics()));
    }
}
