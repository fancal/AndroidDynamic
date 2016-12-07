package com.elianshang.wms.app.procurement.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Space;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.tools.UITool;
import com.elianshang.wms.app.procurement.R;
import com.elianshang.wms.app.procurement.adapter.ChannelListAdapter;
import com.elianshang.wms.app.procurement.bean.ChannelList;
import com.elianshang.wms.app.procurement.bean.ShelfList;
import com.elianshang.wms.app.procurement.provider.ChannelListProvider;
import com.elianshang.wms.app.procurement.provider.ShelfListProvider;
import com.xue.http.impl.DataHull;

import java.util.ArrayList;

/**
 * Created by liuhanzhi on 16/12/5.
 */

public class ShelfListActivity extends DLBasePluginActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Toolbar toolbar;

    private LinearLayout tabLayout;

    private ListView listView;

    private View emptyView;

    private String uId;

    private String uToken;

    private ArrayList<Button> tabViewList = new ArrayList<>();

    private ShelfList shelfList;

    private ChannelList channelList;

    private ChannelListAdapter channelAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shelflist_activity);

        if (readExtras()) {
            findViews();
            requestShelfListTask();
        }

    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");

        // FIXME: 16/12/5
        uId = "1";
        uToken = "274237956828916";

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken)) {
            finish();
            return false;
        }

        return true;
    }

    private void findViews() {
        initToolbar();

        tabLayout = (LinearLayout) findViewById(R.id.tab_layout);
        listView = (ListView) findViewById(R.id.listView);
        emptyView = findViewById(R.id.empty_textView);
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

    private void fillTabs() {
        /**
         * tab间隔竖线(dip)
         */
        final int TAB_SPACE = 1;
        /**
         * 显示区域最多外漏的tab个数
         */
        final int TAB_MAX_NUM = 4;
        int width;
        if (shelfList.size() <= TAB_MAX_NUM) {
            width = (UITool.getScreenWidth(that) - (shelfList.size() - 1) * TAB_SPACE) / shelfList.size();
        } else {
            width = UITool.getScreenWidth(that) / TAB_MAX_NUM - 5;
        }
        for (int i = 0; i < shelfList.size(); i++) {
            Button tabButton = (Button) LayoutInflater.from(that).inflate(R.layout.shelflist_tab_item, null);
            tabButton.setText(shelfList.get(i).getName());
            tabButton.setTag(shelfList.get(i));
            tabButton.setOnClickListener(this);

            tabLayout.addView(tabButton, new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT));

            if (i != shelfList.size() - 1) {
                Space space = new Space(that);
                tabLayout.addView(space, new LinearLayout.LayoutParams(UITool.dipToPx(that, TAB_SPACE), LinearLayout.LayoutParams.WRAP_CONTENT));
            }

            tabViewList.add(tabButton);
        }

        //请求第一个tab数据
        tabViewList.get(0).performClick();


    }

    private void fillChannelList() {
        if (channelList != null) {
            if (channelAdapter == null) {
                channelAdapter = new ChannelListAdapter(that);
                listView.setAdapter(channelAdapter);
            }
            channelAdapter.setChannelList(channelList);
            channelAdapter.notifyDataSetChanged();
            listView.setOnItemClickListener(this);

            listView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }


    }

    private void requestShelfListTask() {
        new ShelfListTask(that).start();
    }

    private void requestChannelListTask(String id) {
        new ChannelListTask(that, id).start();
    }

    @Override
    public void onClick(View v) {
        for (Button button : tabViewList) {
            if (v == button) {
                button.setSelected(true);
                ShelfList.Item item = (ShelfList.Item) button.getTag();
                requestChannelListTask(item.getId());
            } else {
                button.setSelected(false);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TaskListActivity.launch(this, uId, uToken, shelfList.get(position));
    }

    /**
     * 货架区list
     */
    private class ShelfListTask extends HttpAsyncTask<ShelfList> {

        public ShelfListTask(Context context) {
            super(context, true, true, false);
        }

        @Override
        public DataHull<ShelfList> doInBackground() {
            return ShelfListProvider.request(that, uId, uToken);
        }

        @Override
        public void onPostExecute(ShelfList result) {
            shelfList = result;
            fillTabs();
        }

    }

    /**
     * 通道list
     */
    private class ChannelListTask extends HttpAsyncTask<ChannelList> {

        String id;

        public ChannelListTask(Context context, String id) {
            super(context, true, true, false);
            this.id = id;
        }

        @Override
        public DataHull<ChannelList> doInBackground() {
            return ChannelListProvider.request(that, uId, uToken, id);
        }

        @Override
        public void onPostExecute(ChannelList result) {
            channelList = result;
            fillChannelList();
        }

        @Override
        public void dataNull(String errMsg) {
            fillChannelList();
        }

    }

}
