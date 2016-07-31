package com.wowza.gocoder.sdk.sampleapp.users;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.wowza.gocoder.sdk.sampleapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        final EditText etUsuario = (EditText) findViewById(R.id.etUsername);
        final EditText etContra = (EditText) findViewById(R.id.etPassword);

        final EditText etNombre = (EditText) findViewById(R.id.etName);
        final EditText etPaterno = (EditText) findViewById(R.id.etPaterno);
        final EditText etMaterno = (EditText) findViewById(R.id.etMaterno);
        final EditText etEdad = (EditText) findViewById(R.id.etAge);

        final Button bRegistrar = (Button) findViewById(R.id.bRegister);

        bRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usuario = etUsuario.getText().toString();
                final String contra = etContra.getText().toString();

                final String nombre = etNombre.getText().toString();
                final String paterno = etPaterno.getText().toString();
                final String materno = etMaterno.getText().toString();
                final int edad = Integer.parseInt(etEdad.getText().toString());



                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                RegisterActivity.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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

                RegisterRequest registerRequest = new RegisterRequest(usuario, contra, nombre, paterno, materno, edad, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });
    }
}