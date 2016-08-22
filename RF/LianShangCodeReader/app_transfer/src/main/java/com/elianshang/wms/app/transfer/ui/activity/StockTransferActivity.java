package com.elianshang.wms.app.transfer.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.QtyEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.wms.app.transfer.R;
import com.elianshang.wms.app.transfer.bean.StockTransfer;
import com.elianshang.wms.app.transfer.controller.StockTransferController;
import com.elianshang.wms.app.transfer.ui.view.StockTransferView;


/**
 * 移库操作页
 */
public class StockTransferActivity extends DLBasePluginActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener, View.OnClickListener, StockTransferView {

    public static void launch(DLBasePluginActivity activity, String uid, String uToken, StockTransfer stockTransfer) {
        DLIntent intent = new DLIntent(activity.getPackageName(), StockTransferActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        intent.putExtra("stockTransfer", stockTransfer);
        activity.startPluginActivity(intent);
    }


    private Toolbar mToolbar;
    /**
     * taskId
     */
    private TextView mTaskView;
    /**
     * 商品名称
     */
    private TextView mItemNameView;
    /**
     * 包装单位
     */
    private TextView mItemPackNameView;
    /**
     * 商品数量
     */
    private TextView mItemQtyView;
    /**
     * 实际数量
     */
    private QtyEditText mItemQtyRealView;
    /**
     * 实际数量  container
     */
    private View mItemQtyRealContainerView;
    /**
     * 库位Id
     */
    private TextView mLocationIdView;
    /**
     * 确认库位Id
     */
    private ScanEditText mLocationIdConfirmView;
    /**
     * 转入/转出
     */
    private TextView mTypeNameView;

    private ScanEditTextTool scanEditTextTool;
    /**
     * 提交
     */
    private Button mSubmit;
    /**
     * 商品container
     */
    private View mItemView;
    /**
     * 库位container
     */
    private View mLocationView;
    /**
     * 商品库位
     */
    private TextView mItemLocationView;

    private String uId;

    private String uToken;

    private StockTransferController stockTransferController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocktransfer);
        findViews();
        readExtras();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (ScanManager.get() != null) {
            ScanManager.get().addListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ScanManager.get() != null) {
            ScanManager.get().removeListener(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
    }

    @Override
    public void onBackPressed() {
        DialogTools.showTwoButtonDialog(that, "任务未完成，确认退出？", "取消", "确认", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }, true);
    }

    private void readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

        uId = "141871359725260";
        uToken = "243202523137671";

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
        }

        StockTransfer stockTransfer = (StockTransfer) getIntent().getSerializableExtra("stockTransfer");

        stockTransferController = new StockTransferController(that, uId, uToken, stockTransfer, this);

    }

    private void findViews() {
        mTaskView = (TextView) findViewById(R.id.task_id);
        mItemNameView = (TextView) findViewById(R.id.item_name);
        mItemPackNameView = (TextView) findViewById(R.id.item_pack_name);
        mItemQtyView = (TextView) findViewById(R.id.item_qty);
        mItemQtyRealView = (QtyEditText) findViewById(R.id.item_qty_real);
        mItemQtyRealContainerView = findViewById(R.id.item_qty_real_container);
        mLocationIdView = (TextView) findViewById(R.id.location_id);
        mLocationIdConfirmView = (ScanEditText) findViewById(R.id.confirm_location_id);
        mSubmit = (Button) findViewById(R.id.submit_button);
        mTypeNameView = (TextView) findViewById(R.id.transfer_type_name);
        mItemView = findViewById(R.id.item);
        mLocationView = findViewById(R.id.location);
        mItemLocationView = (TextView) findViewById(R.id.item_locationId);

        mSubmit.setOnClickListener(this);
        mSubmit.setVisibility(View.GONE);

        initToolbar();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mSubmit) {
            stockTransferController.onSubmitClick(mItemQtyRealView.getValue());
        }
    }

    @Override
    public void onComplete() {
        stockTransferController.onComplete(mLocationIdConfirmView.getText().toString());
    }

    @Override
    public void onError(ContentEditText editText) {
    }

    @Override
    public void OnBarCodeReceived(String s) {
        scanEditTextTool.setScanText(s);

    }

    @Override
    public void showLocationConfirmView(String typeName, String taskId, String locationName) {
        mLocationView.setVisibility(View.VISIBLE);
        mItemView.setVisibility(View.GONE);
        mSubmit.setVisibility(View.GONE);

        mTaskView.setText(taskId);
        mTypeNameView.setText(typeName);
        mLocationIdView.setText(locationName);
        mLocationIdConfirmView.getText().clear();

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
        scanEditTextTool = new ScanEditTextTool(that, mLocationIdConfirmView);
        scanEditTextTool.setComplete(this);
    }

    @Override
    public void showItemView(String typeName, String itemName, String packName, String qty, String locationName) {
        mLocationView.setVisibility(View.GONE);
        mItemView.setVisibility(View.VISIBLE);
        mSubmit.setVisibility(View.VISIBLE);
        mItemQtyRealView.requestFocus();

        mTypeNameView.setText(typeName);
        mItemNameView.setText(itemName);
        mItemPackNameView.setText(packName);
        mItemQtyView.setText(qty);
        mItemQtyRealView.setHint(qty);
        mItemQtyRealView.setText(null);
        mItemLocationView.setText(locationName);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
        scanEditTextTool = null;
    }
}
