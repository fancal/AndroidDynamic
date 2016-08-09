package com.elianshang.code.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.Pick;
import com.elianshang.code.reader.bean.PickLocation;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.tool.DialogTools;
import com.elianshang.code.reader.tool.ScanEditTextTool;
import com.elianshang.code.reader.tool.ScanManager;
import com.elianshang.code.reader.ui.BaseActivity;
import com.elianshang.code.reader.ui.view.ScanEditText;
import com.elianshang.tools.ToastTool;
import com.xue.http.impl.DataHull;

/**
 * Created by liuhanzhi on 16/8/3. 移库
 */
public class PickActivity extends BaseActivity implements ScanEditTextTool.OnSetComplete, ScanManager.OnBarCodeListener, View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, PickActivity.class);
        context.startActivity(intent);
    }

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
    private EditText mGroup2Qty;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);
        findViews();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ScanManager.get().addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScanManager.get().removeListener(this);
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

        mGroup2LocationIdView = (TextView) mGroup2.findViewById(R.id.location_id);
        mGroup2ConfirmLocationIdView = (ScanEditText) mGroup2.findViewById(R.id.confirm_location_id);
        mGroup2AllocQty = (TextView) mGroup2.findViewById(R.id.allocQty_TextView);
        mGroup2Qty = (EditText) mGroup2.findViewById(R.id.inputQty_EditView);

        mGroup3CollectionIdView = (TextView) mGroup3.findViewById(R.id.collection_id);
        mGroup3ConfirmCollectionIdView = (ScanEditText) mGroup3.findViewById(R.id.confirm_collection_id);

        mSubmit = (Button) findViewById(R.id.submit_Button);


//        mSubmit.setEnabled(false);
//        mSubmit.setClickable(false);
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

                scanEditTextTool = new ScanEditTextTool(this, mGroup1TaskIdView, mGroup1ContainerIdView);
                scanEditTextTool.setComplete(this);
                break;
            case 1:
                mGroup1.setVisibility(View.GONE);
                mGroup2.setVisibility(View.VISIBLE);
                mGroup3.setVisibility(View.GONE);
                mSubmit.setVisibility(View.VISIBLE);

                scanEditTextTool = new ScanEditTextTool(this, mGroup2ConfirmLocationIdView);
                scanEditTextTool.setComplete(this);

                break;
            case 2:
                mGroup1.setVisibility(View.GONE);
                mGroup2.setVisibility(View.GONE);
                mGroup3.setVisibility(View.VISIBLE);
                mSubmit.setVisibility(View.VISIBLE);

                scanEditTextTool = new ScanEditTextTool(this, mGroup3ConfirmCollectionIdView);
                scanEditTextTool.setComplete(this);
                break;
        }
    }

    private void fillPick() {
        switch (mCurPage) {
            case 1:
                mGroup2LocationIdView.setText(mPick.getAllocPickLocation());
                mGroup2ConfirmLocationIdView.getText().clear();
                mGroup2AllocQty.setText(mPick.getAllocQty());
                mGroup2Qty.getText().clear();
                break;
            case 2:
                mGroup3CollectionIdView.setText(mPick.getAllocCollectLocation());
                break;
        }

    }


    @Override
    public void onSetComplete() {
        switch (mCurPage) {
            case 0:
                requestPick(mGroup1TaskIdView.getText().toString(), mGroup1ContainerIdView.getText().toString());
                break;
        }
    }

    @Override
    public void onInputError(int i) {
    }

    @Override
    public void OnBarCodeReceived(String s) {
        scanEditTextTool.setScanText(s);

    }

    @Override
    public void onBackPressed() {
        if (mCurPage > 0) {
            DialogTools.showOneButtonDialog(this, "请完成任务,不要退出", "知道了", null, true);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == mSubmit) {
            submit();
        }
    }

    private void submit() {
        switch (mCurPage) {
            case 1:
                if (TextUtils.isEmpty(mGroup1TaskIdView.getText().toString())) {
                    return;
                }
                if (TextUtils.isEmpty(mGroup2ConfirmLocationIdView.getText().toString())) {
                    return;
                }
                if (TextUtils.isEmpty(mGroup2Qty.getText().toString())) {
                    return;
                }
                requestPickLocation(mGroup1TaskIdView.getText().toString(), mGroup2ConfirmLocationIdView.getText().toString(), mGroup2Qty.getText().toString());
                break;
            case 2:
                if (TextUtils.isEmpty(mGroup1TaskIdView.getText().toString())) {
                    return;
                }
                if (TextUtils.isEmpty(mGroup3ConfirmCollectionIdView.getText().toString())) {
                    return;
                }
                requestPickLocation(mGroup1TaskIdView.getText().toString(), mGroup3ConfirmCollectionIdView.getText().toString(), "");
                break;
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
            return HttpApi.pickScanPickTask(taskId, containerId, BaseApplication.get().getUserId());
        }

        @Override
        public void onPostExecute(int updateId, Pick result) {
            mPick = result;
            if (mCurPage != 1) {
                mCurPage = 1;
                setCurPageView();
            }
            fillPick();
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
            return HttpApi.pickScanPickLocation(taskId, locationId, BaseApplication.get().getUserId(), qty);
        }

        @Override
        public void onPostExecute(int updateId, PickLocation result) {
            mPick = result.getPick();
            if (result.isDone()) {
                if (mCurPage != 2) {
                    mCurPage = 2;
                    setCurPageView();
                    fillPick();
                } else {
                    ToastTool.show(context, "拣货成功");
                    finish();
                }
            } else {
                fillPick();
            }
        }
    }


}
