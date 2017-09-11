package com.elianshang.wms.app.pick.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.tool.FloatUtils;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.QtyEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.pick.R;
import com.elianshang.wms.app.pick.bean.Pick;
import com.elianshang.wms.app.pick.bean.PickLocation;
import com.elianshang.wms.app.pick.bean.Split;
import com.elianshang.wms.app.pick.provider.HoldProvider;
import com.elianshang.wms.app.pick.provider.ScanPickLocationProvider;
import com.elianshang.wms.app.pick.provider.ScanPickProvider;
import com.elianshang.wms.app.pick.provider.SkipProvider;
import com.elianshang.wms.app.pick.provider.SplitNewPickTaskProvider;
import com.xue.http.impl.DataHull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PickActivity extends DLBasePluginActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener, View.OnClickListener, TextWatcher {

    public static void launch(DLBasePluginActivity activity, String uid, String uToken, PickLocation pickLocation) {
        DLIntent intent = new DLIntent(activity.getPackageName(), PickActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        if (pickLocation != null) {
            intent.putExtra("pickLocation", pickLocation);
        }
        activity.startPluginActivity(intent);
    }

    private String uId;

    private String uToken;

    private Toolbar mToolbar;
    /**
     * 移库 第一页容器
     */
    private View taskLayout;
    /**
     * 移库 第二页容器
     */
    private View locationLayout;
    /**
     * 移库 第三页容器
     */
    private View collectionLayout;

    private View splitLayout;

    /**
     * 第一页动态父容器
     */
    private LinearLayout taskLayoutBodyLayout;

    /**
     * 第一页添加按钮
     */
    private Button taskLayoutAddButton;

    /**
     * 第二页 顶部文字
     */
    private TextView locationLayoutHeadTextView;

    /**
     * 第二页 库位码
     */
    private TextView locationLayoutContainerIdCodeView;

    private TextView locationLayoutPickTaskIdView;

    private TextView locationLayoutLocationCodeView;

    private TextView locationLayoutPickTaskOrderView;

    private TextView locationLayoutCustomerNameView;
    /**
     * 第二页 确认库位码
     */
    private ScanEditText locationLayoutConfirmLocationCodeView;

    private TextView locationLayoutItemName;

    private TextView locationLayoutBarcode;

    private TextView locationLayoutSkuCode;

    private TextView locationLayoutPackCode;

    private TextView locationLayoutPackName;

    /**
     * 第二页 分配商品数量
     */
    private TextView locationLayoutAllocQty;
    /**
     * 第二页 实际输入数量
     */
    private QtyEditText locationLayoutQty;

    /**
     * 第二页 确认库位布局
     */
    private View locationLayoutLocationCodeLayout;

    /**
     * 第二页 系统数量布局
     */
    private View locationLayoutSystemQtyLayout;

    /**
     * 第二页 输入数量布局
     */
    private View locationLayoutInputQtyLayot;

    private View locationLayoutItemNameLayot;

    private View locationLayoutBarcodeLayot;

    private View locationLayoutSkuCodeLayot;

    private View locationLayoutPackCodeLayout;

    private View locationLayoutPackNameLayot;

    private View locationLayoutSplitButton;

    private View locationLayoutSkipButton;

    private View locationLayoutHoldButton;

    /**
     * 第三页 集货码
     */
    private TextView collectionLayoutCollectionIdView;

    private TextView collectionLayoutPickTaskIdView;

    private TextView collectionLayoutContainerIdCodeView;

    private TextView collectionLayoutPickTaskOrderView;

    private TextView collectionLayoutCustomerNameView;

    /**
     * 第三页 确认集货码
     */
    private ScanEditText collectionLayoutConfirmCollectionIdView;

    private ScanEditText splitLayoutContainerIdEditText;

    /**
     * 当前所在页 (0:taskLayout   1:locationLayout  2：collectionLayout 3:splitLayout)
     */
    private int mCurPage = 0;
    private Button mSubmit;
    private ScanEditTextTool scanEditTextTool;
    private Pick mPick;

    private ArrayList<ViewHolder> viewHolderList = new ArrayList();

    private String serialNumber;

    private boolean isItemClick;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);

        findViews();
        readExtra();

        ScanManager.init(that);
    }

    private boolean readExtra() {
        Intent intent = getIntent();
        uId = intent.getStringExtra("uId");
        uToken = intent.getStringExtra("uToken");
        PickLocation pickLocation = (PickLocation) intent.getSerializableExtra("pickLocation");

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        if (pickLocation != null) {
            fillPick(pickLocation);
        } else {
            mCurPage = 0;
            setCurPageView();
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        locationLayoutQty.addTextChangedListener(this);
        if (ScanManager.get() != null) {
            ScanManager.get().addListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        locationLayoutQty.removeTextChangedListener(this);
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
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        taskLayout = findViewById(R.id.pick_task);
        locationLayout = findViewById(R.id.pick_location);
        collectionLayout = findViewById(R.id.pick_collection);
        splitLayout = findViewById(R.id.pick_split);
        taskLayoutBodyLayout = (LinearLayout) taskLayout.findViewById(R.id.body_Layout);
        taskLayoutAddButton = (Button) taskLayout.findViewById(R.id.add_Button);

        locationLayoutHeadTextView = (TextView) locationLayout.findViewById(R.id.head_TextView);
        locationLayoutContainerIdCodeView = (TextView) locationLayout.findViewById(R.id.containerId_TextView);
        locationLayoutPickTaskIdView = (TextView) locationLayout.findViewById(R.id.pickTaskId_TextView);
        locationLayoutLocationCodeView = (TextView) locationLayout.findViewById(R.id.location_id);
        locationLayoutPickTaskOrderView = (TextView) locationLayout.findViewById(R.id.pickTaskOrder_TextView);
        locationLayoutCustomerNameView = (TextView) locationLayout.findViewById(R.id.storeName_TextView);
        locationLayoutConfirmLocationCodeView = (ScanEditText) locationLayout.findViewById(R.id.confirm_location_id);
        locationLayoutConfirmLocationCodeView.setCode(true);
        locationLayoutItemName = (TextView) locationLayout.findViewById(R.id.itemName_TextView);
        locationLayoutBarcode = (TextView) locationLayout.findViewById(R.id.barcode_TextView);
        locationLayoutSkuCode = (TextView) locationLayout.findViewById(R.id.skuCode_TextView);
        locationLayoutPackCode = (TextView) locationLayout.findViewById(R.id.packCode_TextView);
        locationLayoutPackName = (TextView) locationLayout.findViewById(R.id.packName_TextView);
        locationLayoutAllocQty = (TextView) locationLayout.findViewById(R.id.allocQty_TextView);
        locationLayoutQty = (QtyEditText) locationLayout.findViewById(R.id.inputQty_EditView);
        locationLayoutLocationCodeLayout = locationLayout.findViewById(R.id.locationCode_Layout);
        locationLayoutSystemQtyLayout = locationLayout.findViewById(R.id.systemQty_Layout);
        locationLayoutInputQtyLayot = locationLayout.findViewById(R.id.inputQty_Layout);
        locationLayoutItemNameLayot = locationLayout.findViewById(R.id.itemName_Layout);
        locationLayoutBarcodeLayot = locationLayout.findViewById(R.id.barcode_Layout);
        locationLayoutSkuCodeLayot = locationLayout.findViewById(R.id.skuCode_Layout);
        locationLayoutPackNameLayot = locationLayout.findViewById(R.id.packName_Layout);
        locationLayoutSplitButton = locationLayout.findViewById(R.id.split_Button);
        locationLayoutSkipButton = locationLayout.findViewById(R.id.skip_Button);
        locationLayoutHoldButton = locationLayout.findViewById(R.id.hold_Button);
        locationLayoutPackCodeLayout = locationLayout.findViewById(R.id.packCode_Layout);

        collectionLayoutPickTaskIdView = (TextView) collectionLayout.findViewById(R.id.pickTaskId_TextView);
        collectionLayoutContainerIdCodeView = (TextView) collectionLayout.findViewById(R.id.containerId_TextView);
        collectionLayoutCollectionIdView = (TextView) collectionLayout.findViewById(R.id.collection_id);
        collectionLayoutPickTaskOrderView = (TextView) collectionLayout.findViewById(R.id.pickTaskOrder_TextView);
        collectionLayoutCustomerNameView = (TextView) collectionLayout.findViewById(R.id.storeName_TextView);
        collectionLayoutConfirmCollectionIdView = (ScanEditText) collectionLayout.findViewById(R.id.confirm_collection_id);
        collectionLayoutConfirmCollectionIdView.setCode(true);

        splitLayoutContainerIdEditText = (ScanEditText) splitLayout.findViewById(R.id.containerId_EditText);

        mSubmit = (Button) findViewById(R.id.submit_Button);

        taskLayoutAddButton.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        locationLayoutSplitButton.setOnClickListener(this);
        locationLayoutSkipButton.setOnClickListener(this);
        locationLayoutHoldButton.setOnClickListener(this);
    }

    /**
     * 扫拣货签&托盘码
     */
    private void requestPick(String uId, String taskList) {
        new RequestPickTask(that, uId, taskList).start();
    }

    /**
     * 扫拣货位/集货位
     */
    private void requestPickLocation(String locationCode, String qty) {
        new RequestPickLocationTask(that, locationCode, qty).start();
    }

    private void setCurPageView() {
        switch (mCurPage) {
            case 0:
                taskLayout.setVisibility(View.VISIBLE);
                locationLayout.setVisibility(View.GONE);
                collectionLayout.setVisibility(View.GONE);
                splitLayout.setVisibility(View.GONE);
                mSubmit.setVisibility(View.VISIBLE);
                mSubmit.setEnabled(false);
                taskLayoutAddButton.setEnabled(false);

                if (scanEditTextTool != null) {
                    scanEditTextTool.release();
                }
                scanEditTextTool = new ScanEditTextTool(that);
                scanEditTextTool.setComplete(this);

                for (ViewHolder viewHolder : viewHolderList) {
                    viewHolder.taskIdEditText.addTextChangedListener(null);
                }

                viewHolderList.clear();
                taskLayoutBodyLayout.removeAllViews();

                addTaskLayout();
                break;
            case 1:
                taskLayout.setVisibility(View.GONE);
                locationLayout.setVisibility(View.VISIBLE);
                collectionLayout.setVisibility(View.GONE);
                splitLayout.setVisibility(View.GONE);
                locationLayoutSplitButton.setVisibility(View.VISIBLE);
                locationLayoutSkipButton.setVisibility(View.GONE);
                locationLayoutHoldButton.setVisibility(View.GONE);
                mSubmit.setVisibility(View.VISIBLE);
                mSubmit.setEnabled(false);
                locationLayoutConfirmLocationCodeView.requestFocus();

                if (scanEditTextTool != null) {
                    scanEditTextTool.release();
                }
                scanEditTextTool = new ScanEditTextTool(that, locationLayoutConfirmLocationCodeView);
                scanEditTextTool.setComplete(this);

                break;
            case 2:
                taskLayout.setVisibility(View.GONE);
                locationLayout.setVisibility(View.GONE);
                collectionLayout.setVisibility(View.VISIBLE);
                splitLayout.setVisibility(View.GONE);
                mSubmit.setVisibility(View.GONE);
                collectionLayoutConfirmCollectionIdView.requestFocus();

                if (scanEditTextTool != null) {
                    scanEditTextTool.release();
                }
                scanEditTextTool = new ScanEditTextTool(that, collectionLayoutConfirmCollectionIdView);
                scanEditTextTool.setComplete(this);
                break;
            case 3:
                taskLayout.setVisibility(View.GONE);
                locationLayout.setVisibility(View.GONE);
                collectionLayout.setVisibility(View.GONE);
                splitLayout.setVisibility(View.VISIBLE);
                mSubmit.setVisibility(View.VISIBLE);
                mSubmit.setEnabled(false);
                splitLayoutContainerIdEditText.setText("");
                splitLayoutContainerIdEditText.requestFocus();

                if (scanEditTextTool != null) {
                    scanEditTextTool.release();
                }
                scanEditTextTool = new ScanEditTextTool(that, splitLayoutContainerIdEditText);
                scanEditTextTool.setComplete(this);
                break;
        }
    }

    private void addTaskLayout() {
        int no = viewHolderList.size();
        if (no == 1) {
            viewHolderList.get(0).taskNoTextView.setVisibility(View.VISIBLE);
        }

        View view = View.inflate(that, R.layout.pick_task_item, null);

        TextView taskNoTextView = (TextView) view.findViewById(R.id.taskNo_TextView);
        ScanEditText taskIdEditText = (ScanEditText) view.findViewById(R.id.taskId_EditText);
        ScanEditText containerIdEditText = (ScanEditText) view.findViewById(R.id.containerId_EditText);

        taskIdEditText.addTextChangedListener(this);
        taskNoTextView.setText("任务" + (no + 1) + ":");
        if (no == 0) {
            taskNoTextView.setVisibility(View.GONE);
        }

        scanEditTextTool.addEditText(taskIdEditText, containerIdEditText);

        taskLayoutBodyLayout.addView(view);
        taskIdEditText.requestFocus();

        ViewHolder vh = new ViewHolder();
        vh.taskNoTextView = taskNoTextView;
        vh.taskIdEditText = taskIdEditText;
        vh.containerIdEditText = containerIdEditText;
        viewHolderList.add(vh);
    }

    private void fillPick(PickLocation pickLocation) {
        if (pickLocation != null) {
            mPick = pickLocation.getPick();

            if (pickLocation.isDone()) {
                ToastTool.show(that, "拣货成功");
                mCurPage = 0;
                setCurPageView();
            } else {
                serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

                if (pickLocation.isPickDone()) {
                    mCurPage = 2;
                    setCurPageView();
                    fillCollection();
                } else {
                    mCurPage = 1;
                    setCurPageView();
                    fillPickLocation();
                }
            }
        }
    }

    private void resetPick() {
        if (mPick != null) {
            serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());
            mCurPage = 1;
            setCurPageView();
            fillPickLocation();
        }
    }

    private void fillSplit() {
        mCurPage = 3;
        setCurPageView();
    }

    /**
     * 第二页 确认拣货位
     */
    private void fillPickLocation() {
        locationLayoutHeadTextView.setText("扫描确认拣货位");

        locationLayoutConfirmLocationCodeView.requestFocus();
        locationLayoutContainerIdCodeView.setText(mPick.getContainerId());
        locationLayoutPickTaskIdView.setText(mPick.getPickTaskId());
        locationLayoutLocationCodeView.setText(mPick.getAllocPickLocationCode());
        locationLayoutPickTaskOrderView.setText(mPick.getPickTaskOrder());
        locationLayoutCustomerNameView.setText(mPick.getCustomerName());
        locationLayoutItemName.setText(mPick.getItemName());
        locationLayoutBarcode.setText(mPick.getBarcode());
        locationLayoutSkuCode.setText(mPick.getSkuCode());
        locationLayoutPackCode.setText(mPick.getPackCode());
        locationLayoutPackName.setText(mPick.getAllocUnitName());
        locationLayoutConfirmLocationCodeView.getText().clear();
        locationLayoutAllocQty.setText(mPick.getAllocQty());
        locationLayoutQty.getText().clear();

        locationLayoutLocationCodeLayout.setVisibility(View.VISIBLE);
        locationLayoutSystemQtyLayout.setVisibility(View.GONE);
        locationLayoutInputQtyLayot.setVisibility(View.GONE);
        locationLayoutItemNameLayot.setVisibility(View.GONE);
        locationLayoutBarcodeLayot.setVisibility(View.GONE);
        locationLayoutSkuCodeLayot.setVisibility(View.GONE);
        locationLayoutPackNameLayot.setVisibility(View.GONE);
        locationLayoutPackCodeLayout.setVisibility(View.GONE);
    }

    /**
     * 第三页 确认拣货数量
     */
    private void fillPickQty() {
        locationLayoutHeadTextView.setText("确认拣货数量");

        locationLayoutLocationCodeLayout.setVisibility(View.GONE);
        locationLayoutSystemQtyLayout.setVisibility(View.VISIBLE);
        locationLayoutInputQtyLayot.setVisibility(View.VISIBLE);
        locationLayoutItemNameLayot.setVisibility(View.VISIBLE);
        locationLayoutBarcodeLayot.setVisibility(View.VISIBLE);
        locationLayoutSkuCodeLayot.setVisibility(View.VISIBLE);
        locationLayoutPackNameLayot.setVisibility(View.VISIBLE);
        locationLayoutPackCodeLayout.setVisibility(View.VISIBLE);
        locationLayoutSplitButton.setVisibility(View.GONE);
        locationLayoutSkipButton.setVisibility(View.VISIBLE);
        locationLayoutHoldButton.setVisibility(View.VISIBLE);
        locationLayoutQty.requestFocus();
    }

    /**
     * 第三页 集货位
     */
    private void fillCollection() {
        collectionLayoutConfirmCollectionIdView.requestFocus();
        collectionLayoutCollectionIdView.setText(mPick.getAllocCollectLocationCode());
        collectionLayoutPickTaskIdView.setText(mPick.getPickTaskId());
        collectionLayoutContainerIdCodeView.setText(mPick.getContainerId());
        collectionLayoutPickTaskOrderView.setText(mPick.getPickTaskOrder());
        collectionLayoutCustomerNameView.setText(mPick.getCustomerName());
        collectionLayoutConfirmCollectionIdView.setText("");
    }


    @Override
    public void onComplete() {
        switch (mCurPage) {
            case 1:
                String locationCode = locationLayoutConfirmLocationCodeView.getText().toString();
                if (TextUtils.equals(locationCode, mPick.getAllocPickLocationCode())) {
                    mSubmit.setEnabled(true);
                    fillPickQty();
                } else {
                    ToastTool.show(that, "错误的拣货位,请重新扫描");
                }
                break;
            case 2:
                String collectionId = collectionLayoutConfirmCollectionIdView.getText().toString();
                if (TextUtils.equals(mPick.getAllocCollectLocationCode(), collectionId)) {
                    requestPickLocation(collectionId, "");
                } else {
                    ToastTool.show(that, "错误的集货位，请重新扫描");
                }
                break;
            case 3:
                mSubmit.setEnabled(true);
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
    public void onBackPressed() {
        if (mCurPage > 0) {
            if (mCurPage == 3) {
                resetPick();
                return;
            } else if (mCurPage == 1) {
                if ("确认拣货数量".equals(locationLayoutHeadTextView.getText().toString())) {
                    resetPick();
                    return;
                }
            }

            DialogTools.showTwoButtonDialog(that, "是否暂退任务,下次回来将会继续", "取消", "确定", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }, true);
            return;
        }
        finish();
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
            submit();
        } else if (v == taskLayoutAddButton) {
            addTaskLayout();
        } else if (v == locationLayoutSplitButton) {
            fillSplit();
        } else if (v == locationLayoutSkipButton) {
            DialogTools.showTwoButtonDialog(that, "确认跳过当前的拣货项：" + mPick.getItemName(), "取消", "确认", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new RequestSkipTask(that, mPick.getPickTaskId(), mPick.getPickOrder()).start();
                }
            }, true);
        } else if (v == locationLayoutHoldButton) {
            DialogTools.showTwoButtonDialog(that, "确认挂起拣货任务：" + mPick.getPickTaskId(), "取消", "确认", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new RequestHoldTask(that, mPick.getPickTaskId()).start();
                }
            }, true);
        }
    }

    private void submit() {
        if (mCurPage == 0) {
            JSONArray jsonArray = new JSONArray();

            for (ViewHolder viewHolder : viewHolderList) {
                String taskId = viewHolder.taskIdEditText.getText().toString();
                String containerId = viewHolder.containerIdEditText.getText().toString();
                if (!TextUtils.isEmpty(taskId)) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("taskId", taskId);
                        jsonObject.put("containerId", containerId);

                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            requestPick(uId, jsonArray.toString());
        } else if (mCurPage == 1) {
            if (TextUtils.isEmpty(locationLayoutConfirmLocationCodeView.getText().toString())) {
                return;
            }

            final String qty = locationLayoutQty.getValue();
            if (TextUtils.isEmpty(qty)) {
                ToastTool.show(that, "请输入正确的数量");
                return;
            }

            if (FloatUtils.equals(qty, mPick.getAllocQty())) {
                requestPickLocation(locationLayoutConfirmLocationCodeView.getText().toString(), qty);
            } else {
                DialogTools.showTwoButtonDialog(that, "拣货数量不足，是否确认提交", "取消", "确定", null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPickLocation(locationLayoutConfirmLocationCodeView.getText().toString(), qty);
                    }
                }, true);
            }
        } else if (mCurPage == 3) {
            String containerId = splitLayoutContainerIdEditText.getText().toString();
            if (!TextUtils.isEmpty("containerId")) {
                new RequestSplitTask(that, mPick.getPickTaskId(), containerId).start();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mCurPage == 0) {
            boolean flag = false;
            for (ViewHolder viewHolder : viewHolderList) {
                if (!TextUtils.isEmpty(viewHolder.taskIdEditText.getText())) {
                    flag = true;
                    break;
                }
            }

            if (flag) {
                taskLayoutAddButton.setEnabled(true);
                mSubmit.setEnabled(true);
            } else {
                taskLayoutAddButton.setEnabled(false);
                mSubmit.setEnabled(false);
            }
        } else {
            String realQtyString = locationLayoutQty.getValue();
            if (!TextUtils.isEmpty(realQtyString)) {
                float realQty = Float.parseFloat(realQtyString);
                float qty = Float.parseFloat(mPick.getAllocQty());

                if (realQty > qty) {
                    locationLayoutQty.setText(mPick.getAllocQty());
                    locationLayoutQty.setSelection(mPick.getAllocQty().length());
                }
            }
        }
    }

    private static class ViewHolder {

        TextView taskNoTextView;

        ScanEditText taskIdEditText;

        ScanEditText containerIdEditText;
    }

    /**
     * 扫拣货签&托盘码
     */
    private class RequestPickTask extends HttpAsyncTask<PickLocation> {

        private String uId;
        private String taskList;

        public RequestPickTask(Context context, String uId, String taskList) {
            super(context, true, true, false);
            this.uId = uId;
            this.taskList = taskList;
        }

        @Override
        public DataHull<PickLocation> doInBackground() {
            return ScanPickProvider.request(context, uId, uToken, taskList);
        }

        @Override
        public void onPostExecute(PickLocation result) {
            fillPick(result);
        }
    }

    /**
     * 扫拣货位/集货位
     */
    private class RequestPickLocationTask extends HttpAsyncTask<PickLocation> {

        private String locationCode;
        private String qty;

        public RequestPickLocationTask(Context context, String locationCode, String qty) {
            super(context, true, true, false);
            this.locationCode = locationCode;
            this.qty = qty;
        }

        @Override
        public DataHull<PickLocation> doInBackground() {
            return ScanPickLocationProvider.request(context, uId, uToken, locationCode, qty, serialNumber);
        }

        @Override
        public void onPostExecute(PickLocation result) {
            fillPick(result);
        }
    }

    /**
     * 跳过
     */
    private class RequestSkipTask extends HttpAsyncTask<PickLocation> {

        private String taskId;
        private String pickOrder;

        public RequestSkipTask(Context context, String taskId, String pickOrder) {
            super(context, true, true, false);
            this.taskId = taskId;
            this.pickOrder = pickOrder;
        }

        @Override
        public DataHull<PickLocation> doInBackground() {
            return SkipProvider.request(context, uId, uToken, taskId, pickOrder, serialNumber);
        }

        @Override
        public void onPostExecute(PickLocation result) {
            fillPick(result);
        }
    }

    /**
     * 挂起
     */
    private class RequestHoldTask extends HttpAsyncTask<PickLocation> {

        private String taskId;

        public RequestHoldTask(Context context, String taskId) {
            super(context, true, true, false);
            this.taskId = taskId;
        }

        @Override
        public DataHull<PickLocation> doInBackground() {
            return HoldProvider.request(context, uId, uToken, taskId, serialNumber);
        }

        @Override
        public void onPostExecute(PickLocation result) {
            fillPick(result);
        }
    }

    /**
     * 添加托盘
     */
    private class RequestSplitTask extends HttpAsyncTask<Split> {

        private String taskId;

        private String containerId;

        public RequestSplitTask(Context context, String taskId, String containerId) {
            super(context, true, true, false);
            this.taskId = taskId;
            this.containerId = containerId;
        }

        @Override
        public DataHull<Split> doInBackground() {
            return SplitNewPickTaskProvider.request(context, uId, uToken, taskId, containerId, serialNumber);
        }

        @Override
        public void onPostExecute(Split result) {
            if (mPick != null) {
                mPick.setContainerId(containerId);
            }
            resetPick();
        }
    }
}
