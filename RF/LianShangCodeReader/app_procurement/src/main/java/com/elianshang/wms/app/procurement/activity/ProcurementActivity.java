package com.elianshang.wms.app.procurement.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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
import com.elianshang.wms.app.procurement.R;
import com.elianshang.wms.app.procurement.bean.Procurement;
import com.elianshang.wms.app.procurement.controller.ProcurementController;
import com.elianshang.wms.app.procurement.view.ProcurementView;

public class ProcurementActivity extends DLBasePluginActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener, View.OnClickListener, ProcurementView {

    public static void launch(DLBasePluginActivity activity, String uid, String uToken, Procurement procurement) {
        DLIntent intent = new DLIntent(activity.getPackageName(), ProcurementActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        intent.putExtra("procurement", procurement);
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

    private TextView mItemBarcodeView;

    private TextView mItemSkuCodeView;
    /**
     * 包装单位
     */
    private TextView mItemPackNameView;
    /**
     * 商品数量
     */
    private TextView mItemQtyView;

    /**
     * 补货剩余数量
     */
    private TextView mItemRemainQtyTextView;
    /**
     * 实际数量
     */
    private QtyEditText mItemQtyRealView;

    private QtyEditText mItemScatterQtyRealView;
    /**
     * 实际数量  container
     */
    private View mItemQtyRealContainerView;

    private TextView mItemToLocationTitleTextView;

    private TextView mItemToLocationTextView;

    private View mItemToLocationTextLayout;

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

    private View detailView;

    private TextView detailItemNameTextView;

    private TextView detailBarcodeTextView;

    private TextView detailSkuCodeTextView;

    private TextView detailPackNameTextView;

    private TextView detailQtyTextView;

    private TextView detailFromLocationTextView;

    private TextView detailToLocationTextView;

    private TextView detailFlashBackTextView;

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

    private ProcurementController procurementController;

    private boolean isItemClick = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.procurement_activity);
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
        DialogTools.showTwoButtonDialog(that, "是否暂退任务,下次回来将会继续", "取消", "确定", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }, true);
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        Procurement procurement = (Procurement) getIntent().getSerializableExtra("procurement");
        procurementController = new ProcurementController(that, uId, uToken, procurement, this);

        return true;
    }

    private void findViews() {
        mTaskView = (TextView) findViewById(R.id.task_id);
        mTypeNameView = (TextView) findViewById(R.id.transfer_type_name);
        mSubmit = (Button) findViewById(R.id.submit_button);

        mItemView = findViewById(R.id.item);
        mItemNameView = (TextView) mItemView.findViewById(R.id.item_name);
        mItemBarcodeView = (TextView) mItemView.findViewById(R.id.item_barcode);
        mItemSkuCodeView = (TextView) mItemView.findViewById(R.id.item_skuCode);
        mItemPackNameView = (TextView) mItemView.findViewById(R.id.item_pack_name);
        mItemQtyView = (TextView) mItemView.findViewById(R.id.item_qty);
        mItemRemainQtyTextView = (TextView) mItemView.findViewById(R.id.remainQty_TextView);
        mItemQtyRealView = (QtyEditText) mItemView.findViewById(R.id.item_qty_real);
        mItemScatterQtyRealView = (QtyEditText) mItemView.findViewById(R.id.item_scatterQty_real);
        mItemQtyRealContainerView = mItemView.findViewById(R.id.item_qty_real_container);
        mItemToLocationTitleTextView = (TextView) mItemView.findViewById(R.id.toLocationCodeTitle_TextView);
        mItemToLocationTextView = (TextView) mItemView.findViewById(R.id.toLocationCode_TextView);
        mItemToLocationTextLayout = mItemView.findViewById(R.id.toLocationCode_Layout);
        mItemLocationView = (TextView) mItemView.findViewById(R.id.item_locationCode);


        mLocationView = findViewById(R.id.location);
        mLocationCodeView = (TextView) mLocationView.findViewById(R.id.location_id);
        mLocationCodeConfirmView = (ScanEditText) mLocationView.findViewById(R.id.confirm_location_id);
        mLocationCodeConfirmView.setCode(true);

        detailView = findViewById(R.id.detail_Layout);
        detailItemNameTextView = (TextView) detailView.findViewById(R.id.itemName_TextView);
        detailBarcodeTextView = (TextView) detailView.findViewById(R.id.barcode_TextView);
        detailSkuCodeTextView = (TextView) detailView.findViewById(R.id.skuCode_TextView);
        detailPackNameTextView = (TextView) detailView.findViewById(R.id.packName_TextView);
        detailQtyTextView = (TextView) detailView.findViewById(R.id.qty_TextView);
        detailFromLocationTextView = (TextView) detailView.findViewById(R.id.fromLocationCode_TextView);
        detailToLocationTextView = (TextView) detailView.findViewById(R.id.toLocationCode_TextView);
        detailFlashBackTextView = (TextView) detailView.findViewById(R.id.flashBack_TextView);

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
            if (detailView.getVisibility() == View.VISIBLE) {
                procurementController.onBindTaskClick();
            } else if (mItemView.getVisibility() == View.VISIBLE) {
                procurementController.onSubmitClick(mItemQtyRealView.getValue(), mItemScatterQtyRealView.getValue());
            }
        }
    }

    @Override
    public void onComplete() {
        procurementController.onComplete(mLocationCodeConfirmView.getText().toString());
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

    @Override
    public void showDetailView(String taskId, String itemName, String barcode, String skuCode, String packName, String qty, String fromLocationCode, String toLocationCode, String flashBack) {
        detailView.setVisibility(View.VISIBLE);
        mLocationView.setVisibility(View.GONE);
        mItemView.setVisibility(View.GONE);
        mSubmit.setVisibility(View.VISIBLE);

        mTaskView.setText(taskId);
        mTypeNameView.setText("补货总览");
        detailItemNameTextView.setText(itemName);
        detailBarcodeTextView.setText(barcode);
        detailSkuCodeTextView.setText(skuCode);
        detailPackNameTextView.setText(packName);
        detailQtyTextView.setText(qty);
        detailFromLocationTextView.setText(fromLocationCode);
        detailToLocationTextView.setText(toLocationCode);
        detailFlashBackTextView.setText(flashBack);
        mSubmit.setText("开始补货");
    }

    @Override
    public void showLocationConfirmView(boolean isIn, String typeName, String taskId, String itemName, String barcode, String skuCode, String packName, String qty, String fromLocationCode, String toLocationCode, String locationName) {
        mLocationView.setVisibility(View.VISIBLE);
        mSubmit.setVisibility(View.GONE);
        detailView.setVisibility(View.GONE);

        if (isIn) {
            mItemView.setVisibility(View.VISIBLE);
            mItemLocationView.setVisibility(View.GONE);
            mItemQtyRealContainerView.setVisibility(View.GONE);
            mItemNameView.setVisibility(View.VISIBLE);
            mItemPackNameView.setVisibility(View.VISIBLE);
            mItemQtyView.setVisibility(View.VISIBLE);
            mItemRemainQtyTextView.setVisibility(View.GONE);

            mItemNameView.setText(itemName);
            mItemBarcodeView.setText(barcode);
            mItemSkuCodeView.setText(skuCode);
            mItemPackNameView.setText(packName);
            mItemQtyView.setText(qty);
            if (TextUtils.isEmpty(fromLocationCode)) {
                mItemToLocationTextLayout.setVisibility(View.GONE);

            } else {
                mItemToLocationTextLayout.setVisibility(View.VISIBLE);
                mItemToLocationTitleTextView.setText("移出库位：");
                mItemToLocationTextView.setText(fromLocationCode);
            }
        } else {
            mItemView.setVisibility(View.GONE);
        }

        mTaskView.setText(taskId);
        mTypeNameView.setText(typeName);

        mLocationCodeView.setText(locationName);
        mLocationCodeConfirmView.getText().clear();

        mLocationCodeConfirmView.requestFocus();

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
        scanEditTextTool = new ScanEditTextTool(that, mLocationCodeConfirmView);
        scanEditTextTool.setComplete(this);
    }

    @Override
    public void showItemView(String typeName, String itemName, String barcode, String skuCode, String packName, String qty, String remainQty, String fromLocationCode, String toLocationCode, String locationName, String numQty) {
        mLocationView.setVisibility(View.GONE);
        mItemView.setVisibility(View.VISIBLE);
        detailView.setVisibility(View.GONE);
        mSubmit.setVisibility(View.VISIBLE);
        mItemQtyRealView.requestFocus();

        mItemLocationView.setVisibility(View.VISIBLE);
        mItemQtyRealContainerView.setVisibility(View.VISIBLE);
        mItemNameView.setVisibility(View.VISIBLE);
        mItemPackNameView.setVisibility(View.VISIBLE);
        mItemQtyView.setVisibility(View.VISIBLE);
        mItemToLocationTextLayout.setVisibility(View.VISIBLE);

        mTypeNameView.setText(typeName);
        mItemNameView.setText(itemName);
        mItemBarcodeView.setText(barcode);
        mItemSkuCodeView.setText(skuCode);
        mItemPackNameView.setText(packName);
        mItemQtyView.setText(qty);
        mItemLocationView.setText(locationName);
        mItemToLocationTitleTextView.setText("移入库位：");
        mItemToLocationTextView.setText(toLocationCode);

        if (TextUtils.isEmpty(remainQty)) {
            mItemRemainQtyTextView.setVisibility(View.GONE);
        } else {
            mItemRemainQtyTextView.setText(remainQty);
            mItemRemainQtyTextView.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(numQty)) {
            mItemQtyRealContainerView.setVisibility(View.GONE);
            mItemQtyRealView.setText(null);
        } else {
            mItemQtyRealContainerView.setVisibility(View.VISIBLE);
            mItemQtyRealView.setText(null);
        }

        mSubmit.setText("提交");

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
        scanEditTextTool = null;
    }
}
