package com.elianshang.wms.app.back.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ContentEditText;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.back.R;
import com.elianshang.wms.app.back.adapter.TransferOutListAdapter;
import com.elianshang.wms.app.back.bean.ResponseState;
import com.elianshang.wms.app.back.bean.SupplierInfo;
import com.elianshang.wms.app.back.bean.TransferList;
import com.elianshang.wms.app.back.provider.TransferOutConfirmProvider;
import com.elianshang.wms.app.back.provider.TransferOutQuickProvider;
import com.elianshang.wms.app.back.provider.TransferOutScanLocationProvider;
import com.xue.http.impl.DataHull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * 退货入库
 */
public class TransferOutActivity extends DLBasePluginActivity implements ScanEditTextTool.OnStateChangeListener, ScanManager.OnBarCodeListener, View.OnClickListener {

    public static void launch(DLBasePluginActivity activity, String uid, String uToken) {
        DLIntent intent = new DLIntent(activity.getPackageName(), TransferOutActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        activity.startPluginActivity(intent);
    }

    private String uId;

    private String uToken;

    private Toolbar toolbar;

    private View scanLayout;

    private ScanEditText scanSoOtherIdEditText;

    private View chooseLayout;

    private Button chooseQuikButton;

    private Button chooseEditButton;

    private View listLayout;

    private ListView listView;

    private Button listSubmitButton;

    private TransferOutListAdapter myAdapter;

    private SupplierInfo supplierInfo;

    private TransferList transferList;

    private ScanEditTextTool scanEditTextTool;

    private String serialNumber;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferout);
        if (readExtras()) {
            findView();
            fillScanLayout();
        }
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    private void findView() {
        initToolbar();


        scanLayout = findViewById(R.id.scan_Layout);
        scanSoOtherIdEditText = (ScanEditText) scanLayout.findViewById(R.id.unknownCode_EditText);

        chooseLayout = findViewById(R.id.choose_layout);
        chooseQuikButton = (Button) chooseLayout.findViewById(R.id.quik_Button);
        chooseEditButton = (Button) chooseLayout.findViewById(R.id.edit_Button);
        chooseQuikButton.setOnClickListener(this);
        chooseEditButton.setOnClickListener(this);

        listLayout = findViewById(R.id.list_Layout);
        listView = (ListView) listLayout.findViewById(R.id.listview);
        listSubmitButton = (Button) listLayout.findViewById(R.id.submit_Button);
        listSubmitButton.setOnClickListener(this);


    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void fillScanLayout() {
        scanLayout.setVisibility(View.VISIBLE);
        chooseLayout.setVisibility(View.GONE);
        listLayout.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        scanEditTextTool = new ScanEditTextTool(that, scanSoOtherIdEditText);
        scanEditTextTool.setComplete(this);
    }

    private void fillChooseLayout() {
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        scanLayout.setVisibility(View.GONE);
        chooseLayout.setVisibility(View.VISIBLE);
        listLayout.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }
    }

    private void fillListLayout() {
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        scanLayout.setVisibility(View.GONE);
        chooseLayout.setVisibility(View.GONE);
        listLayout.setVisibility(View.VISIBLE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
            scanEditTextTool = null;
        }

        if (myAdapter == null) {
            myAdapter = new TransferOutListAdapter(that);
            listView.setAdapter(myAdapter);
        }
        myAdapter.setTransferList(transferList);
        myAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        if (transferList != null) {
            DialogTools.showTwoButtonDialog(that, "是否暂退任务,下次回来将重新开始", "取消", "确定", null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }, true);
        } else {
            super.onBackPressed();
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


    @Override
    public void onComplete() {
        if (scanLayout.getVisibility() == View.VISIBLE) {
            Editable editable = scanSoOtherIdEditText.getText();
            if (editable != null) {
                new ScanLocationTask(that, editable.toString()).start();
            }
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
        if (v == listSubmitButton) {
            if (transferList == null || myAdapter == null) {
                return;
            }
            JSONArray jsonArray = new JSONArray();
            for (TransferList.Item item : transferList) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(item.getSkuId(), item.getRealQty());
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            new ConfirmTask(that, transferList.getTaskId(), jsonArray.toString(), serialNumber).start();
        } else if (v == chooseQuikButton) {
            new QuickTransferTask(that, transferList.getTaskId(), serialNumber);
        } else {
            fillListLayout();
        }
    }

    private class ScanLocationTask extends HttpAsyncTask<TransferList> {

        String soOtherId;

        public ScanLocationTask(Context context, String soOtherId) {
            super(context, true, true);
            this.soOtherId = soOtherId;
        }

        @Override
        public DataHull<TransferList> doInBackground() {
            return TransferOutScanLocationProvider.request(context, uId, uToken, soOtherId);
        }

        @Override
        public void onPostExecute(TransferList result) {
            transferList = result;
            fillChooseLayout();
        }
    }

    private class QuickTransferTask extends HttpAsyncTask<ResponseState> {

        String taskId;

        String serialNumber;

        public QuickTransferTask(Context context, String taskId, String serialNumber) {
            super(context);
            this.taskId = taskId;
            this.serialNumber = serialNumber;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return TransferOutQuickProvider.request(context, uId, uToken, taskId, serialNumber);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            ToastTool.show(context, "退货出库成功!");
            finish();
        }
    }

    private class ConfirmTask extends HttpAsyncTask<ResponseState> {

        String taskId;

        String map;

        String serialNumber;

        public ConfirmTask(Context context, String taskId, String map, String serialNumber) {
            super(context, true, true, false);
            this.taskId = taskId;
            this.map = map;
            this.serialNumber = serialNumber;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return TransferOutConfirmProvider.request(context, uId, uToken, taskId, map, serialNumber);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            ToastTool.show(context, "退货出库成功!");
            finish();
        }
    }
}
