package com.elianshang.wms.app.load.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elianshang.wms.app.load.R;
import com.elianshang.wms.app.load.bean.TuList;

/**
 * Created by liuhanzhi on 16/10/26.
 */

public class TuDetailView extends LinearLayout implements View.OnClickListener {

    private TextView tuTextView;

    private TextView carNumberTextView;

    private TextView driverNameTextView;

    private TextView preBoardTextView;

    private TextView storesTextView;

    private Button nextButton;

    private TuList.Item item;

    private OnNextButtonClick onNextButtonClick;

    public TuDetailView(Context context) {
        super(context);
        initLayout();
    }

    public TuDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public TuDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
    }

    private void initLayout() {
        LayoutInflater.from(getContext()).inflate(R.layout.tu_detail, this, true);
        findView();
    }

    private void findView() {
        tuTextView = (TextView) findViewById(R.id.tu_TextView);
        carNumberTextView = (TextView) findViewById(R.id.carNumber_TextView);
        driverNameTextView = (TextView) findViewById(R.id.driverName_TextView);
        preBoardTextView = (TextView) findViewById(R.id.preBoard_TextView);
        storesTextView = (TextView) findViewById(R.id.stores_TextView);
        nextButton = (Button) findViewById(R.id.next_Button);

        nextButton.setOnClickListener(this);

    }

    public void fillTuDetail(TuList.Item item) {
        if (item == null) {
            return;
        }
        this.item = item;
        tuTextView.setText("TU号:" + item.getTu());
        carNumberTextView.setText("车牌号:" + item.getCarNumber());
        driverNameTextView.setText("司机电话:" + item.getCellphone());
        preBoardTextView.setText("预装版数:" + item.getPreBoard());
        if (item.getStores() != null && item.getStores().size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < item.getStores().size(); i++) {
                TuList.Item.Store store = item.getStores().get(i);
                sb.append("门店");
                sb.append(String.valueOf(i + 1));
                sb.append(":");
                sb.append("【");
                sb.append(store.getStoreNo());
                sb.append("】");
                if (i != item.getStores().size() - 1) {
                    sb.append("\r\n");
                }
            }
            storesTextView.setText(sb.toString());
        }
    }

    public OnNextButtonClick getOnNextButtonClick() {
        return onNextButtonClick;
    }

    public void setOnNextButtonClick(OnNextButtonClick onNextButtonClick) {
        this.onNextButtonClick = onNextButtonClick;
    }

    @Override
    public void onClick(View v) {
        if (v == nextButton) {
            if (onNextButtonClick != null) {
                onNextButtonClick.onNextClick(item);
            }
        }
    }

    public interface OnNextButtonClick {
        void onNextClick(TuList.Item item);
    }

}
