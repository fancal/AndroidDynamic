package com.elianshang.tools;

import android.graphics.Paint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by liuhanzhi on 15/11/27.
 */
public class EditTextTool {


    /**
     * 监听EditText的内容变化，逻辑是：
     * 1.当所有EditText均有内容输入时，提交表单按钮才可点击；
     * 2.当某EditText有内容时，显示其对应的文本清除按钮，无内容则隐藏清除按钮。
     *
     * @param enableView 提交表单按钮
     * @param editTexts  EditText数组
     */
    public static void setOnInputChanged(final View enableView, final EditText[] editTexts) {
        if (enableView == null || editTexts == null) {
            return;
        }
        final int length = editTexts.length;
        for (int i = 0; i < length; i++) {
            final EditText editText = editTexts[i];

            if (editText != null) {

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (TextUtils.isEmpty(editText.getText().toString())) {
                            if (enableView.isEnabled()) {
                                enableView.setEnabled(false);
                                enableView.setClickable(false);
                            }

                        } else {
                            boolean allInput = true;
                            for (int j = 0; j < length; j++) {
                                final EditText tempEdit = editTexts[j];
                                if (tempEdit == null || tempEdit == editText) {
                                    continue;

                                } else {
                                    if (TextUtils.isEmpty(tempEdit.getText().toString())) {
                                        allInput = false;
                                        break;
                                    }
                                }
                            }
                            if (allInput && !enableView.isEnabled()) {
                                enableView.setEnabled(true);
                                enableView.setClickable(true);
                            }
                        }
                    }
                });

            }
        }
    }

    /**
     * 计算textview中文本的宽度。
     *
     * @param textView
     * @return
     */
    public static float measureTextWidth(TextView textView) {
        return measureTextWidth(textView, textView.getText().toString());
    }

    /**
     * 计算textview中文本的宽度。
     *
     * @param textView 被放入的textview
     * @param text     文本
     * @return
     */
    public static float measureTextWidth(TextView textView, String text) {
        float width = 0.0f;
        if (null != textView && null != textView.getPaint() && !TextUtils.isEmpty(text)) {
            Paint paint = textView.getPaint();
            width = paint.measureText(text);
        }
        return width;
    }
}
