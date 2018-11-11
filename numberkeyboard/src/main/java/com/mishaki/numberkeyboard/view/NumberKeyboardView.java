package com.mishaki.numberkeyboard.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mishaki.numberkeyboard.R;


public class NumberKeyboardView extends RecyclerView {
    private float itemHeight = 0f;
    private final NkAdapter adapter;
    private boolean isSwapClearDelPosition = false;

    public NumberKeyboardView(Context context) {
        this(context, null, 0);
    }

    public NumberKeyboardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberKeyboardView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NumberKeyboardView);
            isSwapClearDelPosition = array.getBoolean(R.styleable.NumberKeyboardView_nkv_isSwapClearDelPosition, false);
            array.recycle();
        }
        super.setLayoutManager(new GridLayoutManager(context, 3){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        adapter = new NkAdapter(this);
        adapter.setItemHeight(itemHeight);
        super.setAdapter(adapter);
        setNestedScrollingEnabled(false);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        int width = MeasureSpec.getSize(widthSpec);
        itemHeight = width * 0.167f;
        adapter.itemWidth = width / 3f;
        adapter.itemHeight = itemHeight;
        adapter.textSize = width * 0.04f;
        super.setAdapter(adapter);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.itemHeight = w * 0.167f;
        adapter.itemWidth = w / 3f;
        adapter.itemHeight = itemHeight;
        if (!adapter.isChangeTextSize) {
            adapter.textSize = w * 0.04f;
        }
        super.setAdapter(adapter);
    }

    public final void setItemHeight(final float itemHeight) {
        this.itemHeight = itemHeight;
        requestLayout();
        adapter.setItemHeight(itemHeight);
    }

    public final NkAdapter getNkAdapter() {
        return adapter;
    }

    @Override
    public void setAdapter(Adapter adapter) {

    }

    private void setMyAdapter(Adapter adapter){
        super.setAdapter(adapter);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {

    }

    public static final class NkAdapter extends RecyclerView.Adapter<NkAdapter.ViewHolder> {
        private final int TYPE_NUMBER = 0;
        private final int TYPE_CLEAR = 1;
        private final int TYPE_DEL = 2;
        private final SparseArray<View> viewMap = new SparseArray();
        private float itemHeight;
        private float itemWidth;
        private float textSize;
        private int textSizeUnit = TypedValue.COMPLEX_UNIT_PX;
        private int textColor = 0xff000000;
        private int textViewGraivity = Gravity.CENTER;
        public CreateViewHolderCallback callback;
        private OnNumberKeyboardClickListener listener;
        private boolean isSwapClearDelPosition = false;
        private boolean isChangeTextSize = false;
        private NumberKeyboardView obj;

        public NkAdapter(NumberKeyboardView obj) {
            this.obj = obj;
            this.isSwapClearDelPosition = obj.isSwapClearDelPosition;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                viewMap.clear();
            }
            final int actualType = getActualViewType(viewType);
            final View itemView;
            switch (actualType) {
                case TYPE_CLEAR:
                    final ImageView clearView = new ImageView(parent.getContext());
                    final RecyclerView.LayoutParams clearParams = new RecyclerView.LayoutParams((int) itemWidth, (int) (itemHeight));
                    clearView.setLayoutParams(clearParams);
                    clearView.setPadding(0,(int)(itemHeight / 4),0,(int)(itemHeight / 4));
                    clearView.setBackgroundColor(0xffffffff);
                    clearView.setImageResource(R.mipmap.clear);
                    if (callback != null) {
                        final View actualView = callback.setClearViewAttribute(parent.getContext(), clearView);
                        if (actualView != null) {
                            itemView = actualView;
                        } else {
                            itemView = clearView;
                            viewMap.put(viewType, itemView);
                        }
                    } else {
                        itemView = clearView;
                        viewMap.put(viewType, itemView);
                    }
                    break;
                case TYPE_DEL:
                    final ImageView delView = new ImageView(parent.getContext());
                    final RecyclerView.LayoutParams delParams = new RecyclerView.LayoutParams((int) itemWidth, (int) (itemHeight));
                    delView.setLayoutParams(delParams);
                    delView.setPadding(0,(int)(itemHeight / 4),0,(int)(itemHeight / 4));
                    delView.setImageResource(R.mipmap.del);
                    delView.setBackgroundColor(0xffffffff);
                    if (callback != null) {
                        final View actualView = callback.setDelViewAttribute(parent.getContext(), delView);
                        if (actualView != null) {
                            itemView = actualView;
                        } else {
                            itemView = delView;
                            viewMap.put(viewType, itemView);
                        }
                    } else {
                        itemView = delView;
                        viewMap.put(viewType, itemView);
                    }
                    break;
                default:
                    final TextView numberView = new TextView(parent.getContext());
                    final RecyclerView.LayoutParams numberParams = new RecyclerView.LayoutParams((int) itemWidth, (int) (itemHeight));
                    numberView.setLayoutParams(numberParams);

                    numberView.setTextColor(textColor);
                    numberView.setGravity(textViewGraivity);
                    numberView.setBackgroundResource(R.drawable.number_keyboard_bg);
                    numberView.setTextSize(textSizeUnit, textSize);
                    if (callback != null) {
                        final View actualView = callback.setNumberViewAttribute(parent.getContext(), numberView, viewType);
                        if (actualView != null) {
                            itemView = actualView;
                        } else {
                            itemView = numberView;
                            viewMap.put(viewType, itemView);
                        }
                    } else {
                        itemView = numberView;
                        viewMap.put(viewType, itemView);
                    }
                    break;
            }
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            View view = viewMap.get(position);
            int viewType = getActualViewType(position);
            if (viewType == TYPE_NUMBER && view == holder.itemView && view instanceof TextView) {
                TextView textView = (TextView) view;
                if (position <= 8) {
                    textView.setText(String.valueOf(position + 1));
                } else {
                    textView.setText("0");
                }
            }
            if (listener != null) {
                switch (viewType) {
                    case TYPE_CLEAR:
                        holder.itemView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listener.onClearClick();
                            }
                        });
                        break;
                    case TYPE_DEL:
                        holder.itemView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listener.onDelClick();
                            }
                        });
                        break;
                    default:
                        holder.itemView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (holder.getAdapterPosition() <= 8) {
                                    listener.onNumberClick(holder.getAdapterPosition() + 1);
                                } else {
                                    listener.onNumberClick(0);
                                }
                            }
                        });
                        break;
                }
            }
        }

        @Override
        public int getItemCount() {
            return 12;
        }

        public final NkAdapter setItemHeight(final float itemHeight) {
            this.itemHeight = itemHeight;
            obj.setAdapter(this);
            return this;
        }

        public final NkAdapter setTextSize(final float textSize) {
            isChangeTextSize = true;
            this.textSize = textSize;
            obj.setMyAdapter(this);
            return this;
        }

        public final NkAdapter setTextSizeUnit(final int textSizeUnit) {
            this.textSizeUnit = textSizeUnit;
            obj.setMyAdapter(this);
            return this;
        }

        public final NkAdapter setTextColor(final int textColor) {
            this.textColor = textColor;
            obj.setMyAdapter(this);
            return this;
        }

        public final NkAdapter setTextViewGraivity(final int graivity) {
            this.textViewGraivity = graivity;
            obj.setMyAdapter(this);
            return this;
        }

        public final NkAdapter setTextViewAttribute(final TextViewAttribute attribute) {
            if (attribute.textSize != -1f) {
                this.textSize = attribute.textSize;
            }
            if (attribute.textSizeUnit != -1) {
                this.textSizeUnit = attribute.textSizeUnit;
            }
            this.textColor = attribute.textColor;
            this.textViewGraivity = attribute.gravity;
            obj.setMyAdapter(this);
            return this;
        }

        public final NkAdapter setOnNumberKeyboradClickListener(OnNumberKeyboardClickListener listener) {
            this.listener = listener;
            return this;
        }

        @Override
        public int getItemViewType(final int position) {
            return position;
        }

        private int getActualViewType(final int position) {
            final int type;
            if (isSwapClearDelPosition) {
                switch (position) {
                    case 11:
                        type = TYPE_CLEAR;
                        break;
                    case 9:
                        type = TYPE_DEL;
                        break;
                    default:
                        type = TYPE_NUMBER;
                        break;
                }
            } else {
                switch (position) {
                    case 9:
                        type = TYPE_CLEAR;
                        break;
                    case 11:
                        type = TYPE_DEL;
                        break;
                    default:
                        type = TYPE_NUMBER;
                        break;
                }
            }
            return type;
        }

        interface OnNumberKeyboardClickListener {
            void onNumberClick(final int number);

            void onClearClick();

            void onDelClick();
        }

        interface CreateViewHolderCallback {
            /**
             * 设置清除View的属性,当返回null的时候使用提供的View,否则使用返回的View
             */
            View setClearViewAttribute(final Context context, final ImageView useView);

            /**
             * 设置删除View的属性,当返回null的时候使用提供的View,否则使用返回的View
             */
            View setDelViewAttribute(final Context context, final ImageView useView);

            /**
             * 设置数字View的属性,当返回null的时候使用提供的View,否则使用返回的View
             */
            View setNumberViewAttribute(final Context context, final TextView useView, final int position);
        }

        public final class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View itemView) {
                super(itemView);
            }
        }

        public final static class TextViewAttribute {
            public float textSize = -1f;
            public int textSizeUnit = -1;
            public int textColor = 0xffffffff;
            public int gravity = Gravity.CENTER;
        }
    }
}
