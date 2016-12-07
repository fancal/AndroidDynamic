package com.elianshang.wms.app.procurement.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.dynamic.DLBasePluginActivity;
import com.elianshang.dynamic.internal.DLIntent;
import com.elianshang.wms.app.procurement.R;
import com.elianshang.wms.app.procurement.adapter.TaskListAdapter;
import com.elianshang.wms.app.procurement.bean.ShelfList;
import com.elianshang.wms.app.procurement.bean.TaskList;
import com.elianshang.wms.app.procurement.provider.TaskListProvider;
import com.xue.http.impl.DataHull;

/**
 * Created by liuhanzhi on 16/12/5.
 */

public class TaskListActivity extends DLBasePluginActivity implements AdapterView.OnItemClickListener {

    public static void launch(DLBasePluginActivity activity, String uid, String uToken, ShelfList.Item item) {
        DLIntent intent = new DLIntent(activity.getPackageName(), TaskListActivity.class);
        intent.putExtra("uId", uid);
        intent.putExtra("uToken", uToken);
        intent.putExtra("item", item);
        activity.startPluginActivity(intent);
    }

    private Toolbar toolbar;

    private ListView listView;

    private View emptyView;

    private String uId;

    private String uToken;

    private ShelfList.Item item;

    private TaskList taskList;

    private TaskListAdapter taskListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasklist_activity);

        if (readExtras()) {
            findViews();
            requestTaskListTask(item.getId());
        }

    }

    private boolean readExtras() {
        uId = getIntent().getStringExtra("uId");
        uToken = getIntent().getStringExtra("uToken");
        item = (ShelfList.Item) getIntent().getSerializableExtra("item");

        if (TextUtils.isEmpty(uId) || TextUtils.isEmpty(uToken) || item == null) {
            finish();
            return false;
        }

        return true;
    }

    private void findViews() {
        initToolbar();

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

    private void fillTaskList() {
        if (taskList != null) {
            if (taskListAdapter == null) {
                taskListAdapter = new TaskListAdapter(that);
                listView.setAdapter(taskListAdapter);
            }
            taskListAdapter.setTaskList(taskList);
            taskListAdapter.notifyDataSetChanged();
            listView.setOnItemClickListener(this);

            listView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }


    }

    private void requestTaskListTask(String id) {
        new TaskListTask(that, id).start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * 任务list
     */
    private class TaskListTask extends HttpAsyncTask<TaskList> {

        String id;

        public TaskListTask(Context context, String id) {
            super(context, true, true, false);
            this.id = id;
        }

        @Override
        public DataHull<TaskList> doInBackground() {
            return TaskListProvider.request(that, uId, uToken, id);
        }

        @Override
        public void onPostExecute(TaskList result) {
            taskList = result;
            fillTaskList();
        }

        @Override
        public void dataNull(String errMsg) {
            fillTaskList();
        }

    }

}
