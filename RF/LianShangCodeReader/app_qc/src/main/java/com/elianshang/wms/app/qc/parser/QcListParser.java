package com.elianshang.wms.app.qc.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.qc.bean.QcList;

import org.json.JSONArray;
import org.json.JSONObject;

public class QcListParser extends MasterParser<QcList> {

    @Override
    public QcList parse(JSONObject data) throws Exception {
        QcList qcList = null;
        QcList scatteredList = null;
        if (data != null) {
            qcList = new QcList();
            scatteredList = new QcList();

            String qcTaskId = optString(data, "qcTaskId");
            String customerName = optString(data, "customerName");
            String customerCode = optString(data, "customerCode");
            String collectionRoadCode = optString(data, "collectionRoadCode");
            String containerType = optString(data, "containerType");
            boolean qcTaskDone = optBoolean(data, "qcTaskDone");
            boolean isFirst = optBoolean(data, "isFristQc");
            String itemBoxNum = optString(data, "itemBoxNum");
            String allBoxNum = optString(data, "allBoxNum");
            String turnoverBoxNum = optString(data, "turnoverBoxNum");
            String itemLineNum = optString(data, "itemLineNum");
            String containerId = optString(data, "containerId");

            if (!TextUtils.isEmpty(qcTaskId)
                    && !TextUtils.isEmpty(customerName)
                    && !TextUtils.isEmpty(customerCode)
                    && !TextUtils.isEmpty(containerType)
                    && !TextUtils.isEmpty(itemBoxNum)
                    && !TextUtils.isEmpty(itemLineNum)
                    && !TextUtils.isEmpty(collectionRoadCode)) {
                qcList.setQcTaskId(qcTaskId);
                qcList.setCustomerName(customerName);
                qcList.setCustomerCode(customerCode);
                qcList.setContainerType(containerType);
                qcList.setQcDone(qcTaskDone);
                qcList.setFirst(isFirst);
                qcList.setItemBoxNum(itemBoxNum);
                qcList.setAllBoxNum(allBoxNum);
                qcList.setTurnoverBoxNum(turnoverBoxNum);
                qcList.setItemLineNum(itemLineNum);
                qcList.setCollectionRoadCode(collectionRoadCode);
                qcList.setContainerId(containerId);
            }

            JSONArray jsonArray = optJSONArray(data, "qcList");
            int len = getLength(jsonArray);

            if (len > 0) {
                for (int i = 0; i < len; i++) {
                    JSONObject jo = optJSONObject(jsonArray, i);
                    if (jo != null) {
                        boolean qcDone = optBoolean(jo, "qcDone");
                        boolean isFirstQc = optBoolean(jo, "isFristTime");
                        String itemName = optString(jo, "itemName");
                        String packName = optString(jo, "uom");
                        String skuId = optString(jo, "skuId");
                        String skuCode = optString(jo, "skuCode");
                        String codeType = optString(jo, "codeType");
                        String barCode = optString(jo, "code");
                        String itemId = optString(jo, "itemId");
                        String uomQty = optString(jo, "uomQty");
                        boolean isSplit = optBoolean(jo, "isSplit");

                        if (!TextUtils.isEmpty(itemName)
                                && !TextUtils.isEmpty(packName)
                                && !TextUtils.isEmpty(skuId)
                                && !TextUtils.isEmpty(codeType)
                                && !TextUtils.isEmpty(barCode)
                                && !TextUtils.isEmpty(itemId)
                                && !TextUtils.isEmpty(uomQty)) {

                            QcList.Item item = new QcList.Item();
                            item.setQcDone(qcDone);
                            item.setFirst(isFirstQc);
                            item.setItemName(itemName);
                            item.setPackName(packName);
                            item.setSkuId(skuId);
                            item.setCodeType(codeType);
                            item.setBarCode(barCode);
                            item.setItemId(itemId);
                            item.setUomQty(uomQty);
                            item.setSplit(isSplit);
                            item.setSkuCode(skuCode);

                            if (isSplit) {
                                scatteredList.add(item);
                            } else {
                                qcList.add(item);
                            }
                        }
                    }

                }

                qcList.addAll(scatteredList);

                return qcList;
            }
        }
        return null;
    }

}
