package com.mishaki.numberkeyboardtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.mishaki.numberkeyboard.view.InputNumberView;
import com.mishaki.numberkeyboard.view.NumberKeyboardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final InputNumberView main_inv = findViewById(R.id.main_inv);
        NumberKeyboardView main_nkv = findViewById(R.id.main_nkv);
        main_inv.setNumberKeyboard(main_nkv);
        findViewById(R.id.main_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, main_inv.getInputCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
