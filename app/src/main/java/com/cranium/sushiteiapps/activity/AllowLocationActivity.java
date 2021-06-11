package com.cranium.sushiteiapps.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cranium.sushiteiapps.R;
import com.cranium.sushiteiapps.util.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dayona on 5/24/17.
 */

public class AllowLocationActivity extends AppCompatActivity {

    @BindView(R.id.btn_allow) Button btnAllow;
    @BindView(R.id.btn_dont_allow) Button btnDontAllow;

    int permissionLocation;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allow_location);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(getApplicationContext());

        final Intent[] intent = new Intent[1];

        btnAllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setAllowLocation(true);
                ActivityCompat.requestPermissions(AllowLocationActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1201);
                intent[0] = new Intent(AllowLocationActivity.this, GreetingActivity.class);
                startActivity(intent[0]);
            }
        });

        btnDontAllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setAllowLocation(false);
                intent[0] = new Intent(AllowLocationActivity.this, GreetingActivity.class);
                startActivity(intent[0]);
            }
        });

        permissionLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if ((permissionLocation != PackageManager.PERMISSION_GRANTED) ||
                sessionManager.isAllowLocation()) {
            intent[0] = new Intent(AllowLocationActivity.this, GreetingActivity.class);
            startActivity(intent[0]);
        }
    }
}
