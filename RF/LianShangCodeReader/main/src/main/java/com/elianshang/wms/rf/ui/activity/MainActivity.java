package com.elianshang.wms.rf.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.view.View;

import com.elianshang.wms.rf.BaseApplication;
import com.elianshang.wms.rf.R;
import com.elianshang.wms.rf.plugin.PluginTool;

public class MainActivity extends Activity implements View.OnClickListener {

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
        setContentView(R.layout.activity_main);

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
            PluginTool.load(this, "sdcard/DynamicLoadHost/lsh_app_receipt_v1.0_tttt.apk");
        } else if (v == shelveButton) {
            PluginTool.load(this, "sdcard/DynamicLoadHost/lsh_app_shelve_v1.0_tttt.apk");
        } else if (v == takeStockButton) {
            PluginTool.load(this, "sdcard/DynamicLoadHost/lsh_app_stack_v1.0_tttt.apk");
        } else if (v == createScrapButton) {
            PluginTool.load(this, "sdcard/DynamicLoadHost/lsh_app_create_scrap_v1.0_tttt.apk");
        } else if (v == createReturnButton) {
            PluginTool.load(this, "sdcard/DynamicLoadHost/lsh_app_create_return_v1.0_tttt.apk");
        } else if (v == transferLocationButton) {
        } else if (v == procurementButton) {
            PluginTool.load(this, "sdcard/DynamicLoadHost/lsh_app_procurement_v1.0_tttt.apk");
        } else if (v == pickButton) {
            PluginTool.load(this, "sdcard/DynamicLoadHost/lsh_app_pick_v1.0_tttt.apk");
        } else if (v == qualityControlButton) {
            PluginTool.load(this, "sdcard/DynamicLoadHost/lsh_app_qc_v1.0_tttt.apk");
        } else if (v == shipButton) {
        }
    }
}
