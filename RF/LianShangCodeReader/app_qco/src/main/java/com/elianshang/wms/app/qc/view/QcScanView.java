package com.elianshang.wms.app.qc.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elianshang.bridge.ui.view.QtyEditText;
import com.elianshang.wms.app.qc.R;

public class QcScanView extends LinearLayout {

    public View waitLayout;

    private View detailLayout;

    private TextView detailProgressTextView;

    private TextView detailItemNameTextView;

    private TextView detailPackNameTextView;

    private TextView detailQtyTextView;

    private QtyEditText detailInputQtyEditText;

    private QtyEditText detailShoddynQtyEditText;

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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
        detailInputQtyEditText = (QtyEditText) detailLayout.findViewById(R.id.inputQty_EditView);
        detailShoddynQtyEditText = (QtyEditText) detailLayout.findViewById(R.id.shoddyQty_EditView);

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

    public String getInputQtyValue() {
        return detailInputQtyEditText.getValue();
    }

    public String getShoddyQtyValue() {
        return detailShoddynQtyEditText.getValue();
    }

}
