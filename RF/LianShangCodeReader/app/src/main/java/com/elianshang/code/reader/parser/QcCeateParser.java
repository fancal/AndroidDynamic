package com.elianshang.code.reader.parser;

import com.elianshang.code.reader.bean.QcCreate;

import org.json.JSONObject;

//测试使用
public class QcCeateParser extends MasterParser<QcCreate> {

    @Override
    public QcCreate parse(JSONObject data) throws Exception {

        return new QcCreate();
//        if (data != null) {
//            String containerId = getString(data, "containerId");
//
//            if (!TextUtils.isEmpty(containerId)) {
//                QcCreate qcCreate = new QcCreate();
//                qcCreate.setContainerId(containerId);
//                return qcCreate;
//            }
//        }
//        return null;
    }


}
