package com.elianshang.wms.app.back.parser;


import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.back.bean.SupplierInfo;
import com.xue.http.exception.DataIsErrException;
import com.xue.http.exception.DataIsNullException;
import com.xue.http.exception.DataNoUpdateException;
import com.xue.http.exception.JsonCanNotParseException;
import com.xue.http.exception.ParseException;

import org.json.JSONObject;

public class SupplierInfoParser extends MasterParser<SupplierInfo> {

    @Override
    public SupplierInfo parse(JSONObject data) throws Exception {
        SupplierInfo supplierInfo = null;
        if (data != null) {
            String taskId = optString(data,"taskId");
            String supplierName = optString(data,"supplierName");
            String locationCode = optString(data,"locationCode");
            if(!TextUtils.isEmpty(taskId) && !TextUtils.isEmpty(supplierName) && !TextUtils.isEmpty(locationCode)){
                supplierInfo = new SupplierInfo();
                supplierInfo.setTaskId(taskId);
                supplierInfo.setSupplierName(supplierName);
                supplierInfo.setLocationCode(locationCode);
            }
        }
        return supplierInfo;
    }

    @Override
    public SupplierInfo initialParse(String data) throws JsonCanNotParseException, DataIsNullException, ParseException, DataIsErrException, DataNoUpdateException {
        data = "{\n" +
                "\"head\": {\n" +
                "\"status\": 1,\n" +
                "\"message\": \"success.\",\n" +
                "\"timestamp\":\"20160801142217\"\n" +
                "},\n" +
                "\"body\":\n" +
                "{\n" +
                "\"taskId\":\"12131231231241\",\n" +
                "\"supplierName\":\"这是供商名称\",\n" +
                "\"locationCode\":\"111111\"\n" +
                "}\n" +
                "}";
        return super.initialParse(data);
    }

}
