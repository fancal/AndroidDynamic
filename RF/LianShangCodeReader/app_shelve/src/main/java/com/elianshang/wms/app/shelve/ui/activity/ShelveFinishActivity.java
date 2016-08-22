package com.elianshang.wms.app.shelve.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.wms.app.shelve.R;
import com.elianshang.wms.app.shelve.bean.ResponseState;
import com.elianshang.wms.app.shelve.bean.Shelve;
import com.elianshang.wms.app.shelve.provider.ScanTargetLocationProvider;
import com.xue.http.impl.DataHull;

/**
 * 上架完成页
 */
public class ShelveFinishActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, ScanEditTextTool.OnStateChangeListener {

    public static void launch(DLBasePluginActivity activity, Shelve shelve) {
        DLIntent intent = new DLIntent(activity.getPackageName(), ShelveFinishActivity.class);
        intent.putExtra("shelve", shelve);
        activity.startPluginActivityForResult(intent, 1);
    }

    /**
     * 上架任务信息
     */
    private Shelve shelve;

    /**
     * EditText工具
     */
    private ScanEditTextTool scanEditTextTool;

    /**
     * 任务TextView
     */
    private TextView taskIdTextView;

    /**
     * 库位TextView
     */
    private TextView locationCodeTextView;

    /**
     * 库位扫描输入框
     */
    private ScanEditText locationIdEditText;

    /**
     * 工具栏
     */
    private Toolbar mToolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelvefinish);

        readExtra();
        findViews();
        fillData();
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

    private void readExtra() {
        Intent intent = getIntent();
        shelve = (Shelve) intent.getSerializableExtra("shelve");
    }

    private void findViews() {
        taskIdTextView = (TextView) findViewById(R.id.taskId_TextView);
        locationCodeTextView = (TextView) findViewById(R.id.locationCode_TextView);
        locationIdEditText = (ScanEditText) findViewById(R.id.locationId_EditText);

        scanEditTextTool = new ScanEditTextTool(that, locationIdEditText);
        scanEditTextTool.setComplete(this);

        initToolbar();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressBack();
            }
        });
    }

    private void pressBack() {
        DialogTools.showOneButtonDialog(that, "请完成任务,不要退出", "知道了", null, true);
    }

    @Override
    public void onBackPressed() {
        pressBack();
    }

    private void fillData() {
        if (shelve != null) {
            taskIdTextView.setText(shelve.getTaskId());
            locationCodeTextView.setText(shelve.getAllocLocationId());
        }
    }

    @Override
    public void OnBarCodeReceived(String s) {
        scanEditTextTool.setScanText(s);
    }

    @Override
    public void onComplete() {
        final String location = locationIdEditText.getText().toString();
        if (location.equals(shelve.getAllocLocationId())) {
            new ShelveScanTargetLocationTask(that, shelve.getTaskId(), location).start();
        } else {
            DialogTools.showTwoButtonDialog(that, "扫描库位与目标库位不符,确认上架吗?", "取消", "确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    locationIdEditText.setText("");
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new ShelveScanTargetLocationTask(ShelveFinishActivity.this.that, shelve.getTaskId(), location).start();
                }
            }, false);
        }
    }

    @Override
    public void onError(ContentEditText editText) {

    }


    private class ShelveScanTargetLocationTask extends HttpAsyncTask<ResponseState> {

        private String taskId;

        private String locationId;

        public ShelveScanTargetLocationTask(Context context, String taskId, String locationId) {
            super(context, true, true);
            this.taskId = taskId;
            this.locationId = locationId;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return ScanTargetLocationProvider.request(taskId, locationId);
        }

        @Override
        public void onPostExecute(int updateId, ResponseState result) {
            setResult(RESULT_OK);
            finish();
        }

        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);
        }
    }

}
