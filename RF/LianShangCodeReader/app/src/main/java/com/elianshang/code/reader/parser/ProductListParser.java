package com.elianshang.code.reader.parser;

import com.elianshang.code.reader.bean.Product;
import com.elianshang.code.reader.bean.ProductList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by liuhanzhi on 16/8/3.
 */
public class ProductListParser extends MasterParser<ProductList> {
    @Override
    public ProductList parse(JSONObject data) throws Exception {
        ProductList products = null;
        if (data != null) {
            products = new ProductList();
            ProductParser productParser = new ProductParser();
            JSONArray jsonArray = optJSONArray(data, "list");
            int length = getLength(jsonArray);
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    Product product = productParser.parse(optJSONObject(jsonArray, i));
                    if (product != null) {
                        products.add(product);
                    }
                }
            }
            if (products.size() == 0) {
                products = null;
            }
        }
        return products;
    }

}
