package com.maharsh.rapidcabs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class BookingConfirmation extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);

        TextView pt, dt;
        pt = findViewById(R.id.pickup_text);
        dt = findViewById(R.id.drop_text);

        String start = getIntent().getStringExtra("Start");
        String end = getIntent().getStringExtra("end");

        pt.setText("From : " + start);
        dt.setText("To : "+ end);

        int distance = getIntent().getIntExtra("distace", 0);

        TextView dist = findViewById(R.id.trip_distance_time);
        dist.setText(distance + " km " + distance + " min");

        TextView fare = findViewById(R.id.fare_estimate);
        fare.setText("₹ "+distance/100);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.booking_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        Button btn =  findViewById(R.id.btn_book_another);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it =  new Intent(BookingConfirmation.this, Home.class);
                startActivity(it);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Example pickup and drop
        LatLng pickup = new LatLng(23.223, 72.65);
        LatLng drop = new LatLng(23.050, 72.63);

        mMap.addMarker(new MarkerOptions()
                .position(pickup)
                .title("Pickup")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        mMap.addMarker(new MarkerOptions()
                .position(drop)
                .title("Drop")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        // Draw polyline (simple straight route demo)
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(pickup)
                .add(drop)
                .width(10)
                .color(Color.parseColor("#2196F3"))
                .geodesic(true);

        mMap.addPolyline(polylineOptions);

        // Adjust camera
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(pickup);
        builder.include(drop);
        LatLngBounds bounds = builder.build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }
}