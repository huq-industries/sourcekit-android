package io.huq.sourcekit.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.huq.sourcekit.HISourceKit;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "8aeecd50-db8c-4c10-8c23-476923304223";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = (Button) findViewById(R.id.start_recording);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HISourceKit.getInstance().recordWithAPIKey(API_KEY, getApplicationContext());
            }
        });

        Button stopButton = (Button) findViewById(R.id.stop_recording);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HISourceKit.getInstance().stopRecording();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Check if the user has previously denied the request
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // User has probably previously denied request, show an alert explaining why we need the permission
                new AlertDialog.Builder(this)
                        .setTitle("Location Services")
                        .setMessage("This app requires the use of location services in order for it to function properly.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                requestLocationPermission();
                            }
                        })
                        .show();
            } else {

                // No explanation needed, we can request the permission.
                requestLocationPermission();
            }
        }else
        {
            // we already have the permission we need - set up SourceKit
            HISourceKit.getInstance().recordWithAPIKey(API_KEY, getApplicationContext());
        }
    }

    private void requestLocationPermission()
    {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, start SourceKit
                    HISourceKit.getInstance().recordWithAPIKey(API_KEY, getApplicationContext());

                } else {

                    // permission denied, don't start SourceKit
                }
                return;
            }
        }
    }
}