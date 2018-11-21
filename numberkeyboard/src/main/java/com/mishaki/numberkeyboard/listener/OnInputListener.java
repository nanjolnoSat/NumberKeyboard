package com.mishaki.numberkeyboard.listener;

public interface OnInputListener {
    void onTextChange(String text, int length);
    void onInputFinish(String text);
}
