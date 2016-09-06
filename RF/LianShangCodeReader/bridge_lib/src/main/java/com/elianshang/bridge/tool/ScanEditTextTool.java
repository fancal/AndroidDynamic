package com.elianshang.bridge.tool;

import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.ScanEditText;

import java.util.ArrayList;

public class ScanEditTextTool {

    /**
     * 所属Activity
     */
    private Activity mActivity;

    /**
     * 管理的输入框
     */
    private ArrayList<ContentEditText> mEditTexts;

    /**
     * 监听器
     */
    private OnStateChangeListener complete;

    /**
     * 文本变化监听
     */
    private TextWatcher textWatcher;

    public ScanEditTextTool(Activity activity, ContentEditText... editTexts) {
        if (editTexts == null) {
            return;
        }

        mActivity = activity;
        mEditTexts = new ArrayList<>();

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                for (int i = 0; i < mEditTexts.size(); i++) {
                    ContentEditText text = mEditTexts.get(i);
                    if (text.isRight()) {
                        continue;
                    } else {
                        if (text.hasFocus() && complete != null) {
                            complete.onError(text);
                        }
                        return;
                    }
                }
                if (complete != null) {
                    complete.onComplete();
                }
            }
        };

        if (editTexts != null) {
            for (ContentEditText editText : editTexts) {
                editText.addTextChangedListener(textWatcher);
                if (editText instanceof ScanEditText) {
                    ((ScanEditText) editText).setInputEnd(new ScanEditText.OnSetInputEnd() {
                        @Override
                        public void onSetInputEnd(String s) {
                            setScanText(s);
                        }
                    });
                }

                mEditTexts.add(editText);
            }
        }
    }

    public void addEditText(ContentEditText... editTexts) {
        if (editTexts == null || mEditTexts == null) {
            return;
        }
        for (ContentEditText editText : editTexts) {
            if (textWatcher != null) {
                editText.addTextChangedListener(textWatcher);
            }
            if (editText instanceof ScanEditText) {
                ((ScanEditText) editText).setInputEnd(new ScanEditText.OnSetInputEnd() {
                    @Override
                    public void onSetInputEnd(String s) {
                        setScanText(s);
                    }
                });
            }

            mEditTexts.add(editText);
        }
    }


    public void setScanText(String s) {
        if (mActivity == null) {
            return;
        }

        if (!TextUtils.isEmpty(s)) {
            s = s.trim();
        }

        View v = mActivity.getCurrentFocus();

        if (!(v instanceof ScanEditText)) {
            return;
        }

        boolean request = false;
        for (EditText editText : mEditTexts) {
            if (request) {
                v.clearFocus();
                editText.requestFocus();
                break;
            } else {
                if (v == editText) {
                    editText.setText(s);
                    request = true;
                }
            }
        }
    }

    public void setComplete(OnStateChangeListener complete) {
        this.complete = complete;
    }

    public void release() {
        if (mEditTexts != null) {
            for (EditText editText : mEditTexts) {
                editText.removeTextChangedListener(textWatcher);
            }

            mEditTexts.clear();
        }

        mEditTexts = null;
        textWatcher = null;
        mActivity = null;
    }


    public interface OnStateChangeListener {

        void onComplete();

        void onError(ContentEditText editText);
    }
}
