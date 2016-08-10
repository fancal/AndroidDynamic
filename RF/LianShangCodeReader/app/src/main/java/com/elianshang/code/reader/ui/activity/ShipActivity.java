package com.elianshang.code.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.ResponseState;
import com.elianshang.code.reader.bean.ShipScan;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.tool.ScanEditTextTool;
import com.elianshang.code.reader.tool.ScanManager;
import com.elianshang.code.reader.ui.BaseActivity;
import com.elianshang.code.reader.ui.view.ContentEditText;
import com.elianshang.code.reader.ui.view.ScanEditText;
import com.xue.http.impl.DataHull;

/**
 * Created by liuhanzhi on 16/8/8. 发车
 */
public class ShipActivity extends BaseActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener, View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, ShipActivity.class);
        context.startActivity(intent);
    }

    private Toolbar mToolbar;
    /**
     * 托盘码container
     */
    private View mContainerView;
    /**
     * 托盘码Id
     */
    private ScanEditText mContainerIdView;
    /**
     * 完成container
     */
    private View mFinishView;
    /**
     * 完成 textview
     */
    private TextView mFinishTextView;
    /**
     * 完成 button
     */
    private Button mFinishButton;

    private ScanEditTextTool scanEditTextTool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship);
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
        mContainerView = findViewById(R.id.container);
        mContainerIdView = (ScanEditText) findViewById(R.id.containerId_EditText);
        mFinishView = findViewById(R.id.finish);
        mFinishTextView = (TextView) findViewById(R.id.dock_name);
        mFinishButton = (Button) findViewById(R.id.again_button);

        scanEditTextTool = new ScanEditTextTool(this, mContainerIdView);
        scanEditTextTool.setComplete(this);

        mFinishButton.setOnClickListener(this);

    }

    private void onFinish(ShipScan shipScan) {
        mContainerView.setVisibility(View.GONE);
        mFinishView.setVisibility(View.VISIBLE);
        mFinishTextView.setText(shipScan.getDockName());
    }

    /**
     * 创建任务
     *
     * @param containerId
     */
    private void requestShipCreate(String containerId) {
        new ShipCreateTask(this, containerId).start();
    }

    /**
     * 扫描托盘码发货
     *
     * @param containerId
     */
    private void requestShipScan(String containerId) {
        new ShipScanTask(this, containerId).start();
    }

    @Override
    public void onComplete() {
        requestShipCreate(mContainerIdView.getText().toString());
    }

    @Override
    public void onError(ContentEditText editText) {
    }

    @Override
    public void OnBarCodeReceived(String s) {
        scanEditTextTool.setScanText(s);

    }

    @Override
    public void onClick(View v) {
        if (v == mFinishButton) {
            mContainerView.setVisibility(View.VISIBLE);
            mFinishView.setVisibility(View.GONE);

            mFinishTextView.setText("");
            mContainerIdView.getText().clear();
        }
    }


    /**
     * ship-创建任务(临时，测试用)
     */
    private class ShipCreateTask extends HttpAsyncTask<ResponseState> {

        String containerId;

        public ShipCreateTask(Context context, String containerId) {
            super(context, true, true, false);
            this.containerId = containerId;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return HttpApi.shipCreateTask(containerId);
        }

        @Override
        public void onPostExecute(int updateId, ResponseState result) {
            requestShipScan(containerId);
        }

//        @Override
//        public void dataNull(int updateId, String errMsg) {
//            super.dataNull(updateId, errMsg);
//            requestShipScan(containerId);
//        }
//
//        @Override
//        public void netErr(int updateId, String errMsg) {
//            super.netErr(updateId, errMsg);
//            requestShipScan(containerId);
//        }

    }

    /**
     * ship-扫描托盘码发货
     */
    private class ShipScanTask extends HttpAsyncTask<ShipScan> {

        String containerId;

        public ShipScanTask(Context context, String containerId) {
            super(context, true, true, false);
            this.containerId = containerId;
        }

        @Override
        public DataHull<ShipScan> doInBackground() {
            return HttpApi.shipScanContainer(containerId);
        }

        @Override
        public void onPostExecute(int updateId, ShipScan result) {
            onFinish(result);

        }

    }

}
