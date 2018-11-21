package com.mishaki.numberkeyboard.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mishaki.numberkeyboard.R;
import com.mishaki.numberkeyboard.util.NumberKeyboardUtil;

import java.util.ArrayList;
import java.util.Stack;

public class InputNumberView extends LinearLayout {
    private char encryptCodeChar = '●';
    private int codeCount = 6;
    private float inputViewWidth;
    private float inputViewHeight;
    private float inputTextSize;
    private int inputTextTextSizeUnit = TypedValue.COMPLEX_UNIT_PX;
    private int inputViewGravity = Gravity.CENTER;
    private int inputTextColor = 0xff000000;
    private final Stack<Integer> codeList = new Stack<Integer>();
    private final ArrayList<TextView> inputViewList = new ArrayList<>();
    private boolean isPwd = true;

    private boolean isDrawFrame = true;
    private final Paint framePaint = new Paint();
    private int frameSize = 2;
    private final int DEFAULT_FRAME_COLOR = 0xff999999;

    private boolean isDrawDivider = true;
    private final Paint dividerPaint = new Paint();
    private int dividerSize = 2;
    private final int DEFAULT_DIVIDER_COLOR = 0xff999999;

    private OnTextChangeListener listener;

    public InputNumberView(Context context) {
        this(context, null, 0);
    }

    public InputNumberView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputNumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setOrientation(LinearLayout.HORIZONTAL);
        int codeCount = -1;
        float inputViewWidth = -1f;
        float inputViewHeight = -1f;
        float inputTextSize = -1f;
        int inputTextColor = this.inputTextColor;
        boolean isPwd = false;
        char encryptCodeChar = this.encryptCodeChar;
        boolean isDrawFrame = true;
        int frameColor = DEFAULT_FRAME_COLOR;
        float frameSize = this.frameSize;
        boolean isDrawDivider = true;
        int dividerColor = DEFAULT_DIVIDER_COLOR;
        float dividerSize = this.dividerSize;
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.InputNumberView);

            codeCount = array.getInt(R.styleable.InputNumberView_inv_codeCount, -1);
            inputViewWidth = array.getDimension(R.styleable.InputNumberView_inv_inputViewWidth, -1f);
            inputViewHeight = array.getDimension(R.styleable.InputNumberView_inv_inputViewHeight, -1f);
            inputTextSize = array.getDimension(R.styleable.InputNumberView_inv_inputTextSize, -1f);
            inputTextColor = array.getColor(R.styleable.InputNumberView_inv_inputTextColor, this.inputTextColor);
            isPwd = array.getBoolean(R.styleable.InputNumberView_inv_isPwd, false);
            String encryptCodeCharStr = array.getString(R.styleable.InputNumberView_inv_encryptCodeChar);
            if (encryptCodeCharStr != null && encryptCodeCharStr.length() > 0) {
                encryptCodeChar = encryptCodeCharStr.toCharArray()[0];
            }
            isDrawFrame = array.getBoolean(R.styleable.InputNumberView_inv_isDrawFrame, true);
            frameColor = array.getColor(R.styleable.InputNumberView_inv_frameColor, DEFAULT_FRAME_COLOR);
            frameSize = array.getDimension(R.styleable.InputNumberView_inv_frameSize, this.frameSize);
            isDrawDivider = array.getBoolean(R.styleable.InputNumberView_inv_isDrawDivider, true);
            dividerColor = array.getColor(R.styleable.InputNumberView_inv_dividerColor, DEFAULT_DIVIDER_COLOR);
            dividerSize = array.getDimension(R.styleable.InputNumberView_inv_dividerSize, this.dividerSize);

            array.recycle();
        }
        setGravity(Gravity.CENTER_HORIZONTAL);
        setPadding(0, (int) (NumberKeyboardUtil.px2sp(context, 5)), 0, (int) (NumberKeyboardUtil.px2dp(context, 5)));
        if (codeCount != -1) {
            this.codeCount = codeCount;
        }
        if (inputViewWidth != -1f) {
            this.inputViewWidth = inputViewWidth;
        } else {
            this.inputViewWidth = NumberKeyboardUtil.px2dp(context, 50f);
        }
        if (inputViewHeight != -1f) {
            this.inputViewHeight = inputViewHeight;
        } else {
            this.inputViewHeight = NumberKeyboardUtil.px2dp(context, 50f);
        }
        if (inputTextSize != -1f) {
            this.inputTextSize = inputTextSize;
        } else {
            this.inputTextSize = NumberKeyboardUtil.px2sp(context, 20f);
        }
        this.inputTextColor = inputTextColor;
        this.isPwd = isPwd;
        this.encryptCodeChar = encryptCodeChar;
        this.isDrawFrame = isDrawFrame;
        this.frameSize = (int) frameSize;
        this.isDrawDivider = isDrawDivider;
        this.dividerSize = (int) dividerSize;
        generateInputView(this.codeCount);

        framePaint.setColor(frameColor);
        framePaint.setAntiAlias(true);
        framePaint.setStrokeWidth(1);
        framePaint.setStyle(Paint.Style.FILL);

        dividerPaint.setColor(dividerColor);
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setStyle(Paint.Style.FILL);
    }

    public void generateInputView(final int count) {
        codeList.clear();
        inputViewList.clear();
        removeAllViews();
        this.codeCount = count;
        for (int i = 0; i < count; i++) {
            TextView textView = new TextView(getContext());
            LayoutParams textViewParams = new LayoutParams((int) (inputViewWidth), (int) (inputViewHeight));
            textView.setLayoutParams(textViewParams);
            textView.setTextSize(inputTextTextSizeUnit, inputTextSize);
            textView.setTextColor(inputTextColor);
            textView.setGravity(inputViewGravity);
            super.addView(textView);
            inputViewList.add(textView);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if ((isDrawFrame || isDrawDivider) && getChildCount() != 0) {
            final float allInputViewWidth = inputViewWidth * inputViewList.size();
            final float startX = (getWidth() - allInputViewWidth) / 2f;
            final float endX = startX + allInputViewWidth;
            final float startY = getPaddingTop();
            final float endY = getPaddingTop() + inputViewHeight;
            if (isDrawFrame) {
                //绘制左边的线
                canvas.drawRect(startX, startY, startX + frameSize, endY, framePaint);
                //绘制上面的线
                canvas.drawRect(startX, startY, endX, startY + frameSize, framePaint);
                //绘制右边的线
                canvas.drawRect(endX - frameSize, startY, endX, endY, framePaint);
                //绘制下面的线
                canvas.drawRect(startX, endY - frameSize, endX, endY, framePaint);
            }
            if (isDrawDivider) {
                //绘制中间的分隔线
                for (int i = 1; i < inputViewList.size(); i++) {
                    canvas.drawRect(startX + i * inputViewWidth - dividerSize / 2f, startY + frameSize, startX + i * inputViewWidth + dividerSize / 2f, endY - frameSize, dividerPaint);
                }
            }
        }
    }

    public final InputNumberView setTextSizeUnit(final int unit) {
        if (this.inputTextTextSizeUnit == unit) {
            return this;
        }
        this.inputTextTextSizeUnit = unit;
        for (TextView tv : inputViewList) {
            tv.setTextSize(unit, inputTextSize);
        }
        return this;
    }

    public final InputNumberView setTextSize(final float textSize) {
        if (this.inputTextSize == textSize) {
            return this;
        }
        this.inputTextSize = textSize;
        for (TextView tv : inputViewList) {
            tv.setTextSize(inputTextTextSizeUnit, textSize);
        }
        return this;
    }

    public final InputNumberView setTextSize(final int unit, final float textSize) {
        if (this.inputTextTextSizeUnit == unit && this.inputTextSize == textSize) {
            return this;
        }
        this.inputTextTextSizeUnit = unit;
        this.inputTextSize = textSize;
        for (TextView tv : inputViewList) {
            tv.setTextSize(unit, textSize);
        }
        return this;
    }

    public final InputNumberView setInputViewMarginLeft(int margin) {
        for (int i = 1; i < inputViewList.size(); i++) {
            LayoutParams params = (LayoutParams) inputViewList.get(i).getLayoutParams();
            params.leftMargin = margin;
            inputViewList.get(i).setLayoutParams(params);
        }
        return this;
    }

    public final InputNumberView setInputViewGravity(int gravity) {
        if (this.inputViewGravity == gravity) {
            return this;
        }
        this.inputViewGravity = gravity;
        for (TextView tv : inputViewList) {
            tv.setGravity(gravity);
        }
        return this;
    }

    public final InputNumberView setInputViewBackground(Drawable drawable) {
        for (TextView tv : inputViewList) {
            tv.setBackground(drawable);
        }
        return this;
    }

    public final InputNumberView setInputViewBackgroundResource(int resId) {
        for (TextView tv : inputViewList) {
            tv.setBackgroundResource(resId);
        }
        return this;
    }

    public final InputNumberView setInputViewBackgroundColor(int color) {
        for (TextView tv : inputViewList) {
            tv.setBackgroundColor(color);
        }
        return this;
    }

    public final InputNumberView setInputTextColor(int color) {
        if (this.inputTextColor == color) {
            return this;
        }
        this.inputTextColor = color;
        for (TextView tv : inputViewList) {
            tv.setTextColor(color);
        }
        return this;
    }

    public final InputNumberView isPwd(boolean isPwd) {
        if (this.isPwd == isPwd) {
            return this;
        }
        for (int i = 0; i < codeList.size(); i++) {
            final TextView tv = inputViewList.get(i);
            if (isPwd) {
                tv.setText(String.valueOf(encryptCodeChar));
            } else {
                tv.setText(String.valueOf(codeList.get(i)));
            }
        }
        return this;
    }

    public final InputNumberView setEncryptCodeChar(char encryptCodeChar) {
        if (this.encryptCodeChar == encryptCodeChar) {
            return this;
        }
        if (isPwd) {
            for (int i = 0; i < codeList.size(); i++) {
                final TextView tv = inputViewList.get(i);
                tv.setText(String.valueOf(encryptCodeChar));
            }
        }
        return this;
    }

    public final InputNumberView bindNumberKeyboard(final NumberKeyboardView nkv) {
        nkv.getNkAdapter().setOnNumberKeyboradClickListener(new NumberKeyboardView.NkAdapter.OnNumberKeyboardClickListener() {
            @Override
            public void onNumberClick(int number) {
                if (codeList.size() < codeCount) {
                    if (isPwd) {
                        inputViewList.get(codeList.size()).setText(String.valueOf(encryptCodeChar));
                    } else {
                        inputViewList.get(codeList.size()).setText(String.valueOf(number));
                    }
                    codeList.push(number);
                    if (listener != null) {
                        if (codeList.size() == codeCount) {
                            listener.onInputFinish(getInputCode());
                        } else {
                            listener.onTextChange(getInputCode(), codeList.size());
                        }
                    }
                }
            }

            @Override
            public void onClearClick() {
                codeList.clear();
                for (TextView tv : inputViewList) {
                    tv.setText("");
                }
                if (listener != null) {
                    listener.onTextClear();
                }
            }

            @Override
            public void onDelClick() {
                if (!codeList.isEmpty()) {
                    String text = String.valueOf(codeList.pop());
                    inputViewList.get(codeList.size()).setText("");
                    if (listener != null) {
                        listener.onTextDelete(text);
                    }
                }
            }
        });
        return this;
    }

    public final InputNumberView isDrawFrame(boolean isDrawFrame) {
        if (this.isDrawFrame == isDrawFrame) {
            return this;
        }
        this.isDrawFrame = isDrawFrame;
        invalidate();
        return this;
    }

    public final InputNumberView isDrawDivider(boolean isDrawDivider) {
        if (this.isDrawDivider == isDrawDivider) {
            return this;
        }
        this.isDrawDivider = isDrawDivider;
        invalidate();
        return this;
    }

    public final InputNumberView setFrameColor(int frameColor) {
        if (framePaint.getColor() == frameColor) {
            return this;
        }
        framePaint.setColor(frameColor);
        invalidate();
        return this;
    }

    public final InputNumberView setDividerColor(int dividerColor) {
        if (dividerPaint.getColor() == dividerColor) {
            return this;
        }
        dividerPaint.setColor(dividerColor);
        invalidate();
        return this;
    }

    public final InputNumberView setFrameSize(int frameSize) {
        if (this.frameSize == frameSize) {
            return this;
        }
        this.frameSize = frameSize;
        invalidate();
        return this;
    }

    public final InputNumberView setDividerSize(int dividerSize) {
        if (this.dividerSize == dividerSize) {
            return this;
        }
        this.dividerSize = dividerSize;
        invalidate();
        return this;
    }

    public final String getInputCode() {
        StringBuilder str = new StringBuilder();
        for (int i : codeList) {
            str.append(i);
        }
        return str.toString();
    }

    public final InputNumberView setOnTextChangeListener(OnTextChangeListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void addView(View child) {

    }

    @Override
    public void setOrientation(int orientation) {

    }

    public interface OnTextChangeListener {
        void onTextChange(String text, int length);

        void onInputFinish(String text);

        void onTextClear();

        void onTextDelete(String text);
    }
}
