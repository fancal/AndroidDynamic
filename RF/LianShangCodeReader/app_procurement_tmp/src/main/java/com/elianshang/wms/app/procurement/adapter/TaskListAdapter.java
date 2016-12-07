package com.elianshang.wms.app.procurement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elianshang.wms.app.procurement.R;
import com.elianshang.wms.app.procurement.bean.TaskList;


/**
 * Created by liuhanzhi on 16/10/25.
 */

public class TaskListAdapter extends BaseAdapter {

    private Context context;

    public TaskListAdapter(Context context) {
        this.context = context;
    }

    private TaskList taskList;

    public void setTaskList(TaskList taskList) {
        this.taskList = taskList;
    }

    @Override
    public int getCount() {
        if (taskList == null) {
            return 0;
        }
        return taskList.size();
    }

    @Override
    public TaskList.Item getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.task_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TaskList.Item item = getItem(position);
        viewHolder.nameTextView.setText(item.getName());
        return convertView;
    }

    private static class ViewHolder {

        TextView nameTextView;

    }
}
