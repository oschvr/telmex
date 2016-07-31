package com.wowza.gocoder.sdk.sampleapp;

import android.os.Debug;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://www.vectorthree.com/telmex/Register.php";
    private Map<String, String> params;


    public RegisterRequest(String usuario, String contra, String nombre, String paterno, String materno, int edad, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("usuario", usuario+"");
        params.put("contra", contra);
        params.put("nombre", nombre);
        params.put("paterno", paterno);
        params.put("materno", materno);
        params.put("edad", edad + "");

        System.out.print("TESTTTTTTTT:"+params);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
