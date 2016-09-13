package com.elianshang.wms.app.qc.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.qc.bean.QcList;

import org.json.JSONArray;
import org.json.JSONObject;

public class QcListParser extends MasterParser<QcList> {

    @Override
    public QcList parse(JSONObject data) throws Exception {

        if (data != null) {
            int qcType = optInt(data , "qcType");
            if(qcType != 1 && qcType != 2){
                qcType = 2 ;
            }

            JSONArray jsonArray = optJSONArray(data, "qcList");
            int len = getLength(jsonArray);

            if (len > 0) {
                QcList qcList = new QcList();
                qcList.setQcType(qcType);
                for (int i = 0; i < len; i++) {
                    JSONObject jo = optJSONObject(jsonArray, i);
                    if (jo != null) {
                        String itemName = optString(jo, "itemName");
                        String barCode = optString(jo, "code");
                        String qty = optString(jo, "pickQty");
                        String packName = optString(jo, "packName");

                        if (!TextUtils.isEmpty(itemName) && !TextUtils.isEmpty(barCode) && !TextUtils.isEmpty(packName)) {
                            QcList.Item item = new QcList.Item();

                            item.setBarCode(barCode);
                            item.setItemName(itemName);
                            item.setQty(qty);
                            item.setPackName(packName);

                            qcList.add(item);
                        }
                    }

                }
                return qcList;
            }
        }
        return null;
    }
}
