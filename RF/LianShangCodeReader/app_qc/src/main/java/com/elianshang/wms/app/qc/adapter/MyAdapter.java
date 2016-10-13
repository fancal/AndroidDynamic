package com.elianshang.wms.app.qc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elianshang.wms.app.qc.R;
import com.elianshang.wms.app.qc.bean.QcList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MyAdapter extends BaseAdapter {

    private QcList qcList;

    private Context context;

    private OnItemClickListener onItemClickListener;

    public MyAdapter(Context context, QcList qcList) {
        this.context = context;
        this.qcList = qcList;
    }

    public MyAdapter(Context context) {
        this.context = context;
    }

    public void setQcList(QcList qcList) {
        this.qcList = qcList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getCount() {
        if (qcList == null) {
            return 0;
        }
        return qcList.size();
    }

    @Override
    public QcList.Item getItem(int position) {
        return qcList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, android.view.View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.qualitycontrol_list_item, null, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.itemName_TextView);
            viewHolder.packUnit = (TextView) convertView.findViewById(R.id.packUnit_TextView);
            viewHolder.qty = (TextView) convertView.findViewById(R.id.qty_TextView);
            viewHolder.statusError = (TextView) convertView.findViewById(R.id.status_error);
            viewHolder.statusDone = (TextView) convertView.findViewById(R.id.status_done);
        }
        final QcList.Item item = getItem(position);
        viewHolder.name.setText(item.getItemName());
        viewHolder.packUnit.setText(item.getPackName());
        viewHolder.qty.setText(item.getUomQty());
        viewHolder.qty.setText(item.isSplit() ? "是" : "否");
        viewHolder.statusDone.setVisibility(item.isQcDone() ? VISIBLE : GONE);
        viewHolder.statusError.setVisibility(item.isQcError() ? VISIBLE : GONE);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(item.getBarCode());
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {

        TextView name;

        TextView packUnit;

        TextView qty;

        TextView isPrompt;

        TextView statusError;

        TextView statusDone;


    }

    public interface OnItemClickListener{
        void onItemClick(String barCode);
    }

}

