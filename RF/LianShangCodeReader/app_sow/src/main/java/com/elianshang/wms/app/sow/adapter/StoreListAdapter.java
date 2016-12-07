package com.elianshang.wms.app.sow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elianshang.wms.app.sow.R;
import com.elianshang.wms.app.sow.bean.StoreList;

import java.util.ArrayList;


/**
 * Created by liuhanzhi on 16/10/25.
 */

public class StoreListAdapter extends BaseAdapter {

    private Context context;

    public StoreListAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<StoreList.Item> storeList;

    public void setStoreList(ArrayList<StoreList.Item> storeList) {
        this.storeList = storeList;
    }

    @Override
    public int getCount() {
        if (storeList == null) {
            return 0;
        }
        return storeList.size();
    }

    @Override
    public StoreList.Item getItem(int position) {
        return storeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.store_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.storeNo = (TextView) convertView.findViewById(R.id.storeNo);
            viewHolder.storeName = (TextView) convertView.findViewById(R.id.storeName);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        StoreList.Item item = getItem(position);
        viewHolder.storeNo.setText(item.getStoreNo());
        viewHolder.storeName.setText("[" + item.getStoreName() + "]");
        return convertView;
    }

    private static class ViewHolder {

        TextView storeNo;

        TextView storeName;

    }
}
