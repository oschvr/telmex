package com.wowza.gocoder.sdk.sampleapp.users;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.wowza.gocoder.sdk.sampleapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UserAreaActivity extends Activity {


    ArrayList<HashMap<String, String>> jsonlist = new ArrayList<HashMap<String, String>>();
    ListView lvBrigadasActivas ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        Intent intent = getIntent();
        String nombre = intent.getStringExtra("nombre");
        String usuario = intent.getStringExtra("usuario");
        String paterno = intent.getStringExtra("paterno");
        int edad = intent.getIntExtra("edad", -1);


        final TextView tvUsuario = (TextView) findViewById(R.id.tvUsuario);
        final TextView tvNombre = (TextView) findViewById(R.id.tvNombre);


        tvUsuario.setText(usuario);
        tvNombre.setText(nombre);

        // Display user details
        //String message = name + " welcome to your user area";
        //tvWelcomeMsg.setText(message);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {


                        //Intent intent = new Intent(UserAreaActivity.this, LoginActivity.class);
                        //UserAreaActivity.this.startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserAreaActivity.this);
                        builder.setMessage("Register Failed")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        BrigadeRequest brigadeRequest = new BrigadeRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(UserAreaActivity.this);
        queue.add(brigadeRequest);


    }
}
