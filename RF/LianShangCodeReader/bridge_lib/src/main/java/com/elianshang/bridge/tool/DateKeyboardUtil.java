package com.elianshang.bridge.tool;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.elianshang.bridge.R;

import java.util.Calendar;
import java.util.List;

public class DateKeyboardUtil implements View.OnTouchListener {

    private Activity mActivity;
    private KeyboardView mKeyboardView;
    private EditText mCurEdit;
    private EditText[] mAllEdit;
    /**
     * 键盘--年
     */
    private Keyboard year_keyboard;
    /**
     * 键盘-月
     */
    private Keyboard month_keyboard;
    /**
     * 键盘-日
     */
    private Keyboard day_keyboard;
    /**
     * 0：年    1：月   2：日
     */
    private int step = -1;

    private boolean isInit = false;

    public DateKeyboardUtil(Activity activity, KeyboardView keyboardView, EditText... edit) {
        mActivity = activity;
        this.mKeyboardView = keyboardView;
        mAllEdit = edit;
        mCurEdit = edit[0];
        for (EditText editText : mAllEdit) {
            editText.setInputType(InputType.TYPE_NULL);
            editText.setOnTouchListener(this);
        }
    }

    private synchronized void init() {
        if (isInit) {
            return;
        }
        isInit = true;
        year_keyboard = new Keyboard(mActivity, R.xml.keyboard_year);
        month_keyboard = new Keyboard(mActivity, R.xml.keyboard_month);
        day_keyboard = new Keyboard(mActivity, R.xml.keyboard_day);

        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(listener);

        int year = Calendar.getInstance().get(Calendar.YEAR);
        List<Keyboard.Key> keys = year_keyboard.getKeys();

        for (int i = 0; i < keys.size() - 2; i++) {
            Keyboard.Key key = keys.get(i);
            key.label = String.valueOf(year);
            key.codes = new int[]{year};
            year--;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int step = -1;
        for (int i = 0; i < mAllEdit.length; i++) {
            if (v == mAllEdit[i]) {
                step = i;
                break;
            }
        }
        if (step >= 0) {
            if (this.step != step) {
                init();
                this.step = step;
                mCurEdit = mAllEdit[step];
                mCurEdit.requestFocus();
            }
            hideSoftInputMethod();
            showKeyboard();
        }
        return false;
    }

    private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            if (primaryCode == -1) {// 上一步
                step--;
                mCurEdit = mAllEdit[step];
                mCurEdit.requestFocus();
                setKeyboard();
            } else if (primaryCode == -2) {// 下一步
                step++;
                mCurEdit = mAllEdit[step];
                mCurEdit.requestFocus();
                setKeyboard();
            } else {
                mCurEdit.setText(String.valueOf(primaryCode));
                if (step == 2) {
                    hideKeyboard();
                    return;
                }
                step++;
                mCurEdit = mAllEdit[step];
                mCurEdit.requestFocus();
                setKeyboard();
            }
        }
    };

    private void setKeyboard() {
        switch (step) {
            case 0:
                mKeyboardView.setKeyboard(year_keyboard);
                break;
            case 1:
                mKeyboardView.setKeyboard(month_keyboard);
                break;
            case 2:
                mKeyboardView.setKeyboard(day_keyboard);
                break;
        }
    }

    /**
     * 软键盘展示状态
     */
    public boolean isShow() {
        return mKeyboardView != null && mKeyboardView.getVisibility() == View.VISIBLE;
    }

    /**
     * 软键盘展示
     */
    public void showKeyboard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            mKeyboardView.setVisibility(View.VISIBLE);
        }
        setKeyboard();
    }

    /**
     * 软键盘隐藏
     */
    public void hideKeyboard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            mKeyboardView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 禁掉系统软键盘
     */
    private void hideSoftInputMethod() {
//        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0); //强制隐藏键盘

        ((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); // (WidgetSearchActivity是当前的Activity)
    }

}
