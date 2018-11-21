package com.mishaki.numberkeyboardtest;

import android.view.View;
import android.widget.TextView;

import com.mishaki.numberkeyboard.dialog.NumberKeyboardDialog;
import com.mishaki.numberkeyboard.listener.OnInputListener;
import com.mishaki.numberkeyboard.ui.InputNumberView;

public class NumberKeyboardActivity extends BaseActivity {
    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_number_keyboard);
    }

    @Override
    protected void initView() {
        final NumberKeyboardDialog dialog = new NumberKeyboardDialog(this);
        final InputNumberView number_keyboard_inv = findViewById(R.id.number_keyboard_inv);
        dialog.setInputNumberView(number_keyboard_inv);
        final TextView number_keyboard_tv = findViewById(R.id.number_keyboard_tv);
        findViewById(R.id.number_keyboard_inv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        dialog.setOnInputListener(new OnInputListener() {
            @Override
            public void onTextChange(String text, int length) {
                number_keyboard_tv.setText(text);
            }

            @Override
            public void onInputFinish(String text) {
                number_keyboard_tv.setText(text);
            }
        });
    }

    @Override
    protected void initData() {

    }
}
