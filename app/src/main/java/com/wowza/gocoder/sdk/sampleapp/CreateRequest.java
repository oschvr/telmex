package com.wowza.gocoder.sdk.sampleapp;

import android.os.Debug;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateRequest extends StringRequest {
    private static final String CREATE_REQUEST_URL = "http://www.vectorthree.com/telmex/CrearEvento.php";
    private Map<String, String> params;


    public CreateRequest(String nombre_evento, String descripcion, String categoria, String lat, String longui, Response.Listener<String> listener) {
        super(Method.POST, CREATE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("nombre_evento", nombre_evento+"");
        params.put("descripcion", descripcion);
        params.put("categoria", categoria);
        params.put("lat", lat + "");
        params.put("longui", longui + "");

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
