package com.elianshang.wms.app.load.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elianshang.wms.app.load.R;
import com.elianshang.wms.app.load.bean.TuList;

/**
 * Created by liuhanzhi on 16/10/25.
 */

public class TuListAdapter extends BaseAdapter {

    private Context context;

    public TuListAdapter(Context context) {
        this.context = context;
    }

    private TuList tuList;

    public void setTuList(TuList tuList) {
        this.tuList = tuList;
    }

    @Override
    public int getCount() {
        if (tuList == null) {
            return 0;
        }
        return tuList.size();
    }

    @Override
    public TuList.Item getItem(int position) {
        return tuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.load_tu_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tuTextView = (TextView) convertView.findViewById(R.id.tu_TextView);
            viewHolder.preBoardTextView = (TextView) convertView.findViewById(R.id.preBoard_TextView);
            viewHolder.storesTextView = (TextView) convertView.findViewById(R.id.stores_TextView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TuList.Item item = getItem(position);
        viewHolder.tuTextView.setText("TU号:" + item.getTu());
        viewHolder.preBoardTextView.setText("预装版数:" + item.getPreBoard());
        if (item.getStores() != null && item.getStores().size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < item.getStores().size(); i++) {
                TuList.Item.Store store = item.getStores().get(i);
                sb.append("门店");
                sb.append(String.valueOf(i + 1));
                sb.append(":");
                sb.append(store.getCustomerName());
                sb.append("【");
                sb.append(store.getCustomerCode());
                sb.append("】");
                if (i != item.getStores().size() - 1) {
                    sb.append("\r\n");
                }
            }
            viewHolder.storesTextView.setText(sb.toString());
        }
        return convertView;
    }

    private static class ViewHolder {

        TextView tuTextView;

        TextView preBoardTextView;

        TextView storesTextView;

    }
}
