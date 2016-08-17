package com.elianshang.code.reader.ui.view;

/**
 * Created by liuhanzhi on 16/8/15.
 */
public interface TransferView {

    void showLocationConfirmView(String typeName, String taskId, String locationName);

    void showItemView(String typeName, String itemName, String packName, String qty, String locationName);

}
