package com.mishaki.numberkeyboard.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
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

import java.lang.ref.WeakReference;


public class NumberKeyboardView extends RecyclerView {
    private float itemHeight = 0f;
    private final NkAdapter adapter;
    private boolean isSwapClearDelPosition = false;
    private final int DEFAULT_DIVIDER_SIZE = 5;
    private final int DEFAULT_DIVIDER_COLOR = 0xffffffff;
    private RecyclerDivider rd;

    public NumberKeyboardView(Context context) {
        this(context, null, 0);
    }

    public NumberKeyboardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        int dividerSize = DEFAULT_DIVIDER_SIZE;
        int dividerColor = DEFAULT_DIVIDER_COLOR;
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NumberKeyboardView);
            isSwapClearDelPosition = array.getBoolean(R.styleable.NumberKeyboardView_nkv_isSwapClearDelPosition, false);
            dividerSize = (int) array.getDimension(R.styleable.NumberKeyboardView_nkv_dividerSize, DEFAULT_DIVIDER_SIZE);
            dividerColor = array.getColor(R.styleable.NumberKeyboardView_nkv_dividerColor, DEFAULT_DIVIDER_COLOR);
            array.recycle();
        }
        super.setLayoutManager(new GridLayoutManager(context, 3) {
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
        rd = new RecyclerDivider.Build().setType(RecyclerDivider.ALL_HALF).setSize(dividerSize).setColor(dividerColor).isReviseSize(true).build();
        addItemDecoration(rd);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.itemHeight = w * 0.167f;
        adapter.itemHeight = itemHeight;
        if (!adapter.isChangeTextSize) {
            adapter.textSize = w * 0.04f;
        }
        super.setAdapter(adapter);
    }

    public final void setItemHeight(final float itemHeight) {
        this.itemHeight = itemHeight;
        adapter.setItemHeight(itemHeight);
        super.setAdapter(adapter);
    }

    public final NkAdapter getNkAdapter() {
        return adapter;
    }

    @Override
    public void setAdapter(Adapter adapter) {

    }

    private void setMyAdapter(Adapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {

    }

    @Override
    public void addItemDecoration(ItemDecoration decor) {
        if (rd != null) {
            removeItemDecoration(rd);
            rd = null;
        }
        super.addItemDecoration(decor);
    }

    public void setItemDecoration(RecyclerDivider rd) {
        addItemDecoration(rd);
        this.rd = rd;
    }

    public static final class NkAdapter extends Adapter<NkAdapter.ViewHolder> {
        private final int TYPE_NUMBER = 0;
        private final int TYPE_CLEAR = 1;
        private final int TYPE_DEL = 2;
        private final SparseArray<View> viewMap = new SparseArray();
        private float itemHeight;
        private float textSize;
        private int textSizeUnit = TypedValue.COMPLEX_UNIT_PX;
        private int textColor = 0xff000000;
        private int textViewGraivity = Gravity.CENTER;
        private CreateViewHolderCallback callback;
        private OnNumberKeyboardClickListener listener;
        private boolean isSwapClearDelPosition = false;
        private boolean isChangeTextSize = false;
        private WeakReference<NumberKeyboardView> obj;

        public NkAdapter(NumberKeyboardView obj) {
            this.obj = new WeakReference<>(obj);
            this.isSwapClearDelPosition = obj.isSwapClearDelPosition;
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == 0) {
                viewMap.clear();
            }
            final int actualType = getActualViewType(viewType);
            final View itemView;
            switch (actualType) {
                case TYPE_CLEAR:
                    final ImageView clearView = new ImageView(parent.getContext());
                    final NumberKeyboardView view1 = obj.get();
                    final ViewGroup.LayoutParams clearParams;
                    if (view1 != null) {
                        clearParams = view1.generateDefaultLayoutParams();
                    } else {
                        clearParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (itemHeight));
                    }
                    clearParams.height = (int) (itemHeight);
                    clearView.setLayoutParams(clearParams);
                    clearView.setPadding(0, (int) (itemHeight / 4), 0, (int) (itemHeight / 4));
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
                    final NumberKeyboardView view2 = obj.get();
                    final ViewGroup.LayoutParams delParams;
                    if (view2 != null) {
                        delParams = view2.generateDefaultLayoutParams();
                    } else {
                        delParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (itemHeight));
                    }
                    delParams.height = (int) (itemHeight);
                    delView.setLayoutParams(delParams);
                    delView.setPadding(0, (int) (itemHeight / 4), 0, (int) (itemHeight / 4));
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
                    final NumberKeyboardView view3 = obj.get();
                    final ViewGroup.LayoutParams numberParams;
                    if (view3 != null) {
                        numberParams = view3.generateDefaultLayoutParams();
                    } else {
                        numberParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (itemHeight));
                    }
                    numberParams.height = (int) (itemHeight);
                    numberView.setLayoutParams(numberParams);

                    numberView.setTextColor(textColor);
                    numberView.setGravity(textViewGraivity);
                    numberView.setBackgroundResource(R.drawable.number_keyboard_bg);
                    numberView.setTextSize(textSizeUnit, textSize);
                    if (callback != null) {
                        final View actualView = callback.setNumberViewAttribute(parent.getContext(), numberView, viewType == 10 ? 0 : viewType + 1);
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
            final NumberKeyboardView view = obj.get();
            if (view != null) {
                view.setAdapter(this);
            }
            return this;
        }

        public final NkAdapter setTextSize(final float textSize) {
            isChangeTextSize = true;
            this.textSize = textSize;
            final NumberKeyboardView view = obj.get();
            if (view != null) {
                view.setMyAdapter(this);
            }
            return this;
        }

        public final NkAdapter setTextSizeUnit(final int textSizeUnit) {
            this.textSizeUnit = textSizeUnit;
            final NumberKeyboardView view = obj.get();
            if (view != null) {
                view.setMyAdapter(this);
            }
            return this;
        }

        public final NkAdapter setTextColor(final int textColor) {
            this.textColor = textColor;
            final NumberKeyboardView view = obj.get();
            if (view != null) {
                view.setMyAdapter(this);
            }
            return this;
        }

        public final NkAdapter setTextViewGraivity(final int graivity) {
            this.textViewGraivity = graivity;
            final NumberKeyboardView view = obj.get();
            if (view != null) {
                view.setMyAdapter(this);
            }
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
            final NumberKeyboardView view = obj.get();
            if (view != null) {
                view.setMyAdapter(this);
            }
            return this;
        }

        public final NkAdapter setOnNumberKeyboradClickListener(OnNumberKeyboardClickListener listener) {
            this.listener = listener;
            return this;
        }

        public final NkAdapter setCreateViewHolderCallback(CreateViewHolderCallback callback) {
            this.callback = callback;
            final NumberKeyboardView view = obj.get();
            if (view != null) {
                view.setMyAdapter(this);
            }
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

        public interface CreateViewHolderCallback {
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
            View setNumberViewAttribute(final Context context, final TextView useView, final int number);
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
