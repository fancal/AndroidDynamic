package com.elianshang.wms.app.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.wms.app.view.R;
import com.elianshang.wms.app.view.bean.DataBean;
import com.elianshang.wms.app.view.provider.SearchProvider;
import com.xue.http.impl.DataHull;

public class SearchActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, ScanEditTextTool.OnStateChangeListener, View.OnClickListener {

    private String uId;

    private String uToken;

    /**
     * 工具栏
     */
    private Toolbar mToolbar;

    private View searchScanLayout;

    private ScanEditText scanEditText;

    private View searchContentLayout;

    private TextView contentTextView;

    private Button submitButton;

    /**
     * EditText工具
     */
    private ScanEditTextTool scanEditTextTool;

    private DataBean dataBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (readExtras()) {
            findView();
            initToolbar();
            fillScan();
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
        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
    }

    private void findView() {
        searchScanLayout = findViewById(R.id.search_scan);
        searchContentLayout = findViewById(R.id.search_content);

        scanEditText = (ScanEditText) searchScanLayout.findViewById(R.id.code_EditText);
        scanEditText.setCode(true);
        contentTextView = (TextView) searchContentLayout.findViewById(R.id.content_TextView);
        submitButton = (Button) searchContentLayout.findViewById(R.id.submit_Button);

        submitButton.setOnClickListener(this);
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

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");
//        uId = "1";
//        uToken = "198302935052918";
//        ScanManager.init(that);
        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    private void fillScan() {
        searchScanLayout.setVisibility(View.VISIBLE);
        searchContentLayout.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        scanEditTextTool = new ScanEditTextTool(that, scanEditText);
        scanEditTextTool.setComplete(this);

        scanEditText.setText(null);
    }

    private void fillContent() {
        searchScanLayout.setVisibility(View.GONE);
        searchContentLayout.setVisibility(View.VISIBLE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        contentTextView.setText(dataBean.getData());
    }

    @Override
    public void onComplete() {
        String code = scanEditText.getText().toString();
        new SearchTask(that, code).start();
    }

    @Override
    public void onBackPressed() {
        if (searchContentLayout.getVisibility() == View.VISIBLE) {
            fillScan();
        } else {
            finish();
        }
    }

    @Override
    public void onError(ContentEditText editText) {

    }

    @Override
    public void OnBarCodeReceived(String s) {
        if (scanEditTextTool == null) {
            return;
        }
        scanEditTextTool.setScanText(s);
    }

    @Override
    public void onClick(View v) {
        if (v == submitButton) {
            onBackPressed();
        }
    }

    private class SearchTask extends HttpAsyncTask<DataBean> {

        private String code;

        public SearchTask(Context context, String code) {
            super(context);
            this.code = code;
        }

        @Override
        public DataHull<DataBean> doInBackground() {
            return SearchProvider.request(context, uId, uToken, code);
        }

        @Override
        public void onPostExecute(DataBean result) {
            dataBean = result;
            fillContent();
        }
    }
}