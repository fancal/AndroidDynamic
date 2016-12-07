package com.elianshang.wms.app.procurement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elianshang.wms.app.procurement.R;
import com.elianshang.wms.app.procurement.bean.ChannelList;


/**
 * Created by liuhanzhi on 16/10/25.
 */

public class ChannelListAdapter extends BaseAdapter {

    private Context context;

    public ChannelListAdapter(Context context) {
        this.context = context;
    }

    private ChannelList channelList;

    public void setChannelList(ChannelList channelList) {
        this.channelList = channelList;
    }

    @Override
    public int getCount() {
        if (channelList == null) {
            return 0;
        }
        return channelList.size();
    }

    @Override
    public ChannelList.Item getItem(int position) {
        return channelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.channel_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ChannelList.Item item = getItem(position);
        viewHolder.nameTextView.setText(item.getName());
        return convertView;
    }

    private static class ViewHolder {

        TextView nameTextView;

    }
}
