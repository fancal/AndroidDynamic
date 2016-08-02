package com.elianshang.code.reader.tool;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.elianshang.code.reader.ui.view.ScanEditText;

public class ScanEditTextTool {

    private Activity mActivity;

    private EditText[] mEditTexts;

    private int setCount;

    private OnSetComplete complete;

    private TextWatcher textWatcher;

    public ScanEditTextTool(Activity activity, EditText... editTexts) {
        if(editTexts == null){
            return;
        }

        mActivity = activity;
        mEditTexts = editTexts;

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                for(int i = 0; i < mEditTexts.length; i++){
                    ScanEditText text = (ScanEditText) mEditTexts[i];
                    if (text.isRight()) {
                        continue;
                    } else {
                        if(text.hasFocus() && complete != null){
                            complete.onInputError(i);
                        }
                        return;
                    }
                }
                if(complete != null){
                    complete.onSetComplete();
                }
            }
        };

        for(EditText text : mEditTexts){

            text.addTextChangedListener(textWatcher);
            ((ScanEditText) text).setOnLongClickListener(activity);
        }
    }


    public void setScanText(String s) {

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

//        setCount ++;
//        if (setCount == mEditTexts.length && complete != null) {
//            complete.onSetComplete();
//        }
    }

    public void setComplete(OnSetComplete complete){
        this.complete = complete;
    }


    public interface OnSetComplete {
        void onSetComplete();
        void onInputError(int i);
    }


}
