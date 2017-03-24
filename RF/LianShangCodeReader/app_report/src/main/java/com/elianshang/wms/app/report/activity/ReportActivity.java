package com.elianshang.wms.app.report.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.tools.DeviceTool;
import com.elianshang.wms.app.report.R;

/**
 * 盘点页面
 */
public class ReportActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, View.OnClickListener {

    private String uId;

    private String uToken;

    private Toolbar mToolbar;

    private String serialNumber;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_report);

        if (readExtra()) {
            serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

            findView();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void findView() {

        initToolbar();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private boolean readExtra() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }


    @Override
    public void OnBarCodeReceived(String s) {

    }

    @Override
    public void onClick(View v) {

    }

//    /**
//     * 拖板列表详情
//     */
//    private class CheckMergeTask extends HttpAsyncTask<MergeBean> {
//
//        private String resultList;
//
//        private CheckMergeTask(Context context, String resultList) {
//            super(context, true, true);
//            this.resultList = resultList;
//        }
//
//        @Override
//        public DataHull<MergeBean> doInBackground() {
//            return MergeProvider.request(context, uId, uToken, resultList, serialNumber);
//        }
//
//        @Override
//        public void onPostExecute(MergeBean result) {
//            fillMerge(result);
//        }
//    }
}