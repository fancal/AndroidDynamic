package com.elianshang.wms.app.procurement.view;

/**
 * Created by liuhanzhi on 16/8/15.
 */
public interface ProcurementView {

    void showDetailView(String taskId ,String itemName, String barcode, String skuCode, String packName, String qty, String fromLocationCode, String toLocationCode);

    void showLocationConfirmView(boolean isIn, String typeName, String taskId, String itemName, String barcode, String skuCode, String packName, String qty, String locationName);

    void showItemView(String typeName, String itemName, String barcode, String skuCode, String packName, String qty, String locationName, String numQty);

}
