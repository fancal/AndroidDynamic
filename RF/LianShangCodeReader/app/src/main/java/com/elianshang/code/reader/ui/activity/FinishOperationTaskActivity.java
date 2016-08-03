package com.elianshang.code.reader.ui.activity;

import android.barcode.BarCodeManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.User;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.tool.ScanEditTextTool;
import com.elianshang.code.reader.tool.ScanManager;
import com.elianshang.code.reader.ui.BaseActivity;
import com.elianshang.code.reader.ui.view.ScanEditText;
import com.xue.http.impl.DataHull;

/**
 * Created by wangwenwang on 16/8/3.
 */
public class FinishOperationTaskActivity extends BaseActivity implements BarCodeManager.OnBarCodeReceivedListener, ScanEditTextTool.OnSetComplete {

    public static void launch(Context context) {
        Intent intent = new Intent(context, FinishOperationTaskActivity.class);
        context.startActivity(intent);
    }

    public static void launch(Context context, String taskId, String allocLocationId) {
        Intent intent = new Intent(context, FinishOperationTaskActivity.class);
        intent.putExtra("taskId", taskId);
        intent.putExtra("allocLocationId", allocLocationId);
        context.startActivity(intent);
    }

    private String taskId;
    private String allocLocationId;
    private ScanEditTextTool scanEditTextTool;

    private TextView locationTextView;
    private TextView taskIdTextView;
    private ScanEditText locationEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_operation);

        taskId = getIntent().getStringExtra("taskId");
        allocLocationId = getIntent().getStringExtra("allocLocationId");

        findViews();

        scanEditTextTool = new ScanEditTextTool(this, locationEditText);
        scanEditTextTool.setComplete(this);
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

    private void findViews(){
        locationTextView = (TextView) findViewById(R.id.location_id);
        taskIdTextView = (TextView) findViewById(R.id.task_id);
        locationEditText = (ScanEditText) findViewById(R.id.confirm_location_id);

        taskIdTextView.setText(taskId);
        locationTextView.setText(allocLocationId);
    }

    @Override
    public void OnBarCodeReceived(String s) {
        scanEditTextTool.setScanText(s);
    }

    @Override
    public void onSetComplete() {
        String location = locationEditText.getText().toString();
        if(location.equals(taskId)){
            new RequestFinishOpetationTask(this).start();
        }
    }

    @Override
    public void onInputError(int i) {

    }


    private class RequestFinishOpetationTask extends HttpAsyncTask<User> {

        public RequestFinishOpetationTask(Context context) {
            super(context, true, true);
        }

        @Override
        public DataHull<User> doInBackground() {
            DataHull<User> dataHull = HttpApi.shelveScanTargetLocation("177901840376432", "7");
            return dataHull;
        }

        @Override
        public void onPostExecute(int updateId, User result) {
        }

        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);

        }
    }

}
