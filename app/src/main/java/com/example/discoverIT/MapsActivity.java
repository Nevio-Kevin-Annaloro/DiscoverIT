package com.example.discoverIT;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.content.BroadcastReceiver;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button scan;
    private Button back;
    private Button notfound;
    SupportMapFragment mapFragment;

    private LocationListener locationListener;
    private LocationManager locationManager;

    private final long MIN_TIME = 1000; // 1 second
    private final long MIN_DIST = 10; // 5 Meters

    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        scan = (Button) findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, ScanActivity.class));
            }
        });

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, HomeActivity.class));
            }
        });

        notfound = (Button) findViewById(R.id.notfound);
        notfound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, HelpActivity.class));
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("mylog", "ENTRATO onMApReady");
        mMap = googleMap;

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("mylog", "ENTRATO onLocationChanged");
                try {
                    mMap.clear();
                    loadAlldisco();
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title("My Position").icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_loc)));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19f));
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        }
        catch (SecurityException e){
            e.printStackTrace();
        }

    }

    void loadAlldisco() {
        LatLng duomo_al = new LatLng(44.912262,8.618954);
        mMap.addMarker(new MarkerOptions().position(duomo_al).title("Near this position there's a disco! (main cathedral)"));

        LatLng citt_al = new LatLng(44.921446,8.604480);
        mMap.addMarker(new MarkerOptions().position(citt_al).title("Near this position there's a disco! (main citadel)"));

        LatLng meier_al = new LatLng(44.918829,8.609138);
        mMap.addMarker(new MarkerOptions().position(meier_al).title("Near this position there's a disco! (meier bridge)"));

        LatLng bollente_al = new LatLng(44.675613,8.470425);
        mMap.addMarker(new MarkerOptions().position(bollente_al).title("Near this position there's a disco! (boiling fountain)"));

        LatLng archiromani_al = new LatLng(44.666136,8.467053);
        mMap.addMarker(new MarkerOptions().position(archiromani_al).title("Near this position there's a disco! (roman arches)"));

        LatLng paleologi_al = new LatLng(44.674001,8.472770);
        mMap.addMarker(new MarkerOptions().position(paleologi_al).title("Near this position there's a disco! (castle of the paleologists )"));

        LatLng ottolenghi_al = new LatLng(44.687808,8.477927);
        mMap.addMarker(new MarkerOptions().position(ottolenghi_al).title("Near this position there's a disco! (villa ottolenghi)"));
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, 120, 120);

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(120, 120, Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}