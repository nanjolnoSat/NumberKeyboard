package com.mishaki.hfrecyclerview.model;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.mishaki.hfrecyclerview.util.DividerType;


public class HfDividerModel {
    public int type = DividerType.NONE;
    public float size;
    public Drawable drawable;
    /**
     * 也可以定义自己的Paint,非空则使用该Paint
     */
    public Paint paint;
}