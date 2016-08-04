package com.elianshang.code.reader.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.R;
import com.elianshang.code.reader.asyn.HttpAsyncTask;
import com.elianshang.code.reader.bean.Shelve;
import com.elianshang.code.reader.http.HttpApi;
import com.elianshang.code.reader.tool.ScanEditTextTool;
import com.elianshang.code.reader.tool.ScanManager;
import com.elianshang.code.reader.ui.BaseActivity;
import com.elianshang.code.reader.ui.view.ScanEditText;
import com.xue.http.impl.DataHull;

/**
 * Created by wangwenwang on 16/8/3.
 */
public class ShelveOpenActivity extends BaseActivity implements ScanManager.OnBarCodeListener, ScanEditTextTool.OnSetComplete {

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, ShelveOpenActivity.class);
        activity.startActivityForResult(intent, 1);
    }


    private ScanEditText containerEditText;
    private ScanEditTextTool scanEditTextTool;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_task);

        containerEditText = (ScanEditText) findViewById(R.id.container_id);

        scanEditTextTool = new ScanEditTextTool(this, containerEditText);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    public void OnBarCodeReceived(String s) {
        scanEditTextTool.setScanText(s);
    }

    @Override
    public void onSetComplete() {
        String containerId = containerEditText.getText().toString();

        new RequestShelveScanTask(this, containerId).start();
    }

    @Override
    public void onInputError(int i) {

    }

    private class RequestShelveScanTask extends HttpAsyncTask<Shelve> {

        private String containerId;

        public RequestShelveScanTask(Context context, String containerId) {
            super(context, true, true);
            this.containerId = containerId;
        }

        @Override
        public DataHull<Shelve> doInBackground() {
            HttpApi.shelveCreateTask(containerId);
            return HttpApi.shelveScanContainer(BaseApplication.get().getUserId(), containerId);
        }

        @Override
        public void onPostExecute(int updateId, Shelve result) {
            ShelveFinishActivity.launch(ShelveOpenActivity.this, result);
        }

        @Override
        public void netErr(int updateId, String errMsg) {
            super.netErr(updateId, errMsg);
        }
    }
}
