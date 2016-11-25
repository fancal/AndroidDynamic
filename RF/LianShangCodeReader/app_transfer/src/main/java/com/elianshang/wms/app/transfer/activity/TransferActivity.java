package com.elianshang.wms.app.transfer.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.elianshang.wms.app.transfer.bean.Transfer;
import com.elianshang.wms.app.transfer.controller.StockTransferController;
import com.elianshang.wms.app.transfer.view.StockTransferView;


/**
 * 移库操作页
 */
public class TransferActivity extends DLBasePluginActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener, View.OnClickListener, StockTransferView {

    public static void launch(DLBasePluginActivity activity, String uid, String uToken) {
        DLIntent intent = new DLIntent(activity.getPackageName(), TransferActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        activity.startPluginActivity(intent);
    }

    public static void launch(DLBasePluginActivity activity, String uid, String uToken, Transfer transfer) {
        DLIntent intent = new DLIntent(activity.getPackageName(), TransferActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        intent.putExtra("transfer", transfer);
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

    private View scanLayout;

    private ScanEditText scanLayoutLocationCodeEditText;

    private View workLayout;
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

    private CheckBox mItemQtyRealCheckBox;
    /**
     * 实际数量  container
     */
    private View mItemQtyRealContainerView;
    /**
     * 库位Id
     */
    private TextView mLocationCodeView;
    /**
     * 确认库位Id
     */
    private ScanEditText mLocationCodeConfirmView;
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
        setContentView(R.layout.stocktransfer_activity_main);
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
        if (!stockTransferController.onBackPressed()) {
            DialogTools.showTwoButtonDialog(that, "是否暂退任务,下次回来将会继续", "取消", "确定", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }, true);
        }
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        Object object = getIntent().getSerializableExtra("transfer");
        if (object != null) {//计划移库
            Transfer transfer = (Transfer) object;
            stockTransferController = new StockTransferController(that, uId, uToken, transfer, this);
        } else {//即时移库
            stockTransferController = new StockTransferController(that, uId, uToken, null, this);
        }

        return true;

    }

    private void findViews() {
        mTaskView = (TextView) findViewById(R.id.task_id);
        mItemNameView = (TextView) findViewById(R.id.item_name);
        scanLayout = findViewById(R.id.scan_Layout);
        scanLayoutLocationCodeEditText = (ScanEditText) scanLayout.findViewById(R.id.locationCode_EditText);
        scanLayoutLocationCodeEditText.setCode(true);

        workLayout = findViewById(R.id.work_Layout);

        mItemPackNameView = (TextView) findViewById(R.id.item_pack_name);
        mItemQtyView = (TextView) findViewById(R.id.item_qty);
        mItemQtyRealView = (QtyEditText) findViewById(R.id.item_qty_real);
        mItemQtyRealCheckBox = (CheckBox) findViewById(R.id.item_qty_real_CheckBox);
        mItemQtyRealContainerView = findViewById(R.id.item_qty_real_container);
        mLocationCodeView = (TextView) findViewById(R.id.location_id);
        mLocationCodeConfirmView = (ScanEditText) findViewById(R.id.confirm_location_id);
        mLocationCodeConfirmView.setCode(true);
        mSubmit = (Button) findViewById(R.id.submit_button);
        mTypeNameView = (TextView) findViewById(R.id.transfer_type_name);
        mItemView = findViewById(R.id.item);
        mLocationView = findViewById(R.id.location);
        mItemLocationView = (TextView) findViewById(R.id.item_locationCode);

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
            stockTransferController.onSubmitClick(mItemQtyRealCheckBox.isChecked() ? "-1" : mItemQtyRealView.getValue());
        }
    }

    @Override
    public void onComplete() {
        if (scanLayout.getVisibility() == View.VISIBLE) {
            stockTransferController.onScanComplete(scanLayoutLocationCodeEditText.getText().toString());
        } else if (workLayout.getVisibility() == View.VISIBLE) {
            stockTransferController.onWorkComplete(mLocationCodeConfirmView.getText().toString());
        }
    }

    @Override
    public void onError(ContentEditText editText) {
    }

    @Override
    public void OnBarCodeReceived(String s) {
        if (scanEditTextTool == null) {
            return;
        }
        scanEditTextTool.setScanText(s);
    }

    public void showScanLayout() {
        mTaskView.setVisibility(View.GONE);
        mTypeNameView.setVisibility(View.VISIBLE);
        mTypeNameView.setText("请扫描移出库位");
        scanLayout.setVisibility(View.VISIBLE);
        workLayout.setVisibility(View.GONE);
        mSubmit.setVisibility(View.GONE);
        scanLayoutLocationCodeEditText.setText(null);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
        scanEditTextTool = new ScanEditTextTool(that, scanLayoutLocationCodeEditText);
        scanEditTextTool.setComplete(this);
    }

    @Override
    public void showLocationConfirmView(boolean isIn, String typeName, String taskId, String itemName, String packName, String qty, String locationName) {
        scanLayout.setVisibility(View.GONE);
        workLayout.setVisibility(View.VISIBLE);
        mLocationView.setVisibility(View.VISIBLE);
        mSubmit.setVisibility(View.GONE);

        if (isIn) {
            mItemView.setVisibility(View.VISIBLE);
            mItemLocationView.setVisibility(View.GONE);
            mItemQtyRealContainerView.setVisibility(View.GONE);
            mItemNameView.setVisibility(View.VISIBLE);
            mItemPackNameView.setVisibility(View.VISIBLE);
            mItemQtyView.setVisibility(View.VISIBLE);

            mItemNameView.setText(itemName);
            mItemPackNameView.setText(packName);
            mItemQtyView.setText(qty);
        } else {
            mItemView.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(taskId)) {
            if ("任务：0".equals(taskId)) {
                mTaskView.setVisibility(View.GONE);
            } else {
                mTaskView.setVisibility(View.VISIBLE);
                mTaskView.setText(taskId);
            }
        } else {
            mTaskView.setVisibility(View.GONE);
        }

        mTypeNameView.setText(typeName);

        mLocationCodeView.setText(TextUtils.isEmpty(locationName) ? "请选择闲置库位" : locationName);
        mLocationCodeConfirmView.getText().clear();

        mLocationCodeConfirmView.requestFocus();

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
        scanEditTextTool = new ScanEditTextTool(that, mLocationCodeConfirmView);
        scanEditTextTool.setComplete(this);
    }

    @Override
    public void showItemView(String typeName, String itemName, String packName, String qty, String locationName, String numQty) {
        scanLayout.setVisibility(View.GONE);
        workLayout.setVisibility(View.VISIBLE);
        mLocationView.setVisibility(View.GONE);
        mItemView.setVisibility(View.VISIBLE);
        mSubmit.setVisibility(View.VISIBLE);
        mItemQtyRealView.requestFocus();

        mItemLocationView.setVisibility(View.VISIBLE);
        mItemQtyRealContainerView.setVisibility(View.VISIBLE);
        mItemNameView.setVisibility(View.VISIBLE);
        mItemPackNameView.setVisibility(View.VISIBLE);
        mItemQtyView.setVisibility(View.VISIBLE);

        mTypeNameView.setText(typeName);
        mItemNameView.setText(itemName);
        mItemPackNameView.setText(packName);
        mItemQtyView.setText(qty);
        mItemLocationView.setText(locationName);
        mItemQtyRealCheckBox.setChecked(false);
        mItemQtyRealView.setVisibility(View.VISIBLE);
        mItemQtyRealCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mItemQtyRealView.setVisibility(View.GONE);
                } else {
                    mItemQtyRealView.setVisibility(View.VISIBLE);
                }
            }
        });

        if (numQty == null) {
            mItemQtyRealContainerView.setVisibility(View.GONE);
            mItemQtyRealView.setHint(null);
            mItemQtyRealView.setText(null);
            mItemQtyRealCheckBox.setChecked(true);
        } else if ("".equals(numQty)) {
            mItemQtyRealContainerView.setVisibility(View.VISIBLE);
            mItemQtyRealView.setHint("0");
            mItemQtyRealView.setText(null);
            mItemQtyRealCheckBox.setChecked(false);
        } else {
            mItemQtyRealContainerView.setVisibility(View.VISIBLE);
            mItemQtyRealView.setHint(numQty);
            mItemQtyRealView.setText(null);
            mItemQtyRealCheckBox.setChecked(false);
        }

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
        scanEditTextTool = null;
    }
}
