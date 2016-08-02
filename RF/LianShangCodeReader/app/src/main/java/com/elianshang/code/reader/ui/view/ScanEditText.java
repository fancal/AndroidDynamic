package com.elianshang.code.reader.ui.view;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;

import com.elianshang.code.reader.tool.DialogTools;

/**
 * 扫描EditText
 */
public class ScanEditText extends ContentEditText {

    private OnSetInputEnd inputEnd;

    public ScanEditText(Context context) {
        super(context);
        init();
    }

    public ScanEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScanEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ScanEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }



    private void init() {
        setInputType(InputType.TYPE_NULL);
    }

    public void setOnLongClickListener(final Activity activity) {
        setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                DialogTools.showEditViewDialog(activity, "请入码值", "", "取消", "确认", null, new DialogTools.OnEditViewPositiveButtonClick() {
                    @Override
                    public void onClick(String editText) {
                        if (inputEnd != null) {
                            inputEnd.onSetInputEnd(editText);
                        }
                    }
                }, false);

                return false;
            }
        });
    }

    public void setInputEnd (OnSetInputEnd inputEnd){
        this.inputEnd = inputEnd;
    }


    /**
     * 输入完成的接口
     */
    public interface OnSetInputEnd {
        void onSetInputEnd(String s);
    }

//    public boolean isRight(){
//        String editStr = getText().toString().trim();
//        return !isEmpty(editStr) && isNumeric(editStr);
//    }


}
