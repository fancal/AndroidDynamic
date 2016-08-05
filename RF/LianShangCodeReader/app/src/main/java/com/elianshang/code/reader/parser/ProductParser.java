package com.elianshang.code.reader.parser;

import com.elianshang.code.reader.bean.Product;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by liuhanzhi on 16/8/3.
 */
public class ProductParser extends MasterParser<Product> {
    @Override
    public Product parse(JSONObject data) throws Exception {
        Product product = null;
        if (data != null) {
            product = new Product();
            product.setName(optString(data, "name"));
            product.setItemId(optString(data, "itemId"));

            JSONArray jsonArray = optJSONArray(data, "packName");
            int length = getLength(jsonArray);
            if (length > 0) {
                ArrayList<String> packList = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    packList.add(optString(jsonArray, i));
                }
                product.setPackNameList(packList);
            }

        }
        return product;
    }
}
