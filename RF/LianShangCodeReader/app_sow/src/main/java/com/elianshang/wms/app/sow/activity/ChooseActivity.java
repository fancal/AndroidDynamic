package com.elianshang.wms.app.sow.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.wms.app.sow.R;
import com.elianshang.wms.app.sow.bean.Restore;
import com.elianshang.wms.app.sow.provider.RestoreProvider;
import com.xue.http.impl.DataHull;

public class ChooseActivity extends DLBasePluginActivity implements View.OnClickListener {

    private String uId;

    private String uToken;

    private Button orderSowButton;

    private Button containerSowButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (readExtras()) {
            new FetchProcurementTask(uId, uToken).start();
        }
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

        // FIXME: 16/12/5
//        uId = "1";
//        uToken = "274237956828916";
//        ScanManager.init(that);

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    private void showChooseView() {

        setContentView(R.layout.sow_activity_choose);

        orderSowButton = (Button) findViewById(R.id.orderSow_Button);
        containerSowButton = (Button) findViewById(R.id.containerSow_Button);

        orderSowButton.setOnClickListener(this);
        containerSowButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (orderSowButton == v) {
            SowActivity.launch(ChooseActivity.this, uId, uToken, null, SowActivity.Type.ORDER);
            finish();
        } else if (containerSowButton == v) {
            SowActivity.launch(ChooseActivity.this, uId, uToken, null, SowActivity.Type.CONTAINER);
            finish();
        }
    }

    private class FetchProcurementTask extends HttpAsyncTask<Restore> {

        private String uId;

        private String uToken;

        public FetchProcurementTask(String uId, String uToken) {
            super(ChooseActivity.this.that, true, true, false, false);
            this.uId = uId;
            this.uToken = uToken;
        }

        @Override
        public DataHull<Restore> doInBackground() {
            return RestoreProvider.request(context, uId, uToken);
        }

        @Override
        public void onPostExecute(Restore result) {
            if (result.isDone() || result.getSow() == null) {
                showChooseView();
            } else {
                SowActivity.launch(ChooseActivity.this, uId, uToken, result.getSow(), null);
                ChooseActivity.this.finish();
            }
        }


        @Override
        public void netErr(String errMsg) {
            super.netErr(errMsg);
            ChooseActivity.this.finish();
        }

        @Override
        public void netNull() {
            super.netNull();
            ChooseActivity.this.finish();
        }

        @Override
        public void dataNull(String errMsg) {
            super.dataNull(errMsg);
            ChooseActivity.this.finish();
        }
    }
}
