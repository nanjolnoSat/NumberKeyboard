package com.mishaki.numberkeyboardtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.main_pd_btn).setOnClickListener(this);
        findViewById(R.id.main_nkd_btn).setOnClickListener(this);
        findViewById(R.id.main_nkd_btn1).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_pd_btn:
                startActivity(new Intent(this, PasswordActivity.class));
                break;
            case R.id.main_nkd_btn:
                startActivity(new Intent(this, NumberKeyboardActivity.class));
                break;
            case R.id.main_nkd_btn1:
                startActivity(new Intent(this, NumberKeyboardActivity1.class));
                break;
        }
    }
}
