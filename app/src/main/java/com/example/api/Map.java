package com.example.api;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Map extends AppCompatActivity {
    GoogleMap mapAPI;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    ArrayList<LatLng> arrayList = new ArrayList<LatLng>();
    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapAPI);

        client = LocationServices.getFusedLocationProviderClient(this);
        mQueue = Volley.newRequestQueue(this);

        if(ActivityCompat.checkSelfPermission(Map.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            jsonParseMap();
            getCurrentLocation();
        }else{
            ActivityCompat.requestPermissions(Map.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    }


    private void getCurrentLocation() {
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {

                            LatLng latLng = new LatLng(location.getLatitude()
                                    , location.getLongitude());
                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("Je suis Là !");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                            googleMap.addMarker(options);

                            for(int i = 0; i < arrayList.size(); i++)
                            {
                                Log.e("DEV", "" + arrayList.get(i));
                                googleMap.addMarker(new MarkerOptions().position(arrayList.get(i)).title("Ancienne Position"));
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 44){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }

    private void jsonParseMap() {
        String url = "http://192.168.1.70/server/apiLoclisation.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("location");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject results = jsonArray.getJSONObject(i);

                                String Lat = results.getString("Lat");
                                String Longi = results.getString("Longi");



                                arrayList.add(i, new LatLng(Double.parseDouble(Lat), Double.parseDouble(Longi)));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }
}