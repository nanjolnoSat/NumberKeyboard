package com.mishaki.hfrecyclerview.ui;

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
import android.util.SparseArray;
import android.view.View;

import com.mishaki.hfrecyclerview.model.HfDividerModel;
import com.mishaki.hfrecyclerview.util.DividerType;


public class HfRecyclerDivider extends RecyclerView.ItemDecoration {
    private final int DRAW_TYPE_HEADER = 0;
    private final int DRAW_TYPE_FOOTER = 1;

    private float headerSize;
    private float childSize;
    private float footerSize;

    private int headerType;
    private int childType;
    private int footerType;

    private Paint headerPaint = new Paint();
    private Paint childPaint = new Paint();
    private Paint footerPaint = new Paint();

    private Drawable headerDrawable;
    private Drawable childDrawable;
    private Drawable footerDrawable;

    private SparseArray<HfDividerModel> headerDividerModelList = new SparseArray<>();
    private SparseArray<HfDividerModel> footerDividerModelList = new SparseArray<>();

    private Paint clipPaint = new Paint();

    private HfRecyclerDivider(float headerSize, float childSize, float footerSize,
                              int headerType, int childType, int footerType,
                              int headerColor, int childColor, int footerColor,
                              Drawable headerDrawable, Drawable childDrawable, Drawable footerDrawable,
                              Paint headerPaint, Paint childPaint, Paint footerPaint,
                              SparseArray<HfDividerModel> headerDividerModelList, SparseArray<HfDividerModel> footerDividerModelList) {
        this.headerSize = headerSize;
        this.childSize = childSize;
        this.footerSize = footerSize;

        this.headerType = headerType;
        this.childType = childType;
        this.footerType = footerType;

        if (headerPaint != null) {
            this.headerPaint = headerPaint;
        } else {
            initPaint(this.headerPaint, headerColor);
        }
        if (childPaint != null) {
            this.childPaint = childPaint;
        } else {
            initPaint(this.childPaint, childColor);
        }
        if (footerPaint != null) {
            this.footerPaint = footerPaint;
        } else {
            initPaint(this.footerPaint, footerColor);
        }
        this.headerDrawable = headerDrawable;
        this.childDrawable = childDrawable;
        this.footerDrawable = footerDrawable;
        if (headerDividerModelList.size() != 0) {
            this.headerDividerModelList = headerDividerModelList;
        }
        if (footerDividerModelList.size() != 0) {
            this.footerDividerModelList = footerDividerModelList;
        }

        clipPaint.setAntiAlias(true);
        clipPaint.setStyle(Paint.Style.FILL);
        clipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    private void initPaint(Paint paint, int color) {
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
    }

    public static class Build {
        private float headerSize;
        private float childSize;
        private float footerSize;

        private int headerType = DividerType.NONE;
        private int childType = DividerType.MIDDLE;
        private int footerType = DividerType.NONE;

        private int headerColor = 0xffffffff;
        private int childColor = 0xffffffff;
        private int footerColor = 0xffffffff;

        private Drawable headerDrawable;
        private Drawable childDrawable;
        private Drawable footerDrawable;

        private Paint headerPaint;
        private Paint childPaint;
        private Paint footerPaint;

        private SparseArray<HfDividerModel> headerDividerModelList = new SparseArray<>();
        private SparseArray<HfDividerModel> footerDividerModelList = new SparseArray<>();

        public Build setHeaderSize(float headerSize) {
            this.headerSize = headerType;
            return this;
        }

        public Build setChildSize(float childSize) {
            this.childSize = childSize;
            return this;
        }

        public Build setFooterSize(float footerSize) {
            this.footerSize = footerType;
            return this;
        }

        public Build setHeaderType(int type) {
            this.headerType = type;
            return this;
        }

        public Build setChildType(int type) {
            this.childType = type;
            return this;
        }

        public Build setFooterType(int type) {
            this.footerType = type;
            return this;
        }

        public Build setHeaderDividerColor(int color) {
            this.headerColor = color;
            return this;
        }

        public Build setChildDividerColor(int color) {
            this.childColor = color;
            return this;
        }

        public Build setFooterDividerColor(int color) {
            this.footerColor = color;
            return this;
        }

        public Build setHeaderDrawable(Drawable drawable) {
            this.headerDrawable = drawable;
            return this;
        }

        public Build setChildDrawable(Drawable drawable) {
            this.childDrawable = drawable;
            return this;
        }

        public Build setFooterDrawable(Drawable drawable) {
            this.footerDrawable = drawable;
            return this;
        }

        public Build setHeaderDrawable(Context context, int drawableId) {
            this.headerDrawable = ContextCompat.getDrawable(context, drawableId);
            return this;
        }

        public Build setChildDrawable(Context context, int drawableId) {
            this.childDrawable = ContextCompat.getDrawable(context, drawableId);
            return this;
        }

        public Build setFooterDrawable(Context context, int drawableId) {
            this.footerDrawable = ContextCompat.getDrawable(context, drawableId);
            return this;
        }

        public Build setHeaderPaint(Paint paint) {
            this.headerPaint = paint;
            return this;
        }

        public Build setChildPaint(Paint paint) {
            this.childPaint = paint;
            return this;
        }

        public Build setFooterPaint(Paint paint) {
            this.footerPaint = paint;
            return this;
        }

        public Build putHeaderDividerModel(int position, HfDividerModel model) {
            this.headerDividerModelList.put(position, model);
            return this;
        }

        /**
         * @param position 是footer的position,不是adapter的position
         */
        public Build putFooterDividerModel(int position, HfDividerModel model) {
            this.footerDividerModelList.put(position, model);
            return this;
        }

        public HfRecyclerDivider build() {
            return new HfRecyclerDivider(headerSize, childSize, footerSize,
                    headerType, childType, footerType,
                    headerColor, childColor, footerColor,
                    headerDrawable, childDrawable, footerDrawable,
                    headerPaint, childPaint, footerPaint,
                    headerDividerModelList, footerDividerModelList);
        }
    }

    public void putHeaderDividerModel(int position, HfDividerModel model) {
        headerDividerModelList.put(position, model);
    }

    /**
     * @param position 是footer的position,不是adapter的position
     */
    public void putFooterDividerModel(int position, HfDividerModel model) {
        footerDividerModelList.put(position, model);
    }

    public void removeHeaderDividerModel(int position) {
        headerDividerModelList.remove(position);
    }

    /**
     * @param position 是footer的position,不是adapter的position
     */
    public void removeFooterDividerModel(int position) {
        footerDividerModelList.remove(position);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent instanceof HfRecyclerView && (parent.getLayoutManager() instanceof LinearLayoutManager && !(parent.getLayoutManager() instanceof GridLayoutManager)) && parent.getAdapter() instanceof HfRecyclerView.HfAdapter) {
            if (((LinearLayoutManager) parent.getLayoutManager()).getOrientation() == LinearLayoutManager.VERTICAL) {
                calcVerticalItemOffsets(outRect, view, (HfRecyclerView) parent, state);
            } else {
                calcHorizontalItemOffsets(outRect, view, (HfRecyclerView) parent, state);
            }
        }
    }

    private void calcVerticalItemOffsets(final Rect outRect, final View view, final HfRecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int headerCount = parent.getHeaderViewCount();
        int adapterChildCount = ((HfRecyclerView.HfAdapter) parent.getAdapter()).getHfItemCount();
        if (position < headerCount) {//header
            HfDividerModel model = headerDividerModelList.get(position);
            if (model != null) {
                if (model.type == DividerType.TOP) {
                    outRect.top = (int) model.size;
                } else if (model.type == DividerType.BOTTOM) {
                    outRect.bottom = (int) model.size;
                }
            } else {
                switch (headerType) {
                    case DividerType.TOP:
                        outRect.top = (int) headerSize;
                        break;
                    case DividerType.MIDDLE:
                        if (position != headerCount - 1) {
                            outRect.bottom = (int) headerSize;
                        }
                        break;
                    case DividerType.BOTTOM:
                        outRect.bottom = (int) headerSize;
                        break;
                    case DividerType.ALL:
                        if (position == 0) {
                            outRect.top = (int) headerSize;
                        }
                        outRect.bottom = (int) headerSize;
                        break;
                    case DividerType.ALL_HALF:
                        outRect.top = get1_2HeaderSize();
                        outRect.bottom = get1_2HeaderSize();
                        break;
                }
            }
        } else if (position > headerCount + adapterChildCount - 1) {//footer
            HfDividerModel model = footerDividerModelList.get(position - headerCount - adapterChildCount);
            if (model != null) {
                if (model.type == DividerType.TOP) {
                    outRect.top = (int) model.size;
                } else if (model.type == DividerType.BOTTOM) {
                    outRect.bottom = (int) model.size;
                }
            } else {
                switch (footerType) {
                    case DividerType.TOP:
                        outRect.top = (int) footerSize;
                        break;
                    case DividerType.MIDDLE:
                        if (position != parent.getAdapter().getItemCount() - 1) {
                            outRect.bottom = (int) footerSize;
                        }
                        break;
                    case DividerType.BOTTOM:
                        outRect.bottom = (int) footerSize;
                        break;
                    case DividerType.ALL:
                        if (position == headerCount + adapterChildCount) {
                            outRect.top = (int) footerSize;
                        }
                        outRect.bottom = (int) footerSize;
                        break;
                    case DividerType.ALL_HALF:
                        outRect.top = get1_2FooterSize();
                        outRect.bottom = get1_2FooterSize();
                        break;
                }
            }
        } else {
            switch (childType) {
                case DividerType.TOP:
                    outRect.top = (int) childSize;
                    break;
                case DividerType.MIDDLE:
                    if (position != headerCount + adapterChildCount - 1) {
                        outRect.bottom = (int) childSize;
                    }
                    break;
                case DividerType.BOTTOM:
                    outRect.bottom = (int) childSize;
                    break;
                case DividerType.ALL:
                    if (position == headerCount) {
                        outRect.top = (int) childSize;
                    }
                    outRect.bottom = (int) childSize;
                    break;
                case DividerType.ALL_HALF:
                    outRect.top = get1_2ChildSize();
                    outRect.bottom = get1_2ChildSize();
                    break;
            }
        }
    }

    private void calcHorizontalItemOffsets(Rect outRect, View view, HfRecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int headerCount = parent.getHeaderViewCount();
        int adapterChildCount = ((HfRecyclerView.HfAdapter) parent.getAdapter()).getHfItemCount();
        if (position < headerCount) {//header
            HfDividerModel model = headerDividerModelList.get(position);
            if (model != null) {
                if (model.type == DividerType.LEFT) {
                    outRect.left = (int) model.size;
                } else if (model.type == DividerType.RIGHT) {
                    outRect.right = (int) model.size;
                }
            } else {
                switch (headerType) {
                    case DividerType.LEFT:
                        outRect.left = (int) headerSize;
                        break;
                    case DividerType.MIDDLE:
                        if (position != headerCount - 1) {
                            outRect.right = (int) headerSize;
                        }
                        break;
                    case DividerType.RIGHT:
                        outRect.right = (int) headerSize;
                        break;
                    case DividerType.ALL:
                        if (position == 0) {
                            outRect.left = (int) headerSize;
                        }
                        outRect.right = (int) headerSize;
                        break;
                    case DividerType.ALL_HALF:
                        outRect.left = get1_2HeaderSize();
                        outRect.right = get1_2HeaderSize();
                        break;
                }
            }
        } else if (position > headerCount + adapterChildCount - 1) {//footer
            HfDividerModel model = footerDividerModelList.get(position - headerCount - adapterChildCount);
            if (model != null) {
                if (model.type == DividerType.LEFT) {
                    outRect.left = (int) model.size;
                } else if (model.type == DividerType.RIGHT) {
                    outRect.right = (int) model.size;
                }
            } else {
                switch (headerType) {
                    case DividerType.LEFT:
                        outRect.left = (int) footerSize;
                        break;
                    case DividerType.MIDDLE:
                        if (position != parent.getAdapter().getItemCount()) {
                            outRect.right = (int) footerSize;
                        }
                        break;
                    case DividerType.RIGHT:
                        outRect.right = (int) footerSize;
                        break;
                    case DividerType.ALL:
                        if (position == headerCount + adapterChildCount) {
                            outRect.left = (int) footerSize;
                        }
                        outRect.right = (int) footerSize;
                        break;
                    case DividerType.ALL_HALF:
                        outRect.left = get1_2FooterSize();
                        outRect.right = get1_2FooterSize();
                        break;
                }
            }
        } else {
            switch (childType) {
                case DividerType.LEFT:
                    outRect.left = (int) childSize;
                    break;
                case DividerType.MIDDLE:
                    if (position != headerCount + adapterChildCount - 1) {
                        outRect.right = (int) childSize;
                    }
                    break;
                case DividerType.RIGHT:
                    outRect.right = (int) childSize;
                    break;
                case DividerType.ALL:
                    if (position == headerCount) {
                        outRect.left = (int) childSize;
                    }
                    outRect.right = (int) childSize;
                    break;
                case DividerType.ALL_HALF:
                    outRect.left = get1_2ChildSize();
                    outRect.right = get1_2ChildSize();
                    break;
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent instanceof HfRecyclerView && (parent.getLayoutManager() instanceof LinearLayoutManager && !(parent.getLayoutManager() instanceof GridLayoutManager)) && parent.getAdapter() instanceof HfRecyclerView.HfAdapter) {
            int id = c.saveLayer(0f, 0f, parent.getWidth(), parent.getHeight(), null, Canvas.ALL_SAVE_FLAG);
            if (((LinearLayoutManager) parent.getLayoutManager()).getOrientation() == LinearLayoutManager.VERTICAL) {
                drawVerticalDivider(c, (HfRecyclerView) parent, state);
            } else {
                drawHorizontalDivider(c, (HfRecyclerView) parent, state);
            }
            c.drawRect(0f, 0f, parent.getPaddingLeft(), parent.getHeight(), clipPaint);
            c.drawRect(parent.getPaddingLeft(), 0, parent.getWidth() - parent.getPaddingRight(), parent.getPaddingTop(), clipPaint);
            c.drawRect(parent.getWidth() - parent.getPaddingRight(), 0, parent.getWidth(), parent.getHeight(), clipPaint);
            c.drawRect(parent.getPaddingLeft(), parent.getHeight() - parent.getPaddingBottom(), parent.getWidth() - parent.getPaddingRight(), parent.getHeight(), clipPaint);
            c.save(id);
        }
    }

    private void drawVerticalDivider(Canvas c, HfRecyclerView parent, RecyclerView.State state) {
        HfRecyclerView.HfAdapter adapter = (HfRecyclerView.HfAdapter) parent.getAdapter();
        int childCount = parent.getChildCount();
        int headerCount = parent.getHeaderViewCount();
        int adapterChildCount = adapter.getHfItemCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int position = parent.getChildAdapterPosition(child);
            if (position < headerCount) {//header
                HfDividerModel model = headerDividerModelList.get(position);
                //如果该position有设置model就使用model的设置
                if (model != null) {
                    drawVerticalHfModelDivider(c, child, model, DRAW_TYPE_HEADER);
                } else {
                    switch (headerType) {
                        case DividerType.TOP:
                            drawHeaderTopDivider(c, child);
                            break;
                        case DividerType.MIDDLE:
                            if (position != headerCount - 1) {
                                drawHeaderBottomDivider(c, child);
                            }
                            break;
                        case DividerType.BOTTOM:
                            drawHeaderBottomDivider(c, child);
                            break;
                        case DividerType.ALL:
                            if (position == 0) {
                                drawHeaderTopDivider(c, child);
                            } else {
                                drawHeaderBottomDivider(c, child);
                            }
                            break;
                        case DividerType.ALL_HALF:
                            float left = child.getLeft();
                            float right = child.getRight();

                            float top1 = child.getTop() - get1_2HeaderSize();
                            float bottom1 = child.getTop();
                            drawHeaderDivider(c, left, top1, right, bottom1);

                            float top2 = child.getBottom();
                            float bottom2 = child.getBottom() + get1_2HeaderSize();
                            drawHeaderDivider(c, left, top2, right, bottom2);
                            break;
                    }
                }
            } else if (position > headerCount + adapterChildCount - 1) {//footer
                HfDividerModel model = footerDividerModelList.get(position - headerCount - adapterChildCount);
                if (model != null) {
                    drawVerticalHfModelDivider(c, child, model, DRAW_TYPE_FOOTER);
                } else {
                    switch (footerType) {
                        case DividerType.TOP:
                            drawFooterBottomDivider(c, child);
                            break;
                        case DividerType.MIDDLE:
                            if (position != parent.getAdapter().getItemCount() - 1) {
                                drawFooterBottomDivider(c, child);
                            }
                            break;
                        case DividerType.BOTTOM:
                            drawFooterBottomDivider(c, child);
                            break;
                        case DividerType.ALL:
                            if (position == headerCount + adapterChildCount - 1) {
                                drawFooterTopDivider(c, child);
                            }
                            drawFooterBottomDivider(c, child);
                            break;
                        case DividerType.ALL_HALF:
                            float left = child.getLeft();
                            float right = child.getRight();

                            float top1 = child.getTop() - get1_2FooterSize();
                            float bottom1 = child.getTop();
                            drawFooterDivider(c, left, top1, right, bottom1);

                            float top2 = child.getBottom();
                            float bottom2 = child.getBottom() + get1_2FooterSize();
                            drawFooterDivider(c, left, top2, right, bottom2);
                            break;
                    }
                }
            } else {
                switch (childType) {
                    case DividerType.TOP:
                        drawChildTopDivider(c, child);
                        break;
                    case DividerType.MIDDLE:
                        if (position != headerCount + adapterChildCount - 1) {
                            drawChildBottomDivider(c, child);
                        }
                        break;
                    case DividerType.BOTTOM:
                        drawChildBottomDivider(c, child);
                        break;
                    case DividerType.ALL:
                        if (position == headerCount) {
                            drawChildTopDivider(c, child);
                        }
                        drawChildBottomDivider(c, child);
                        break;
                    case DividerType.ALL_HALF:
                        float left = child.getLeft();
                        float right = child.getRight();

                        float top1 = child.getTop() - get1_2ChildSize();
                        float bottom1 = child.getTop();
                        drawChildDivider(c, left, top1, right, bottom1);

                        float top2 = child.getBottom();
                        float bottom2 = child.getBottom() + get1_2ChildSize();
                        drawChildDivider(c, left, top2, right, bottom2);
                        break;
                }
            }
        }
    }

    private void drawHorizontalDivider(Canvas c, HfRecyclerView parent, RecyclerView.State state) {
        HfRecyclerView.HfAdapter adapter = (HfRecyclerView.HfAdapter) parent.getAdapter();
        int childCount = parent.getChildCount();
        int headerCount = parent.getHeaderViewCount();
        int adapterItemCount = adapter.getHfItemCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if (position < headerCount) {//header
                HfDividerModel model = headerDividerModelList.get(position);
                if (model != null) {
                    drawHorizontalHfModelDivider(c, child, model, DRAW_TYPE_HEADER);
                } else {
                    switch (headerType) {
                        case DividerType.LEFT:
                            drawHeaderLeftDivider(c, child);
                            break;
                        case DividerType.MIDDLE:
                            if (position != headerCount - 1) {
                                drawHeaderRightDivider(c, child);
                            }
                            break;
                        case DividerType.RIGHT:
                            drawHeaderRightDivider(c, child);
                            break;
                        case DividerType.ALL:
                            if (position == 0) {
                                drawHeaderLeftDivider(c, child);
                            }
                            drawHeaderRightDivider(c, child);
                            break;
                        case DividerType.ALL_HALF:
                            float top = child.getTop();
                            float bottom = child.getBottom();

                            float left1 = child.getLeft() - get1_2HeaderSize();
                            float right1 = child.getLeft();
                            drawHeaderDivider(c, left1, top, right1, bottom);

                            float left2 = child.getRight();
                            float righ2 = child.getRight() + get1_2HeaderSize();
                            drawHeaderDivider(c, left2, top, righ2, bottom);
                            break;
                    }
                }
            } else if (position > headerCount + adapterItemCount - 1) {//footer
                HfDividerModel model = footerDividerModelList.get(position - headerCount - adapterItemCount);
                if (model != null) {
                    drawHorizontalHfModelDivider(c, child, model, DRAW_TYPE_FOOTER);
                } else {
                    switch (footerType) {
                        case DividerType.LEFT:
                            drawFooterLeftDivider(c, child);
                            break;
                        case DividerType.MIDDLE:
                            if (position != parent.getAdapter().getItemCount() - 1) {
                                drawFooterRightDivider(c, child);
                            }
                            break;
                        case DividerType.RIGHT:
                            drawFooterRightDivider(c, child);
                            break;
                        case DividerType.ALL:
                            if (position == headerCount + adapterItemCount) {
                                drawFooterLeftDivider(c, child);
                            }
                            drawFooterRightDivider(c, child);
                            break;
                        case DividerType.ALL_HALF:
                            float top = child.getTop();
                            float bottom = child.getBottom();

                            float left1 = child.getLeft() - get1_2FooterSize();
                            float right1 = child.getLeft();
                            drawFooterDivider(c, left1, top, right1, bottom);

                            float left2 = child.getRight();
                            float right2 = child.getRight() + get1_2FooterSize();
                            drawFooterDivider(c, left2, top, right2, bottom);
                            break;
                    }
                }
            } else {
                switch (childType) {
                    case DividerType.LEFT:
                        drawChildLeftDivider(c, child);
                        break;
                    case DividerType.MIDDLE:
                        if (position != headerCount + adapterItemCount - 1) {
                            drawChildRightDivider(c, child);
                        }
                        break;
                    case DividerType.RIGHT:
                        drawChildRightDivider(c, child);
                        break;
                    case DividerType.ALL:
                        if (position == headerCount) {
                            drawChildLeftDivider(c, child);
                        }
                        drawChildRightDivider(c, child);
                        break;
                    case DividerType.ALL_HALF:
                        float top = child.getTop();
                        float bottom = child.getBottom();

                        float left1 = child.getLeft() - get1_2ChildSize();
                        float right1 = child.getLeft();
                        drawChildDivider(c, left1, top, right1, bottom);

                        float left2 = child.getRight();
                        float right2 = child.getRight() + get1_2ChildSize();
                        drawChildDivider(c, left2, top, right2, bottom);
                        break;
                }
            }
        }
    }

    private void drawVerticalHfModelDivider(Canvas c, View child, HfDividerModel model, int drawType) {
        if (model.type == DividerType.TOP) {
            float left = child.getLeft();
            float top = child.getTop() - model.size;
            float right = child.getRight();
            float bottom = child.getTop();
            if (drawType == DRAW_TYPE_HEADER) {
                drawHeaderModelDivider(c, model, left, top, right, bottom);
            } else {
                drawFooterModelDivider(c, model, left, top, right, bottom);
            }
        } else if (model.type == DividerType.BOTTOM) {
            float left = child.getLeft();
            float top = child.getBottom();
            float right = child.getRight();
            float bottom = child.getBottom() + model.size;
            if (drawType == DRAW_TYPE_HEADER) {
                drawHeaderModelDivider(c, model, left, top, right, bottom);
            } else {
                drawFooterModelDivider(c, model, left, top, right, bottom);
            }
        }
    }

    private void drawHorizontalHfModelDivider(Canvas c, View child, HfDividerModel model, int drawType) {
        if (model.type == DividerType.LEFT) {
            float left = child.getLeft() - model.size;
            float top = child.getTop();
            float right = child.getRight();
            float bottom = child.getBottom();
            if (drawType == DRAW_TYPE_FOOTER) {
                drawFooterModelDivider(c, model, left, top, right, bottom);
            } else {
                drawHeaderModelDivider(c, model, left, top, right, bottom);
            }
        } else if (model.type == DividerType.RIGHT) {
            float left = child.getRight();
            float top = child.getTop();
            float right = child.getRight() + model.size;
            float bottom = child.getBottom();
            if (drawType == DRAW_TYPE_FOOTER) {
                drawFooterModelDivider(c, model, left, top, right, bottom);
            } else {
                drawHeaderModelDivider(c, model, left, top, right, bottom);
            }
        }
    }

    private void drawHeaderModelDivider(Canvas c, HfDividerModel model, float left, float top, float right, float bottom) {
        drawModelDivider(c, headerPaint, model, left, top, right, bottom);
    }

    private void drawFooterModelDivider(Canvas c, HfDividerModel model, float left, float top, float right, float bottom) {
        drawModelDivider(c, footerPaint, model, left, top, right, bottom);
    }

    private void drawModelDivider(Canvas c, Paint drawPaint, HfDividerModel model, float left, float top, float right, float bottom) {
        Paint paint = drawPaint;
        if (model.paint != null) {
            paint = model.paint;
        }
        drawDivider(c, paint, model.drawable, left, top, right, bottom);
    }

    private void drawHeaderLeftDivider(Canvas c, View child) {
        float left = child.getLeft() - headerSize;
        float top = child.getTop();
        float right = child.getLeft();
        float bottom = child.getBottom();
        drawHeaderDivider(c, left, top, right, bottom);
    }

    private void drawHeaderTopDivider(Canvas c, View child) {
        float left = child.getLeft();
        float top = child.getTop() - headerSize;
        float right = child.getRight();
        float bottom = child.getTop();
        drawHeaderDivider(c, left, top, right, bottom);
    }

    private void drawHeaderRightDivider(Canvas c, View child) {
        float left = child.getRight();
        float top = child.getTop();
        float right = child.getRight() + headerSize;
        float bottom = child.getBottom();
        drawHeaderDivider(c, left, top, right, bottom);
    }

    private void drawHeaderBottomDivider(Canvas c, View child) {
        float left = child.getLeft();
        float top = child.getBottom();
        float right = child.getRight();
        float bottom = child.getBottom() + headerSize;
        drawHeaderDivider(c, left, top, right, bottom);
    }

    private void drawHeaderDivider(Canvas c, float left, float top, float right, float bottom) {
        drawDivider(c, headerPaint, headerDrawable, left, top, right, bottom);
    }

    private void drawFooterLeftDivider(Canvas c, View child) {
        float left = child.getLeft() - footerSize;
        float top = child.getTop();
        float right = child.getLeft();
        float bottom = child.getBottom();
        drawFooterDivider(c, left, top, right, bottom);
    }

    private void drawFooterTopDivider(Canvas c, View child) {
        float left = child.getLeft();
        float top = child.getTop() - footerSize;
        float right = child.getRight();
        float bottom = child.getTop();
        drawFooterDivider(c, left, top, right, bottom);
    }

    private void drawFooterRightDivider(Canvas c, View child) {
        float left = child.getRight();
        float top = child.getTop();
        float right = child.getRight() + footerSize;
        float bottom = child.getBottom();
        drawFooterDivider(c, left, top, right, bottom);
    }

    private void drawFooterBottomDivider(Canvas c, View child) {
        float left = child.getLeft();
        float top = child.getBottom();
        float right = child.getRight();
        float bottom = child.getBottom() + footerSize;
        drawFooterDivider(c, left, top, right, bottom);
    }

    private void drawFooterDivider(Canvas c, float left, float top, float right, float bottom) {
        drawDivider(c, footerPaint, footerDrawable, left, top, right, bottom);
    }

    private void drawChildLeftDivider(Canvas c, View child) {
        float left = child.getLeft() - childSize;
        float top = child.getTop();
        float right = child.getLeft();
        float bottom = child.getBottom();
        drawChildDivider(c, left, top, right, bottom);
    }

    private void drawChildTopDivider(Canvas c, View child) {
        float left = child.getLeft();
        float top = child.getTop() - childSize;
        float right = child.getRight();
        float bottom = child.getTop();
        drawChildDivider(c, left, top, right, bottom);
    }

    private void drawChildRightDivider(Canvas c, View child) {
        float left = child.getRight();
        float top = child.getTop();
        float right = child.getRight() + childSize;
        float bottom = child.getBottom();
        drawChildDivider(c, left, top, right, bottom);
    }

    private void drawChildBottomDivider(Canvas c, View child) {
        float left = child.getLeft();
        float top = child.getBottom();
        float right = child.getRight();
        float bottom = child.getBottom() + childSize;
        drawChildDivider(c, left, top, right, bottom);
    }

    private void drawChildDivider(Canvas c, float left, float top, float right, float bottom) {
        drawDivider(c, childPaint, childDrawable, left, top, right, bottom);
    }

    private void drawDivider(Canvas c, Paint paint, Drawable drawable, float left, float top, float right, float bottom) {
        if (drawable != null) {
            drawable.setBounds((int) (left), (int) (top), (int) (right), (int) (bottom));
            drawable.draw(c);
        } else {
            c.drawRect(left, top, right, bottom, paint);
        }
    }

    private int get1_2ChildSize() {
        return (int) (childSize / 2);
    }

    private int get1_2HeaderSize() {
        return (int) (headerSize / 2);
    }

    private int get1_2FooterSize() {
        return (int) (footerSize / 2);
    }
}