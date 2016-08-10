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

    public void fillDetailData(String progress, String itemName, String itemPackName, String itemQty, String inputQty, String inputQtyHint, String shoddyQty, String shoddyQtyHint) {
        waitLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.VISIBLE);

        detailProgressTextView.setText(progress);
        detailItemNameTextView.setText(itemName);
        detailPackNameTextView.setText(itemPackName);
        detailQtyTextView.setText(itemQty);
        detailInputQtyEditText.setHint(inputQtyHint);
        detailInputQtyEditText.setText(inputQty);
        detailShoddynQtyEditText.setHint(shoddyQtyHint);
        detailShoddynQtyEditText.setText(shoddyQty);

        detailInputQtyEditText.requestFocus();
    }

    public String inputQtyText() {
        if (detailInputQtyEditText == null || detailInputQtyEditText.getText() == null) {
            return null;
        }
        return detailInputQtyEditText.getText().toString();
    }

    public String inputQtyHintText() {
        if (detailInputQtyEditText == null || detailInputQtyEditText.getHint() == null) {
            return null;
        }
        return detailInputQtyEditText.getHint().toString();
    }

    public String shoddyQtyText() {
        if (detailShoddynQtyEditText == null || detailShoddynQtyEditText.getText() == null) {
            return null;
        }
        return detailShoddynQtyEditText.getText().toString();
    }

    public String shoddyQtyHintText() {
        if (detailShoddynQtyEditText == null || detailShoddynQtyEditText.getHint() == null) {
            return null;
        }
        return detailShoddynQtyEditText.getHint().toString();
    }

}
