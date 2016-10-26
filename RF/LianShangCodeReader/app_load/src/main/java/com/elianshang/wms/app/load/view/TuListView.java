package com.elianshang.wms.app.load.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.elianshang.bridge.asyn.HttpAsyncTask;
import com.elianshang.wms.app.load.R;
import com.elianshang.wms.app.load.adapter.TuListAdapter;
import com.elianshang.wms.app.load.bean.TuList;
import com.elianshang.wms.app.load.provider.TuListProvier;
import com.xue.http.impl.DataHull;

/**
 * Created by liuhanzhi on 16/10/25.
 */

public class TuListView extends FrameLayout implements AdapterView.OnItemClickListener {

    private String status;

    private String uId;

    private String uToken;

    private ListView listView;

    private TextView emptyTextView;

    private TuListAdapter adapter;

    private TuList tuList;

    private TuListTask tuListTask;

    private OnItemClickListener onItemClickListener;

    public TuListView(Context context) {
        super(context);
        initView();
    }

    public TuListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TuListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.tu_list, this, true);
        findView();
    }

    private void findView() {
        emptyTextView = (TextView) findViewById(R.id.empty_TextView);
        listView = (ListView) findViewById(R.id.listview);
    }

    public void initTuList(String uId, String uToken, String status) {
        if (tuListTask != null) {
            tuListTask.cancel();
            tuListTask = null;
        }
        this.uId = uId;
        this.uToken = uToken;
        this.status = status;
        tuListTask = new TuListTask(getContext());
        tuListTask.start();
    }

    private void fillTuList() {
        if (tuList != null) {
            emptyTextView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            if (adapter == null) {
                adapter = new TuListAdapter(getContext());
                listView.setAdapter(adapter);
            }
            adapter.setTuList(tuList);
            adapter.notifyDataSetChanged();
            listView.setOnItemClickListener(this);
        } else {
            emptyTextView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(onItemClickListener != null){
            onItemClickListener.onItemClick(tuList.get(position));
        }
    }

    private class TuListTask extends HttpAsyncTask<TuList> {

        public TuListTask(Context context) {
            super(context, true, true, false);
        }

        @Override
        public DataHull<TuList> doInBackground() {
            return TuListProvier.request(context, uId, uToken, status);
        }

        @Override
        public void onPostExecute(TuList result) {
            tuList = result;
            fillTuList();
        }

        @Override
        public void dataNull(String errMsg) {
            super.dataNull(errMsg);
            fillTuList();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TuList.Item item);
    }

}
