package com.mishaki.numberkeyboard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.mishaki.numberkeyboard.R;
import com.mishaki.numberkeyboard.listener.OnInputListener;
import com.mishaki.numberkeyboard.ui.InputNumberView;
import com.mishaki.numberkeyboard.ui.NumberKeyboardView;

public final class PasswordDialog extends BaseDialog {
    private OnInputListener listener;
    private NumberKeyboardView.NkAdapter nkAdapter;

    public PasswordDialog(Context context) {
        super(context);
    }

    @Override
    View initContentView(final Context context) {
        return LayoutInflater.from(context).inflate(R.layout.dialog_password, null);
    }

    @Override
    void initChildView(final View view) {
        final InputNumberView dialog_password_inv = view.findViewById(R.id.dialog_password_inv);
        final NumberKeyboardView dialog_password_nkv = view.findViewById(R.id.dialog_password_nkv);
        nkAdapter = dialog_password_nkv.getNkAdapter();
        dialog_password_inv.bindNumberKeyboard(dialog_password_nkv);
        dialog_password_inv.setOnTextChangeListener(new InputNumberView.OnTextChangeListener() {
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
                if(listener != null) {
                    listener.onTextChange("",0);
                }
            }

            @Override
            public void onTextDelete(String text) {
                if(listener != null) {
                    listener.onTextChange(dialog_password_inv.getInputCode(),dialog_password_inv.getInputCode().length());
                }
            }
        });
    }

    @Override
    protected void initWindowAttribute(final WindowManager.LayoutParams attribute) {
        attribute.gravity = Gravity.BOTTOM;
        attribute.dimAmount = 0.7f;
        attribute.width = ViewGroup.LayoutParams.MATCH_PARENT;
        attribute.windowAnimations = R.style.dialog_bottom_anim;
    }

    public void setOnInputFinishListener(final OnInputListener listener) {
        this.listener = listener;
    }

    public final NumberKeyboardView.NkAdapter getNkAdapter(){
        return nkAdapter;
    }

    @Override
    protected void setCancel(Dialog dialog) {
        super.setCancel(dialog);
        dialog.setCanceledOnTouchOutside(true);
    }
}
