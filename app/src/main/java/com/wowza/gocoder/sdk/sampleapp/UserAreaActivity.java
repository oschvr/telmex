package com.wowza.gocoder.sdk.sampleapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UserAreaActivity extends AppCompatActivity implements  SeekBar.OnSeekBarChangeListener,
        GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener, OnMapReadyCallback {


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

        ///MAP
        mColorBar = (SeekBar) findViewById(R.id.hueSeekBar);
        mColorBar.setMax(HUE_MAX);
        mColorBar.setProgress(0);

        mAlphaBar = (SeekBar) findViewById(R.id.alphaSeekBar);
        mAlphaBar.setMax(ALPHA_MAX);
        mAlphaBar.setProgress(127);

        mWidthBar = (SeekBar) findViewById(R.id.widthSeekBar);
        mWidthBar.setMax(WIDTH_MAX);
        mWidthBar.setProgress(10);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mClickabilityCheckbox = (CheckBox) findViewById(R.id.toggleClickability);







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

    /**************MAPS *************/

    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private static  final LatLng MEXICO = new LatLng(19.432698,-99.133207 );
    private static  final LatLng DELVALLE = new LatLng(19.382858, -99.165076);
    private static  final LatLng TEPITO = new LatLng(19.446663, -99.129392);
    private static  final LatLng AZCAPOTZALCO = new LatLng(19.479983, -99.186903);









    //private static final double DEFAULT_RADIUS = 1000000;
    private static final double DEFAULT_RADIUS = 100;

    public static final double RADIUS_OF_EARTH_METERS = 6371009;

    private static final int WIDTH_MAX = 50;
    private static final int HUE_MAX = 360;
    private static final int ALPHA_MAX = 255;

    private GoogleMap mMap;

    private List<DraggableCircle> mCircles = new ArrayList<DraggableCircle>(1);

    private SeekBar mColorBar;
    private SeekBar mAlphaBar;
    private SeekBar mWidthBar;
    private int mStrokeColor;
    private int mFillColor;
    private CheckBox mClickabilityCheckbox;

    private class DraggableCircle {
        private final Marker centerMarker;
        private final Marker radiusMarker;
        private final Circle circle;
        private double radius;
        public DraggableCircle(LatLng center, double radius, boolean clickable) {
            this.radius = radius;
            centerMarker = mMap.addMarker(new MarkerOptions()
                    .position(center)
                    .draggable(true));
            radiusMarker = mMap.addMarker(new MarkerOptions()
                    .position(toRadiusLatLng(center, radius))
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_AZURE)));
            circle = mMap.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radius)
                    .strokeWidth(mWidthBar.getProgress())
                    .strokeColor(mStrokeColor)
                    .fillColor(mFillColor)
                    .clickable(clickable));
        }

        public DraggableCircle(LatLng center, LatLng radiusLatLng, boolean clickable) {
            this.radius = toRadiusMeters(center, radiusLatLng);
            centerMarker = mMap.addMarker(new MarkerOptions()
                    .position(center)
                    .draggable(true));
            radiusMarker = mMap.addMarker(new MarkerOptions()
                    .position(radiusLatLng)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_AZURE)));
            circle = mMap.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radius)
                    .strokeWidth(mWidthBar.getProgress())
                    .strokeColor(mStrokeColor)
                    .fillColor(mFillColor)
                    .clickable(clickable));
        }

        public boolean onMarkerMoved(Marker marker) {
            if (marker.equals(centerMarker)) {
                circle.setCenter(marker.getPosition());
                radiusMarker.setPosition(toRadiusLatLng(marker.getPosition(), radius));
                return true;
            }
            if (marker.equals(radiusMarker)) {
                radius = toRadiusMeters(centerMarker.getPosition(), radiusMarker.getPosition());
                circle.setRadius(radius);
                return true;
            }
            return false;
        }

        public void onStyleChange() {
            circle.setStrokeWidth(mWidthBar.getProgress());
            circle.setFillColor(mFillColor);
            circle.setStrokeColor(mStrokeColor);
        }

        public void setClickable(boolean clickable) {
            circle.setClickable(clickable);
        }
    }

    /** Generate LatLng of radius marker */
    private static LatLng toRadiusLatLng(LatLng center, double radius) {
        double radiusAngle = Math.toDegrees(radius / RADIUS_OF_EARTH_METERS) /
                Math.cos(Math.toRadians(center.latitude));
        return new LatLng(center.latitude, center.longitude + radiusAngle);
    }

    private static double toRadiusMeters(LatLng center, LatLng radius) {
        float[] result = new float[1];
        Location.distanceBetween(center.latitude, center.longitude,
                radius.latitude, radius.longitude, result);
        return result[0];
    }



    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Override the default content description on the view, for accessibility mode.
        map.setContentDescription(getString(R.string.map_circle_description));


        mColorBar.setOnSeekBarChangeListener(this);
        mAlphaBar.setOnSeekBarChangeListener(this);
        mWidthBar.setOnSeekBarChangeListener(this);

        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);


        mFillColor = Color.HSVToColor(
                mAlphaBar.getProgress(), new float[]{mColorBar.getProgress(), 1, 1});
        mStrokeColor = Color.BLACK;



        DraggableCircle circle =
                new DraggableCircle(MEXICO, DEFAULT_RADIUS, mClickabilityCheckbox.isChecked());
        mCircles.add(circle);
        DraggableCircle circle2 =
                new DraggableCircle(DELVALLE, DEFAULT_RADIUS, mClickabilityCheckbox.isChecked());
        mCircles.add(circle2);
        DraggableCircle circle3 =
                new DraggableCircle(TEPITO, DEFAULT_RADIUS, mClickabilityCheckbox.isChecked());
        mCircles.add(circle3);
        DraggableCircle circle4 =
                new DraggableCircle(AZCAPOTZALCO, DEFAULT_RADIUS, mClickabilityCheckbox.isChecked());
        mCircles.add(circle4);



        // Move the map so that it is centered on the initial circle
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MEXICO, 15.0f));











        // Set up the click listener for the circle.
        map.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {
                // Flip the r, g and b components of the circle's stroke color.
                int strokeColor = circle.getStrokeColor() ^ 0x00ffffff;
                circle.setStrokeColor(strokeColor);
            }
        });
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Don't do anything here.
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Don't do anything here.
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == mColorBar) {
            mFillColor = Color.HSVToColor(Color.alpha(mFillColor), new float[]{progress, 1, 1});
        } else if (seekBar == mAlphaBar) {
            mFillColor = Color.argb(progress, Color.red(mFillColor), Color.green(mFillColor),
                    Color.blue(mFillColor));
        }

        for (DraggableCircle draggableCircle : mCircles) {
            draggableCircle.onStyleChange();
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        onMarkerMoved(marker);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        onMarkerMoved(marker);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        onMarkerMoved(marker);
    }

    private void onMarkerMoved(Marker marker) {
        for (DraggableCircle draggableCircle : mCircles) {
            if (draggableCircle.onMarkerMoved(marker)) {
                break;
            }
        }
    }

    @Override
    public void onMapLongClick(LatLng point) {
        // We know the center, let's place the outline at a point 3/4 along the view.
        View view = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getView();
        LatLng radiusLatLng = mMap.getProjection().fromScreenLocation(new Point(
                view.getHeight() * 3 / 4, view.getWidth() * 3 / 4));

        // ok create it
        DraggableCircle circle =
                new DraggableCircle(point, radiusLatLng, mClickabilityCheckbox.isChecked());
        mCircles.add(circle);
    }

    public void toggleClickability(View view) {
        boolean clickable = ((CheckBox) view).isChecked();
        // Set each of the circles to be clickable or not, based on the
        // state of the checkbox.
        for (DraggableCircle draggableCircle : mCircles) {
            draggableCircle.setClickable(clickable);
        }
    }

}
