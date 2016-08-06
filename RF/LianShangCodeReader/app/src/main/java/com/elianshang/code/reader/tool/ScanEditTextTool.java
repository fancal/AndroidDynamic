package com.elianshang.code.reader.tool;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.elianshang.code.reader.ui.view.ContentEditText;
import com.elianshang.code.reader.ui.view.ScanEditText;

import java.util.ArrayList;

public class ScanEditTextTool {

    private Activity mActivity;

    private ArrayList<EditText> mEditTexts;

    private OnSetComplete complete;

    private TextWatcher textWatcher;

    public ScanEditTextTool(Activity activity, EditText... editTexts) {
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
                    ContentEditText text = (ContentEditText) mEditTexts.get(i);
                    if (text.isRight()) {
                        continue;
                    } else {
                        if (text.hasFocus() && complete != null) {
                            complete.onInputError(i);
                        }
                        return;
                    }
                }
                if (complete != null) {
                    complete.onSetComplete();
                }
            }
        };


        for (EditText editText : editTexts) {
            editText.addTextChangedListener(textWatcher);
            if (editText instanceof ScanEditText) {
                ((ScanEditText) editText).setOnLongClickListener(activity);
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

    public void addEditText(EditText... editTexts) {
        if (editTexts == null || mEditTexts == null) {
            return;
        }
        for (EditText editText : editTexts) {
            if (textWatcher != null) {
                editText.addTextChangedListener(textWatcher);
            }
            if (editText instanceof ScanEditText) {
                ((ScanEditText) editText).setOnLongClickListener(mActivity);
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

    public void setComplete(OnSetComplete complete) {
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


    public interface OnSetComplete {
        void onSetComplete();

        void onInputError(int i);
    }


}
