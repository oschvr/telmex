package com.wowza.gocoder.sdk.sampleapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import android.os.Debug;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UserAreaRequest extends StringRequest {
    private static final String BRIGADE_REQUEST_URL = "http://www.vectorthree.com/telmex/BrigadasActivas.php";
    private Map<String, String> params;

    public UserAreaRequest(Response.Listener<String> listener) {
        super(Method.GET, BRIGADE_REQUEST_URL, listener, null);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
