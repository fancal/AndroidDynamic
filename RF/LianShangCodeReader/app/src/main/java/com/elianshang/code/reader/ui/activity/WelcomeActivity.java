package com.elianshang.code.reader.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.FetchProcurementTask;
import com.elianshang.code.reader.asyn.FetchTransferTask;
import com.elianshang.code.reader.ui.BaseActivity;

public class WelcomeActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 登陆
     */
    private Button loginButton;

    /**
     * 收货
     */
    private Button receiptButton;

    /**
     * 上架
     */
    private Button shelveButton;

    /**
     * 盘点
     */
    private Button takeStockButton;

    /**
     * 转残次
     */
    private Button createScrapButton;

    /**
     * 转退货
     */
    private Button createReturnButton;
    /**
     * 移库
     */
    private Button transferLocationButton;
    /**
     * 补货
     */
    private Button procurementButton;

    /**
     * 拣货
     */
    private Button pickButton;

    /**
     * QC
     */
    private Button qualityControlButton;

    /**
     * 发车
     */
    private Button shipButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        findViews();
    }

    private void findViews() {
        loginButton = (Button) findViewById(R.id.login_Button);
        receiptButton = (Button) findViewById(R.id.receipt_Button);
        shelveButton = (Button) findViewById(R.id.shelve_Button);
        takeStockButton = (Button) findViewById(R.id.takeStock_Button);
        createScrapButton = (Button) findViewById(R.id.createScrap_Button);
        createReturnButton = (Button) findViewById(R.id.createReturn_Button);
        transferLocationButton = (Button) findViewById(R.id.transferLocation_Button);
        procurementButton = (Button) findViewById(R.id.procurement_Button);
        pickButton = (Button) findViewById(R.id.pick_Button);
        qualityControlButton = (Button) findViewById(R.id.qualityControl_Button);
        shipButton = (Button) findViewById(R.id.ship_Button);

        loginButton.setOnClickListener(this);
        receiptButton.setOnClickListener(this);
        shelveButton.setOnClickListener(this);
        takeStockButton.setOnClickListener(this);
        createScrapButton.setOnClickListener(this);
        createReturnButton.setOnClickListener(this);
        transferLocationButton.setOnClickListener(this);
        procurementButton.setOnClickListener(this);
        qualityControlButton.setOnClickListener(this);
        pickButton.setOnClickListener(this);
        shipButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == loginButton) {
            LoginActivity.launch(this);
        } else if (v == receiptButton) {
            ReceiptOpenActivity.launch(this);
        } else if (v == shelveButton) {
            ShelveOpenActivity.launch(this);
        } else if (v == takeStockButton) {
            TakeStockActivity.launch(this, BaseApplication.get().getUserId());
        } else if (v == createScrapButton) {
            CreateScrapActivity.launch(this);
        } else if (v == createReturnButton) {
            CreateReturnActivity.launch(this);
        } else if (v == transferLocationButton) {
            new FetchTransferTask(this).start();
        } else if (v == procurementButton) {
            new FetchProcurementTask(this).start();
        } else if (v == pickButton) {
            PickActivity.launch(this);
        } else if (v == qualityControlButton) {
            QualityControlActivity.launch(this);
        } else if (v == shipButton) {
            ShipActivity.launch(this);
        }
    }
}
