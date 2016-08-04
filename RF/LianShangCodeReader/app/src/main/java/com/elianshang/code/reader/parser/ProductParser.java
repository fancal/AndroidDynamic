package com.elianshang.code.reader.parser;

import com.elianshang.code.reader.bean.Product;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by liuhanzhi on 16/8/3.
 */
public class ProductParser extends MasterParser<Product> {
    @Override
    public Product parse(JSONObject data) throws Exception {
        Product product = null;
        if(data != null){
            product = new Product();
            product.setName(optString(data,"name"));
            product.setItemId(optString(data,"itemId"));

            String packName = optString(data,"packName");
            ArrayList<String> packList = new ArrayList<>();
            packList.add(packName);
            packList.add(packName);
            packList.add(packName);
            product.setPackName(packList);
        }
        return product;
    }
}
