package com.wowza.gocoder.sdk.sampleapp;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    @Override   
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);


        if (getActionBar() != null) {
            getActionBar().setTitle(getResources().getString(R.string.app_name_long));
        }

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        /*
        setListAdapter(new SimpleAdapter(this, createActivityList(),
                R.layout.example_row,
                new String[] { TITLE, DESCRIPTION, ICON },
                new int[] { R.id.example_title, R.id.example_description, R.id.example_icon } ));
        */



    }

}
