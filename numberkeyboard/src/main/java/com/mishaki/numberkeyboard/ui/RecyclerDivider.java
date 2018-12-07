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
    /**
     * 在child的左边绘制,可用于LinearLayoutManager的horizontal和GridLayoutManager.
     * 在GridLayoutManager中可与TOP、BOTTOM相加一起使用.
     */
    public static final int LEFT = 0b1;
    /**
     * 在2个child中间绘制.
     */
    public static final int MIDDLE = 0b100;
    /**
     * 在child的右边绘制,可用于LinearLayoutManager的horizontal和GridLayoutManager.
     * 在GridLayoutManager中可与TOP、BOTTOM相加一起使用.
     */
    public static final int RIGHT = 0b10000;
    /**
     * 在child的上面绘制,可用于LinearLayoutManager的vertical和GridLayoutManager.
     * 在GridLayoutManager中可与LEFT、RIGHT相加一起使用.
     */
    public static final int TOP = 0b1000000;
    /**
     * 在child的下面绘制,可用于LinearLayoutManager的vertical和GridLayoutManager.
     * 在GridLayoutManager中可与LEFT、RIGHT相加一起使用.
     */
    public static final int BOTTOM = 0b100000000;
    /**
     * 在child的四周绘制
     */
    public static final int ALL = 0b10000000000;
    /**
     * 在child的四周绘制,边缘部分宽度为原来的一半
     */
    public static final int ALL_HALF = 0b1000000000000;

    protected float size;
    private boolean isReviseSize;

    private int type;

    private Paint paint = new Paint();
    private Paint clipPaint = new Paint();

    private float horizontalDividerSize;
    private float verticalDividerSize;

    private Drawable drawable;

    private RecyclerDivider(float size, Paint paint, float horizontalDividerSize, float verticalDividerSize, boolean isReviseSize, int color, int type, Drawable drawable) {
        this.size = size;
        this.horizontalDividerSize = horizontalDividerSize;
        this.verticalDividerSize = verticalDividerSize;
        this.isReviseSize = isReviseSize;
        this.type = type;
        this.drawable = drawable;

        if (paint != null) {
            this.paint = paint;
        } else {
            this.paint.setAntiAlias(true);
            this.paint.setStyle(Paint.Style.FILL);
            this.paint.setColor(color);
        }

        clipPaint.setAntiAlias(true);
        clipPaint.setColor(0x00ffffff);
        clipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    public static final class Build {
        private float size;
        private float horizontalDividerSize = -1;
        private float verticalDividerSize = -1;
        private boolean isReviseSize;

        private int color = 0xffffffff;

        private int type = RecyclerDivider.MIDDLE;
        private Drawable drawable;

        private Paint paint;

        /**
         * 当没有设置horizontalDividerSize和verticalDividerSize的时候,会顺便设置
         */
        public Build setSize(float size) {
            this.size = size;
            if (this.horizontalDividerSize == -1) {
                this.horizontalDividerSize = size;
            }
            if (this.verticalDividerSize == -1) {
                this.verticalDividerSize = size;
            }
            return this;
        }

        /**
         * 设置GridLayoutManager的水平分割线的宽度
         */
        public Build setHorizontalDividerSize(float horizontalDividerSize) {
            this.horizontalDividerSize = horizontalDividerSize;
            return this;
        }

        /**
         * 设置GridLayoutManager的垂直分割线的宽度
         */
        public Build setVerticalDividerSize(float verticalDividerSize) {
            this.verticalDividerSize = verticalDividerSize;
            return this;
        }

        /**
         * 需要修正必须满足3个条件:
         * 1,LayoutManager必须为GridLayoutManager
         * 2,Divider的type为MIDDLE、ALL、ALL_HALF
         * 3,行数/列数超过一行/列
         * tips:
         * 1):当orientation为horizontal时,会使verticalSize的倍数为spanCount的倍数,horizontalSize的倍数为2的倍数.
         * 2):当orientation为vertical时,会使horizontalSize的倍数为spanCount的倍数,verticalSize的倍数为2的倍数.
         * 3):只有type为ALL_HALF时才会进行2倍的修正,其他type并不会
         */
        public Build isReviseSize(boolean isReviseSize) {
            this.isReviseSize = isReviseSize;
            return this;
        }

        public Build setColor(int color) {
            this.color = color;
            return this;
        }

        /**
         * 默认为MIDDLE
         */
        public Build setType(int type) {
            this.type = type;
            return this;
        }

        public Build setDrawable(Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        public Build setDrawable(Context context, int drawableResId) {
            this.drawable = ContextCompat.getDrawable(context, drawableResId);
            return this;
        }

        public Build setPaint(Paint paint) {
            this.paint = paint;
            return this;
        }

        public final RecyclerDivider build() {
            return new RecyclerDivider(size, paint, horizontalDividerSize, verticalDividerSize, isReviseSize, color, type, drawable);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
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

    private void calcHorizontalItemOffsets(Rect outRect, View view, RecyclerView parent) {
        int index = parent.getChildAdapterPosition(view);
        int count = parent.getAdapter().getItemCount();
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

    private void calcVerticalItemOffsets(Rect outRect, View view, RecyclerView parent) {
        int index = parent.getChildAdapterPosition(view);
        int count = parent.getAdapter().getItemCount();
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

    private void calcGridVerticalItemOffsets(Rect outRect, View view, RecyclerView parent) {
        int index = parent.getChildAdapterPosition(view);
        int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
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
                /*
                 * 由于宽度是有限的,如果只是简单的判断是否第一个然后设置间隔的话,每个child的宽度不都一样.
                 * 所以要换别的解决方式.
                 * 1,假设一行有6个child,则有5个间隔,即每个child要负责间隔的5/6.
                 * 2,第一个的right毫无疑问是6-1/6,因为第一个的left必须为0/6.
                 * 3,第二个的left为1/6,right为4/6
                 * 4,最后一个的left是6-1/6,right为0/6.
                 * 5,可以发现一个规律,child的left的分子由0递增至6-1,right由6-1递减至0.
                 * 6,所以只要计算出child在这行的第n个,就能计算出left和right
                 * */
                int childCount = parent.getAdapter().getItemCount();
                int lines;
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
                //和MIDDLE同理,5个间隔变成7个,所以5/6变成7/6.最开始的left由0/6变成6/6,right变成1/6
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

    private void calcGridHorizontalItemOffsets(Rect outRect, View view, RecyclerView parent) {
        int index = parent.getChildAdapterPosition(view);
        int spanCount = ((GridLayoutManager) (parent.getLayoutManager())).getSpanCount();
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
                int childCount = parent.getAdapter().getItemCount();
                int columns;
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
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        //当有设置padding的时候,在padding部分子view不会显示,但分隔符会显示,所以需要利用遮罩模式和图层剪掉
        int id = c.saveLayer(0f, 0f, parent.getWidth(), parent.getHeight(), null, Canvas.ALL_SAVE_FLAG);
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
        //覆盖部分clear掉
        c.drawRect(0f, 0f, parent.getPaddingLeft(), parent.getHeight(), clipPaint);
        c.drawRect(parent.getPaddingLeft(), 0, parent.getWidth() - parent.getPaddingRight(), parent.getPaddingTop(), clipPaint);
        c.drawRect(parent.getWidth() - parent.getPaddingRight(), 0, parent.getWidth(), parent.getHeight(), clipPaint);
        c.drawRect(parent.getPaddingLeft(), parent.getHeight() - parent.getPaddingBottom(), parent.getWidth() - parent.getPaddingRight(), parent.getHeight(), clipPaint);
        c.save(id);
    }

    private void drawHorizontalDivider(Canvas canvas, RecyclerView parent) {
        int childCount = parent.getChildCount();
        switch (type) {
            case LEFT:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawLeftDivider(canvas, child);
                }
                break;
            case MIDDLE:
                final int actualChildCount = parent.getAdapter().getItemCount();
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    int position = parent.getChildLayoutPosition(child);
                    if (position != actualChildCount - 1) {
                        drawRightDivider(canvas, child);
                    }
                }
                break;
            case RIGHT:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawRightDivider(canvas, child);
                }
                break;
            case ALL:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawRightDivider(canvas, child);
                    if (i == 0) {
                        drawLeftDivider(canvas, child);
                    }
                }
                break;
            case ALL_HALF:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);

                    float top = child.getTop();
                    float bottom = child.getBottom();

                    float left1 = child.getLeft() - get1_2Size();
                    float right1 = child.getLeft();
                    drawDivider(canvas, left1, top, right1, bottom);

                    float left2 = child.getRight();
                    float right2 = child.getRight() + get1_2Size();
                    drawDivider(canvas, left2, top, right2, bottom);
                }
                break;
        }
    }

    private void drawVerticalDivider(Canvas canvas, RecyclerView parent) {
        int childCount = parent.getChildCount();
        switch (type) {
            case TOP:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawTopDivider(canvas, child);
                }
                break;
            case MIDDLE:
                int actualChildCount = parent.getAdapter().getItemCount();
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    int position = parent.getChildLayoutPosition(child);
                    if (position != actualChildCount - 1) {
                        drawBottomDivider(canvas, child);
                    }
                }
                break;
            case BOTTOM:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawBottomDivider(canvas, child);
                }
                break;
            case ALL:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawBottomDivider(canvas, child);
                    if (i == 0) {
                        drawTopDivider(canvas, child);
                    }
                }
                break;
            case ALL_HALF:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);

                    float left = child.getLeft();
                    float right = child.getRight();

                    float top1 = child.getTop() - get1_2Size();
                    float bottom1 = child.getTop();
                    drawDivider(canvas, left, top1, right, bottom1);

                    float top2 = child.getBottom();
                    float bottom2 = child.getBottom() + get1_2Size();
                    drawDivider(canvas, left, top2, right, bottom2);
                }
                break;
        }
    }

    private void drawGridVerticalDivider(Canvas canvas, RecyclerView parent) {
        int childCount = parent.getChildCount();
        int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        int actualChildCount = parent.getAdapter().getItemCount();
        int actualLines;
        if (actualChildCount % spanCount == 0) {
            actualLines = actualChildCount / spanCount;
        } else {
            actualLines = actualChildCount / spanCount + 1;
        }
        switch (type) {
            case LEFT:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawLeftDivider(canvas, child);
                }
                break;
            case TOP:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawTopDivider(canvas, child);
                }
                break;
            case RIGHT:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawRightDivider(canvas, child);
                }
                break;
            case BOTTOM:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawBottomDivider(canvas, child);
                }
                break;
            case MIDDLE:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    int position = parent.getChildLayoutPosition(child);
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
                    View child = parent.getChildAt(i);
                    drawLeftDivider(canvas, child);
                    drawTopDivider(canvas, child);
                    drawGridRectDivider(canvas, child.getLeft() - horizontalDividerSize, child.getTop() - verticalDividerSize);
                }
                break;
            case LEFT + BOTTOM:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawLeftDivider(canvas, child);
                    drawBottomDivider(canvas, child);
                    drawGridRectDivider(canvas, child.getLeft() - horizontalDividerSize, child.getBottom());
                }
                break;
            case RIGHT + TOP:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawRightDivider(canvas, child);
                    drawTopDivider(canvas, child);
                    drawGridRectDivider(canvas, child.getRight(), child.getTop() - verticalDividerSize);
                }
                break;
            case RIGHT + BOTTOM:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawRightDivider(canvas, child);
                    drawBottomDivider(canvas, child);
                    drawGridRectDivider(canvas, child.getRight(), child.getBottom());
                }
                break;
            case ALL:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
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
                    View child = parent.getChildAt(i);
                    int position = parent.getChildAdapterPosition(child);
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
                            float left = child.getLeft() - get1_2HorizontalSize();
                            float top = child.getTop();
                            float right = child.getLeft();
                            float bottom = child.getBottom() + verticalDividerSize;
                            drawDivider(canvas, left, top, right, bottom);
                        } else {
                            float left = child.getLeft() - get1_2HorizontalSize();
                            float top = child.getTop();
                            float right = child.getLeft();
                            float bottom = child.getBottom();
                            drawDivider(canvas, left, top, right, bottom);
                        }
                    }
                    //第一行
                    if (i / spanCount == 0) {
                        if ((i + 1) % spanCount != 0) {//去掉一行的最后一个
                            float left = child.getLeft();
                            float top = child.getTop() - get1_2VerticalSize();
                            float right = child.getRight() + horizontalDividerSize;
                            float bottom = child.getTop();
                            drawDivider(canvas, left, top, right, bottom);
                        } else {
                            float left = child.getLeft();
                            float top = child.getTop() - get1_2VerticalSize();
                            float right = child.getRight();
                            float bottom = child.getTop();
                            drawDivider(canvas, left, top, right, bottom);
                        }
                    }
                    if ((i + 1) % spanCount == 0) {//最后一列
                        if (position != actualChildCount - 1) {//去掉最后一个
                            float left = child.getRight();
                            float top = child.getTop();
                            float right = child.getRight() + get1_2HorizontalSize();
                            float bottom = child.getBottom() + verticalDividerSize;
                            drawDivider(canvas, left, top, right, bottom);
                        } else {//最后一个
                            float left = child.getRight();
                            float top = child.getTop();
                            float right = child.getRight() + get1_2HorizontalSize();
                            float bottom = child.getBottom();
                            drawDivider(canvas, left, top, right, bottom);
                        }
                    }
                    if (position / spanCount == actualLines - 1) {//最后一行
                        if (position != actualChildCount - 1 || (i + 1) % spanCount != 0) {//去掉最后一个
                            float left = child.getLeft();
                            float top = child.getBottom();
                            float right = child.getRight() + horizontalDividerSize;
                            float bottom = child.getBottom() + get1_2VerticalSize();
                            drawDivider(canvas, left, top, right, bottom);
                        } else {//最后一个
                            float left = child.getLeft();
                            float top = child.getBottom();
                            float right = child.getRight();
                            float bottom = child.getBottom() + get1_2VerticalSize();
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

    private void drawGridHorizontalDivider(Canvas canvas, RecyclerView parent) {
        int childCount = parent.getChildCount();
        int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        int actualChildCount = parent.getAdapter().getItemCount();
        int columns;
        if (actualChildCount % spanCount == 0) {
            columns = actualChildCount / spanCount;
        } else {
            columns = actualChildCount / spanCount + 1;
        }
        switch (type) {
            case LEFT:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawLeftDivider(canvas, child);
                }
                break;
            case TOP:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawTopDivider(canvas, child);
                }
                break;
            case RIGHT:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawRightDivider(canvas, child);
                }
                break;
            case BOTTOM:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawBottomDivider(canvas, child);
                }
                break;
            case MIDDLE:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    int position = parent.getChildLayoutPosition(child);
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
                    View child = parent.getChildAt(i);
                    drawLeftDivider(canvas, child);
                    drawTopDivider(canvas, child);
                    drawGridRectDivider(canvas, child.getLeft() - horizontalDividerSize, child.getTop() - verticalDividerSize);
                }
                break;
            case LEFT + BOTTOM:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawLeftDivider(canvas, child);
                    drawBottomDivider(canvas, child);
                    drawGridRectDivider(canvas, child.getLeft() - horizontalDividerSize, child.getBottom());
                }
                break;
            case RIGHT + TOP:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    drawRightDivider(canvas, child);
                    drawTopDivider(canvas, child);
                    drawGridRectDivider(canvas, child.getRight(), child.getTop() - verticalDividerSize);
                }
                break;
            case RIGHT + BOTTOM:
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
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
                    View child = parent.getChildAt(i);
                    int position = parent.getChildAdapterPosition(child);
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
                            float left = child.getLeft();
                            float top = child.getTop() - get1_2VerticalSize();
                            float right = child.getRight() + horizontalDividerSize;
                            float bottom = child.getTop();
                            drawDivider(canvas, left, top, right, bottom);
                        } else {
                            float left = child.getLeft();
                            float top = child.getTop() - get1_2VerticalSize();
                            float right = child.getRight();
                            float bottom = child.getTop();
                            drawDivider(canvas, left, top, right, bottom);
                        }
                    }
                    if (i / spanCount == 0) {//第一列
                        if ((i + 1) % spanCount != 0) {//如果不是最后一个
                            float left = child.getLeft() - get1_2HorizontalSize();
                            float top = child.getTop();
                            float right = child.getLeft();
                            float bottom = child.getBottom() + verticalDividerSize;
                            drawDivider(canvas, left, top, right, bottom);
                        } else {
                            float left = child.getLeft() - get1_2HorizontalSize();
                            float top = child.getTop();
                            float right = child.getLeft();
                            float bottom = child.getBottom();
                            drawDivider(canvas, left, top, right, bottom);
                        }
                    }
                    if ((i + 1) % spanCount == 0) {//最后一行
                        if (position != actualChildCount - 1) {//去掉右下角
                            float left = child.getLeft();
                            float top = child.getBottom();
                            float right = child.getRight() + horizontalDividerSize;
                            float bottom = child.getBottom() + get1_2VerticalSize();
                            drawDivider(canvas, left, top, right, bottom);
                        } else {
                            float left = child.getLeft();
                            float top = child.getBottom();
                            float right = child.getRight();
                            float bottom = child.getBottom() + get1_2VerticalSize();
                            drawDivider(canvas, left, top, right, bottom);
                        }
                    }
                    if (position / spanCount == columns - 1) {//最后一列
                        if ((i + 1) % spanCount != 0) {//去掉右下角
                            float left = child.getRight();
                            float top = child.getTop();
                            float right = child.getRight() + get1_2HorizontalSize();
                            float bottom = child.getBottom() + verticalDividerSize;
                            drawDivider(canvas, left, top, right, bottom);
                        } else {
                            float left = child.getRight();
                            float top = child.getTop();
                            float right = child.getRight() + get1_2HorizontalSize();
                            float bottom = child.getBottom();
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

    private void drawLeftDivider(Canvas canvas, View child) {
        float left = child.getLeft() - horizontalDividerSize;
        float top = child.getTop();
        float right = child.getLeft();
        float bottom = child.getBottom();
        drawDivider(canvas, left, top, right, bottom);
    }

    private void drawTopDivider(Canvas canvas, View child) {
        float left = child.getLeft();
        float top = child.getTop() - verticalDividerSize;
        float right = child.getRight();
        float bottom = child.getTop();
        drawDivider(canvas, left, top, right, bottom);
    }

    private void drawRightDivider(Canvas canvas, View child) {
        float left = child.getRight();
        float top = child.getTop();
        float right = child.getRight() + horizontalDividerSize;
        float bottom = child.getBottom();
        drawDivider(canvas, left, top, right, bottom);
    }

    private void drawBottomDivider(Canvas canvas, View child) {
        float left = child.getLeft();
        float top = child.getBottom();
        float right = child.getRight();
        float bottom = child.getBottom() + verticalDividerSize;
        drawDivider(canvas, left, top, right, bottom);
    }

    private void drawGridRectDivider(Canvas canvas, float left, float top) {
        float right = left + horizontalDividerSize;
        float bottom = top + verticalDividerSize;
        drawDivider(canvas, left, top, right, bottom);
    }

    private void drawSmallGridRectDivider(Canvas canvas, float left, float top) {
        float right = left + get1_2HorizontalSize();
        float bottom = top + get1_2VerticalSize();
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

    private void drawDivider(Canvas canvas, float left, float top, float right, float bottom) {
        if (drawable != null) {
            drawable.setBounds((int) left, (int) top, (int) right, (int) bottom);
            drawable.draw(canvas);
        } else {
            canvas.drawRect(left, top, right, bottom, paint);
        }
    }
}