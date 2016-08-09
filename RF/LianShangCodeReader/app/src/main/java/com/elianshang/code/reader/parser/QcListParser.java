package com.elianshang.code.reader.parser;

import android.text.TextUtils;

import com.elianshang.code.reader.bean.QcList;

import org.json.JSONArray;
import org.json.JSONObject;

public class QcListParser extends MasterParser<QcList> {

    @Override
    public QcList parse(JSONObject data) throws Exception {

        if (data != null) {
            JSONArray jsonArray = optJSONArray(data, "qcList");
            int len = getLength(jsonArray);

            if (len > 0) {
                QcList qcList = new QcList();
                for (int i = 0; i < len; i++) {
                    JSONObject jo = optJSONObject(jsonArray, i);
                    if (jo != null) {

                        String itemName = optString(jo, "itemName");
                        String barCode = optString(jo, "code");
                        float qty = optFloat(jo, "pickQty");
                        String packName = optString(jo, "packName");

                        if (!TextUtils.isEmpty(itemName) && !TextUtils.isEmpty(barCode) && qty > 0 && !TextUtils.isEmpty(packName)) {
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
