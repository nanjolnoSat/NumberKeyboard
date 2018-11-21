package com.mishaki.numberkeyboard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import com.mishaki.numberkeyboard.R;

public abstract class BaseDialog {
    protected final Context context;
    protected final Dialog dialog;
    protected final View contentView;

    public BaseDialog(Context context) {
        this(context, R.style.base_dialog);
    }

    public BaseDialog(Context context, int styleId) {
        this.context = context;
        dialog = new Dialog(context, styleId);
        contentView = initContentView(context);
        setCancel(dialog);
        dialog.setContentView(contentView);
        WindowManager.LayoutParams attribute = dialog.getWindow().getAttributes();
        initWindowAttribute(attribute);
        dialog.getWindow().setAttributes(attribute);
        initChildView(contentView);
    }

    abstract View initContentView(final Context context);

    abstract void initChildView(final View view);

    protected void setCancel(final Dialog dialog) {
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    protected void initWindowAttribute(final WindowManager.LayoutParams attribute) {

    }

    public void show(){
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }
}
