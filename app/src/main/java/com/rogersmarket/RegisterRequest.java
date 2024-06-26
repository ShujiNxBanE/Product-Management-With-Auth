package com.rogersmarket;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL="http://10.0.164.63/roger_market_db/register_app.php";
    private final Map<String,String> params;
    public RegisterRequest(String name, String username, String password, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("username", username);
        params.put("password", password);
    }
    @Override
    public Map<String, String> getParams(){
        return params;
    }
}
