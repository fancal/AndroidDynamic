package com.elianshang.wms.app.sow.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.wms.app.sow.R;
import com.elianshang.wms.app.sow.adapter.OrderListAdapter;
import com.elianshang.wms.app.sow.adapter.StoreListAdapter;
import com.elianshang.wms.app.sow.bean.OrderList;
import com.elianshang.wms.app.sow.bean.Sow;
import com.elianshang.wms.app.sow.bean.StoreList;
import com.elianshang.wms.app.sow.provider.OrderListProvider;
import com.elianshang.wms.app.sow.provider.StoreListProvider;
import com.elianshang.wms.app.sow.provider.ViewProvider;
import com.xue.http.impl.DataHull;

import java.util.ArrayList;

public class SowActivity extends DLBasePluginActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener, View.OnClickListener, AdapterView.OnItemClickListener {

    public static void launch(DLBasePluginActivity activity, String uid, String uToken, Sow sow, Type type) {
        DLIntent intent = new DLIntent(activity.getPackageName(), SowActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        if (type != null) {
            intent.putExtra("type", type.intValue());
        }
        if (sow != null) {
            intent.putExtra("sow", sow);
        }
        activity.startPluginActivity(intent);
    }

    private String uId;

    private String uToken;

    private Toolbar mToolbar;

    private View scanLayout;

    private View scanContainerLayout;

    private ScanEditText scanContainerIdEditText;

    private View scanOrderLayout;

    private ScanEditText scanOrderBarcodeEditText;

    private Button scanSubmitButton;

    /**
     * 订单播种--order列表
     */
    private View orderListLayout;

    private ListView orderListView;

    /**
     * 订单播种--store列表
     */
    private View storeListLayout;

    private View storeListTab1;

    private View storeListTab2;

    private View storeListEmptyView;

    private ListView storeListView;

    private OrderList orderList;

    private OrderList.Item curOrderItem;

    private OrderListAdapter orderListAdapter;

    private StoreList storeList;

    private StoreList.Item curStoreItem;

    private StoreListAdapter storeListAdapter;

    private ScanEditTextTool scanEditTextTool;

    private Sow curSow;

    private String serialNumber;

    private Type mType;

    /**
     * 播种方式
     */
    public enum Type {
        /**
         * 托盘码播种
         */
        CONTAINER(1),

        /**
         * 订单播种
         */
        ORDER(2);

        int type;

        Type(int type) {
            this.type = type;
        }

        public int intValue() {
            return type;
        }

        public static Type getType(int value) {
            if (value == 2) {
                return ORDER;
            }
            return CONTAINER;
        }


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sow_activity_main);

        if (readExtra()) {
            findViews();
        }
    }

    private boolean readExtra() {
        Intent intent = getIntent();
        uId = intent.getStringExtra("uId");
        uToken = intent.getStringExtra("uToken");
        curSow = (Sow) intent.getSerializableExtra("sow");
        mType = Type.getType(intent.getIntExtra("type", 1));

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }


        return true;
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

    private void findViews() {

        scanLayout = findViewById(R.id.sow_scan);
        scanContainerLayout = scanLayout.findViewById(R.id.container);
        scanContainerIdEditText = (ScanEditText) scanLayout.findViewById(R.id.containerId_EditText);
        scanOrderLayout = scanLayout.findViewById(R.id.order);
        scanOrderBarcodeEditText = (ScanEditText) scanLayout.findViewById(R.id.barcode_EditText);
        scanSubmitButton = (Button) scanLayout.findViewById(R.id.Submit_Button);

        orderListLayout = findViewById(R.id.order_list);
        orderListView = (ListView) orderListLayout.findViewById(R.id.listView);

        storeListLayout = findViewById(R.id.store_list);
        storeListTab1 = storeListLayout.findViewById(R.id.tab1);
        storeListTab2 = storeListLayout.findViewById(R.id.tab2);
        storeListEmptyView = storeListLayout.findViewById(R.id.emptyView);
        storeListView = (ListView) storeListLayout.findViewById(R.id.listView);

        storeListTab1.setOnClickListener(this);
        storeListTab2.setOnClickListener(this);

        initToolbar();

        if (curSow != null) {
            launchDetail();
        } else {
            fillScan();
        }
    }

    private void fillScan() {
        scanLayout.setVisibility(View.VISIBLE);
        orderListLayout.setVisibility(View.GONE);
        storeListLayout.setVisibility(View.GONE);

        scanSubmitButton.setOnClickListener(this);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
        if (mType == Type.CONTAINER) {
            scanContainerLayout.setVisibility(View.VISIBLE);
            scanOrderLayout.setVisibility(View.GONE);
            scanContainerIdEditText.requestFocus();
            scanEditTextTool = new ScanEditTextTool(that, scanContainerIdEditText);
            scanEditTextTool.setComplete(this);
        } else {
            scanContainerLayout.setVisibility(View.GONE);
            scanOrderLayout.setVisibility(View.VISIBLE);
            scanOrderBarcodeEditText.requestFocus();
            scanEditTextTool = new ScanEditTextTool(that, scanOrderBarcodeEditText);
            scanEditTextTool.setComplete(this);
        }
    }

    private void fillOrderListLayout() {
        scanLayout.setVisibility(View.GONE);
        orderListLayout.setVisibility(View.VISIBLE);
        storeListLayout.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }

        if (orderList == null) {
            return;
        }

        if (orderListAdapter == null) {
            orderListAdapter = new OrderListAdapter(that);
            orderListAdapter.setOrderList(orderList);
            orderListView.setAdapter(orderListAdapter);
        }
        orderListAdapter.notifyDataSetChanged();

        orderListView.setOnItemClickListener(this);


    }

    private void fillStoreListLayout() {
        scanLayout.setVisibility(View.GONE);
        orderListLayout.setVisibility(View.GONE);
        storeListLayout.setVisibility(View.VISIBLE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }

        ArrayList<StoreList.Item> stores = null;
        if (storeList != null) {
            stores = storeListTab1.isSelected() ? storeList.getSmallStoreList() : storeList.getSuperStoreList();
        }

        if (stores == null) {
            storeListEmptyView.setVisibility(View.VISIBLE);
            storeListView.setVisibility(View.GONE);
        } else {
            storeListEmptyView.setVisibility(View.GONE);
            storeListView.setVisibility(View.VISIBLE);

            if (storeListAdapter == null) {
                storeListAdapter = new StoreListAdapter(that);
                storeListView.setAdapter(storeListAdapter);
            }
            storeListAdapter.setStoreList(stores);
            storeListAdapter.notifyDataSetChanged();
        }

        storeListView.setOnItemClickListener(this);

    }


    private void launchDetail() {
        SowDetailActivity.launch(this, uId, uToken, curSow, mToolbar.getTitle().toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == RESULT_OK) {
            StoreList storeList = (StoreList) data.getSerializableExtra("storeList");
            if (storeList == null) {
                fillComplete();
            } else {
                this.storeList = storeList;
                fillStoreListLayout();
            }
        }
    }

    private void fillComplete() {
        fillScan();
        curSow = null;
        scanContainerIdEditText.setText(null);
        scanOrderBarcodeEditText.setText(null);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolbar.setTitle(mType == Type.CONTAINER ? "托盘播种" : "订单播种");
    }

    @Override
    public void onBackPressed() {
        if (orderListLayout.getVisibility() == View.VISIBLE) {
            fillScan();
        } else if (storeListLayout.getVisibility() == View.VISIBLE) {
            if (mType == Type.ORDER) {
                fillOrderListLayout();
            } else {
                fillScan();
            }
        } else {
            finish();
        }
    }

    @Override
    public void onComplete() {
        if (scanLayout.getVisibility() == View.VISIBLE) {
            if (mType == Type.CONTAINER) {
                Editable editable = scanContainerIdEditText.getText();
                if (editable != null) {
                    String containerId = editable.toString();
                    if (!TextUtils.isEmpty(containerId)) {
                        scanSubmitButton.setEnabled(true);
                    } else {
                        scanSubmitButton.setEnabled(false);
                    }
                }
            } else {
                Editable editable2 = scanOrderBarcodeEditText.getText();
                if (editable2 != null) {
                    String barcode = editable2.toString();
                    if (!TextUtils.isEmpty(barcode)) {
                        scanSubmitButton.setEnabled(true);
                    } else {
                        scanSubmitButton.setEnabled(false);
                    }
                }
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (orderListLayout.getVisibility() == View.VISIBLE) {
            curOrderItem = orderList.get(position);
            storeListTab1.setSelected(true);//默认选中第一个
            storeListTab2.setSelected(false);

            Editable editable2 = scanOrderBarcodeEditText.getText();
            if (editable2 != null) {
                String barcode = editable2.toString();
                if (!TextUtils.isEmpty(barcode)) {
                    new StoreListTask(that, barcode, "", curOrderItem.getOrderId(), "").start();
                }
            }
        } else if (storeListLayout.getVisibility() == View.VISIBLE) {
            curStoreItem = storeListAdapter.getItem(position);

            String containerId = "0";
            if (mType == Type.CONTAINER) {
                Editable editable = scanContainerIdEditText.getText();
                if (editable != null) {
                    containerId = editable.toString();
                }
            }
            new ViewTask(that, curStoreItem.getTaskId(), containerId).start();

        }
    }

    @Override
    public void onClick(View v) {
        if (v == scanSubmitButton) {
            if (mType == Type.CONTAINER) {
                storeListTab1.setSelected(true);//默认选中第一个
                storeListTab2.setSelected(false);

                Editable editable = scanContainerIdEditText.getText();
                if (editable != null) {
                    String containerId = editable.toString();
                    if (!TextUtils.isEmpty(containerId)) {
//                        new AssignByContainerTask(that, storeNo, "2").start();
                        new StoreListTask(that, "", containerId, "", "").start();
                    }
                }
            } else {

                Editable editable2 = scanOrderBarcodeEditText.getText();
                if (editable2 != null) {
                    String barcode = editable2.toString();
                    if (!TextUtils.isEmpty(barcode)) {
//                        new AssignByOrderTask(that, barcode, "2").start();
                        new OrderListTask(that, barcode).start();
                    }
                }
            }
        } else if (v == storeListTab1) {
            storeListTab1.setSelected(true);
            storeListTab2.setSelected(false);

            fillStoreListLayout();
        } else if (v == storeListTab2) {
            storeListTab1.setSelected(false);
            storeListTab2.setSelected(true);

            fillStoreListLayout();
        }
    }

    private class OrderListTask extends HttpAsyncTask<OrderList> {

        private String barcode;

        public OrderListTask(Context context, String barcode) {
            super(context, true, true, false);
            this.barcode = barcode;
        }

        @Override
        public DataHull<OrderList> doInBackground() {
            return OrderListProvider.request(context, uId, uToken, barcode);
        }

        @Override
        public void onPostExecute(OrderList result) {
            orderList = result;
            fillOrderListLayout();
        }
    }

    private class StoreListTask extends HttpAsyncTask<StoreList> {

        private String barcode;

        private String containerId;

        private String orderId;

        private String storeType;

        public StoreListTask(Context context, String barcode, String containerId, String orderId, String storeType) {
            super(context, true, true, false);
            this.barcode = barcode;
            this.containerId = containerId;
            this.orderId = orderId;
            this.storeType = storeType;
        }

        @Override
        public DataHull<StoreList> doInBackground() {
            return StoreListProvider.request(context, uId, uToken, barcode, containerId, orderId, storeType);
        }

        @Override
        public void onPostExecute(StoreList result) {
            storeList = result;
            fillStoreListLayout();
        }


        @Override
        public void dataNull(String errMsg) {
            fillStoreListLayout();
        }
    }

    private class ViewTask extends HttpAsyncTask<Sow> {

        private String containerId;

        private String taskId;

        public ViewTask(Context context, String taskId, String containerId) {
            super(context, true, true, false);
            this.containerId = containerId;
            this.taskId = taskId;
        }

        @Override
        public DataHull<Sow> doInBackground() {
            return ViewProvider.request(context, uId, uToken, taskId, containerId);
        }

        @Override
        public void onPostExecute(Sow result) {
            curSow = result;
            launchDetail();
        }
    }


}
