package com.elianshang.wms.app.load.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.bridge.tool.ScanManager;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.wms.app.load.R;
import com.elianshang.wms.app.load.bean.TuJobList;
import com.elianshang.wms.app.load.bean.TuList;
import com.elianshang.wms.app.load.provider.TuJobListProvier;
import com.elianshang.wms.app.load.view.TuDetailView;
import com.elianshang.wms.app.load.view.TuListView;
import com.xue.http.impl.DataHull;

/**
 * Created by liuhanzhi on 16/10/25.
 */

public class TuPageActivity extends DLBasePluginActivity implements View.OnClickListener, TuListView.OnItemClickListener, TuDetailView.OnNextButtonClick {

    private String uId;

    private String uToken;

    private Toolbar toolbar;

    private Button tab1;

    private Button tab2;

    private TuListView unloadTuListView;

    private TuListView loadedTuListView;

    private TuDetailView tuDetailView;

    private RelativeLayout content;

    private String[] status = new String[]{"1", "5"};

    private TuJobListTask tuJobListTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_page);
        if (readExtras()) {
            findView();
        }
    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

        //FIXME
        uId = "141871359725260";
        uToken = "25061134202027";
        ScanManager.init(that);

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    private void findView() {
        initToolbar();

        tab1 = (Button) findViewById(R.id.tab1);
        tab2 = (Button) findViewById(R.id.tab2);
        content = (RelativeLayout) findViewById(R.id.content);

        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);

        tab1.performClick();
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

    @Override
    public void onBackPressed() {
        if (content.getChildAt(0) instanceof TuDetailView) {
            if (tab1.isSelected()) {
                fillUnloadList();
            } else {
                fillLoadedList();
            }
            return;
        }
        finish();
    }

    private void fillUnloadList() {
        content.removeAllViews();
        if (unloadTuListView == null) {
            unloadTuListView = new TuListView(that);
        }
        unloadTuListView.initTuList(uId, uToken, status[0]);
        unloadTuListView.setOnItemClickListener(this);
        content.addView(unloadTuListView);

    }

    private void fillLoadedList() {
        content.removeAllViews();
        if (loadedTuListView == null) {
            loadedTuListView = new TuListView(that);
        }
        loadedTuListView.initTuList(uId, uToken, status[1]);
        loadedTuListView.setOnItemClickListener(this);
        content.addView(loadedTuListView);
    }

    private void fillTuDetail(TuList.Item item) {
        content.removeAllViews();
        if (tuDetailView == null) {
            tuDetailView = new TuDetailView(that);
        }
        tuDetailView.setOnNextButtonClick(this);
        tuDetailView.fillTuDetail(item);
        content.addView(tuDetailView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    @Override
    public void onClick(View v) {
        if (v == tab1) {
            tab1.setSelected(true);
            tab2.setSelected(false);
            fillUnloadList();
        } else if (v == tab2) {
            tab1.setSelected(false);
            tab2.setSelected(true);
            fillLoadedList();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if(tab2.isSelected()){//FIXME
                return;
            }
            fillUnloadList();
        }
    }

    @Override
    public void onItemClick(TuList.Item item) {
        fillTuDetail(item);
    }

    @Override
    public void onNextClick(TuList.Item item) {
        if (tuJobListTask != null) {
            tuJobListTask.cancel();
            tuJobListTask = null;
        }
        tuJobListTask = new TuJobListTask(that, item.getTu());
        tuJobListTask.start();
    }

    /**
     * 请求尾货接口
     */
    private class TuJobListTask extends HttpAsyncTask<TuJobList> {

        private String tu;

        public TuJobListTask(Context context, String tu) {
            super(context, true, true, false);
            this.tu = tu;
        }

        @Override
        public DataHull<TuJobList> doInBackground() {
            return TuJobListProvier.request(context, uId, uToken, tu);
        }

        @Override
        public void onPostExecute(TuJobList result) {
            TuJobActivity.launch(TuPageActivity.this, uId, uToken, tu, result);
        }

        @Override
        public void dataNull(String errMsg) {
            super.dataNull(errMsg);
        }
    }
}
