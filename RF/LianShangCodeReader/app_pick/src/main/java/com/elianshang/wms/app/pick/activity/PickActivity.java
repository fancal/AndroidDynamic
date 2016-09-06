package com.elianshang.wms.app.pick.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DialogTools;
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
import com.elianshang.wms.app.pick.provider.ScanPickLocationProvider;
import com.elianshang.wms.app.pick.provider.ScanPickProvider;
import com.xue.http.impl.DataHull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PickActivity extends DLBasePluginActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener, View.OnClickListener {

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
    private View mGroup1;
    /**
     * 移库 第二页容器
     */
    private View mGroup2;
    /**
     * 移库 第三页容器
     */
    private View mGroup3;

    /**
     * 第一页动态父容器
     */
    private LinearLayout mGroup1BodyLayout;

    /**
     * 第一页添加按钮
     */
    private Button mGroup1AddButton;

    /**
     * 第二页 顶部文字
     */
    private TextView mGroup2HeadTextView;

    /**
     * 第二页 库位码
     */
    private TextView mGroup2LocationIdView;
    /**
     * 第二页 确认库位码
     */
    private ScanEditText mGroup2ConfirmLocationIdView;
    /**
     * 第二页 分配商品数量
     */
    private TextView mGroup2AllocQty;
    /**
     * 第二页 实际输入数量
     */
    private QtyEditText mGroup2Qty;

    /**
     * 第二页 确认库位布局
     */
    private View mGroup2LocationIdLayout;

    /**
     * 第二页 系统数量布局
     */
    private View mGroup2SystemQtyLayout;

    /**
     * 第二页 输入数量布局
     */
    private View mGroup2InputQtyLayot;

    /**
     * 第三页 集货码
     */
    private TextView mGroup3CollectionIdView;
    /**
     * 第三页 确认集货码
     */
    private ScanEditText mGroup3ConfirmCollectionIdView;

    /**
     * 当前所在页 (0:第一页   1：第二页  2：第三页)
     */
    private int mCurPage = 0;
    private Button mSubmit;
    private ScanEditTextTool scanEditTextTool;
    private Pick mPick;

    private ArrayList<ViewHolder> viewHolderList = new ArrayList();

    private String serialNumber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);
        findViews();
        readExtra();
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
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mGroup1 = findViewById(R.id.pick_task);
        mGroup2 = findViewById(R.id.pick_location);
        mGroup3 = findViewById(R.id.pick_collection);
        mGroup1BodyLayout = (LinearLayout) mGroup1.findViewById(R.id.body_Layout);
        mGroup1AddButton = (Button) mGroup1.findViewById(R.id.add_Button);

        mGroup2HeadTextView = (TextView) mGroup2.findViewById(R.id.head_TextView);
        mGroup2LocationIdView = (TextView) mGroup2.findViewById(R.id.location_id);
        mGroup2ConfirmLocationIdView = (ScanEditText) mGroup2.findViewById(R.id.confirm_location_id);
        mGroup2AllocQty = (TextView) mGroup2.findViewById(R.id.allocQty_TextView);
        mGroup2Qty = (QtyEditText) mGroup2.findViewById(R.id.inputQty_EditView);
        mGroup2LocationIdLayout = mGroup2.findViewById(R.id.locationId_Layout);
        mGroup2SystemQtyLayout = mGroup2.findViewById(R.id.systemQty_Layout);
        mGroup2InputQtyLayot = mGroup2.findViewById(R.id.inputQty_Layout);

        mGroup3CollectionIdView = (TextView) mGroup3.findViewById(R.id.collection_id);
        mGroup3ConfirmCollectionIdView = (ScanEditText) mGroup3.findViewById(R.id.confirm_collection_id);

        mSubmit = (Button) findViewById(R.id.submit_Button);

        mGroup1AddButton.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
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
    private void requestPickLocation(String locationId, String qty) {
        new RequestPickLocationTask(that, locationId, qty).start();
    }

    private void setCurPageView() {
        switch (mCurPage) {
            case 0:
                mGroup1.setVisibility(View.VISIBLE);
                mGroup2.setVisibility(View.GONE);
                mGroup3.setVisibility(View.GONE);
                mSubmit.setVisibility(View.VISIBLE);
                mSubmit.setEnabled(false);
                mGroup1AddButton.setEnabled(false);

                if (scanEditTextTool != null) {
                    scanEditTextTool.release();
                }
                scanEditTextTool = new ScanEditTextTool(that);
                scanEditTextTool.setComplete(this);
                viewHolderList.clear();
                mGroup1BodyLayout.removeAllViews();

                addTaskLayout();
                break;
            case 1:
                mGroup1.setVisibility(View.GONE);
                mGroup2.setVisibility(View.VISIBLE);
                mGroup3.setVisibility(View.GONE);
                mSubmit.setVisibility(View.VISIBLE);

                if (scanEditTextTool != null) {
                    scanEditTextTool.release();
                }
                scanEditTextTool = new ScanEditTextTool(that, mGroup2ConfirmLocationIdView);
                scanEditTextTool.setComplete(this);

                break;
            case 2:
                mGroup1.setVisibility(View.GONE);
                mGroup2.setVisibility(View.GONE);
                mGroup3.setVisibility(View.VISIBLE);
                mSubmit.setVisibility(View.GONE);

                if (scanEditTextTool != null) {
                    scanEditTextTool.release();
                }
                scanEditTextTool = new ScanEditTextTool(that, mGroup3ConfirmCollectionIdView);
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

        taskNoTextView.setText("任务" + (no + 1) + ":");
        if (no == 0) {
            taskNoTextView.setVisibility(View.GONE);
        }

        scanEditTextTool.addEditText(taskIdEditText, containerIdEditText);

        mGroup1BodyLayout.addView(view);
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
                finish();
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

    /**
     * 第二页 确认拣货位
     */
    private void fillPickLocation() {
        mGroup2HeadTextView.setText("扫描确认拣货位");

        mGroup2ConfirmLocationIdView.requestFocus();
        mGroup2LocationIdView.setText(mPick.getAllocPickLocationCode());
        mGroup2ConfirmLocationIdView.getText().clear();
        mGroup2AllocQty.setText(mPick.getAllocQty());
        mGroup2Qty.getText().clear();
        mGroup2Qty.setHint(mPick.getAllocQty());

        mGroup2LocationIdLayout.setVisibility(View.VISIBLE);
        mGroup2SystemQtyLayout.setVisibility(View.GONE);
        mGroup2InputQtyLayot.setVisibility(View.GONE);
    }

    /**
     * 第三页 确认拣货数量
     */
    private void fillPickQty() {
        mGroup2HeadTextView.setText("确认拣货数量");

        mGroup2LocationIdLayout.setVisibility(View.GONE);
        mGroup2SystemQtyLayout.setVisibility(View.VISIBLE);
        mGroup2InputQtyLayot.setVisibility(View.VISIBLE);
        mGroup2Qty.requestFocus();
    }

    /**
     * 第三页 集货位
     */
    private void fillCollection() {
        mGroup3CollectionIdView.setText(mPick.getAllocCollectLocationCode());
    }


    @Override
    public void onComplete() {
        switch (mCurPage) {
            case 0:
                mSubmit.setEnabled(true);
                mGroup1AddButton.setEnabled(true);
                break;
            case 1:
                String locationId = mGroup2ConfirmLocationIdView.getText().toString();
                if (TextUtils.equals(locationId, mPick.getAllocPickLocationId())) {
                    fillPickQty();
                } else {
                    ToastTool.show(that, "错误的拣货位,请重新扫描");
                }
                break;
            case 2:
                String collectionId = mGroup3ConfirmCollectionIdView.getText().toString();
                if (TextUtils.equals(mPick.getAllocCollectLocationId(), collectionId)) {
                    requestPickLocation(collectionId, "");
                } else {
                    ToastTool.show(that, "错误的集货位，请重新扫描");
                }
                break;

        }
    }

    @Override
    public void onError(ContentEditText editText) {
        switch (mCurPage) {
            case 0:
                mSubmit.setEnabled(true);
                mGroup1AddButton.setEnabled(true);
                break;

        }
    }

    @Override
    public void OnBarCodeReceived(String s) {
        scanEditTextTool.setScanText(s);

    }

    @Override
    public void onBackPressed() {
        if (mCurPage > 0) {
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
        if (v == mSubmit) {
            submit();
        } else if (v == mGroup1AddButton) {
            addTaskLayout();
        }
    }

    private void submit() {
        if (mCurPage == 0) {
            JSONArray jsonArray = new JSONArray();

            for (ViewHolder viewHolder : viewHolderList) {
                String taskId = viewHolder.taskIdEditText.getText().toString();
                String containerId = viewHolder.containerIdEditText.getText().toString();
                if (!TextUtils.isEmpty(taskId) && !TextUtils.isEmpty(containerId)) {
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
            if (TextUtils.isEmpty(mGroup2ConfirmLocationIdView.getText().toString())) {
                return;
            }

            String qty = mGroup2Qty.getValue();

            if (TextUtils.isEmpty(qty)) {
                return;
            }
            requestPickLocation(mGroup2ConfirmLocationIdView.getText().toString(), qty);
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

        private String locationId;
        private String qty;

        public RequestPickLocationTask(Context context, String locationId, String qty) {
            super(context, true, true, false);
            this.locationId = locationId;
            this.qty = qty;
        }

        @Override
        public DataHull<PickLocation> doInBackground() {
            return ScanPickLocationProvider.request(context, uId, uToken, locationId, qty, serialNumber);
        }

        @Override
        public void onPostExecute(PickLocation result) {
            fillPick(result);
        }
    }
}
