package com.elianshang.code.reader.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.view.View;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.R;
import com.elianshang.code.reader.ui.BaseActivity;

public class WelcomeActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 收货
     */
    private View receiptButton;

    /**
     * 上架
     */
    private View shelveButton;

    /**
     * 盘点
     */
    private View takeStockButton;

    /**
     * 转残次
     */
    private View createScrapButton;

    /**
     * 转退货
     */
    private View createReturnButton;
    /**
     * 移库
     */
    private View transferLocationButton;
    /**
     * 补货
     */
    private View procurementButton;

    /**
     * 拣货
     */
    private View pickButton;

    /**
     * QC
     */
    private View qualityControlButton;

    /**
     * 发车
     */
    private View shipButton;

    private PowerManager.WakeLock m_wklk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        findViews();

        if (!BaseApplication.get().isLogin()) {
            LoginActivity.launch(this);
        }

        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        m_wklk = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "cn");
        m_wklk.acquire(); //设置保持唤醒
    }

    private void findViews() {
        receiptButton = findViewById(R.id.receipt_Button);
        shelveButton = findViewById(R.id.shelve_Button);
        takeStockButton = findViewById(R.id.takeStock_Button);
        createScrapButton = findViewById(R.id.createScrap_Button);
        createReturnButton = findViewById(R.id.createReturn_Button);
        transferLocationButton = findViewById(R.id.transferLocation_Button);
        procurementButton = findViewById(R.id.procurement_Button);
        pickButton = findViewById(R.id.pick_Button);
        qualityControlButton = findViewById(R.id.qualityControl_Button);
        shipButton = findViewById(R.id.ship_Button);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode != RESULT_OK) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        m_wklk.release();
    }

    @Override
    public void onClick(View v) {
        if (v == receiptButton) {
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
            TransferActivity.launch(this);
        } else if (v == procurementButton) {
            ProcurementActivity.launch(this);
        } else if (v == pickButton) {
            PickActivity.launch(this);
        } else if (v == qualityControlButton) {
            QualityControlActivity.launch(this);
        } else if (v == shipButton) {
            ShipActivity.launch(this);
        }
    }
}
