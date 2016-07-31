package com.wowza.gocoder.sdk.sampleapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.VideoView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CreateActivity extends AppCompatActivity  {


    String lat;
    String longui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);


        final EditText etNombre = (EditText) findViewById(R.id.etNombreBrigada);
        final EditText etDescripcion = (EditText) findViewById(R.id.etDescripcion);
        final EditText etCategoria = (EditText) findViewById(R.id.etCategoria);
        final Button bCrear = (Button) findViewById(R.id.bCrear);

        //Cambia por mapa
        VideoView videoView = (VideoView) findViewById(R.id.videoView);

        videoView.setVideoPath("http://vectorthree.com/fuck/fuck.webm");

        videoView.start();

        bCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nombre_evento = etNombre.getText().toString();
                final String descripcion = etDescripcion.getText().toString();
                final String categoria = etCategoria.getText().toString();

                lat = "19.414481";
                longui = "99.168917";

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {

                                //Create INTENT
                                Intent intent = new Intent(CreateActivity.this, CameraActivity.class);
                                CreateActivity.this.startActivity(intent);

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CreateActivity.this);
                                builder.setMessage("Creation Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                CreateRequest createRequest = new CreateRequest(nombre_evento, descripcion, categoria, lat, longui, responseListener);
                RequestQueue queue = Volley.newRequestQueue(CreateActivity.this);
                queue.add(createRequest);


            }
        });
    }





}