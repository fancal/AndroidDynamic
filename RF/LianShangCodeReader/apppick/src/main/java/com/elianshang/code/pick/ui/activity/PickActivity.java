package com.elianshang.code.pick.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.http.HttpApi;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.QtyEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.code.pick.R;
import com.elianshang.code.pick.bean.Pick;
import com.elianshang.code.pick.bean.PickLocation;
import com.elianshang.code.pick.parser.PickLocationParser;
import com.elianshang.code.pick.parser.PickParser;
import com.elianshang.tools.ToastTool;
import com.ryg.dynamicload.DLBasePluginActivity;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;

/**
 * Created by liuhanzhi on 16/8/3. 移库
 */
public class PickActivity extends DLBasePluginActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener, View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, PickActivity.class);
        context.startActivity(intent);
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
     * 第一页 托盘码
     */
    private ScanEditText mGroup1ContainerIdView;
    /**
     * 第一页 拣货签
     */
    private ScanEditText mGroup1TaskIdView;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);
        findViews();

        readExtra();
    }

    private void readExtra() {
        Intent intent = getIntent();
        uId = intent.getStringExtra("uId");
        uToken = intent.getStringExtra("uToken");

        uId = "123123132";
        uToken = "aewrq23r243423eq3e";

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
        }
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
        mGroup1TaskIdView = (ScanEditText) mGroup1.findViewById(R.id.taskId_EditText);
        mGroup1ContainerIdView = (ScanEditText) mGroup1.findViewById(R.id.containerId_EditText);

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

        mSubmit.setOnClickListener(this);

        setCurPageView();
    }

    /**
     * 扫拣货签&托盘码
     */
    private void requestPick(String taskId, String containerId) {
        new RequestPickTask(this, taskId, containerId).start();
    }

    /**
     * 扫拣货位/集货位
     */
    private void requestPickLocation(String taskId, String locationId, String qty) {
        new RequestPickLocationTask(this, taskId, locationId, qty).start();
    }

    private void setCurPageView() {
        switch (mCurPage) {
            case 0:
                mGroup1.setVisibility(View.VISIBLE);
                mGroup2.setVisibility(View.GONE);
                mGroup3.setVisibility(View.GONE);
                mSubmit.setVisibility(View.GONE);

                scanEditTextTool = new ScanEditTextTool(that, mGroup1TaskIdView, mGroup1ContainerIdView);
                scanEditTextTool.setComplete(this);
                break;
            case 1:
                mGroup1.setVisibility(View.GONE);
                mGroup2.setVisibility(View.VISIBLE);
                mGroup3.setVisibility(View.GONE);
                mSubmit.setVisibility(View.VISIBLE);

                scanEditTextTool = new ScanEditTextTool(that, mGroup2ConfirmLocationIdView);
                scanEditTextTool.setComplete(this);

                break;
            case 2:
                mGroup1.setVisibility(View.GONE);
                mGroup2.setVisibility(View.GONE);
                mGroup3.setVisibility(View.VISIBLE);
                mSubmit.setVisibility(View.GONE);

                scanEditTextTool = new ScanEditTextTool(that, mGroup3ConfirmCollectionIdView);
                scanEditTextTool.setComplete(this);
                break;
        }
    }

    /**
     * 第二页 确认拣货位
     */
    private void fillPickLocation() {
        mGroup2HeadTextView.setText("扫描确认拣货位");

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
                requestPick(mGroup1TaskIdView.getText().toString(), mGroup1ContainerIdView.getText().toString());
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
                if (TextUtils.equals(mPick.getAllocCollectLocationId(), mGroup3ConfirmCollectionIdView.getText().toString())) {
                    requestPickLocation(mGroup1TaskIdView.getText().toString(), mGroup3ConfirmCollectionIdView.getText().toString(), "");
                } else {
                    ToastTool.show(that, "错误的集货位，请重新扫描");
                }
                break;

        }
    }

    @Override
    public void onError(ContentEditText editText) {
    }

    @Override
    public void OnBarCodeReceived(String s) {
        scanEditTextTool.setScanText(s);

    }

    @Override
    public void onBackPressed() {
        if (mCurPage > 0) {
            DialogTools.showOneButtonDialog(that, "请完成任务,不要退出", "知道了", null, true);
            return;
        }
        super.onBackPressed();
    }


    @Override
    public void onClick(View v) {
        if (v == mSubmit) {
            submit();
        }
    }

    private void submit() {
        if (mCurPage == 1) {
            if (TextUtils.isEmpty(mGroup1TaskIdView.getText().toString())) {
                return;
            }
            if (TextUtils.isEmpty(mGroup2ConfirmLocationIdView.getText().toString())) {
                return;
            }

            String qty = mGroup2Qty.getValue();

            if (TextUtils.isEmpty(qty)) {
                return;
            }
            requestPickLocation(mGroup1TaskIdView.getText().toString(), mGroup2ConfirmLocationIdView.getText().toString(), qty);
        }
    }

    /**
     * 扫拣货签&托盘码
     */
    private class RequestPickTask extends HttpAsyncTask<Pick> {

        private String taskId;
        private String containerId;

        public RequestPickTask(Context context, String taskId, String containerId) {
            super(context, true, true, false);
            this.taskId = taskId;
            this.containerId = containerId;
        }

        @Override
        public DataHull<Pick> doInBackground() {
            return HttpApi.doPost("outbound/pick/scanPickTask", new PickParser(), new DefaultKVPBean("taskId", taskId), new DefaultKVPBean("containerId", containerId), new DefaultKVPBean("operator", uId));
        }

        @Override
        public void onPostExecute(int updateId, Pick result) {
            mPick = result;
            if (mCurPage != 1) {
                mCurPage = 1;
                setCurPageView();
            }
            fillPickLocation();
        }
    }

    /**
     * 扫拣货位/集货位
     */
    private class RequestPickLocationTask extends HttpAsyncTask<PickLocation> {

        private String taskId;
        private String locationId;
        private String qty;

        public RequestPickLocationTask(Context context, String taskId, String locationId, String qty) {
            super(context, true, true, false);
            this.taskId = taskId;
            this.locationId = locationId;
            this.qty = qty;
        }

        @Override
        public DataHull<PickLocation> doInBackground() {
            return HttpApi.doPost("outbound/pick/scanPickLocation", new PickLocationParser(), new DefaultKVPBean("taskId", taskId), new DefaultKVPBean("locationId", locationId), new DefaultKVPBean("operator", uId), new DefaultKVPBean("qty", qty));
        }

        @Override
        public void onPostExecute(int updateId, PickLocation result) {
            mPick = result.getPick();
            if (result.isDone()) {
                if (mCurPage != 2) {
                    mCurPage = 2;
                    setCurPageView();
                    fillPickLocation();
                } else {
                    ToastTool.show(context, "拣货成功");
                    finish();
                }
            } else {
                fillCollection();
            }
        }
    }
}
