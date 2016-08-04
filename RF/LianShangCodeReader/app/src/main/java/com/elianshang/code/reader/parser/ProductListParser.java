package com.elianshang.code.reader.parser;

import com.elianshang.code.reader.bean.Product;
import com.elianshang.code.reader.bean.ProductList;

import org.json.JSONArray;
import org.json.JSONException;
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
            JSONArray jsonArray = optJSONArray(data, "products");
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

    @Override
    protected JSONObject getData(String data) throws JSONException {
        data = "{\n" +
                "  \"head\": {\n" +
                "    \"status\": 1,\n" +
                "    \"message\": \"success.\",\n" +
                "    \"timestamp\": \"20160803183850\"\n" +
                "  },\n" +
                "  \"body\": {\n" +
                "    \"products\":[\n" +
                "    {\n" +
                "      \"name\": \"花千骨冰红茶500ml\",\n" +
                "      \"packName\": \"H24\",\n" +
                "      \"itemId\": 211346750730108\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"茶π\",\n" +
                "      \"packName\": \"H24\",\n" +
                "      \"itemId\": 223888055232904\n" +
                "    }\n" +
                "  ]\n" +
                "  } \n" +
                "}";
        return super.getData(data);
    }


}
