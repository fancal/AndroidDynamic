package com.elianshang.code.reader.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elianshang.code.reader.R;
import com.elianshang.code.reader.bean.QcList;
import com.elianshang.code.reader.tool.BaseQcController;

import java.util.HashMap;

/**
 * Created by liuhanzhi on 16/8/9.
 */
public class QcScanView extends LinearLayout {

    public View waitLayout;

    private View detailLayout;

    private TextView detailProgressTextView;

    private TextView detailItemNameTextView;

    private TextView detailPackNameTextView;

    private TextView detailQtyTextView;

    private ContentEditText detailInputQtyEditText;

    private ContentEditText detailShoddynQtyEditText;

    private HashMap<String, BaseQcController.CacheQty> submitMap;

    private QcList qcList;

    public QcScanView(Context context) {
        super(context);
        init();
    }

    public QcScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QcScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public QcScanView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.qualitycontrol_scan, this, true);
        waitLayout = findViewById(R.id.wait_Layout);
        detailLayout = findViewById(R.id.detail_Layout);
        detailProgressTextView = (TextView) detailLayout.findViewById(R.id.progress_TextView);
        detailItemNameTextView = (TextView) detailLayout.findViewById(R.id.itemName_TextView);
        detailPackNameTextView = (TextView) detailLayout.findViewById(R.id.packName_TextView);
        detailQtyTextView = (TextView) detailLayout.findViewById(R.id.qty_TextView);
        detailInputQtyEditText = (ContentEditText) detailLayout.findViewById(R.id.inputQty_EditView);
        detailShoddynQtyEditText = (ContentEditText) detailLayout.findViewById(R.id.shoddyQty_EditView);

    }

    public void fill(QcList qcList, HashMap<String, BaseQcController.CacheQty> submitMap) {
        this.submitMap = submitMap;
        this.qcList = qcList;
        waitLayout.setVisibility(VISIBLE);
    }

    public void fillDetailData(QcList.Item item) {
        int progress = 0;
        if (submitMap.containsKey(item.getBarCode())) {
            progress = submitMap.size();
        } else {
            progress = submitMap.size() + 1;
        }
        detailProgressTextView.setText(progress + "/" + qcList.size());

        detailLayout.setVisibility(View.VISIBLE);
        waitLayout.setVisibility(View.GONE);

        detailItemNameTextView.setText(item.getItemName());
        detailPackNameTextView.setText(item.getPackName());
        detailQtyTextView.setText("" + item.getQty());

        if (submitMap.containsKey(item.getBarCode())) {
            BaseQcController.CacheQty cacheQty = submitMap.get(item.getBarCode());
            detailInputQtyEditText.setHint(null);
            detailInputQtyEditText.setText(String.valueOf(cacheQty.qty));
            detailShoddynQtyEditText.setHint(null);
            detailShoddynQtyEditText.setText(String.valueOf(cacheQty.exceptionQty));
        } else {
            detailInputQtyEditText.setText(null);
            detailInputQtyEditText.setHint(String.valueOf(item.getQty()));
            detailShoddynQtyEditText.setText(null);
            detailShoddynQtyEditText.setHint("0");
        }
        detailInputQtyEditText.requestFocus();
    }

    /**
     * 填充QC列表不存在的商品
     */
    public void fillDetailDataNull(String barCode) {
        int progress = 0;
        if (submitMap.containsKey(barCode)) {
            progress = submitMap.size();
        } else {
            progress = submitMap.size() + 1;
        }
        detailProgressTextView.setText(progress + "/" + qcList.size());

        waitLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.VISIBLE);

        detailItemNameTextView.setText("错品(" + barCode + ")");
        detailPackNameTextView.setText("请按EA查点数量");
        detailQtyTextView.setText("如果是误扫,请输入0,或不要输入数量");


        if (submitMap.containsKey(barCode)) {
            BaseQcController.CacheQty cacheQty = submitMap.get(barCode);
            detailInputQtyEditText.setHint(null);
            detailInputQtyEditText.setText(String.valueOf(cacheQty.qty));
            detailShoddynQtyEditText.setHint(null);
            detailShoddynQtyEditText.setText(String.valueOf(cacheQty.exceptionQty));
        } else {
            detailInputQtyEditText.setText(null);
            detailInputQtyEditText.setHint("1");
            detailShoddynQtyEditText.setText(null);
            detailShoddynQtyEditText.setHint("0");
        }
        detailInputQtyEditText.requestFocus();
    }

    public String inputQtyText() {
        return detailInputQtyEditText.getText().toString();
    }

    public String inputQtyHintText() {
        if (detailInputQtyEditText == null || detailInputQtyEditText.getHint() == null) {
            return null;
        }
        return detailInputQtyEditText.getHint().toString();
    }

    public String shoddyQtyText() {
        return detailShoddynQtyEditText.getText().toString();
    }

    public String shoddyQtyHintText() {
        if (detailShoddynQtyEditText == null || detailShoddynQtyEditText.getHint() == null) {
            return null;
        }
        return detailShoddynQtyEditText.getHint().toString();
    }

}
