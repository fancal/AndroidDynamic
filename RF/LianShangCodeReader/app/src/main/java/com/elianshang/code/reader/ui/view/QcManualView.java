package com.elianshang.code.reader.ui.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elianshang.code.reader.R;
import com.elianshang.code.reader.bean.QcList;
import com.elianshang.code.reader.tool.BaseQcController;

import java.util.HashMap;

/**
 * Created by liuhanzhi on 16/8/9.
 */
public class QcManualView extends LinearLayout {

    private RecyclerView mRecyclerView;

    private MyAdapter mAdapter;

    private QcManualControllerListener listener;

    private HashMap<String, BaseQcController.CacheQty> submitMap;

    private QcList qcList;

    public QcManualView(Context context) {
        super(context);
        init();
    }

    public QcManualView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QcManualView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public QcManualView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public QcManualControllerListener getListener() {
        return listener;
    }

    public void setListener(QcManualControllerListener listener) {
        this.listener = listener;
    }

    private void init() {
        setOrientation(VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.qualitycontrol_manual, this, true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    public void fill(QcList qcList, HashMap<String, BaseQcController.CacheQty> submitMap) {
        this.submitMap = submitMap;
        this.qcList = qcList;
        if (mAdapter == null) {
            mAdapter = new MyAdapter();
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    public void notifySetDataChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 刷新某一条数据
     * @param position
     */
    public void notifyItemChanged(int position){
        if(mAdapter != null){
            mAdapter.notifyItemChanged(position);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qualitycontrol_manual_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.fill(qcList.get(position));
        }

        @Override
        public int getItemCount() {
            if (qcList == null) {
                return 0;
            }
            return qcList.size();
        }

    }

    private class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        CheckBox mCheckBox;

        TextView mItemName;

        TextView mItemPack;

        TextView mItemQty;

        Button mException;

        QcList.Item item;

        public ViewHolder(View itemView) {
            super(itemView);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            mItemName = (TextView) itemView.findViewById(R.id.itemName_TextView);
            mItemPack = (TextView) itemView.findViewById(R.id.packUnit_TextView);
            mItemQty = (TextView) itemView.findViewById(R.id.qty_TextView);
            mException = (Button) itemView.findViewById(R.id.exception_button);
            mException.setOnClickListener(this);
            mCheckBox.setOnClickListener(this);
        }

        public void fill(QcList.Item item) {
            this.item = item;
            mItemName.setText(item.getItemName());
            mItemPack.setText(item.getPackName());
            StringBuilder qtyText = new StringBuilder(String.valueOf(item.getQty()));
            if (submitMap.containsKey(item.getBarCode())) {
                BaseQcController.CacheQty cacheQty = submitMap.get(item.getBarCode());
                qtyText.append("  实：" + cacheQty.qty);
                qtyText.append("  残：" + cacheQty.exceptionQty);
                mCheckBox.setChecked(true);
            } else {
                mCheckBox.setChecked(false);
            }
            mItemQty.setText(qtyText.toString());
        }

        @Override
        public void onClick(View v) {
            if (v == mException) {
                if (listener != null) {
                    listener.onExceptionClick(item);
                }
            } else if (v == mCheckBox) {
                if (listener != null) {
                    listener.onItemSelect(item, mCheckBox.isChecked());
                }
            }
        }
    }

    public interface QcManualControllerListener {

        void onItemSelect(QcList.Item item, boolean isSelect);

        void onExceptionClick(QcList.Item item);

    }
}
