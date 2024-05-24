package com.rogersmarket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WSJsonProducts extends WSJson {
    private static final String SERVER = "10.0.2.2";

    public static Products getProducts() throws IOException, JSONException {
        Products result = new Products();

        JSONObject jsonObj = getJson("http://" + SERVER + "/roger_market_db/get_products.php");

        if (jsonObj != null) {
            JSONArray products = jsonObj.getJSONArray("products");

            for (int i = 0; i < products.length(); i++) {
                JSONObject c = products.getJSONObject(i);

                Product temp = new Product();
                temp.setId(c.getInt("id"));
                temp.setName(c.getString("name"));
                temp.setAmount(c.getString("amount"));
                temp.setPrice(c.getString("price"));
                temp.setType(c.getString("type"));
                temp.setProvider(c.getString("provider"));

                result.add(temp);
            }
        } else {
            throw new IOException("No response from server");
        }
        return result;
    }

    public static boolean insertProduct(Product product) throws IOException, JSONException {
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("name", product.getName());
        jsonParam.put("amount", product.getAmount());
        jsonParam.put("price", product.getPrice());
        jsonParam.put("type", product.getType());
        jsonParam.put("provider", product.getProvider());

        JSONObject jsonResult = sendJson("http://" + SERVER + "/roger_market_db/insert_products.php", jsonParam);

        if (jsonResult == null) return false;

        String estado = jsonResult.getString("estado");

        return estado.compareTo("1") == 0;
    }
}
