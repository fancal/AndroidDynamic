package com.elianshang.wms.app.back.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elianshang.bridge.ui.view.QtyEditText;
import com.elianshang.wms.app.back.R;
import com.elianshang.wms.app.back.bean.TransferList;

public class TransferOutListAdapter extends BaseAdapter {

    private TransferList transferList;

    private Context context;

    public TransferOutListAdapter(Context context, TransferList transferList) {
        this.context = context;
        this.transferList = transferList;
    }

    public TransferOutListAdapter(Context context) {
        this.context = context;
    }

    public void setTransferList(TransferList transferList) {
        this.transferList = transferList;
    }

    @Override
    public int getCount() {
        if (transferList == null) {
            return 0;
        }
        return transferList.size();
    }

    @Override
    public TransferList.Item getItem(int position) {
        return transferList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.transferout_list_item, null, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.itemName_TextView);
            viewHolder.packUnit = (TextView) convertView.findViewById(R.id.packName_TextView);
            viewHolder.qty = (TextView) convertView.findViewById(R.id.qty_TextView);
            viewHolder.realQty = (QtyEditText) convertView.findViewById(R.id.inputQty_EditView);
            convertView.setTag(viewHolder);
        }
        final TransferList.Item item = getItem(position);
        viewHolder.name.setText(item.getSkuName());
        viewHolder.packUnit.setText("规格:"+item.getPackName());
        viewHolder.qty.setText("数量:"+item.getQty());
        viewHolder.realQty.setHint(item.getQty());
        item.setRealQty(item.getQty());
        viewHolder.realQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                item.setRealQty(viewHolder.realQty.getValue());
            }
        });

        return convertView;
    }

    private class ViewHolder {

        TextView name;

        TextView packUnit;

        TextView qty;

        QtyEditText realQty;

    }

}

