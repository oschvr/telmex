package com.wowza.gocoder.sdk.sampleapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UserAreaActivity extends AppCompatActivity {


    ListView lvBrigadasActivas ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        Intent intent = getIntent();
        String nombre = intent.getStringExtra("nombre");
        String usuario = intent.getStringExtra("usuario");

        final TextView tvUsuario = (TextView) findViewById(R.id.tvUsuario);
        final Button bUnirse = (Button)findViewById(R.id.bUnirse);
        final Button bCrear = (Button)findViewById(R.id.bCrear);

        tvUsuario.setText(usuario);

        final JSONArray jArray;

        bUnirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createIntent = new Intent(UserAreaActivity.this, CreateActivity.class);
                UserAreaActivity.this.startActivity(createIntent);
            }
        });

        bCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createIntent = new Intent(UserAreaActivity.this, CreateActivity.class);
                UserAreaActivity.this.startActivity(createIntent);
            }
        });


        // Response received from the server
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.print("JSON RESPONSE"+response);

                try {
                    JSONObject jsonResponse = new JSONObject();
                    JSONArray jsonArray = jsonResponse.getJSONArray(response);

                    ArrayList<String> listdata = new ArrayList<String>();
                    JSONArray jArray = (JSONArray)jsonArray;
                    if (jArray != null) {
                        for (int i=0;i<jArray.length();i++){

                            System.out.print("JSON ARRAI I"+ jArray.get(i));

                            listdata.add(jArray.get(i).toString());
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };



        UserAreaRequest userAreaRequest = new UserAreaRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(UserAreaActivity.this);
        queue.add(userAreaRequest);


    }

    private List toList(JSONArray array) throws JSONException {
        List list = new ArrayList();
        int size = array.length();
        for (int i = 0; i < size; i++) {
            System.out.print("JSON OBJECT TO LIST "+array.get(i));
            list.add(fromJson(array.get(i)));
        }
        return list;
    }

    private Object fromJson(Object json) throws JSONException {
        if (json == JSONObject.NULL) {
            return null;
        } else if (json instanceof JSONObject) {
            return jsonToMap((JSONObject) json);
        } else if (json instanceof JSONArray) {
            return toList((JSONArray) json);
        } else {
            return json;
        }
    }

    public Map<String, String> jsonToMap(JSONObject object) throws JSONException {
        Map<String, String> map = new HashMap();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, fromJson(object.get(key)).toString());
        }
        return map;
    }
}
