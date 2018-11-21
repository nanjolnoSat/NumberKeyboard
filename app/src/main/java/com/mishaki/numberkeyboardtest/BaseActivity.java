package com.mishaki.numberkeyboardtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        initView();
        initData();
    }

    protected abstract void setContentView();
    protected abstract void initView();
    protected abstract void initData();
}
