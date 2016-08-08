package com.elianshang.code.reader.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.elianshang.code.reader.R;
import com.elianshang.code.reader.tool.ScanEditTextTool;
import com.elianshang.code.reader.tool.ScanManager;
import com.elianshang.code.reader.ui.BaseActivity;
import com.elianshang.code.reader.ui.view.ContentEditText;
import com.elianshang.code.reader.ui.view.ScanEditText;


public class QualityControlActivity extends BaseActivity implements ScanManager.OnBarCodeListener, View.OnClickListener, ScanEditTextTool.OnSetComplete {

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, QualityControlActivity.class);
        activity.startActivity(intent);
    }

    private Toolbar mToolbar;

    private View createLayout;

    private ScanEditText createContainerIdEditText;

    private View detailLayout;

    private TextView detailProgressTextView;

    private TextView detailItemNameTextView;

    private TextView detailPackNameTextView;

    private TextView detailQtyTextView;

    private ContentEditText detailInputQtyTextView;

    private Button detailSubmitButton;

    private ScanEditTextTool scanEditTextTool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScanManager.get().addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScanManager.get().removeListener(this);
    }

    private void findView() {
        createLayout = findViewById(R.id.create_Layout);
        createContainerIdEditText = (ScanEditText) createLayout.findViewById(R.id.containerId_EditText);

        detailLayout = findViewById(R.id.detail_Layout);
        detailProgressTextView = (TextView) detailProgressTextView.findViewById(R.id.progress_TextView);
        detailItemNameTextView = (TextView) detailProgressTextView.findViewById(R.id.itemName_TextView);
        detailPackNameTextView = (TextView) detailProgressTextView.findViewById(R.id.packName_TextView);
        detailQtyTextView = (TextView) detailProgressTextView.findViewById(R.id.qty_TextView);
        detailInputQtyTextView = (ContentEditText) detailProgressTextView.findViewById(R.id.inputQty_EditView);
        detailSubmitButton = (Button) detailLayout.findViewById(R.id.submit_Button);

        detailSubmitButton.setOnClickListener(this);

        initToolbar();
    }

    private void initToolbar() {
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fillCreateData() {
        scanEditTextTool = new ScanEditTextTool(this, createContainerIdEditText);
        scanEditTextTool.setComplete(this);

        createContainerIdEditText.requestFocus();
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSetComplete() {

    }

    @Override
    public void onInputError(int i) {

    }

    @Override
    public void OnBarCodeReceived(String s) {

    }
}