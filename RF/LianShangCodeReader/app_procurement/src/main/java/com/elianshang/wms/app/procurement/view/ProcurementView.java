package com.elianshang.wms.app.procurement.view;

/**
 * Created by liuhanzhi on 16/8/15.
 */
public interface ProcurementView {

    void showLocationConfirmView(boolean isIn, String typeName, String taskId, String itemName, String packName, String qty, String locationName);

    void showItemView(String typeName, String itemName, String packName, String qty, String locationName, String numQty);

}
