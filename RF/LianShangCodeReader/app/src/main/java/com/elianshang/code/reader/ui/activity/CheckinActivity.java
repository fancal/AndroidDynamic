package com.elianshang.code.reader.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.elianshang.code.reader.R;
import com.elianshang.code.reader.ui.BaseActivity;

/**
 * Created by xfilshy on 16/8/1.
 */
public class CheckinActivity extends BaseActivity {

    private EditText orderidEditText;
    private EditText tuoidEditText;
    private EditText productidEditText;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checkin);

        orderidEditText = (EditText) findViewById(R.id.orderid_edittext);
        tuoidEditText = (EditText) findViewById(R.id.tuoid_edittext);
        productidEditText = (EditText) findViewById(R.id.productid_edittext);

        button = (Button) findViewById(R.id.button);

        orderidEditText.setInputType(InputType.TYPE_NULL);
        tuoidEditText.setInputType(InputType.TYPE_NULL);
        productidEditText.setInputType(InputType.TYPE_NULL);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View fv = getCurrentFocus();

                if (fv == orderidEditText) {
                    orderidEditText.clearFocus();
                    tuoidEditText.requestFocus();
                } else if (fv == tuoidEditText) {
                    tuoidEditText.clearFocus();
                    productidEditText.requestFocus();
                } else if (fv == productidEditText) {
                    productidEditText.clearFocus();
                    button.requestFocus();
                }
            }
        });
    }
}
