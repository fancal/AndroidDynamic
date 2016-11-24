package com.elianshang.wms.app.load.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elianshang.wms.app.load.R;
import com.elianshang.wms.app.load.bean.ExpensiveList;

/**
 * Created by liuhanzhi on 16/10/25.
 */

public class ExpensiveListAdapter extends BaseAdapter {

    private Context context;

    public ExpensiveListAdapter(Context context) {
        this.context = context;
    }

    private ExpensiveList tuJobList;

    public void setExpensiveList(ExpensiveList tuJobList) {
        this.tuJobList = tuJobList;
    }

    @Override
    public int getCount() {
        if (tuJobList == null) {
            return 0;
        }
        return tuJobList.size();
    }

    @Override
    public ExpensiveList.Item getItem(int position) {
        return tuJobList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.load_expensive_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.containerIdTextView = (TextView) convertView.findViewById(R.id.containerId_TextView);
            viewHolder.boxNumTextView = (TextView) convertView.findViewById(R.id.boxNum_TextView);
            viewHolder.turnOverBoxNumTextView = (TextView) convertView.findViewById(R.id.turnOverBoxNum_TextView);
            viewHolder.mergeTimeTextView = (TextView) convertView.findViewById(R.id.mergeTime_TextView);
            viewHolder.isLoadTextView = (TextView) convertView.findViewById(R.id.isLoad_TextView);
            viewHolder.taskBoardQtyTextView = (TextView) convertView.findViewById(R.id.taskBoardQty_TextView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ExpensiveList.Item item = getItem(position);
        viewHolder.containerIdTextView.setText("托盘码:" + item.getMarkContainerId());
        viewHolder.boxNumTextView.setText("总箱数:" + item.getBoxNum());
        viewHolder.turnOverBoxNumTextView.setText("总周转箱数:" + item.getTurnoverBoxNum());
        viewHolder.isLoadTextView.setText("是否装车:" + (item.isLoaded() ? "是" : "否"));
        return convertView;
    }

    private static class ViewHolder {

        TextView containerIdTextView;

        TextView boxNumTextView;

        TextView turnOverBoxNumTextView;

        TextView mergeTimeTextView;

        TextView isLoadTextView;

        TextView taskBoardQtyTextView;

    }
}
