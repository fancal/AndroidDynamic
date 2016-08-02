package com.elianshang.code.reader.ui.activity;

import android.barcode.BarCodeManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.elianshang.code.reader.R;
import com.elianshang.code.reader.tool.ScanEditTextTool;
import com.elianshang.code.reader.ui.BaseActivity;
import com.elianshang.code.reader.ui.view.ScanEditText;
import com.elianshang.tools.ToastTool;

/**
 * Created by xfilshy on 16/8/1.
 */
public class CheckinActivity extends BaseActivity implements BarCodeManager.OnBarCodeReceivedListener, ScanEditTextTool.OnSetComplete{

    private BarCodeManager mBarCode;

    private ScanEditText orderidEditText;
    private ScanEditText tuoidEditText;
    private ScanEditText productidEditText;
    private Button button;
    private Toolbar mToolbar;

    private ScanEditTextTool scanEditTextTool;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        mBarCode = (BarCodeManager) getSystemService("barcode");
        mBarCode.addListener(this);

        findViews();
    }

    private void findViews(){
        orderidEditText = (ScanEditText) findViewById(R.id.orderid_edittext);
        tuoidEditText = (ScanEditText) findViewById(R.id.tuoid_edittext);
        productidEditText = (ScanEditText) findViewById(R.id.productid_edittext);
        button = (Button) findViewById(R.id.button);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        button.setEnabled(false);
        button.setClickable(false);
        scanEditTextTool = new ScanEditTextTool(this, orderidEditText, tuoidEditText, productidEditText);
        scanEditTextTool.setComplete(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastTool.show(CheckinActivity.this, "扫描完成提交数据");
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBarCode.removeListener(this);
    }

    @Override
    public void OnBarCodeReceived(String s) {
        Log.e("xue" , "s == " + s);
        scanEditTextTool.setScanText(s);

//        View fv = getCurrentFocus();
//        if (fv == orderidEditText) {
//            orderidEditText.setText(s);
//            orderidEditText.clearFocus();
//            tuoidEditText.requestFocus();
//        } else if (fv == tuoidEditText) {
//            tuoidEditText.setText(s);
//            tuoidEditText.clearFocus();
//            productidEditText.requestFocus();
//        } else if (fv == productidEditText) {
//            productidEditText.setText(s);
//            ToastTool.show(this, "扫描完成提交数据");
//        }
    }

    @Override
    public void onSetComplete() {
        button.setEnabled(true);
        button.setClickable(true);

    }

    @Override
    public void onInputError(int i) {
        button.setEnabled(false);
        button.setClickable(false);

    }
}
