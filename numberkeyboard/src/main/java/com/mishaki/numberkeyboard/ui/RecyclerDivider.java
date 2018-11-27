package com.mishaki.numberkeyboard.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerDivider extends RecyclerView.ItemDecoration {
    public static final int LEFT = 0b1;
    public static final int MIDDLE = 0b100;
    public static final int RIGHT = 0b10000;
    public static final int TOP = 0b1000000;
    public static final int BOTTOM = 0b100000000;
    public static final int ALL = 0b10000000000;
    public static final int ALL_HALF = 0b1000000000000;

    private float size;
    private boolean isReviseSize;

    private int type;

    private final Paint mPaint = new Paint();
    private final Paint clipPaint = new Paint();

    private float horizontalDividerSize;
    private float verticalDividerSize;

    private Drawable drawable;

    private RecyclerDivider(final float size, final float horizontalDividerSize, final float verticalDividerSize, final boolean isReviseSize, final int color, final int type, final Drawable drawable) {
        this.size = size;
        this.horizontalDividerSize = horizontalDividerSize;
        this.verticalDividerSize = verticalDividerSize;
        this.isReviseSize = isReviseSize;
        this.type = type;
        this.drawable = drawable;

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);

        clipPaint.setAntiAlias(true);
        clipPaint.setColor(0x00ffffff);
        clipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    public static class Build {
        private float size;
        private float horizontalDividerSize = -1;
        private float verticalDividerSize = -1;
        private boolean isReviseSize;

        private int color = 0xffffffff;

        private int type = RecyclerDivider.MIDDLE;
        private Drawable drawable;

        private Context context;
        private int drawableResId = Integer.MAX_VALUE;

        public Build setSize(final float size) {
            this.size = size;
            if (this.horizontalDividerSize == -1) {
                this.horizontalDividerSize = size;
            }
            if (this.verticalDividerSize == -1) {
                this.verticalDividerSize = size;
            }
            return this;
        }

        public Build setHorizontalDividerSize(final float horizontalDividerSize) {
            this.horizontalDividerSize = horizontalDividerSize;
            return this;
        }

        public Build setVeticalDividerSize(final float verticalDividerSize) {
            this.verticalDividerSize = verticalDividerSize;
            return this;
        }

        /**
         * 需要修正必须满足3个条件:
         * 1,LayoutManager必须为GridManager
         * 2,Divider的type为MIDDLE、ALL、ALL_HALF
         * 3,行数/列数超过一行/列
         * tips:
         * 1):当orientation为horizontal时,会使verticalSize的倍数为spanCount的倍数,horizontalSize的倍数为2的倍数.
         * 2):当orientation为vertical时,会使horizontalSize的倍数为spanCount的倍数,verticalSize的倍数为2的倍数.
         * 3):只有type为ALL_HALF时才会进行2倍的修正,其他type并不会
         */
        public Build isReviseSize(final boolean isReviseSize) {
            this.isReviseSize = isReviseSize;
            return this;
        }

        public Build setColor(final int color) {
            this.color = color;
            return this;
        }

        public Build setType(final int type) {
            this.type = type;
            return this;
        }

        public Build setDrawable(final Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        public Build setDrawable(final Context context, final int drawableResId) {
            this.context = context.getApplicationContext();
            this.drawableResId = drawableResId;
            return this;
        }

        public RecyclerDivider build() {
            Drawable dividerDrawable = drawable;
            if (context != null && drawableResId != Integer.MAX_VALUE) {
                dividerDrawable = ContextCompat.getDrawable(context, drawableResId);
            }
            return new RecyclerDivider(size, horizontalDividerSize, verticalDividerSize, isReviseSize, color, type, dividerDrawable);
        }
    }

    @Override
    public void getItemOffsets(final Rect outRect, final View view, final RecyclerView parent, final RecyclerView.State state) {
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if (((GridLayoutManager) layoutManager).getOrientation() == RecyclerView.VERTICAL) {
                calcGridVerticalItemOffsets(outRect, view, parent);
            } else {
                calcGridHorizontalItemOffsets(outRect, view, parent);
            }
        } else {
            if (((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                calcHorizontalItemOffsets(outRect, view, parent);
            } else {
                calcVerticalItemOffsets(outRect, view, parent);
            }
        }
    }

    private void calcHorizontalItemOffsets(final Rect outRect, final View view, final RecyclerView parent) {
        final int index = parent.getChildAdapterPosition(view);
        final int count = parent.getAdapter().getItemCount();
        switch (type) {
            case LEFT:
                outRect.left = (int) size;
                break;
            case MIDDLE:
                if (index != count - 1) {
                    outRect.right = (int) size;
                }
                break;
            case RIGHT:
                outRect.right = (int) size;
                break;
            case ALL:
                if (count == 1) {
                    outRect.left = (int) size;
                    outRect.right = (int) size;
                } else {
                    if (index == 0) {
                        outRect.left = (int) size;
                        outRect.right = (int) size;
                    } else {
                        outRect.right = (int) size;
                    }
                }
                break;
            case ALL_HALF:
                outRect.left = get1_2Size();
                outRect.right = get1_2Size();
                break;
        }
    }

    private void calcVerticalItemOffsets(final Rect outRect, final View view, final RecyclerView parent) {
        final int index = parent.getChildAdapterPosition(view);
        final int count = parent.getAdapter().getItemCount();
        switch (type) {
            case TOP:
                outRect.top = (int) size;
                break;
            case MIDDLE:
                if (index != count - 1) {
                    outRect.bottom = (int) size;
                }
                break;
            case BOTTOM:
                outRect.bottom = (int) size;
                break;
            case ALL:
                if (count == 1) {
                    outRect.top = (int) size;
                    outRect.bottom = (int) size;
                } else {
                    if (index == 0) {
                        outRect.top = (int) size;
                        outRect.bottom = (int) size;
                    } else {
                        outRect.bottom = (int) size;
                    }
                }
                break;
            case ALL_HALF:
                outRect.top = get1_2Size();
                outRect.bottom = get1_2Size();
                break;
        }
    }

    private void calcGridVerticalItemOffsets(final Rect outRect, final View view, final RecyclerView parent) {
        final int index = parent.getChildAdapterPosition(view);
        final int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        if (isReviseSize) {
            if (type == MIDDLE || type == ALL) {
                horizontalDividerSize = ((int) (horizontalDividerSize / spanCount)) * spanCount;
            }
            //当使用ALL_HALF的时候,控制间隔的让一个view上线左右各腾出dividerSize的一半空间
            //这个时候如果dividerSize是奇数的话,就会损失一px,这样如果颜色是透明的话,在绘制的时候
            //就可以很明显的看出来
            if (type == ALL_HALF) {
                verticalDividerSize = ((int) (verticalDividerSize / 2)) * 2;
                horizontalDividerSize = ((int) (horizontalDividerSize / 2)) * 2;
            }
            isReviseSize = false;
        }
        switch (type) {
            case LEFT:
                outRect.left = (int) horizontalDividerSize;
                break;
            case TOP:
                outRect.top = (int) verticalDividerSize;
                break;
            case RIGHT:
                outRect.right = (int) horizontalDividerSize;
                break;
            case BOTTOM:
                outRect.bottom = (int) verticalDividerSize;
                break;
            case MIDDLE:
                final int childCount = parent.getAdapter().getItemCount();
                final int lines;
                if (childCount % spanCount == 0) {
                    lines = childCount / spanCount;
                } else {
                    lines = childCount / spanCount + 1;
                }
                outRect.left = (int) (index % spanCount * horizontalDividerSize / spanCount);
                outRect.right = (int) ((spanCount - index % spanCount - 1) * horizontalDividerSize / spanCount);
                if (index / spanCount != lines - 1) {
                    outRect.bottom = (int) verticalDividerSize;
                }
                break;
            case LEFT + TOP:
                outRect.left = (int) horizontalDividerSize;
                outRect.top = (int) verticalDividerSize;
                break;
            case LEFT + BOTTOM:
                outRect.left = (int) horizontalDividerSize;
                outRect.bottom = (int) verticalDividerSize;
                break;
            case RIGHT + TOP:
                outRect.right = (int) horizontalDividerSize;
                outRect.top = (int) verticalDividerSize;
                break;
            case RIGHT + BOTTOM:
                outRect.right = (int) horizontalDividerSize;
                outRect.bottom = (int) verticalDividerSize;
                break;
            case ALL:
                outRect.left = (int) ((spanCount - index % spanCount) * horizontalDividerSize / spanCount);
                outRect.right = (int) ((index % spanCount + 1) * horizontalDividerSize / spanCount);
                if (index / spanCount == 0) {
                    outRect.top = (int) verticalDividerSize;
                }
                outRect.bottom = (int) verticalDividerSize;
                break;
            case ALL_HALF:
                outRect.set(get1_2HorizontalSize(), get1_2VerticalSize(), get1_2HorizontalSize(), get1_2VerticalSize());
                break;
        }
    }

    private void calcGridHorizontalItemOffsets(final Rect outRect, final View view, final RecyclerView parent) {
        final int index = parent.getChildAdapterPosition(view);
        final int spanCount = ((GridLayoutManager) (parent.getLayoutManager())).getSpanCount();
        if (isReviseSize) {
            if (type == MIDDLE || type == ALL) {
                verticalDividerSize = ((int) (verticalDividerSize / spanCount)) * spanCount;
            }
            //当使用ALL_HALF的时候,控制间隔的让一个view上线左右各腾出dividerSize的一半空间
            //这个时候如果dividerSize是奇数的话,就会损失一px,这样如果颜色是透明的话,在绘制的时候
            //就可以很明显的看出来
            if (type == ALL_HALF) {
                horizontalDividerSize = ((int) horizontalDividerSize / 2) * 2;
                verticalDividerSize = ((int) horizontalDividerSize / 2) * 2;
            }
            isReviseSize = false;
        }
        switch (type) {
            case LEFT:
                outRect.left = (int) horizontalDividerSize;
                break;
            case TOP:
                outRect.top = (int) verticalDividerSize;
                break;
            case RIGHT:
                outRect.right = (int) horizontalDividerSize;
                break;
            case BOTTOM:
                outRect.bottom = (int) verticalDividerSize;
                break;
            case MIDDLE:
                final int childCount = parent.getAdapter().getItemCount();
                final int columns;
                if (childCount % spanCount == 0) {
                    columns = childCount / spanCount;
                } else {
                    columns = childCount / spanCount + 1;
                }
                if (index / spanCount != columns - 1) {
                    outRect.right = (int) horizontalDividerSize;
                }
                outRect.top = (int) (index % spanCount * verticalDividerSize / spanCount);
                outRect.bottom = (int) ((spanCount - index % spanCount - 1) * verticalDividerSize / spanCount);
                break;
            case LEFT + TOP:
                outRect.left = (int) horizontalDividerSize;
                outRect.top = (int) verticalDividerSize;
                break;
            case LEFT + BOTTOM:
                outRect.left = (int) horizontalDividerSize;
                outRect.bottom = (int) verticalDividerSize;
                break;
            case RIGHT + TOP:
                outRect.right = (int) horizontalDividerSize;
                outRect.top = (int) verticalDividerSize;
                break;
            case RIGHT + BOTTOM:
                outRect.right = (int) horizontalDividerSize;
                outRect.bottom = (int) verticalDividerSize;
                break;
            case ALL:
                if (index / spanCount == 0) {
                    outRect.left = (int) horizontalDividerSize;
                }
                outRect.right = (int) horizontalDividerSize;
                outRect.top = (int) ((spanCount - index % spanCount) * verticalDividerSize / spanCount);
                outRect.bottom = (int) ((index % spanCount + 1) * verticalDividerSize / spanCount);
                break;
            case ALL_HALF:
                outRect.set(get1_2HorizontalSize(), get1_2VerticalSize(), get1_2HorizontalSize(), get1_2VerticalSize());
                break;
        }
    }

    @Override
    public void onDraw(final Canvas c, final RecyclerView parent, final RecyclerView.State state) {
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        final int id = c.saveLayer(parent.getPaddingLeft(),0,parent.getWidth() - parent.getPaddingRight(),parent.getHeight(),null,Canvas.ALL_SAVE_FLAG);
        if (layoutManager instanceof GridLayoutManager) {
            if (((GridLayoutManager) parent.getLayoutManager()).getOrientation() == RecyclerView.VERTICAL) {
                drawGridVerticalDivider(c, parent);
            } else {
                drawGridHorizontalDivider(c, parent);
            }
        } else {
            if (((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                drawHorizontalDivider(c, parent);
            } else {
                drawVerticalDivider(c, parent);
            }
        }
        c.drawRect(parent.getPaddingLeft(),0,parent.getWidth() - parent.getPaddingRight(),parent.getPaddingTop(),clipPaint);
        c.drawRect(parent.getPaddingLeft(),parent.getHeight() - parent.getPaddingBottom(),parent.getWidth() - parent.getPaddingRight(),parent.getHeight(),clipPaint);
        c.save(id);
    }

    private void drawHorizontalDivider(final Canvas canvas, final RecyclerView parent) {
        final int childCount = parent.getChildCount();
        switch (type) {
            case LEFT:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawLeftDivider(canvas, child);
                }
                break;
            case MIDDLE:
                final int actualChildCount = parent.getAdapter().getItemCount();
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    final int position = parent.getChildLayoutPosition(child);
                    if (position != actualChildCount - 1) {
                        drawRightDivider(canvas, child);
                    }
                }
                break;
            case RIGHT:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawRightDivider(canvas, child);
                }
                break;
            case ALL:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawRightDivider(canvas, child);
                    if (i == 0) {
                        drawLeftDivider(canvas, child);
                    }
                }
                break;
            case ALL_HALF:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);

                    final float top = child.getTop();
                    final float bottom = child.getBottom();

                    final float left1 = child.getLeft() - get1_2Size();
                    final float right1 = child.getLeft();
                    drawDivider(canvas, left1, top, right1, bottom);

                    final float left2 = child.getRight();
                    final float right2 = child.getRight() + get1_2Size();
                    drawDivider(canvas, left2, top, right2, bottom);
                }
                break;
        }
    }

    private void drawVerticalDivider(final Canvas canvas, final RecyclerView parent) {
        final int childCount = parent.getChildCount();
        switch (type) {
            case TOP:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawTopDivider(canvas, child);
                }
                break;
            case MIDDLE:
                final int actualChildCount = parent.getAdapter().getItemCount();
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    final int position = parent.getChildLayoutPosition(child);
                    if (position != actualChildCount - 1) {
                        drawBottomDivider(canvas, child);
                    }
                }
                break;
            case BOTTOM:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawBottomDivider(canvas, child);
                }
                break;
            case ALL:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawBottomDivider(canvas, child);
                    if (i == 0) {
                        drawTopDivider(canvas, child);
                    }
                }
                break;
            case ALL_HALF:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);

                    final float left = child.getLeft();
                    final float right = child.getRight();

                    final float top1 = child.getTop() - get1_2Size();
                    final float bottom1 = child.getTop();
                    drawDivider(canvas, left, top1, right, bottom1);

                    final float top2 = child.getBottom();
                    final float bottom2 = child.getBottom() + get1_2Size();
                    drawDivider(canvas, left, top2, right, bottom2);
                }
                break;
        }
    }

    private void drawGridVerticalDivider(final Canvas canvas, final RecyclerView parent) {
        final int childCount = parent.getChildCount();
        final int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        final int actualChildCount = parent.getAdapter().getItemCount();
        final int actualLines;
        if (actualChildCount % spanCount == 0) {
            actualLines = actualChildCount / spanCount;
        } else {
            actualLines = actualChildCount / spanCount + 1;
        }
        switch (type) {
            case LEFT:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawLeftDivider(canvas, child);
                }
                break;
            case TOP:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawTopDivider(canvas, child);
                }
                break;
            case RIGHT:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawRightDivider(canvas, child);
                }
                break;
            case BOTTOM:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawBottomDivider(canvas, child);
                }
                break;
            case MIDDLE:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    final int position = parent.getChildLayoutPosition(child);
                    if (i % spanCount != 0) {
                        drawLeftDivider(canvas, child);
                    }
                    if (position / spanCount != actualLines - 1) {
                        drawBottomDivider(canvas, child);
                    }
                    if (position % spanCount != 0 && position / spanCount != actualLines - 1) {
                        drawGridRectDivider(canvas, child.getLeft() - horizontalDividerSize, child.getBottom());
                    }
                }
                break;
            case LEFT + TOP:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawLeftDivider(canvas, child);
                    drawTopDivider(canvas, child);
                    drawGridRectDivider(canvas, child.getLeft() - horizontalDividerSize, child.getTop() - verticalDividerSize);
                }
                break;
            case LEFT + BOTTOM:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawLeftDivider(canvas, child);
                    drawBottomDivider(canvas, child);
                    drawGridRectDivider(canvas, child.getLeft() - horizontalDividerSize, child.getBottom());
                }
                break;
            case RIGHT + TOP:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawRightDivider(canvas, child);
                    drawTopDivider(canvas, child);
                    drawGridRectDivider(canvas, child.getRight(), child.getTop() - verticalDividerSize);
                }
                break;
            case RIGHT + BOTTOM:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawRightDivider(canvas, child);
                    drawBottomDivider(canvas, child);
                    drawGridRectDivider(canvas, child.getRight(), child.getBottom());
                }
                break;
            case ALL:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawRightDivider(canvas, child);
                    drawBottomDivider(canvas, child);
                    drawGridRectDivider(canvas, child.getRight(), child.getBottom());
                    if (i == 0) {//左上角
                        drawGridRectDivider(canvas, child.getLeft() - horizontalDividerSize, child.getTop() - verticalDividerSize);
                    }
                    if (i % spanCount == 0) {//第一列
                        drawLeftDivider(canvas, child);
                        drawGridRectDivider(canvas, child.getLeft() - horizontalDividerSize, child.getBottom());
                    }
                    if (i / spanCount == 0) {//第一行
                        drawTopDivider(canvas, child);
                        drawGridRectDivider(canvas, child.getRight(), child.getTop() - verticalDividerSize);
                    }
                }
                break;
            case ALL_HALF:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    final int position = parent.getChildAdapterPosition(child);
                    //非最后一个行
                    if (position / spanCount != actualLines - 1) {
                        drawBottomDivider(canvas, child);
                    }
                    if ((i + 1) % spanCount != 0) {//非最后一列
                        drawRightDivider(canvas, child);
                    }
                    //非最后一个行并且非最后一列
                    if (position / spanCount != actualLines - 1 && (i + 1) % spanCount != 0) {
                        drawGridRectDivider(canvas, child.getRight(), child.getBottom());
                    }
                    //第一列
                    if (i % spanCount == 0) {
                        if (position / spanCount != actualLines - 1 || i % spanCount != 0) {//去掉左下角
                            final float left = child.getLeft() - get1_2HorizontalSize();
                            final float top = child.getTop();
                            final float right = child.getLeft();
                            final float bottom = child.getBottom() + verticalDividerSize;
                            drawDivider(canvas, left, top, right, bottom);
                        } else {
                            final float left = child.getLeft() - get1_2HorizontalSize();
                            final float top = child.getTop();
                            final float right = child.getLeft();
                            final float bottom = child.getBottom();
                            drawDivider(canvas, left, top, right, bottom);
                        }
                    }
                    //第一行
                    if (i / spanCount == 0) {
                        if ((i + 1) % spanCount != 0) {//去掉一行的最后一个
                            final float left = child.getLeft();
                            final float top = child.getTop() - get1_2VerticalSize();
                            final float right = child.getRight() + horizontalDividerSize;
                            final float bottom = child.getTop();
                            drawDivider(canvas, left, top, right, bottom);
                        } else {
                            final float left = child.getLeft();
                            final float top = child.getTop() - get1_2VerticalSize();
                            final float right = child.getRight();
                            final float bottom = child.getTop();
                            drawDivider(canvas, left, top, right, bottom);
                        }
                    }
                    if ((i + 1) % spanCount == 0) {//最后一列
                        if (position != actualChildCount- 1) {//去掉最后一个
                            final float left = child.getRight();
                            final float top = child.getTop();
                            final float right = child.getRight() + get1_2HorizontalSize();
                            final float bottom = child.getBottom() + verticalDividerSize;
                            drawDivider(canvas, left, top, right, bottom);
                        } else {//最后一个
                            final float left = child.getRight();
                            final float top = child.getTop();
                            final float right = child.getRight() + get1_2HorizontalSize();
                            final float bottom = child.getBottom();
                            drawDivider(canvas, left, top, right, bottom);
                        }
                    }
                    if (position / spanCount == actualLines - 1) {//最后一行
                        if (position != actualChildCount - 1 || (i + 1) % spanCount != 0) {//去掉最后一个
                            final float left = child.getLeft();
                            final float top = child.getBottom();
                            final float right = child.getRight() + horizontalDividerSize;
                            final float bottom = child.getBottom() + get1_2VerticalSize();
                            drawDivider(canvas, left, top, right, bottom);
                        } else {//最后一个
                            final float left = child.getLeft();
                            final float top = child.getBottom();
                            final float right = child.getRight();
                            final float bottom = child.getBottom() + get1_2VerticalSize();
                            drawDivider(canvas, left, top, right, bottom);
                        }
                    }
                    if (i == 0) {//左上角
                        drawSmallGridRectDivider(canvas, child.getLeft() - get1_2HorizontalSize(), child.getTop() - get1_2VerticalSize());
                    }
                    if (i / spanCount == 0 && (i + 1) % spanCount == 0) {//右上角
                        drawSmallGridRectDivider(canvas, child.getRight(), child.getTop() - get1_2VerticalSize());
                    }
                    if (position / spanCount == actualLines - 1 && i % spanCount == 0) {//左下角
                        drawSmallGridRectDivider(canvas, child.getLeft() - get1_2HorizontalSize(), child.getBottom());
                    }
                    if (position / spanCount == actualLines - 1 && (i + 1) % spanCount == 0) {//右下角
                        drawSmallGridRectDivider(canvas, child.getRight(), child.getBottom());
                    }
                }
                break;
        }
    }

    private void drawGridHorizontalDivider(final Canvas canvas, final RecyclerView parent) {
        final int childCount = parent.getChildCount();
        final int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        final int actualChildCount = parent.getAdapter().getItemCount();
        final int columns;
        if (actualChildCount % spanCount == 0) {
            columns = actualChildCount / spanCount;
        } else {
            columns = actualChildCount / spanCount + 1;
        }
        switch (type) {
            case LEFT:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawLeftDivider(canvas, child);
                }
                break;
            case TOP:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawTopDivider(canvas, child);
                }
                break;
            case RIGHT:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawRightDivider(canvas, child);
                }
                break;
            case BOTTOM:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawBottomDivider(canvas, child);
                }
                break;
            case MIDDLE:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    final int position = parent.getChildLayoutPosition(child);
                    if (position / spanCount != columns - 1) {
                        drawRightDivider(canvas, child);
                    }
                    if (i % spanCount != 0) {
                        drawTopDivider(canvas, child);
                    }
                    if (position / spanCount != columns - 1 && position % spanCount != 0) {
                        drawGridRectDivider(canvas, child.getRight(), child.getTop() - verticalDividerSize);
                    }
                }
                break;
            case LEFT + TOP:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawLeftDivider(canvas, child);
                    drawTopDivider(canvas, child);
                    drawGridRectDivider(canvas, child.getLeft() - horizontalDividerSize, child.getTop() - verticalDividerSize);
                }
                break;
            case LEFT + BOTTOM:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawLeftDivider(canvas, child);
                    drawBottomDivider(canvas, child);
                    drawGridRectDivider(canvas, child.getLeft() - horizontalDividerSize, child.getBottom());
                }
                break;
            case RIGHT + TOP:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawRightDivider(canvas, child);
                    drawTopDivider(canvas, child);
                    drawGridRectDivider(canvas, child.getRight(), child.getTop() - verticalDividerSize);
                }
                break;
            case RIGHT + BOTTOM:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawRightDivider(canvas, child);
                    drawBottomDivider(canvas, child);
                    drawGridRectDivider(canvas, child.getRight(), child.getBottom());
                }
                break;
            case ALL:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    drawRightDivider(canvas, child);
                    drawBottomDivider(canvas, child);
                    drawGridRectDivider(canvas, child.getRight(), child.getBottom());
                    if (i % spanCount == 0) {//第一行
                        drawTopDivider(canvas, child);
                        drawGridRectDivider(canvas, child.getRight(), child.getTop() - verticalDividerSize);
                    }
                    if (i / spanCount == 0) {//第一列
                        drawLeftDivider(canvas, child);
                        drawGridRectDivider(canvas, child.getLeft() - horizontalDividerSize, child.getBottom());
                    }
                    if (i % spanCount == 0 && i / spanCount == 0) {//左上角
                        drawGridRectDivider(canvas, child.getLeft() - horizontalDividerSize, child.getTop() - verticalDividerSize);
                    }
                }
                break;
            case ALL_HALF:
                for (int i = 0; i < childCount; i++) {
                    final View child = parent.getChildAt(i);
                    final int position = parent.getChildAdapterPosition(child);
                    if (i % spanCount != spanCount - 1) {//如果不是最后一行
                        drawBottomDivider(canvas, child);
                    }
                    if (position / spanCount != columns - 1) {//如果不是最后一列
                        drawRightDivider(canvas, child);
                    }
                    //如果不是最后一行且不是最后一列,绘制小方块
                    if (i % spanCount != spanCount - 1 && position / spanCount != columns - 1) {
                        drawGridRectDivider(canvas, child.getRight(), child.getBottom());
                    }
                    if (i % spanCount == 0) {//第一行
                        if (position / spanCount != columns - 1) {//如果不是最后一个
                            final float left = child.getLeft();
                            final float top = child.getTop() - get1_2VerticalSize();
                            final float right = child.getRight() + horizontalDividerSize;
                            final float bottom = child.getTop();
                            drawDivider(canvas, left, top, right, bottom);
                        } else {
                            final float left = child.getLeft();
                            final float top = child.getTop() - get1_2VerticalSize();
                            final float right = child.getRight();
                            final float bottom = child.getTop();
                            drawDivider(canvas, left, top, right, bottom);
                        }
                    }
                    if (i / spanCount == 0) {//第一列
                        if ((i + 1) % spanCount != 0) {//如果不是最后一个
                            final float left = child.getLeft() - get1_2HorizontalSize();
                            final float top = child.getTop();
                            final float right = child.getLeft();
                            final float bottom = child.getBottom() + verticalDividerSize;
                            drawDivider(canvas, left, top, right, bottom);
                        } else {
                            final float left = child.getLeft() - get1_2HorizontalSize();
                            final float top = child.getTop();
                            final float right = child.getLeft();
                            final float bottom = child.getBottom();
                            drawDivider(canvas, left, top, right, bottom);
                        }
                    }
                    if ((i + 1) % spanCount == 0) {//最后一行
                        if (position != actualChildCount - 1) {//去掉右下角
                            final float left = child.getLeft();
                            final float top = child.getBottom();
                            final float right = child.getRight() + horizontalDividerSize;
                            final float bottom = child.getBottom() + get1_2VerticalSize();
                            drawDivider(canvas, left, top, right, bottom);
                        } else {
                            final float left = child.getLeft();
                            final float top = child.getBottom();
                            final float right = child.getRight();
                            final float bottom = child.getBottom() + get1_2VerticalSize();
                            drawDivider(canvas, left, top, right, bottom);
                        }
                    }
                    if (position / spanCount == columns - 1) {//最后一列
                        if ((i + 1) % spanCount != 0) {//去掉右下角
                            final float left = child.getRight();
                            final float top = child.getTop();
                            final float right = child.getRight() + get1_2HorizontalSize();
                            final float bottom = child.getBottom() + verticalDividerSize;
                            drawDivider(canvas, left, top, right, bottom);
                        } else {
                            final float left = child.getRight();
                            final float top = child.getTop();
                            final float right = child.getRight() + get1_2HorizontalSize();
                            final float bottom = child.getBottom();
                            drawDivider(canvas, left, top, right, bottom);
                        }
                    }
                    if (i == 0) {//左上角
                        drawSmallGridRectDivider(canvas, child.getLeft() - get1_2HorizontalSize(), child.getTop() - get1_2VerticalSize());
                    }
                    if (position / spanCount == columns - 1 && i % spanCount == 0) {//右上角
                        drawSmallGridRectDivider(canvas, child.getRight(), child.getTop() - get1_2VerticalSize());
                    }
                    if (i / spanCount == 0 && (i + 1) % spanCount == 0) {//左下角
                        drawSmallGridRectDivider(canvas, child.getLeft() - get1_2HorizontalSize(), child.getBottom());
                    }
                    if (position / spanCount == columns - 1 && (i + 1) % spanCount == 0) {//右下角
                        drawSmallGridRectDivider(canvas, child.getRight(), child.getBottom());
                    }
                }
                break;
        }
    }

    private void drawLeftDivider(final Canvas canvas, final View child) {
        final float left = child.getLeft() - horizontalDividerSize;
        final float top = child.getTop();
        final float right = child.getLeft();
        final float bottom = child.getBottom();
        drawDivider(canvas, left, top, right, bottom);
    }

    private void drawTopDivider(final Canvas canvas, final View child) {
        final float left = child.getLeft();
        final float top = child.getTop() - verticalDividerSize;
        final float right = child.getRight();
        final float bottom = child.getTop();
        drawDivider(canvas, left, top, right, bottom);
    }

    private void drawRightDivider(final Canvas canvas, final View child) {
        final float left = child.getRight();
        final float top = child.getTop();
        final float right = child.getRight() + horizontalDividerSize;
        final float bottom = child.getBottom();
        drawDivider(canvas, left, top, right, bottom);
    }

    private void drawBottomDivider(final Canvas canvas, final View child) {
        final float left = child.getLeft();
        final float top = child.getBottom();
        final float right = child.getRight();
        final float bottom = child.getBottom() + verticalDividerSize;
        drawDivider(canvas, left, top, right, bottom);
    }

    private void drawGridRectDivider(final Canvas canvas, final float left, final float top) {
        final float right = left + horizontalDividerSize;
        final float bottom = top + verticalDividerSize;
        drawDivider(canvas, left, top, right, bottom);
    }

    private void drawSmallGridRectDivider(final Canvas canvas, final float left, final float top) {
        final float right = left + get1_2HorizontalSize();
        final float bottom = top + get1_2VerticalSize();
        drawDivider(canvas, left, top, right, bottom);
    }

    private int get1_2Size() {
        return (int) (size / 2);
    }

    private int get1_2HorizontalSize() {
        return (int) (horizontalDividerSize / 2);
    }

    private int get1_2VerticalSize() {
        return (int) (verticalDividerSize / 2);
    }

    private void drawDivider(final Canvas canvas, final float left, final float top, final float right, final float bottom) {
        if (drawable != null) {
            drawable.setBounds((int) left, (int) top, (int) right, (int) bottom);
            drawable.draw(canvas);
        } else {
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
    }
}
