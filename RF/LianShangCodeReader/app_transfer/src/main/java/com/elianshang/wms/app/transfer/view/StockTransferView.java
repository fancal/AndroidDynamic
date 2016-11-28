package com.elianshang.wms.app.transfer.view;

/**
 * Created by liuhanzhi on 16/8/15.
 */
public interface StockTransferView {

    void showLocationConfirmView(boolean isIn, String typeName, String taskId, String itemName, String packName, String qty, String locationName);

    void showItemView(String typeName, String itemName, String barcode, String owner, String packName, String qty, String locationName, String unmQty, String needBarcode, String needOwner);

    void showScanLayout();
}
