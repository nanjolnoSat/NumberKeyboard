package com.mishaki.numberkeyboardtest;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mishaki.numberkeyboard.dialog.PasswordDialog;
import com.mishaki.numberkeyboard.listener.OnInputListener;
import com.mishaki.numberkeyboard.ui.NumberKeyboardView;

public class PasswordActivity extends BaseActivity {
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_password);
    }

    @Override
    protected void initView() {
        final PasswordDialog dialog = new PasswordDialog(this);
        final TextView password_pwd_tv = findViewById(R.id.password_pwd_tv);
        dialog.setOnInputFinishListener(new OnInputListener() {
            @Override
            public void onTextChange(String text, int length) {

            }

            @Override
            public void onInputFinish(String pwd) {
                password_pwd_tv.setText(pwd);
            }
        });
        findViewById(R.id.password_show_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        dialog.getNkAdapter().setCreateViewHolderCallback(new NumberKeyboardView.NkAdapter.CreateViewHolderCallback() {
            @Override
            public View setClearViewAttribute(Context context, ImageView useView) {
                return null;
            }

            @Override
            public View setDelViewAttribute(Context context, ImageView useView) {
                return new ImageView(context);
            }

            @Override
            public View setNumberViewAttribute(Context context, TextView useView, int number) {
                TextView tv = new TextView(context);
                tv.setText(String.valueOf(number));
                tv.setTextSize(15);
                tv.setPadding(0,20,0,20);
                tv.setGravity(Gravity.CENTER);
                tv.setBackgroundColor(0xffff0000);
                tv.setTextColor(0xff00ff00);
                return tv;
            }
        });
    }

    @Override
    protected void initData() {

    }
}
