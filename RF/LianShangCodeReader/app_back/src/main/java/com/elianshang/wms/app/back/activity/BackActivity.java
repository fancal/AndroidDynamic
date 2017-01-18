package com.elianshang.wms.app.back.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.QtyEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.back.R;
import com.elianshang.wms.app.back.bean.BackList;
import com.elianshang.wms.app.back.bean.ResponseState;
import com.elianshang.wms.app.back.provider.DoProvider;
import com.xue.http.impl.DataHull;

public class BackActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, View.OnClickListener, ScanEditTextTool.OnStateChangeListener {

    public static void launch(DLBasePluginActivity activity, String uid, String uToken, BackList backList) {
        DLIntent intent = new DLIntent(activity.getPackageName(), BackActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        if (backList != null) {
            intent.putExtra("backList", backList);
        }
        activity.startPluginActivityForResult(intent, 1);
    }

    private Toolbar mToolbar;

    private TextView progressTextView;

    private TextView itemNameTextView;

    private TextView barcodeTextView;

    private TextView skuCodeTextView;

    private TextView packCodeTextView;

    private TextView packNameTextView;

    private TextView locationCodeTextView;

    private View locationCodeLayout;

    private ScanEditText locationCodeEditText;

    private View qtyLayout;

    private QtyEditText umoQtyEditText;

    private QtyEditText scatterQtyEditText;

    private Button skipButton;

    private Button submitButton;

    private String uId;

    private String uToken;

    private BackList backList;

    private BackList.Item curItem;

    private int curPos;

    private ScanEditTextTool scanEditTextTool;

    private boolean isItemClick;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_do);

        if (readExtra()) {
            findView();

            curPos = 0;
            curItem = backList.get(curPos);
            fillLocationModeView();
        }
    }

    private void findView() {
        progressTextView = (TextView) findViewById(R.id.progress_TextView);
        itemNameTextView = (TextView) findViewById(R.id.itemName_TextView);
        skuCodeTextView = (TextView) findViewById(R.id.skuCode_TextView);
        barcodeTextView = (TextView) findViewById(R.id.barcode_TextView);
        packCodeTextView = (TextView) findViewById(R.id.packCode_TextView);
        packNameTextView = (TextView) findViewById(R.id.packName_TextView);
        locationCodeTextView = (TextView) findViewById(R.id.locationCode_TextView);
        locationCodeLayout = findViewById(R.id.locationCode_Layout);
        locationCodeEditText = (ScanEditText) findViewById(R.id.locationCode_EditText);
        locationCodeEditText.setCode(true);
        qtyLayout = findViewById(R.id.qty_Layout);
        umoQtyEditText = (QtyEditText) findViewById(R.id.umoQty_EditText);
        scatterQtyEditText = (QtyEditText) findViewById(R.id.scatterQty_EditText);

        skipButton = (Button) findViewById(R.id.skip_Button);
        submitButton = (Button) findViewById(R.id.submit_Button);

        skipButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);

        initToolbar();
    }

    private void next() {
        if (curPos + 1 == backList.size()) {
            ToastTool.show(that, "返仓操作完成");
            setResult(RESULT_OK);
            finish();
            return;
        }

        curPos++;
        curItem = backList.get(curPos);
        fillLocationModeView();
    }

    private void fillLocationModeView() {
        locationCodeLayout.setVisibility(View.VISIBLE);
        qtyLayout.setVisibility(View.GONE);
        skipButton.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.GONE);

        progressTextView.setText((curPos + 1) + "/" + backList.size());
        itemNameTextView.setText(curItem.getSkuName());
        barcodeTextView.setText(curItem.getBarcode());
        skuCodeTextView.setText(curItem.getSkuCode());
        packCodeTextView.setText(curItem.getPackCode());
        packNameTextView.setText(curItem.getPackName());
        locationCodeTextView.setText(curItem.getLocationCode());
        locationCodeEditText.setText("");
        locationCodeEditText.requestFocus();

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        scanEditTextTool = new ScanEditTextTool(that, locationCodeEditText);
        scanEditTextTool.setComplete(this);
    }

    private void fillQtyModeView() {
        locationCodeLayout.setVisibility(View.GONE);
        qtyLayout.setVisibility(View.VISIBLE);
        skipButton.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.VISIBLE);
        umoQtyEditText.setText("");
        scatterQtyEditText.setText("");
        umoQtyEditText.requestFocus();

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }
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

    @Override
    public void onBackPressed() {
        DialogTools.showTwoButtonDialog(that, "批量返仓未完成，退出后将不会记录未做的返仓项目", "取消", "确定", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }, true);
    }

    private boolean readExtra() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

//        uId = "2";
//        uToken = "1231231231231";
//        ScanManager.init(that);

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        backList = (BackList) getIntent().getSerializableExtra("backList");

        return true;
    }

    @Override
    public void onClick(View v) {
        if (isItemClick) {
            return;
        }

        isItemClick = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isItemClick = false;
            }
        }, 500);

        if (v == skipButton) {
            next();
        } else if (v == submitButton) {
            String locationCode = locationCodeTextView.getText().toString();
            String barcode = curItem.getBarcode();
            String umoQty = umoQtyEditText.getValue();
            String scatterQty = scatterQtyEditText.getValue();

            if (TextUtils.isEmpty(umoQty) && TextUtils.isEmpty(scatterQty)) {
                Toast.makeText(that, "请输入正确的数量", Toast.LENGTH_SHORT).show();
                return;
            }

            new DoTask(that, locationCode, barcode, umoQty, scatterQty).start();
        }
    }

    @Override
    public void OnBarCodeReceived(String s) {
        if (scanEditTextTool != null) {
            scanEditTextTool.setScanText(s);
        }
    }

    @Override
    public void onComplete() {
        final String locationCode = locationCodeEditText.getText().toString();

        if (!TextUtils.equals(locationCode, curItem.getLocationCode())) {
            DialogTools.showTwoButtonDialog(that, "扫描的库位与推荐的库位不符，请确认移入", "取消", "确定", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    locationCodeTextView.setText(locationCode);
                    fillQtyModeView();
                }
            }, true);
        } else {
            fillQtyModeView();
        }
    }

    @Override
    public void onError(ContentEditText editText) {

    }

    private class DoTask extends HttpAsyncTask<ResponseState> {

        private String locationCode;

        private String barcode;

        private String umoQty;

        private String scatterQty;

        public DoTask(Context context, String locationCode, String barcode, String umoQty, String scatterQty) {
            super(context, true, true);
            this.locationCode = locationCode;
            this.barcode = barcode;
            this.umoQty = umoQty;
            this.scatterQty = scatterQty;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return DoProvider.request(context, uId, uToken, locationCode, barcode, umoQty, scatterQty);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            next();
        }
    }
}
