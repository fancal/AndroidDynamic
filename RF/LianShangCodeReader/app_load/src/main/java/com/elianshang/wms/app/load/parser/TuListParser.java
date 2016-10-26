package com.elianshang.wms.app.load.parser;

import android.text.TextUtils;

import com.elianshang.bridge.parser.MasterParser;
import com.elianshang.wms.app.load.bean.TuList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by liuhanzhi on 16/10/25.
 */

public class TuListParser extends MasterParser<TuList> {

    @Override
    public TuList parse(JSONObject data) throws Exception {
        TuList tuList = null;
        if (data != null) {
            JSONArray jsonArray = optJSONArray(data, "result");
            int length = getLength(jsonArray);
            if (length > 0) {
                tuList = new TuList();
                for (int i = 0; i < length; i++) {
                    JSONObject jsonObject = optJSONObject(jsonArray,i);
                    TuList.Item item = parserTuListItem(jsonObject);
                    if(item != null){
                        tuList.add(item);
                    }
                }
            }
        }
        return tuList;
    }

    public TuList.Item parserTuListItem(JSONObject data) {
        TuList.Item item = null;
        if (data != null) {
            try {
                String tu = optString(data, "tu");
                String carNumber = optString(data, "carNumber");
                String preBoard = optString(data, "preBoard");
                String number = optString(data, "number");
                String cellphone = optString(data, "cellphone");
                if (!TextUtils.isEmpty(tu)) {
                    item = new TuList.Item();
                    item.setTu(tu);
                    item.setCarNumber(carNumber);
                    item.setPreBoard(preBoard);
                    item.setNumber(number);
                    item.setCellphone(cellphone);
                    JSONArray jsonArray = optJSONArray(data,"stores");
                    int length = getLength(jsonArray);
                    if(length > 0){
                        ArrayList<TuList.Item.Store> stores = new ArrayList<>();
                        for (int i = 0; i < length; i++) {
                            TuList.Item.Store store = parserItemStore(optJSONObject(jsonArray,i));
                            if(store != null){
                                stores.add(store);
                            }
                        }
                        item.setStores(stores);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return item;
    }

    private TuList.Item.Store parserItemStore(JSONObject data) {
        TuList.Item.Store store = null;
        if (data != null) {
            try {
                String storeId = optString(data, "storeId");
                String storeName = optString(data, "storeName");
                String storeNo = optString(data, "storeNo");
                if (!TextUtils.isEmpty(storeId)) {
                    store = new TuList.Item.Store();
                    store.setStoreId(storeId);
                    store.setStoreNo(storeNo);
                    store.setStoreName(storeName);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return store;
    }


//    @Override
//    public TuList initialParse(String data) throws JsonCanNotParseException, DataIsNullException, ParseException, DataIsErrException, DataNoUpdateException {
//        data = "{\"head\":{\"status\":1,\"message\":\"success.\",\"timestamp\":\"20161026193233\"},\"body\":{\"tuList\":[{\"tu\":\"1111\",\"cellphone\":\"123456789011\",\"carNumber\":\"京N12132\",\"preBoard\":10,\"number\":1,\"stores\":[{\"storeNo\":\"6486\",\"storeName\":\"jlh001店\",\"storeId\":1},{\"storeNo\":\"6485\",\"storeName\":\"ljh店\",\"storeId\":7}],\"driverName\":\"老张\"},{\"tu\":\"1112\",\"cellphone\":\"123456789011\",\"carNumber\":\"京N12132\",\"preBoard\":10,\"number\":2,\"stores\":[{\"storeNo\":\"6486\",\"storeName\":\"jlh001店\",\"storeId\":1},{\"storeNo\":\"6485\",\"storeName\":\"ljh店\",\"storeId\":7}],\"driverName\":\"老张\"},{\"tu\":\"1113\",\"cellphone\":\"123456789011\",\"carNumber\":\"京N12132\",\"preBoard\":10,\"number\":3,\"stores\":[{\"storeNo\":\"6486\",\"storeName\":\"jlh001店\",\"storeId\":1},{\"storeNo\":\"6485\",\"storeName\":\"ljh店\",\"storeId\":7}],\"driverName\":\"老张\"}]}}";
//        return super.initialParse(data);
//    }
}
