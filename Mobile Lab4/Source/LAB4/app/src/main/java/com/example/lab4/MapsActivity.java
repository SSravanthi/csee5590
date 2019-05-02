package com.example.lab4;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    //Declaring the variables
    private GoogleMap mMap;
    private static final String TAG = "MainActivity";
    private boolean mLocationPermission = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        checkLocationPermission();
    }

    /*
     * This method will call after Map is ready.
     * In this we will call the current location of the device.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mLocationPermission) {
            getCurrentLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);
        }
    }

    /*
     * This method will call once the user select the permission either allow or deny.
     * Once the permission is allow then we are initializing the map.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermission = false;
        switch (requestCode) {
            case 2345:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermission = true;
                    initMap();
                }
        }
    }

    /*
     * In this method we will initialize the map settings.
     */
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /*
     * This method will call when the user clicks on display info button.
     * We will take the user entered location and then we will get details of the location by calling geocoder.getLocationFromName method.
     */
    public void getLocationDetails(View view){
        EditText place = (EditText) findViewById(R.id.txtLocation);
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> addressList = new ArrayList<Address>();
        try{
            addressList = geocoder.getFromLocationName(place.getText().toString(),1);
        }catch (IOException e){
            Log.e(TAG,e.getMessage());
        }
        if(addressList.size() > 0){
            Address address = addressList.get(0);
            updateLocation(new LatLng(address.getLatitude(),address.getLongitude()),10f);
        }
    }

    /*
     * In this method we will check the location permission.
     */
    private void checkLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermission = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 2345);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, 2345);
        }
    }

    /*
     * In this method we will get the current location details.
     */
    private void getCurrentLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermission){
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful())
                        {
                            Log.i(TAG,"Location found");
                            Location currentLocation = (Location) task.getResult();
                            updateLocation(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),15f);
                        } else{
                            Log.e(TAG,"Location not found");
                        }
                    }
                });
            }
        }catch (SecurityException e)
        {
            Log.e(TAG,e.getMessage());
        }
    }

    /*
     * In this method we will update the marker and latitude and longitude details.
     */
    private void updateLocation(LatLng latlng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom));
        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(latlng).title("Latitude : "+latlng.latitude+" "+"Longitude : "+latlng.longitude));

        TextView lat = (TextView) findViewById(R.id.lblLatText);
        TextView lng = (TextView) findViewById(R.id.lblLngText);
        lat.setText(latlng.latitude+"");
        lng.setText(latlng.longitude+"");
    }

    public void goTohome(View view) {
        Intent redirect = new Intent(MapsActivity.this,HomeActivity.class);
        startActivity(redirect);
    }
}
