package com.mishaki.numberkeyboard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.mishaki.numberkeyboard.R;
import com.mishaki.numberkeyboard.listener.OnInputListener;
import com.mishaki.numberkeyboard.ui.InputNumberView;
import com.mishaki.numberkeyboard.ui.NumberKeyboardView;

public final class NumberKeyboardDialog extends BaseDialog {
    private OnInputListener listener;

    public NumberKeyboardDialog(Context context) {
        super(context);
    }

    @Override
    View initContentView(Context context) {
        return new NumberKeyboardView(context);
    }

    @Override
    void initChildView(final View view) {
        view.setBackgroundColor(0xffffffff);
    }

    @Override
    protected void initWindowAttribute(final WindowManager.LayoutParams attribute) {
        attribute.gravity = Gravity.BOTTOM;
        attribute.dimAmount = 0f;
        attribute.width = ViewGroup.LayoutParams.MATCH_PARENT;
        attribute.windowAnimations = R.style.dialog_bottom_anim;
    }

    public final void setInputNumberView(final InputNumberView inv) {
        inv.bindNumberKeyboard(getNumberKeyboardView());
        inv.setOnTextChangeListener(new InputNumberView.OnTextChangeListener() {
            @Override
            public void onTextChange(String text, int length) {
                if (listener != null) {
                    listener.onTextChange(text, length);
                }
            }

            @Override
            public void onInputFinish(String text) {
                if (listener != null) {
                    listener.onInputFinish(text);
                }
            }

            @Override
            public void onTextClear() {
                if (listener != null) {
                    listener.onTextChange("", 0);
                }
            }

            @Override
            public void onTextDelete(String text) {
                if (listener != null) {
                    listener.onTextChange(inv.getInputCode(), inv.getInputCode().length());
                }
            }
        });
    }

    public final void setOnInputListener(OnInputListener listener) {
        this.listener = listener;
    }

    public final NumberKeyboardView.NkAdapter getNkAdapter(){
        return getNumberKeyboardView().getNkAdapter();
    }

    private NumberKeyboardView getNumberKeyboardView() {
        return (NumberKeyboardView) contentView;
    }

    @Override
    protected void setCancel(final Dialog dialog) {
        super.setCancel(dialog);
        dialog.setCanceledOnTouchOutside(true);
    }

}
