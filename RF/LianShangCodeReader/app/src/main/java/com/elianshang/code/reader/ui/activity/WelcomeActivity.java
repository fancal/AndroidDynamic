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

/**
 * Created by wangwenwang on 16/7/28.
 */
public class WelcomeActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 登陆
     */
    private Button loginBtn;

    /**
     * 收货
     */
    private Button receiptBtn;

    /**
     * 上架
     */
    private Button shelveBtn;

    /**
     * 盘点
     */
    private Button takestockBtn;

    /**
     * 转残次
     */
    private Button createScrap;

    /**
     * 转退货
     */
    private Button createReturn;
    /**
     * 移库
     */
    private Button transferLocation;
    /**
     * 补货
     */
    private Button procurement;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        findViews();
    }

    private void findViews() {
        loginBtn = (Button) findViewById(R.id.login);
        receiptBtn = (Button) findViewById(R.id.receipt);
        shelveBtn = (Button) findViewById(R.id.shelve);
        takestockBtn = (Button) findViewById(R.id.takestock);
        createScrap = (Button) findViewById(R.id.create_scrap);
        createReturn = (Button) findViewById(R.id.create_return);
        transferLocation = (Button) findViewById(R.id.transfer_location);
        procurement = (Button) findViewById(R.id.procurement);

        loginBtn.setOnClickListener(this);
        receiptBtn.setOnClickListener(this);
        shelveBtn.setOnClickListener(this);
        takestockBtn.setOnClickListener(this);
        createScrap.setOnClickListener(this);
        createReturn.setOnClickListener(this);
        transferLocation.setOnClickListener(this);
        procurement.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == loginBtn) {
            LoginActivity.launch(this);
        } else if (v == receiptBtn) {
            ReceiptOpenActivity.launch(this);
        } else if (v == shelveBtn) {
            ShelveOpenActivity.launch(this);
        } else if (v == takestockBtn) {
            TakeStockActivity.launch(this, BaseApplication.get().getUserId());
        } else if (v == createScrap) {
            CreateScrapActivity.launch(this);
        } else if (v == createReturn) {
            CreateReturnActivity.launch(this);
        } else if (v == transferLocation) {
            new FetchTransferTask(this).start();
        } else if (v == procurement) {
            new FetchProcurementTask(this).start();
        }
    }
}
