package com.hear_word_and_type.app.ui.base;

import android.view.View;

public interface BaseCallback {
    void onClick(View view);
    default void onViewClick(View view, int position){}
}
