package com.elianshang.code.reader.ui.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;

public class DateEditText extends ContentEditText {

    public DateEditText(Context context) {
        super(context);
    }

    public DateEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DateEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DateEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("xue", "beforeTextChanged == " + s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("xue", "onTextChanged == " + s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("xue", "afterTextChanged == " + s);
            }
        });
    }
}
