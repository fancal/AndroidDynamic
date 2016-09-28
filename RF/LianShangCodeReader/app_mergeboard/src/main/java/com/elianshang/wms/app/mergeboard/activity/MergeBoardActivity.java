package com.elianshang.wms.app.mergeboard.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.bridge.tool.ScanEditTextTool;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.bridge.ui.view.ScanEditText;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.tools.DeviceTool;
import com.elianshang.tools.ToastTool;
import com.elianshang.wms.app.mergeboard.R;
import com.elianshang.wms.app.mergeboard.bean.BoardDetailList;
import com.elianshang.wms.app.mergeboard.bean.ResponseState;
import com.elianshang.wms.app.mergeboard.provider.BoardDetailListProvider;
import com.elianshang.wms.app.mergeboard.provider.MergeBoardProvider;
import com.xue.http.impl.DataHull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 盘点页面
 */
public class MergeBoardActivity extends DLBasePluginActivity implements ScanManager.OnBarCodeListener, View.OnClickListener {

    private String uId;

    private String uToken;

    private Toolbar mToolbar;

    /**
     * 添加托盘布局
     */
    private View inputView;

    /**
     * 添加托盘布局 item容器
     */
    private LinearLayout inputLayout;

    /**
     * 添加托盘布局 添加按钮
     */
    private Button inputAddButton;

    /**
     * 详情布局
     */
    private View detailView;

    /**
     * 详情布局 item容器
     */
    private LinearLayout detailLayout;

    /**
     * 提交
     */
    private Button submitButton;

    /**
     * EditText工具
     */
    private ScanEditTextTool scanEditTextTool;

    /**
     * 动态布局记录列表
     */
    private ArrayList<ViewHolder> vhList = new ArrayList<>();

    private String serialNumber;
    /**
     * 最大允许合板数
     */
    private final int MAX_BOARD_NUM = 8;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mergeboard);

        if (readExtra()) {
            findView();
            fillInitBoard();
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

        inputView = findViewById(R.id.container_input);
        inputLayout = (LinearLayout) inputView.findViewById(R.id.input_Layout);
        inputAddButton = (Button) inputView.findViewById(R.id.add_Button);
        detailView = findViewById(R.id.detail_Layout);
        detailLayout = (LinearLayout) detailView.findViewById(R.id.detail_Layout);
        submitButton = (Button) findViewById(R.id.submit_Button);

        inputAddButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);

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

        //Fixme test
        uId = "141871359725260";
        uToken = "131370164694198";
        ScanManager.init(this);

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    private void fillInitBoard() {
        serialNumber = DeviceTool.generateSerialNumber(that, getClass().getName());

        inputView.setVisibility(View.VISIBLE);
        detailView.setVisibility(View.GONE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }
        scanEditTextTool = new ScanEditTextTool(that);

        vhList.clear();
        inputLayout.removeAllViews();
        for (int i = 0; i < 2; i++) {
            addItemView();
        }
        if (vhList.size() > 0) {
            vhList.get(0).containerIdEditText.requestFocus();
        }
    }

    private void fillBoardDetail(BoardDetailList boardDetailList) {
        inputView.setVisibility(View.GONE);
        detailView.setVisibility(View.VISIBLE);

        if (scanEditTextTool != null) {
            scanEditTextTool.release();
        }

        vhList.clear();
        detailLayout.removeAllViews();
        for (BoardDetailList.BoardDetail boardDetail : boardDetailList) {
            addDetailItemView(boardDetail);
        }


    }

    private void addItemView() {
        View view = View.inflate(that, R.layout.input_item_view, null);

        ScanEditText containerIdEditText = (ScanEditText) view.findViewById(R.id.containerId_EditText);

        scanEditTextTool.addEditText(containerIdEditText);

        inputLayout.addView(view);
        containerIdEditText.requestFocus();

        ViewHolder vh = new ViewHolder();
        vh.containerIdEditText = containerIdEditText;
        vhList.add(vh);
        if(vhList.size() >= MAX_BOARD_NUM){
            inputAddButton.setVisibility(View.GONE);
        }
    }

    private void addDetailItemView(BoardDetailList.BoardDetail boardDetail) {
        View view = View.inflate(that, R.layout.detail_item_view, null);
        detailLayout.addView(view);
    }


    @Override
    public void onBackPressed() {
        DialogTools.showTwoButtonDialog(that, "是否退出合板,下次回来将重新开始", "取消", "确定", null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }, true);
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
        if (inputAddButton == v) {
            addItemView();
        } else if (submitButton == v) {
            if (inputView.getVisibility() == View.VISIBLE) {
                new GetBoardDetailListTask(that, getBoardListString()).start();
            } else {
                new MergeBoardTask(that, getBoardListString()).start();
            }
        }
    }

    private String getBoardListString() {
        final JSONObject jsonObject = new JSONObject();
        final JSONArray jsonarray = new JSONArray();

        try {
            for (ViewHolder vh : vhList) {
                String containerId = vh.containerIdEditText.getText().toString();
                if (TextUtils.isEmpty(containerId)) {
                } else {
                    JSONObject jso = new JSONObject();
                    jso.put("containerId", containerId);
                    jsonarray.put(jso);
                }
            }

            jsonObject.put("list", jsonarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private class ViewHolder {
        ScanEditText containerIdEditText;
    }

    /**
     * 拖板列表详情
     */
    private class GetBoardDetailListTask extends HttpAsyncTask<BoardDetailList> {

        private String resultList;

        private GetBoardDetailListTask(Context context, String resultList) {
            super(context, true, true);
            this.resultList = resultList;
        }

        @Override
        public DataHull<BoardDetailList> doInBackground() {
            return BoardDetailListProvider.request(context, uId, uToken, resultList);
        }

        @Override
        public void onPostExecute(BoardDetailList result) {
            fillBoardDetail(result);
        }

    }

    /**
     * 合板
     */
    private class MergeBoardTask extends HttpAsyncTask<ResponseState> {

        private String resultList;

        private MergeBoardTask(Context context, String resultList) {
            super(context, true, true);
            this.resultList = resultList;
        }

        @Override
        public DataHull<ResponseState> doInBackground() {
            return MergeBoardProvider.request(context, uId, uToken, resultList, serialNumber);
        }

        @Override
        public void onPostExecute(ResponseState result) {
            finish();
            ToastTool.show(context, "合板完成");
        }
    }
}