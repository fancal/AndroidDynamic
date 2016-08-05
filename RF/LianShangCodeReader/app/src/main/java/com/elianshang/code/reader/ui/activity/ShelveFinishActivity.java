package com.elianshang.code.reader.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.ResponseState;
import com.elianshang.code.reader.bean.Shelve;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.tool.DialogTools;
import com.elianshang.code.reader.tool.ScanEditTextTool;
import com.elianshang.code.reader.tool.ScanManager;
import com.elianshang.code.reader.ui.BaseActivity;
import com.elianshang.code.reader.ui.view.ScanEditText;
import com.xue.http.impl.DataHull;

/**
 * Created by wangwenwang on 16/8/3.
 */
public class ShelveFinishActivity extends BaseActivity implements ScanManager.OnBarCodeListener, ScanEditTextTool.OnSetComplete {

    public static void launch(Activity activity, Shelve shelve) {
        Intent intent = new Intent(activity, ShelveFinishActivity.class);
        intent.putExtra("shelve", shelve);
        activity.startActivityForResult(intent, 1);
    }

    private Shelve shelve;

    private ScanEditTextTool scanEditTextTool;

    private TextView locationTextView;
    private TextView taskIdTextView;
    private ScanEditText locationEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_operation);

        readExtra();
        findViews();
        fillData();
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

    private void readExtra() {
        Intent intent = getIntent();
        shelve = (Shelve) intent.getSerializableExtra("shelve");
    }

    private void findViews() {
        locationTextView = (TextView) findViewById(R.id.location_id);
        taskIdTextView = (TextView) findViewById(R.id.task_id);
        locationEditText = (ScanEditText) findViewById(R.id.confirm_location_id);

        scanEditTextTool = new ScanEditTextTool(this, locationEditText);
        scanEditTextTool.setComplete(this);
    }

    private void fillData() {
        if (shelve != null) {
            taskIdTextView.setText(shelve.getTaskId());
            locationTextView.setText(shelve.getAllocLocationId());
        }
    }

    @Override
    public void OnBarCodeReceived(String s) {
        scanEditTextTool.setScanText(s);
    }

    @Override
    public void onSetComplete() {
        final String location = locationEditText.getText().toString();
        if (location.equals(shelve.getAllocLocationId())) {
            new RequestFinishOpetationTask(this, shelve.getTaskId(), location).start();
        } else {
            DialogTools.showTwoButtonDialog(this, "扫描库位与目标库位不符,确认上架吗?", "取消", "确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    locationEditText.setText("");
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new RequestFinishOpetationTask(ShelveFinishActivity.this, shelve.getTaskId(), location).start();
                }
            }, false);
        }
    }

    @Override
    public void onInputError(int i) {

    }


    private class RequestFinishOpetationTask extends HttpAsyncTask<ResponseState> {

        private String taskId;

        private String locationId;

        public RequestFinishOpetationTask(Context context, String taskId, String locationId) {
            super(context, true, true);
            this.taskId = taskId;
            this.locationId = locationId;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return HttpApi.shelveScanTargetLocation(taskId, locationId);
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
