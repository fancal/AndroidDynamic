package com.elianshang.code.reader.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.Item;
import com.elianshang.code.reader.bean.ResponseState;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.tool.DialogTools;
import com.elianshang.code.reader.tool.ScanEditTextTool;
import com.elianshang.code.reader.tool.ScanManager;
import com.elianshang.code.reader.ui.BaseActivity;
import com.elianshang.code.reader.ui.view.ContentEditText;
import com.elianshang.code.reader.ui.view.QtyEditText;
import com.elianshang.code.reader.ui.view.ScanEditText;
import com.xue.http.impl.DataHull;

public class CreateScrapActivity extends BaseActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener, View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, CreateScrapActivity.class);
        context.startActivity(intent);
    }

    /**
     * 工具栏
     */
    private Toolbar mToolbar;

    /**
     * 创建布局
     */
    private View createLayout;

    /**
     * 创建布局 库位扫描输入框
     */
    private ScanEditText createLocationIdEditText;

    /**
     * 创建布局 barCode 扫描输入框
     */
    private ScanEditText createBarCodeEditText;

    /**
     * 详情布局
     */
    private View detailLayout;

    /**
     * 详情布局 名称文本框
     */
    private TextView detailItemNameTextView;

    /**
     * 详情布局 规格文本框
     */
    private TextView detailPackNameTextView;

    /**
     * 详情布局 数量输入框
     */
    private QtyEditText detailInputQtyEditText;

    /**
     * 详情布局 提交按钮
     */
    private Button detailSubmitButton;

    /**
     * 扫描输入框工具
     */
    private ScanEditTextTool scanEditTextTool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_scrap);

        findViews();
        fillCreateData();
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


    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressBack();
            }
        });
    }


    private void findViews() {
        createLayout = findViewById(R.id.create_Layout);
        createLocationIdEditText = (ScanEditText) createLayout.findViewById(R.id.locationId_EditText);
        createBarCodeEditText = (ScanEditText) createLayout.findViewById(R.id.barCode_EditText);

        detailLayout = findViewById(R.id.detail_Layout);
        detailItemNameTextView = (TextView) detailLayout.findViewById(R.id.itemName_TextView);
        detailPackNameTextView = (TextView) detailLayout.findViewById(R.id.packName_TextView);
        detailInputQtyEditText = (QtyEditText) detailLayout.findViewById(R.id.inputQty_EditView);
        detailSubmitButton = (Button) findViewById(R.id.submit_Button);

        detailSubmitButton.setOnClickListener(this);

        initToolBar();
    }

    private void fillCreateData() {
        createLayout.setVisibility(View.VISIBLE);
        detailLayout.setVisibility(View.GONE);

        createLocationIdEditText.requestFocus();
        scanEditTextTool = new ScanEditTextTool(this, createLocationIdEditText, createBarCodeEditText);
        scanEditTextTool.setComplete(this);
    }

    private void fillDetailData(Item item) {
        createLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.VISIBLE);

        detailItemNameTextView.setText(item.getName());
        detailPackNameTextView.setText(item.getPackName());
        detailInputQtyEditText.requestFocus();
    }

    private void pressBack() {
        if (detailLayout.getVisibility() == View.VISIBLE) {
            DialogTools.showTwoButtonDialog(this, "是否放弃转残次任务", "取消", "确定", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }, true);
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        pressBack();
    }

    @Override
    public void onComplete() {
        String locationId = createLocationIdEditText.getText().toString();
        String barCode = createBarCodeEditText.getText().toString();
        new StockGetItemTask(this, locationId, barCode).start();
    }

    @Override
    public void onError(ContentEditText editText) {

    }

    @Override
    public void OnBarCodeReceived(String s) {
        scanEditTextTool.setScanText(s);
    }

    @Override
    public void onClick(View v) {
        if (v == detailSubmitButton) {
            submit();
        }
    }

    /**
     * 提交
     */
    private void submit() {
        String locationId = createLocationIdEditText.getText().toString();
        String barCode = createBarCodeEditText.getText().toString();
        String qty = detailInputQtyEditText.getValue();

        if (TextUtils.isEmpty(locationId)) {
            return;
        }

        if (TextUtils.isEmpty(barCode)) {
            return;
        }

        if (TextUtils.isEmpty(qty)) {
            return;
        }

        new CreateScrapTask(this, locationId, barCode, qty).start();
    }

    /**
     * 获取商品信息
     */
    private class StockGetItemTask extends HttpAsyncTask<Item> {

        private String locationId;

        private String barCode;

        public StockGetItemTask(Context context, String locationId, String barCode) {
            super(context, true, true, false);
            this.locationId = locationId;
            this.barCode = barCode;
        }

        @Override
        public DataHull<Item> doInBackground() {
            return HttpApi.stockGetItem(locationId, barCode);
        }

        @Override
        public void onPostExecute(int updateId, Item result) {
            fillDetailData(result);
        }
    }

    /**
     * 提交残次数量
     */
    private class CreateScrapTask extends HttpAsyncTask<ResponseState> {

        private String locationId;

        private String barCode;

        private String qty;

        public CreateScrapTask(Context context, String locationId, String barCode, String qty) {
            super(context, true, true, false);
            this.locationId = locationId;
            this.barCode = barCode;
            this.qty = qty;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return HttpApi.createScrap(locationId, barCode, qty, BaseApplication.get().getUser().getUid());
        }

        @Override
        public void onPostExecute(int updateId, ResponseState result) {
            finish();
        }
    }
}
