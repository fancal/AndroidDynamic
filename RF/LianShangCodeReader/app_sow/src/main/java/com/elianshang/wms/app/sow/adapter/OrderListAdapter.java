package com.elianshang.wms.app.sow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elianshang.wms.app.sow.R;
import com.elianshang.wms.app.sow.bean.OrderList;


/**
 * Created by liuhanzhi on 16/10/25.
 */

public class OrderListAdapter extends BaseAdapter {

    private Context context;

    public OrderListAdapter(Context context) {
        this.context = context;
    }

    private OrderList orderList;

    public void setOrderList(OrderList orderList) {
        this.orderList = orderList;
    }

    @Override
    public int getCount() {
        if (orderList == null) {
            return 0;
        }
        return orderList.size();
    }

    @Override
    public OrderList.Item getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.order_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.orderId = (TextView) convertView.findViewById(R.id.orderId);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OrderList.Item item = getItem(position);
        viewHolder.orderId.setText(item.getOrderId());
        return convertView;
    }

    private static class ViewHolder {

        TextView orderId;

    }
}
