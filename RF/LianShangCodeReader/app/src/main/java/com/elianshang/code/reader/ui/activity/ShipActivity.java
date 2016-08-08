package com.elianshang.code.reader.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.elianshang.code.reader.ui.view.ScanEditText;
import com.xue.http.impl.DataHull;

/**
 * Created by liuhanzhi on 16/8/8. 发车
 */
public class ShipActivity extends BaseActivity implements ScanEditTextTool.OnSetComplete, ScanManager.OnBarCodeListener, View.OnClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, ShipActivity.class);
        context.startActivity(intent);
    }

    private View mContainerView;
    private ScanEditText mContainerIdView;
    private View mFinishView;
    private TextView mFinishTextView;
    private Button mAgainButton;

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
        mContainerView = findViewById(R.id.container);
        mContainerIdView = (ScanEditText) findViewById(R.id.containerId_EditText);
        mFinishView = findViewById(R.id.finish);
        mFinishTextView = (TextView) findViewById(R.id.dock_name);
        mAgainButton = (Button) findViewById(R.id.again_button);

        scanEditTextTool = new ScanEditTextTool(this, mContainerIdView);
        scanEditTextTool.setComplete(this);

        mAgainButton.setOnClickListener(this);

    }

    private void onFinish(ShipScan shipScan) {
        mContainerView.setVisibility(View.GONE);
        mFinishView.setVisibility(View.VISIBLE);
        mFinishTextView.setText(shipScan.getDockName());
        mAgainButton.setVisibility(View.VISIBLE);
    }

    private void requestShipCreate(String containerId) {
        new ShipCreateTask(this, containerId).start();
    }

    private void requestShipScan(String containerId) {
        new ShipScanTask(this, containerId).start();
    }

    @Override
    public void onSetComplete() {
        requestShipCreate(mContainerIdView.getText().toString());
    }

    @Override
    public void onInputError(int i) {
    }

    @Override
    public void OnBarCodeReceived(String s) {
        scanEditTextTool.setScanText(s);

    }

    @Override
    public void onClick(View v) {
        if (v == mAgainButton) {
            mContainerView.setVisibility(View.VISIBLE);
            mFinishView.setVisibility(View.GONE);
            mFinishTextView.setText("");
            mContainerIdView.getText().clear();
            mAgainButton.setVisibility(View.GONE);
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
