package com.maharsh.rapidcabs;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BookingFrag extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap gMap;

    private FusedLocationProviderClient fusedLocationClient;
    private Location route;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private EditText startLoc;
    private EditText endLoc;
    private Button schedule;
    private Button book;

    private LatLng l1, l2;

    private LatLng getRandomNearbyLocation(LatLng userLatLng, double radiusInMeters) {
        double radiusInDegrees = radiusInMeters / 111000f; // 1 degree ≈ 111 km

        double u = Math.random();
        double v = Math.random();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double latOffset = w * Math.cos(t);
        double lngOffset = w * Math.sin(t) / Math.cos(Math.toRadians(userLatLng.latitude));

        double newLat = userLatLng.latitude + latOffset;
        double newLng = userLatLng.longitude + lngOffset;

        return new LatLng(newLat, newLng);
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (gMap != null) {
                gMap.setMyLocationEnabled(true);
                getDeviceLocation();
            }
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }
    }

    private void getDeviceLocation() {
        try {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            if (location != null && gMap != null) {
                                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
                                gMap.addMarker(new MarkerOptions()
                                        .position(currentLatLng)
                                        .title("You are here"));

                                // 🚗 Add a nearby driver
                                for (int i = 0; i < 3; i++) {  // show 3 nearby drivers
                                    LatLng driverLatLng = getRandomNearbyLocation(currentLatLng, 300 + (i * 100)); // within 300-500m
                                    gMap.addMarker(new MarkerOptions()
                                            .position(driverLatLng)
                                            .title("Driver " + (i + 1))
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                }
                            }
                        });
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_booking, container, false);

        mapView = root.findViewById(R.id.mapView);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        }

        startLoc=root.findViewById(R.id.startLoc);
        endLoc=root.findViewById(R.id.endLoc);
        schedule=root.findViewById(R.id.button);
        book=root.findViewById(R.id.button2);


        startLoc.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
            }
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            double lat = location.getLatitude();
                            double lng = location.getLongitude();

                            // Bias suggestions to ~10 km around current location
                            LatLng southwest = new LatLng(lat - 0.1, lng - 0.1);
                            LatLng northeast = new LatLng(lat + 0.1, lng + 0.1);
                            LatLngBounds bounds = new LatLngBounds(southwest, northeast);

                            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);

                            Intent intent = new Autocomplete.IntentBuilder(
                                    AutocompleteActivityMode.OVERLAY, fields)
                                    .setLocationBias(RectangularBounds.newInstance(bounds))
                                    .build(requireContext());

                            startActivityForResult(intent, 101);
                        } else {
                            Toast.makeText(requireContext(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        endLoc.setOnClickListener(v -> {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            double lat = location.getLatitude();
                            double lng = location.getLongitude();

                            // Bias suggestions to ~10 km around current location
                            LatLng southwest = new LatLng(lat - 0.1, lng - 0.1);
                            LatLng northeast = new LatLng(lat + 0.1, lng + 0.1);
                            LatLngBounds bounds = new LatLngBounds(southwest, northeast);

                            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);

                            Intent intent = new Autocomplete.IntentBuilder(
                                    AutocompleteActivityMode.OVERLAY, fields)
//                                    .setLocationBias(RectangularBounds.newInstance(bounds))
                                    .build(requireContext());
                            Log.d("Sugges", "startinh");
                            startActivityForResult(intent, 102);
                        } else {
                            Toast.makeText(requireContext(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startAdd = startLoc.getText().toString();
                String endAdd = endLoc.getText().toString();

                if(startAdd=="" || endAdd==""){
                    Toast.makeText(root.getContext(), "Please enter address", Toast.LENGTH_SHORT).show();
                }else{
                    Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
                    try{
                        List<Address> addresses = geocoder.getFromLocationName(startAdd, 1);
                        double lat = addresses.get(0).getLatitude();
                        double lon = addresses.get(0).getLongitude();
                        l1 = new LatLng(lat, lon);
                        addresses = geocoder.getFromLocationName(endAdd, 1);
                        lat=addresses.get(0).getLatitude();
                        lon=addresses.get(0).getLongitude();
                        l2 = new LatLng(lat, lon);

                        gMap.addMarker(new MarkerOptions().position(l1).title("Start"));
                        gMap.addMarker(new MarkerOptions().position(l2).title("End"));

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(l1);
                        builder.include(l2);
                        gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));

                        String url = "https://maps.googleapis.com/maps/api/directions/json?"
                                + "origin=" + l1.latitude + "," + l1.longitude
                                + "&destination=" + l2.latitude + "," + l2.longitude
                                + "&key="+BuildConfig.MAPS_API_KEY;

                        OkHttpClient client = new OkHttpClient();

                        Request request = new Request.Builder().url(url).build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                String data = response.body().string();
                                try {
                                    JSONObject json = new JSONObject(data);
                                    JSONArray routes = json.getJSONArray("routes");
                                    if (routes.length() > 0) {
                                        JSONObject route = routes.getJSONObject(0);
                                        JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                                        String encoded = overviewPolyline.getString("points");

                                        List<LatLng> points = decodePoly(encoded);

                                        requireActivity().runOnUiThread(() -> {
                                            gMap.addPolyline(new PolylineOptions()
                                                    .addAll(points)
                                                    .width(8)
                                                    .color(Color.BLUE));
                                        });
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(l1 != null && l2 != null){
                    @SuppressLint({"NewApi", "LocalSuppress"}) Date date = Date.from(Instant.now());
                    float[] dist = new float[2];
                    Location.distanceBetween(l1.latitude, l1.longitude, l2.latitude, l2.longitude, dist);
                    int distance = (int) dist[0];
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                    int id = sharedPreferences.getInt("uid", -1);

                    ApiService apiService = RetroFitClient.getApiService();
                    apiService.addRides(id, date, distance).enqueue(new retrofit2.Callback<ApiResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                            ApiResponse resp = response.body();
                            int status = response.code();
                            if(status==200){
                                Intent it = new Intent(requireActivity(), BookingConfirmation.class);
                                it.putExtra("Start", startLoc.getText().toString());
                                it.putExtra("end", endLoc.getText().toString());
                                it.putExtra("distace", distance);
                                startActivity(it);
                                Toast.makeText(getActivity(), "Ride Added", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), "Ride couldn't be added", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<ApiResponse> call, Throwable t) {
                            Toast.makeText(getActivity(), "Failed with error", Toast.LENGTH_SHORT).show();
                            Log.d("Error", t.getMessage());
                        }
                    });
                }
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("OnComplete status", String.valueOf(requestCode));
        Log.d("OnComplete result", String.valueOf(resultCode));
        if ((requestCode == 101 || requestCode == 102) && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            if (requestCode == 101) {
                startLoc.setText(place.getName());
            } else {
                endLoc.setText(place.getName());
            }
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Log.e("AUTOCOMPLETE", "Error: " + status.getStatusMessage());
        }
    }



    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        enableMyLocation();
    }

    // Forward lifecycle methods to MapView
    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) mapView.onSaveInstanceState(outState);
    }
}
