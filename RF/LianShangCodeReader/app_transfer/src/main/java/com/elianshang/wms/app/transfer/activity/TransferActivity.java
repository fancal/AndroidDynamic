package com.elianshang.wms.app.transfer.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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

import static com.elianshang.wms.app.transfer.R.id.location;


/**
 * 移库操作页
 */
public class TransferActivity extends DLBasePluginActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener, View.OnClickListener, StockTransferView, TextWatcher {

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

    private ScanEditText scanLayoutBarcodeEditText;

    private EditText scanLayoutOwnereEditText;

    private View workLayout;
    /**
     * 包装单位
     */
    private TextView mItemPackNameView;

    private TextView mItemBarcodeView;

    private TextView mItemSkuCodeView;

    private TextView mItemOwnerView;
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

    private boolean isItemClick = false;

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
    public void onStop() {
        super.onStop();
        scanLayoutLocationCodeEditText.removeTextChangedListener(this);
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
        scanLayoutBarcodeEditText = (ScanEditText) scanLayout.findViewById(R.id.barcode_EditText);
        scanLayoutOwnereEditText = (EditText) scanLayout.findViewById(R.id.owner_EditText);

        workLayout = findViewById(R.id.work_Layout);

        mItemView = findViewById(R.id.item);

        mItemPackNameView = (TextView) mItemView.findViewById(R.id.item_pack_name);
        mItemBarcodeView = (TextView) mItemView.findViewById(R.id.item_barcode);
        mItemSkuCodeView = (TextView) mItemView.findViewById(R.id.item_skuCode);
        mItemOwnerView = (TextView) mItemView.findViewById(R.id.item_owner);
        mItemQtyView = (TextView) mItemView.findViewById(R.id.item_qty);
        mItemQtyRealView = (QtyEditText) mItemView.findViewById(R.id.item_qty_real);
        mItemQtyRealCheckBox = (CheckBox) mItemView.findViewById(R.id.item_qty_real_CheckBox);
        mItemQtyRealContainerView = mItemView.findViewById(R.id.item_qty_real_container);
        mItemLocationView = (TextView) mItemView.findViewById(R.id.item_locationCode);

        mLocationView = findViewById(location);

        mLocationCodeView = (TextView) mLocationView.findViewById(R.id.location_id);
        mLocationCodeConfirmView = (ScanEditText) mLocationView.findViewById(R.id.confirm_location_id);
        mLocationCodeConfirmView.setCode(true);

        mTypeNameView = (TextView) findViewById(R.id.transfer_type_name);
        mSubmit = (Button) findViewById(R.id.submit_button);

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
        if (isItemClick) {
            return;
        }

        isItemClick = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isItemClick = false;
            }
        }, 500);

        if (v == mSubmit) {
            if (workLayout.getVisibility() == View.VISIBLE && mItemView.getVisibility() == View.VISIBLE) {
                stockTransferController.onSubmitClick(mItemQtyRealCheckBox.isChecked() ? "-1" : mItemQtyRealView.getValue());
            } else if (scanLayout.getVisibility() == View.VISIBLE) {
                stockTransferController.onScanComplete(scanLayoutLocationCodeEditText.getText().toString(), scanLayoutBarcodeEditText.getText().toString(), scanLayoutOwnereEditText.getText().toString());
            }
        }
    }

    @Override
    public void onComplete() {
        if (workLayout.getVisibility() == View.VISIBLE) {
            if (mLocationView.getVisibility() == View.VISIBLE) {
                stockTransferController.onWorkComplete(mLocationCodeConfirmView.getText().toString());
            }
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

    public void requestFocusBarcode() {
        scanLayoutBarcodeEditText.requestFocus();
    }

    public void requestFocusOwner() {
        scanLayoutOwnereEditText.requestFocus();
    }

    public void showScanLayout() {
        mTaskView.setVisibility(View.GONE);
        mTypeNameView.setVisibility(View.VISIBLE);
        mTypeNameView.setText("请扫描移出库位");
        scanLayout.setVisibility(View.VISIBLE);
        workLayout.setVisibility(View.GONE);
        mSubmit.setVisibility(View.VISIBLE);
        scanLayoutLocationCodeEditText.requestFocus();

        if (TextUtils.isEmpty(scanLayoutLocationCodeEditText.getText().toString())) {
            mSubmit.setEnabled(false);
        } else {
            mSubmit.setEnabled(true);
        }

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
        scanEditTextTool = new ScanEditTextTool(that, scanLayoutLocationCodeEditText, scanLayoutBarcodeEditText);
        scanEditTextTool.setComplete(this);
        scanLayoutLocationCodeEditText.addTextChangedListener(this);
    }

    @Override
    public void showLocationConfirmView(boolean isIn, String typeName, String taskId, String itemName, String barcode, String skuCoe, String owner, String packName, String qty, String locationName) {
        scanLayoutLocationCodeEditText.removeTextChangedListener(this);


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
            mItemBarcodeView.setText(barcode);
            mItemSkuCodeView.setText(skuCoe);
            mItemOwnerView.setText(owner);
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
    public void showItemView(String typeName, String itemName, String barcode, String skuCoe, String owner, String packName, String qty, String locationName, String numQty) {
        scanLayoutLocationCodeEditText.removeTextChangedListener(this);

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

        if (TextUtils.isEmpty(itemName)) {
            mItemNameView.setVisibility(View.GONE);
        } else {
            mItemNameView.setVisibility(View.VISIBLE);
            mItemNameView.setText(itemName);
        }

        if (TextUtils.isEmpty(barcode)) {
            mItemBarcodeView.setVisibility(View.GONE);
        } else {
            mItemBarcodeView.setVisibility(View.VISIBLE);
            mItemBarcodeView.setText(barcode);
        }

        if (TextUtils.isEmpty(skuCoe)) {
            mItemSkuCodeView.setVisibility(View.GONE);
        } else {
            mItemSkuCodeView.setVisibility(View.VISIBLE);
            mItemSkuCodeView.setText(skuCoe);
        }

        if (TextUtils.isEmpty(owner)) {
            mItemOwnerView.setVisibility(View.GONE);
        } else {
            mItemOwnerView.setVisibility(View.VISIBLE);
            mItemOwnerView.setText(owner);
        }

        if (TextUtils.isEmpty(packName)) {
            mItemPackNameView.setVisibility(View.GONE);
        } else {
            mItemPackNameView.setVisibility(View.VISIBLE);
            mItemPackNameView.setText(packName);
        }

        if (TextUtils.isEmpty(qty)) {
            mItemQtyView.setVisibility(View.GONE);
        } else {
            mItemQtyView.setVisibility(View.VISIBLE);
            mItemQtyView.setText(qty);
        }

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
            mItemQtyRealView.setText(null);
            mItemQtyRealCheckBox.setChecked(true);
        } else if ("".equals(numQty)) {
            mItemQtyRealContainerView.setVisibility(View.VISIBLE);
            mItemQtyRealView.setText(null);
            mItemQtyRealCheckBox.setChecked(false);
        } else {
            mItemQtyRealContainerView.setVisibility(View.VISIBLE);
            mItemQtyRealView.setText(null);
            mItemQtyRealCheckBox.setChecked(false);
        }

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
        scanEditTextTool = null;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s.toString())) {
            mSubmit.setEnabled(true);
        } else {
            mSubmit.setEnabled(false);
        }
    }
}
